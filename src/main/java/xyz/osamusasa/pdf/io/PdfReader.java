package xyz.osamusasa.pdf.io;


import xyz.osamusasa.pdf.PdfFile;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.*;
import xyz.osamusasa.pdf.parser.PdfNamedObjectParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PDFファイルを読み込むクラス
 */
public class PdfReader {
    private RandomAccessFile file;

    /**
     * 与えられたストリームから、PDFリーダーを作成する
     *
     * @param file PDFファイルを表すファイルオブジェクト
     */
    PdfReader(File file) throws FileNotFoundException{
        this.file = new RandomAccessFile(file, "r");
    }

    /**
     * ファイルオブジェクトから読み込み、PDFファイルオブジェクトを作成する。
     *
     * @return PDFファイルオブジェクト
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    PdfFile read() throws IOException, PdfFormatException {
        String version = getVersion();
        PdfTrailer trailer = getTrailer();
        PdfCrossReferenceTable xref = getXref(trailer);
        PdfBody body = new PdfBody(
                readObject(xref.getCrossReferences().get(1).getOffset()),
                readObject(xref.getCrossReferences().get(2).getOffset())
        );

        return new PdfFile(version, body, xref, trailer);
    }

    /**
     * ファイルから、PDF文書のバージョンを読み込む。
     *
     * @return PDF文書のバージョンをあらわす文字列。
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private String getVersion() throws IOException, PdfFormatException {

        file.seek(0);
        String line = file.readLine();
        if (!line.startsWith("%")) {
            throw new PdfFormatException("Header Error");
        }
        //先頭から、連続する複数の”％”を削除
        line = line.replaceFirst("^%*", "").trim();

        if (!line.startsWith("PDF-")) {
            throw new PdfFormatException("Header Error");
        }

        line = line.replaceFirst("^PDF-", "");

        return line;
    }

    /**
     * トレイラーの情報を用いて、ファイルからクロスリファレンステーブルを読み込む
     *
     * @return PDFのクロスリファレンステーブルを表すオブジェクト
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfCrossReferenceTable getXref(PdfTrailer trailer) throws IOException, PdfFormatException {

        long xrefStart = trailer.getStartxref();
        int xrefLen = trailer.getkSize();

        int start = -1;
        int len = -1;
        List<PdfCrossReferenceTableRow> rows = new ArrayList<>(xrefLen);

        String content = read(xrefStart, 50);
        if (!content.startsWith("xref")) {
            throw new PdfFormatException("xref Error");
        }

        String subSection = content.substring(
                "xref\r\n".length(),
                content.indexOf("\r\n", "xref\r\n".length())
        );
        String[] subSections = subSection.split(" ");

        start = Integer.valueOf(subSections[0]);
        len = Integer.valueOf(subSections[1]);

        long xrefRowPos = xrefStart + "xref\r\n".length() + subSection.length() + "\r\n".length();
        String[] rowsStr = read(xrefRowPos, 20 * xrefLen).split("\r\n");

        for (String r: rowsStr) {
            int offset = Integer.valueOf(r.substring(0, 10));
            int gen = Integer.valueOf(r.substring(11, 16));
            char state = r.charAt(17);
            rows.add(new PdfCrossReferenceTableRow(offset, gen, state));
        }

        return new PdfCrossReferenceTable(start, len, rows);
    }

    /**
     * ファイルから、トレイラーを読み込む
     * @return PDFのトレイラーを表すオブジェクト
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfTrailer getTrailer() throws IOException, PdfFormatException {

        int readNByte = 200;
        long from = (file.length() - 1) - (long)readNByte + 1;
        String tail = read(from, readNByte);

        if (!tail.endsWith("%%EOF\r\n")) {
            System.err.println(tail);
            throw new PdfFormatException("Trailer Error");
        }

        long startxref = Long.valueOf(tail.substring(tail.indexOf("startxref")).split("\r\n")[1]);

        int trailerIndex = tail.indexOf("trailer");
        trailerIndex += "trailer\r\n".length();

        if (!tail.substring(trailerIndex).startsWith("<<\r\n")) {
            System.err.println(tail);
            throw new PdfFormatException("Trailer Error");
        }
        trailerIndex += "<<\r\n".length();
        int size = -1;
        long prev = -1;
        PdfNamedObject root = null;
        PdfNamedObject encrypt = null;
        PdfNamedObject info = null;
        String id = "";

        while (!tail.substring(trailerIndex).startsWith(">>")) {
            String[] row = tail.substring(trailerIndex, tail.indexOf("\r\n", trailerIndex)).split(" ");

            if (row[0].startsWith("/Size")) {
                size = Integer.valueOf(row[1]);
            } else if (row[0].startsWith("/Prev")) {

            } else if (row[0].startsWith("/Root")) {

            } else if (row[0].startsWith("/Encrypt")) {

            } else if (row[0].startsWith("/Info")) {

            } else if (row[0].startsWith("/ID")) {
                id = row[1];
            }

            trailerIndex = tail.indexOf("\r\n", trailerIndex) + "\r\n".length();
        }

        return new PdfTrailer(size, prev, root, encrypt, info, id, startxref);
    }

    /**
     * 指定された位置から始まるオブジェクトを読み込む
     *
     * @param pos オブジェクトの開始オフセット
     * @return 読み込んだオブジェクト
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfNamedObject readObject(long pos) throws IOException, PdfFormatException {
        Byte[] content = read(pos, "endobj");

        if (content == null) {
            throw new PdfFormatException("read object at:" + pos);
        }

        try {
            return byteAry2NamedObject(content);
        } catch (PdfFormatException e) {
            throw new PdfFormatException("read object at:" + pos, e);
        }
    }

    /**
     * ファイルから、指定された範囲の文字列を読み込む。
     *
     * @param from 読み込む開始地点
     * @param len 読み込む長さ
     * @return 読み込んだ文字列
     * @throws IOException 入出力エラーが発生した場合。
     */
    private String read(long from, int len) throws IOException {
        byte[] buffer = new byte[len];
        file.seek(from);
        file.read(buffer);
        return new String(buffer);
    }

