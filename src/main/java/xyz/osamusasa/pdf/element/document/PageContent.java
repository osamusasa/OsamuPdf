package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfStream;

import java.util.Objects;

/**
 * ページのコンテント
 */
public class PageContent extends Node {
    private PdfStream value;

    /**
     * コンストラクタ
     *
     * @param stream PdfStream
     */
    public PageContent(PdfStream stream) {
        this.value = stream;
    }

    @Override
    public String toString() {
        return "PageContent{" +
                "value=" + value +
                ", children=" + children +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageContent)) return false;
        PageContent that = (PageContent) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
