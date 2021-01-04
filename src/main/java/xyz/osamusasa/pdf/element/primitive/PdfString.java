package xyz.osamusasa.pdf.element.primitive;

import java.util.Objects;

/**
 * PDF文書のstringを表すクラス
 *
 * stringは、バイトデータの並びを表現します。
 * いわゆる文字列データ（literal string）やバイナリーデータ（hexadecimal string）を表します。
 * 文字列データは、"( )"で囲み以下のように表します。
 */
public class PdfString extends PdfObject {
    /**
     * value
     */
    private String value;

    /**
     * バイナリであるか
     *  true:   バイナリデータ
     *  false:  文字列データ
     */
    private boolean isBinary;

    /**
     * コンストラクタ
     *
     * @param value value
     */
    public PdfString(String value) {
        this(value, false);
    }

    /**
     * コンストラクタ
     *
     * @param value value
     * @param isBinary バイナリであるか
     */
    public PdfString(String value, boolean isBinary) {
        this.value = value;
        this.isBinary = isBinary;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getString() throws PdfPrimitiveTypeException {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfString pdfString = (PdfString) o;
        return Objects.equals(value, pdfString.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PdfString{" +
                "value='" + value + '\'' +
                '}';
    }
}
