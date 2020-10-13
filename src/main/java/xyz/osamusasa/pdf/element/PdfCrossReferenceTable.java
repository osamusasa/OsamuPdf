package xyz.osamusasa.pdf.element;

import java.util.List;

/**
 * PDFファイルのクロスリファレンステーブル部を表すクラス
 */
public class PdfCrossReferenceTable {
    private int start;
    private int len;
    private List<PdfCrossReferenceTableRow> crossReferences;

    public PdfCrossReferenceTable(int start, int len, List<PdfCrossReferenceTableRow> crossReferences) {
        this.start = start;
        this.len = len;
        this.crossReferences = crossReferences;
    }

    public int getStart() {
        return start;
    }

    public int getLen() {
        return len;
    }

    public List<PdfCrossReferenceTableRow> getCrossReferences() {
        return crossReferences;
    }

    @Override
    public String toString() {
        return "PdfCrossReferenceTable{" +
                "start=" + start +
                ", len=" + len +
                ", crossReferences=" + crossReferences +
                '}';
    }
}
