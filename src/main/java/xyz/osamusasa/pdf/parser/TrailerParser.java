package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.PdfArray;
import xyz.osamusasa.pdf.element.primitive.PdfReference;
import xyz.osamusasa.pdf.element.primitive.PdfString;
import xyz.osamusasa.pdf.element.primitive.PdfTrailer;
import xyz.osamusasa.pdf.util.ByteArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * PDFファイルのトレイラー部を読み込むためのクラス
 */
public class TrailerParser {

    /**
     * srcからトレイラー部を読み込む
     *
     * srcにトレイラー部全体が含まれている必要がある。
     * 配列の末尾が文字列"%%EOF"で終了している必要があるが、文字列"trailer"で始まっている必要はない。（含まれていればよい）
     * 配列の先頭が文字列"trailer"で始まっていない場合は、それより前の文字列は無視される。
     *
     * @param src トレイラー部が含まれたbyte配列
     * @return トレイラーオブジェクト
     * @throws PdfFormatException  PDFファイルとして読み込めなかった場合。
     */
    @Deprecated
    public static PdfTrailer readTrailer(byte[] src) throws PdfFormatException {
        if (!ByteArrayUtil.endWith(src, "%%EOF")) {
            throw new PdfFormatException("Trailer部は%%EOFで終わります。");
        }
        if (!ByteArrayUtil.contains(src, "trailer")) {
            throw new PdfFormatException("Trailer部はtrailerで始まります。");
        }

        return readTrailer(src);
    }

    /**
     * srcからトレイラー部を読み込む
     *
     * 配列の先頭が文字列"trailer"で開始し、配列の末尾が文字列"%%EOF"で終了している必要がある。
     *
     * @param src トレイラー部が含まれたStringオブジェクト
     * @return トレイラーオブジェクト
     * @throws PdfFormatException  PDFファイルとして読み込めなかった場合。
     */
    @Deprecated
    private static PdfTrailer parseTrailer(byte[] src) throws PdfFormatException {
        return new TrailerParser().parse(new String(src));
    }

    /**
     * srcからトレイラー部を読み込む
     *
     * srcにトレイラー部全体が含まれている必要がある。
     * 配列の末尾が文字列"%%EOF"で終了している必要があるが、文字列"trailer"で始まっている必要はない。（含まれていればよい）
     * 配列の先頭が文字列"trailer"で始まっていない場合は、それより前の文字列は無視される。
     *
     * このメソッドはトレイラー部の先頭と末尾の文字列をチェックし、パースはparseTrailerメソッドが行う。
     *
     * @param src トレイラー部が含まれたStringオブジェクト
     * @return トレイラーオブジェクト
     * @throws PdfFormatException  PDFファイルとして読み込めなかった場合。
     */
    public static PdfTrailer readTrailer(String src) throws PdfFormatException {
        // 末尾の改行コードを削除
        src = src.strip();

        if (!src.endsWith("%%EOF")) {
            throw new PdfFormatException("Trailer部は%%EOFで終わります。");
        }
        if (!src.contains("trailer")) {
            throw new PdfFormatException("Trailer部はtrailerで始まります。");
        }

        return parseTrailer(src.substring(src.indexOf("trailer")));
    }

    /**
     * srcからトレイラー部を読み込む
     *
     * 配列の先頭が文字列"trailer"で開始し、配列の末尾が文字列"%%EOF"で終了している必要がある。
     *
     * @param src トレイラー部が含まれたStringオブジェクト
     * @return トレイラーオブジェクト
     * @throws PdfFormatException  PDFファイルとして読み込めなかった場合。
     */
    private static PdfTrailer parseTrailer(String src) throws PdfFormatException {
        return new TrailerParser().parse(src);
    }

