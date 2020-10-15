package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.PdfDictionary;
import xyz.osamusasa.pdf.element.PdfName;
import xyz.osamusasa.pdf.element.PdfNamedObject;
import xyz.osamusasa.pdf.element.PdfObject;
import xyz.osamusasa.pdf.util.ByteArrayUtil;

import java.util.HashMap;
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
     */
    public static PdfNamedObject parse(Byte[] source) throws PdfFormatException {
        PdfNamedObjectParser parser = new PdfNamedObjectParser(source);
        return parser.parse();
    }

    /**
     * すべてのオブジェクトへの分岐
     *
     * @return オブジェクト
     */
    private PdfObject s1() throws PdfFormatException {
        switch (source[pos]) {
            case '(': {

            }
            case '<': {
                pos++;
                return s2();
            }
            case '/': {

            }
            case '[': {

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
        }
        return null;
    }

    /**
     * バイナリorディクショナリ
     *
     * @return オブジェクト
     */
    private PdfObject s2() throws PdfFormatException {
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
     */
    private PdfObject s3() throws PdfFormatException {
        Map<PdfName, PdfObject> dict = new HashMap<>();
        while (source[pos] != '>') {
            PdfName name = (PdfName) s4();
            PdfObject object = s1();
            dict.put(name, object);
        }
        return new PdfDictionary(dict);
    }

    /**
     * PdfNameが始まる
     *
     * @return オブジェクト
     */
    private PdfObject s4() throws PdfFormatException {
        if (source[pos] == '/') {
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
        int start = pos;
        while (isNameChar(source[pos])) {
            pos++;
        }
        return null;
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
}
