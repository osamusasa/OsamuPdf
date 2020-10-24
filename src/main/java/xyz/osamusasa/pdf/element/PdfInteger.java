package xyz.osamusasa.pdf.element;

import java.util.Objects;

/**
 * PDF文書のintegerを表すクラス
 *
 * 数値の値は-2,147,483,648 から 4,147,483,647 まで
 */
public class PdfInteger extends PdfNumber {

    /**
     * value
     */
    private int value;

    /**
     * コンストラクタ
     *
     * @param value value
     */
    public PdfInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfInteger that = (PdfInteger) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PdfInteger{" +
                "value=" + value +
                '}';
    }
}
