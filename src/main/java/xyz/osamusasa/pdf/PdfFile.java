package xyz.osamusasa.pdf;

import xyz.osamusasa.pdf.element.document.DocumentTree;
import xyz.osamusasa.pdf.element.primitive.*;
import xyz.osamusasa.pdf.parser.DocumentTreeDecoder;

import java.awt.*;

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

    /**
     * PDFドキュメント
     */
    private DocumentTree tree;

    /**
     * コンストラクタ
     *
     * @param version バージョン
     * @param body PdfBody
     * @param xref PdfCrossReferenceTable
     * @param trailer PdfTrailer
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public PdfFile(
            String version,
            PdfBody body,
            PdfCrossReferenceTable xref,
            PdfTrailer trailer
    ) throws PdfFormatException {
        this.version = version;
        this.body = body;
        this.xref = xref;
        this.trailer = trailer;

        this.tree = DocumentTreeDecoder.decode(this.body, this.xref, this.trailer);
    }

    /**
     * 指定されたページのサイズを返す
     *
     * @param pos ページ
     * @return ページのサイズ
     */
    public Rectangle getPageSize(int pos) {
        //TODO
        PdfReference root = (PdfReference) trailer.getkRoot();
        PdfNamedObject catalog = body.getObject(root);
        PdfReference pageRef = (PdfReference) ((PdfDictionary)catalog.getValue()).get("Pages");
        System.out.println(pageRef);

        return null;
    }

    @Override
    public String toString() {
        return "PdfFile{" +
                "version='" + version + '\'' +
                ", body=" + body +
                ", xref=" + xref +
                ", trailer=" + trailer +
                ", tree=" + tree +
                '}';
    }
}