    /**
     * srcからトレイラー部を読み込む
     *
     * 配列の先頭が文字列"trailer"で開始し、配列の末尾が文字列"%%EOF"で終了している必要がある。
     *
     * @param src トレイラー部が含まれたStringオブジェクト
     * @return トレイラーオブジェクト
     * @throws PdfFormatException  PDFファイルとして読み込めなかった場合。
     */
    private PdfTrailer parse(String src) throws PdfFormatException {
        List<TrailerNode> tokens = new ArrayList<>();

        // tokenize
        for (int i = 0; i < src.length(); i++) {
            // skip whitespace
            if (Character.isWhitespace(src.charAt(i))) {
                continue;
            }

            if (src.substring(i).startsWith("trailer")) {
                tokens.add(new TrailerNode("trailer", TrailerLabel.TRAILER));
                i += "trailer".length() - 1;
                continue;
            }

            if (src.substring(i).startsWith("startxref")) {
                tokens.add(new TrailerNode("startxref", TrailerLabel.STARTXREF));
                i += "startxref".length() - 1;
                continue;
            }

            if (src.substring(i).startsWith("%%EOF")) {
                tokens.add(new TrailerNode("%%EOF", TrailerLabel.EOF));
                i += "%%EOF".length() - 1;
                continue;
            }

            if (src.substring(i).startsWith("<<")) {
                tokens.add(new TrailerNode("<<", TrailerLabel.TRAILER_START));
                i += "<<".length() - 1;
                continue;
            }

            if (src.substring(i).startsWith(">>")) {
                tokens.add(new TrailerNode(">>", TrailerLabel.TRAILER_END));
                i += ">>".length() - 1;
                continue;
            }

            if (src.substring(i).startsWith("/")) {
                int j = i;
                for (;j < src.length() && !Character.isWhitespace(src.charAt(j)); j++);
                var s = src.substring(i, j);
                tokens.add(new TrailerNode(s, TrailerLabel.NAME));
                i += s.length() - 1;
                continue;
            }

            if (Character.isDigit(src.charAt(i))) {
                int j = i;
                for (;j < src.length() && Character.isDigit(src.charAt(j)); j++);
                var s = src.substring(i, j);
                tokens.add(new TrailerNode(s, TrailerLabel.NUMERIC));
                i += s.length() - 1;
                continue;
            }

            if (src.charAt(i) == 'R') {
                tokens.add(new TrailerNode("R", TrailerLabel.REFERENCE));
                continue;
            }

            if (src.charAt(i) == '<') {
                int j = i;
                for (;j < src.length() && src.charAt(j) != '>'; j++);
                j++;
                var s = src.substring(i, j);
                tokens.add(new TrailerNode(s, TrailerLabel.STRING));
                i += s.length() - 1;
                continue;
            }

            if (src.charAt(i) == '[') {
                tokens.add(new TrailerNode("[", TrailerLabel.ARRAY_START));
                continue;
            }

            if (src.charAt(i) == ']') {
                tokens.add(new TrailerNode("]", TrailerLabel.ARRAY_END));
                continue;
            }
        }

        // parser
        int size = -1;
        int prev = -1;
        PdfReference root = null;
        PdfReference encrypt = null;
        PdfReference info = null;
        PdfArray<PdfString> id = new PdfArray<>();
        int startxref = -1;

        for (int i = 0; i < tokens.size(); i++) {
            TrailerNode token = tokens.get(i);

            if (token.label == TrailerLabel.NAME) {
                switch (token.value) {
                    case "/Size": {
                        if (tokens.get(i+1).label != TrailerLabel.NUMERIC) {
                            throw new PdfFormatException("PDF TrailerのSizeの後には、数字が来ます。");
                        }

                        TrailerNode sizeToken = tokens.get(++i);
                        size = Integer.parseInt(sizeToken.value);
                        break;
                    }
                    case "/Prev": {
                        if (tokens.get(i+1).label != TrailerLabel.NUMERIC) {
                            throw new PdfFormatException("PDF TrailerのPrevの後には、数字が来ます。");
                        }

                        TrailerNode prevToken = tokens.get(++i);
                        prev = Integer.parseInt(prevToken.value);
                        break;
                    }
                    case "/Root": {
                        if (tokens.get(i+1).label != TrailerLabel.NUMERIC
                                || tokens.get(i+2).label !=TrailerLabel.NUMERIC
                                || tokens.get(i+3).label != TrailerLabel.REFERENCE) {
                            throw new PdfFormatException("PDF TrailerのRootの後には、参照が来ます。");
                        }

                        TrailerNode refObjToken = tokens.get(++i);
                        TrailerNode refGenToken = tokens.get(++i);
                        i++;

                        root = new PdfReference(Integer.parseInt(refObjToken.value), Integer.parseInt(refGenToken.value));
                        break;
                    }
                    case "/Encrypt": {
                        if (tokens.get(i+1).label != TrailerLabel.NUMERIC
                                || tokens.get(i+2).label !=TrailerLabel.NUMERIC
                                || tokens.get(i+3).label != TrailerLabel.REFERENCE) {
                            throw new PdfFormatException("PDF TrailerのEncryptの後には、参照が来ます。");
                        }

                        TrailerNode refObjToken = tokens.get(++i);
                        TrailerNode refGenToken = tokens.get(++i);
                        i++;

                        encrypt = new PdfReference(Integer.parseInt(refObjToken.value), Integer.parseInt(refGenToken.value));
                        break;
                    }
                    case "/Info": {
                        if (tokens.get(i+1).label != TrailerLabel.NUMERIC
                                || tokens.get(i+2).label !=TrailerLabel.NUMERIC
                                || tokens.get(i+3).label != TrailerLabel.REFERENCE) {
                            throw new PdfFormatException("PDF TrailerのInfoの後には、参照が来ます。");
                        }

                        TrailerNode refObjToken = tokens.get(++i);
                        TrailerNode refGenToken = tokens.get(++i);
                        i++;

                        info = new PdfReference(Integer.parseInt(refObjToken.value), Integer.parseInt(refGenToken.value));
                        break;
                    }
                    case "/ID": {
                        if (tokens.get(i+1).label != TrailerLabel.ARRAY_START
                                || tokens.get(i+2).label != TrailerLabel.STRING
                                || tokens.get(i+3).label != TrailerLabel.STRING
                                || tokens.get(i+4).label != TrailerLabel.ARRAY_END) {
                            throw new PdfFormatException("PDF TrailerのIDの後には、文字列の配列が来ます。");
                        }

                        i++;
                        TrailerNode str1Token = tokens.get(++i);
                        TrailerNode str2Token = tokens.get(++i);
                        i++;

                        id.add(new PdfString(str1Token.value.substring(1, str1Token.value.length()-1)));
                        id.add(new PdfString(str2Token.value.substring(1, str2Token.value.length()-1)));
                        break;
                    }
                }
            } else if (token.label == TrailerLabel.STARTXREF) {
                if (tokens.get(i+1).label != TrailerLabel.NUMERIC) {
                    throw new PdfFormatException("PDF TrailerのStartXRefの後には、数字が来ます。");
                }

                TrailerNode xrefToken = tokens.get(++i);
                startxref = Integer.parseInt(xrefToken.value);
            }

            switch (token.label) {
            }
        }

        return new PdfTrailer(size, prev, root, encrypt, info, id, startxref);
    }

    private class TrailerNode {
        String value;
        TrailerLabel label;

        public TrailerNode(String value, TrailerLabel label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return "TrailerNode{" +
                    "value='" + value + '\'' +
                    ", label=" + label +
                    '}';
        }
    }

    private enum TrailerLabel {
        TRAILER,            // "trailer"
        TRAILER_START,      // "<<"
        TRAILER_END,        // ">>"
        NAME,               // "/"文字列
        NUMERIC,            // 数字
        REFERENCE,          // "R"
        ARRAY_START,        // "["
        ARRAY_END,          // "]"
        STRING,             // 文字列
        STARTXREF,          // "startxref"
        EOF,                // "%%EOF"
    }
}
