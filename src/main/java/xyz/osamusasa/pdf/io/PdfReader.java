package xyz.osamusasa.pdf.io;


import xyz.osamusasa.pdf.PdfFile;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.*;
import xyz.osamusasa.pdf.parser.PdfNamedObjectParser;
import xyz.osamusasa.pdf.parser.TrailerParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntBiFunction;

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
        PdfBody body = new PdfBody(getObject(xref));

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
     * クロスリファレンステーブルを用いて、すべてのオブジェクトを読み込む
     *
     * @param xref PDFのクロスリファレンステーブルを表すオブジェクト
     * @return PDFのすべてのオブジェクトの配列
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfNamedObject[] getObject(PdfCrossReferenceTable xref) throws IOException, PdfFormatException {
        List<PdfNamedObject> list = new ArrayList<>();

        for (PdfCrossReferenceTableRow rows: xref.getRowsInUse()) {
            list.add(readObject(rows.getOffset()));
        }

        return list.toArray(new PdfNamedObject[0]);
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

        int start;
        int len;
        List<PdfCrossReferenceTableRow> rows = new ArrayList<>(xrefLen);

        String content = read(xrefStart, 50);
        if (!content.startsWith("xref")) {
            System.err.println(content);
            throw new PdfFormatException("xref Error");
        }

        ToIntBiFunction<String, String> rtnLen = (value, regex) -> {
            if (value.contains(regex + "\r\n")) {
                return "\r\n".length();
            } else if (value.contains(regex + "\r")){
                return "\r".length();
            } else if (value.contains(regex + "\n")){
                return "\n".length();
            } else {
                return -1;
            }
        };

        int xrefRtnLen = rtnLen.applyAsInt(content, "xref");
        if (xrefRtnLen == -1) {
            System.err.println(content);
            throw new PdfFormatException("xref Error");
        }

        String subSection = content.split("\r\n|\r|\n")[1];
        String[] subSections = subSection.split(" ");
        int subSectionRtnLen = rtnLen.applyAsInt(content, subSection);
        if (subSectionRtnLen == -1) {
            System.err.println(content);
            throw new PdfFormatException("xref Error");
        }

        start = Integer.valueOf(subSections[0]);
        len = Integer.valueOf(subSections[1]);

        long xrefRowPos = xrefStart + "xref".length() + xrefRtnLen + subSection.length() + subSectionRtnLen;
        String[] rowsStr = read(xrefRowPos, 20 * xrefLen).split("\r\n|\r|\n");

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

        if (!(tail.endsWith("%%EOF\r\n") || tail.endsWith("%%EOF\n") || tail.endsWith("%%EOF\r"))) {
            System.err.println(tail);
            throw new PdfFormatException("Trailer Error");
        }

        return TrailerParser.readTrailer(tail);
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
     * 指定された位置から連続するホワイトスペースの文字数を返す。
     *
     * startの位置の文字がホワイトスペースでない場合は、0を返す。
     * ホワイトスペースは' '、'\r'、'\n'である。
     *
     * @param start 開始位置
     * @return 連続する空白相当文字の文字数
     * @throws IOException 入出力エラーが発生した場合。
     */
    private int whiteSpace(long start) throws IOException {
        int cnt = 0;
        long oldPos = file.getFilePointer();
        file.seek(start);
        while (file.readChar() == ' ' || file.readChar() == '\r' || file.readChar() == '\n') {
            cnt++;
        }
        file.seek(oldPos);
        return cnt;
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
