package xyz.osamusasa.pdf;

import xyz.osamusasa.pdf.element.primitive.*;

/**
 * PDfファイルのデータオブジェクト
 */
public class PdfFile {

    /**
     * ヘッダーに含まれるPDF文書のバージョン
     */
    private String version;

    /**
     * PDFファイルのボディー部を表すオブジェクト
     */
    private PdfBody body;

    /**
     * PDFファイルのクロスリファレンステーブル部を表すオブジェクト
     */
    private PdfCrossReferenceTable xref;

    /**
     * PDFファイルのトレイラー部を表すオブジェクト
     */
    private PdfTrailer trailer;

    public PdfFile(
            String version,
            PdfBody body,
            PdfCrossReferenceTable xref,
            PdfTrailer trailer
    ) {
        this.version = version;
        this.body = body;
        this.xref = xref;
        this.trailer = trailer;
    }

    @Override
    public String toString() {
        return "PdfFile{" +
                "version='" + version + '\'' +
                ", body=" + body +
                ", xref=" + xref +
                ", trailer=" + trailer +
                '}';
    }
}
