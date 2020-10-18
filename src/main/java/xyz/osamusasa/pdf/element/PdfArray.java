package xyz.osamusasa.pdf.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * PDF文書のarrayを表すクラス
 */
public class PdfArray extends PdfObject {

    /**
     * Array
     *
     * arrayは、１次元の配列でその並び順に意味があるものです。
     * PDFのarrayは、その項目に他のオブジェクトを含めることができます。
     * arrayは、"["と"]"で囲み以下のように表します。
     */
    private List<PdfObject> list;

    /**
     * コンストラクタ
     *
     * @param list Array
     */
    public PdfArray(List<PdfObject> list) {
        this.list = list;
    }

    /**
     * コンストラクタ
     *
     * @param objects Array
     */
    public PdfArray(PdfObject... objects) {
        list = new ArrayList<>(Arrays.asList(objects));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfArray pdfArray = (PdfArray) o;
        return Objects.equals(list, pdfArray.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "PdfArray{" +
                "list=" + list +
                '}';
    }
}
