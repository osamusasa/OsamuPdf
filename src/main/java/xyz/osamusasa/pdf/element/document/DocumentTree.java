package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfBody;
import xyz.osamusasa.pdf.element.primitive.PdfCrossReferenceTable;
import xyz.osamusasa.pdf.element.primitive.PdfTrailer;

import java.util.List;

/**
 * PDFドキュメント
 */
public class DocumentTree {
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
     * ドキュメントツリーのルート要素
     */
    private Node root;

    /**
     * 各ページへの参照
     */
    private List<Node> pageList;

    @Deprecated
    public DocumentTree(PdfBody body, PdfCrossReferenceTable xref, PdfTrailer trailer) {
        this.body = body;
        this.xref = xref;
        this.trailer = trailer;
    }

    /**
     * コンストラクタ
     *
     * @param body PdfBody
     * @param xref PdfCrossReferenceTable
     * @param trailer PdfTrailer
     * @param root ルートノード
     * @param pageList すべてのページのリスト
     */
    public DocumentTree(PdfBody body, PdfCrossReferenceTable xref, PdfTrailer trailer, Node root, List<Node> pageList) {
        this.body = body;
        this.xref = xref;
        this.trailer = trailer;
        this.root = root;
        this.pageList = pageList;
    }

    /**
     * 各ページへの参照リストの長さを返す
     *
     * @return ページ数
     */
    public int getPageSize() {
        return pageList.size();
    }

    /**
     * 指定された位置のPageを返す
     *
     * @param pos 位置
     * @return Page
     */
    public Page getPage(int pos) {
        return (Page) pageList.get(pos);
    }

    @Override
    public String toString() {
        return "DocumentTree{" +
                "root=" + root +
                ", pageList=" + pageList +
                '}';
    }
}
