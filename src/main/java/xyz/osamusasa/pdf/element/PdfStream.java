package xyz.osamusasa.pdf.element;

import java.util.Arrays;
import java.util.Objects;

/**
 * PDF文書のstreamを表すクラス
 */
public class PdfStream extends PdfObject {

    /**
     * PdfDictionary
     */
    private PdfDictionary dict;

    /**
     * バイトデータ
     */
    private byte[] value;

    /**
     * コンストラクタ
     *
     * @param dict PdfDictionary
     * @param value バイトデータ
     */
    public PdfStream(PdfDictionary dict, byte[] value) {
        this.dict = dict;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfStream pdfStream = (PdfStream) o;
        return Objects.equals(dict, pdfStream.dict) &&
                Arrays.equals(value, pdfStream.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dict);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        return "PdfStream{" +
                "dict=" + dict +
                ", bytes=" + Arrays.toString(value) +
                '}';
    }
}
