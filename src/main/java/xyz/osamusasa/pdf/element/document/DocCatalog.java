package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfDictionary;
import xyz.osamusasa.pdf.element.primitive.PdfName;
import xyz.osamusasa.pdf.element.primitive.PdfReference;

import java.util.Objects;

/**
 * ドキュメントカタログ
 *
 * オブジェクトグラフの頂点のオブジェクトであり、
 * その他すべてのオブジェクトはここから間接参照を通じて
 * アクセスされる。
 */
public class DocCatalog extends Node {
    private PdfName type;
    private PdfReference pages;
    private Object pageLabels;
    private PdfDictionary names;
    private PdfDictionary dests;
    private PdfDictionary viewerPreferences;
    private PdfName pageLayout;
    private PdfName pageMode;
    private PdfReference outLines;
    private PdfReference metadata;

    /**
     * コンストラクタ
     *
     * @param source source
     */
    public DocCatalog(PdfDictionary source) {
        super();
        this.type = source.get("Type", PdfName.class);
        this.pages = source.get("Pages", PdfReference.class);
        this.pageLabels = source.get("PageLabels", Object.class);
        this.names = source.get("Names", PdfDictionary.class);
        this.dests = source.get("Dests", PdfDictionary.class);
        this.viewerPreferences = source.get("ViewerPreferences", PdfDictionary.class);
        this.pageLayout = source.get("PageLayout", PdfName.class);
        this.pageMode = source.get("PageMode", PdfName.class);
        this.outLines = source.get("OutLines", PdfReference.class);
        this.metadata = source.get("Metadata", PdfReference.class);
    }

    public PdfReference getPages() {
        return pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocCatalog)) return false;
        DocCatalog that = (DocCatalog) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(pages, that.pages) &&
                Objects.equals(pageLabels, that.pageLabels) &&
                Objects.equals(names, that.names) &&
                Objects.equals(dests, that.dests) &&
                Objects.equals(viewerPreferences, that.viewerPreferences) &&
                Objects.equals(pageLayout, that.pageLayout) &&
                Objects.equals(pageMode, that.pageMode) &&
                Objects.equals(outLines, that.outLines) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pages, pageLabels, names, dests, viewerPreferences, pageLayout, pageMode, outLines, metadata);
    }

    @Override
    public String toString() {
        return "DocCatalog{" +
                "type=" + type +
                ", pages=" + pages +
                ", pageLabels=" + pageLabels +
                ", names=" + names +
                ", dests=" + dests +
                ", viewerPreferences=" + viewerPreferences +
                ", pageLayout=" + pageLayout +
                ", pageMode=" + pageMode +
                ", outLines=" + outLines +
                ", metadata=" + metadata +
                ", children=" + children +
                '}';
    }
}
