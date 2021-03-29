package xyz.osamusasa.pdf.element.primitive;

import java.util.Objects;

/**
 * PDF文書のrealを表すクラス
 *
 * 数値の範囲は以下
 *  ±3.403x1038（最大・最小値）、
 *  ±1.175x10-38（0ではない一番小さな値）、
 *  5（精度、小数点以下の桁数）
 */
public class PdfReal extends PdfNumber {
    /**
     * value
     */
    private double value;

    /**
     * コンストラクタ
     *
     * @param value value
     */
    public PdfReal(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PdfReal{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfReal)) return false;
        PdfReal pdfReal = (PdfReal) o;
        return Double.compare(pdfReal.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int getInt() throws PdfPrimitiveTypeException {
        return (int) getValue();
    }
}