    /**
     * 指定された位置から特定の文字列が最後に含まれるような部分データ列を読み込む
     *
     * ファイルの終わりまで読んで、指定された文字が見つからなかったときは、nullを返す。
     *
     * @param from 読み込む開始地点
     * @param regex データ列の最後に現れる文字列
     * @return 指定された文字列が最後に含まれる部分データ列
     * @throws IOException 入出力エラーが発生した場合。
     */
    private Byte[] read(long from, String regex) throws IOException {
        int bufCapacity = 100;
        List<Byte> ary = new ArrayList<>(bufCapacity);
        byte[] buf = new byte[bufCapacity];

        file.seek(from);
        int strPos = 0;
        int readLen = 0;

        while (readLen != -1) {
            readLen = file.read(buf);
            for (int i = 0; i < readLen; i++) {
                if (buf[i] == regex.charAt(strPos)) {
                    strPos++;
                } else {
                    strPos = 0;
                }

                ary.add(buf[i]);

                if (strPos == regex.length()) {
                    return ary.toArray(new Byte[0]);
                }
            }
        }

        return null;
    }

    /**
     * 与えられたファイルオブジェクトから読み込み、PDFファイルオブジェクトを作成する。
     *
     * @param file PDFファイルを表すファイルオブジェクト
     * @return PDFファイルオブジェクト
     * @throws IOException 入出力エラーが発生した場合。
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public static PdfFile readPdf(File file) throws IOException, PdfFormatException {
        return new PdfReader(file).read();
    }

    /**
     * Byte配列の内容をもとに、PdfNamedObjectを作成する
     *
     * @param ary Byte配列
     * @return PdfObject
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private static PdfNamedObject byteAry2NamedObject(Byte[] ary) throws PdfFormatException {
        return PdfNamedObjectParser.parse(ary);
    }
}
