package xyz.osamusasa.pdf.element;

import java.util.Objects;

/**
 * PDF文書のNameを表すクラス
 */
public class PdfName extends PdfObject {
    /**
     * Name
     *
     * NULL以外のASCII文字を連続して並べたもの
     */
    private String value;

    public PdfName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfName pdfName = (PdfName) o;
        return value.equals(pdfName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "/" + value;
    }
}
