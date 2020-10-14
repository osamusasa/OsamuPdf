package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.element.PdfNamedObject;

public class PdfNamedObjectParser {
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
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @return 解析したインダイレクト・オブジェクト
     */
    private PdfNamedObject parse() {
        return null;
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @param source ソース
     * @return 解析したインダイレクト・オブジェクト
     */
    public static PdfNamedObject parse(Byte[] source) {
        PdfNamedObjectParser parser = new PdfNamedObjectParser(source);
        return parser.parse();
    }

}
