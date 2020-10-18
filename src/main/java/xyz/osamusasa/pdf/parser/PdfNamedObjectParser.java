package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.*;
import xyz.osamusasa.pdf.util.ByteArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfNamedObjectParser {
    /**
     * ソースから次に読み込む位置を示す。
     */
    private int pos;

    /**
     * ソース
     */
    private Byte[] source;

    /**
     * コンストラクタ
     *
     * @param source バイト配列
     */
    private PdfNamedObjectParser(Byte[] source) {
        this.source = source;
        this.pos = 0;
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @return 解析したインダイレクト・オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfNamedObject parse() throws PdfFormatException {
        String firstLine = new String(ByteArrayUtil.subAryUntilReturn(source, 0));
        String[] firstLines = firstLine.split(" ");
        if (!(firstLines.length == 3 && "obj".equals(firstLines[2]))) {
            throw new PdfFormatException("named object needs to start \"obj\" keyword");
        }
        int objectNumber = Integer.valueOf(firstLines[0]);
        int generation = Integer.valueOf(firstLines[1]);

        pos = firstLine.length();

        PdfObject object = s1();

        return new PdfNamedObject(objectNumber, generation, object);
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @param source ソース
     * @return 解析したインダイレクト・オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public static PdfNamedObject parse(Byte[] source) throws PdfFormatException {
        PdfNamedObjectParser parser = new PdfNamedObjectParser(source);
        return parser.parse();
    }

    /**
     * すべてのオブジェクトへの分岐
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s1() throws PdfFormatException {
//        System.out.println("s1(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        switch (source[pos]) {
            case '(': {

            }
            case '<': {
                pos++;
                return s2();
            }
            case '/': {
                pos++;
                return s5();
            }
            case '[': {
                return s6();
            }
            case 's': {

            }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {

            }
            case ' ':
            case '\r':
            case '\n': {
                pos++;
                return s1();
            }
        }
        return null;
    }

    /**
     * バイナリorディクショナリ
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s2() throws PdfFormatException {
//        System.out.println("s2(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        switch (source[pos]) {
            case '<': {
                pos++;
                return s3();
            }
            default: {

            }
        }
        return null;
    }

    /**
     * ディクショナリ
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s3() throws PdfFormatException {
//        System.out.println("s3(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        Map<PdfName, PdfObject> dict = new HashMap<>();
        while (source[pos] != '>') {
            PdfName name = (PdfName) s4();
            PdfObject object = s1();
            dict.put(name, object);
            skipWhiteSpace();
        }

        pos++;
        if (source[pos] != '>') {
            throw new PdfFormatException("dictionaryは\">>\"で終わります。");
        } else {
            pos++;
        }

        return new PdfDictionary(dict);
    }

    /**
     * PdfNameが始まる
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s4() throws PdfFormatException {
//        System.out.println("s4(" + source[pos] + "," + (char)(byte)source[pos] + ")");

        skipWhiteSpace();

        if (source[pos] == '/') {
            pos++;
            return s5();
        } else {
            throw new PdfFormatException("PdfNameは/から始まります");
        }
    }

    /**
     * PdfNameの内容
     *
     * @return オブジェクト
     */
    private PdfObject s5() {
        System.out.println("s5(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        int start = pos;
        while (isNameChar(source[pos])) {
            pos++;
        }
        return new PdfName(ByteArrayUtil.subString(source, start, pos));
    }

    /**
     * arrayが始まる
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s6() throws PdfFormatException {
        System.out.println("s6(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        skipWhiteSpace();

        if (source[pos] == '[') {
            pos++;
            return s7();
        } else {
            throw new PdfFormatException("PdfArrayは[から始まります");
        }
    }

    /**
     * arrayの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s7() throws PdfFormatException {
        System.out.println("s7(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        List<PdfObject> list = new ArrayList<>();

        skipWhiteSpace();
        while (source[pos] != ']') {
            PdfObject object = s1();
            list.add(object);
            skipWhiteSpace();
        }

        pos++;

        return new PdfArray(list);
    }


    /**
     * PdfNameを構成する文字であるか。
     *
     * nameは、NULL以外のASCII文字を連続して並べたもの
     *
     * @param c 文字
     * @return PdfNameを構成する文字であるか
     */
    private boolean isNameChar(byte c) {
        return 33 <= c && c <= 126 && c != 47;
    }

    /**
     * ホワイトスペースでない文字が出るまでposを進める
     *
     * ' '、'\r'、'\n'をスキップする
     */
    private void skipWhiteSpace() {
        while (source[pos] == ' ' || source[pos] == '\r' || source[pos] == '\n') {
            pos++;
        }
    }
}
