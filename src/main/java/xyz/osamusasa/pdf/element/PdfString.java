package xyz.osamusasa.pdf.element;

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
     * コンストラクタ
     *
     * @param value value
     */
    public PdfString(String value) {
        this.value = value;
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
