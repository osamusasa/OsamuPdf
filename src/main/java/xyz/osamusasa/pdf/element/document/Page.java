package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.*;

import java.awt.*;
import java.util.Objects;

/**
 * ページ
 */
public class Page extends Node {

    /**
     * 親ノード
     */
    private PdfReference parent;

    /**
     * ページのリソース
     *
     * 省略された場合は、親ノードから継承される。
     * リソースがない場合は、空の辞書が格納される。
     */
    private PdfDictionary resource;

    /**
     * グラフィカルなコンテンツ
     */
    private PdfReference contents;

    /**
     * 閲覧時のページの向き
     *
     * 上を起点に時計回りであらわし、値は90の倍数になる。
     * 省略された場合は、親ノードから継承される。
     */
    private int rotate;

    /**
     * 用紙のサイズ
     *
     * 省略された場合は、親ノードから継承される。
     */
    private Rectangle mediaBox;

    /**
     * ページのクロップボックス
     *
     * 表示や印刷する際の可視領域を表す。
     * 省略された場合は、メディアボックスと同じ値になる。
     */
    private Rectangle cropBox;

    /**
     * コンストラクタ
     *
     * @param source source
     */
    public Page(PdfDictionary source) {
        super();
        this.parent = source.get("Parent", PdfReference.class);
        this.resource = source.get("Resources", PdfDictionary.class);
        this.contents = source.get("Contents", PdfReference.class);
        PdfInteger _rotate = source.get("Parent", PdfInteger.class);
        if (_rotate == null) {
            this.rotate = 0;
        } else {
            this.rotate = _rotate.getValue();
        }
        PdfArray mediaBoxArr = source.get("MediaBox", PdfArray.class);
        PdfArray cropBoxArr = source.get("CropBox", PdfArray.class);
        this.mediaBox = new Rectangle();
        this.cropBox = null;

        if (mediaBoxArr != null && mediaBoxArr.size() == 4) {
            try {
                this.mediaBox = new Rectangle(
                        mediaBoxArr.get(0).getInt(),
                        mediaBoxArr.get(1).getInt(),
                        mediaBoxArr.get(2).getInt(),
                        mediaBoxArr.get(3).getInt()
                );
            } catch (PdfPrimitiveTypeException e) {
                e.printStackTrace();
            }

        }
    }

    public PdfDictionary getResource() {
        return resource;
    }

    public PdfReference getContents() {
        return contents;
    }

    public Rectangle getMediaBox() {
        return mediaBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return rotate == page.rotate &&
                parent.equals(page.parent) &&
                Objects.equals(resource, page.resource) &&
                Objects.equals(contents, page.contents) &&
                mediaBox.equals(page.mediaBox) &&
                Objects.equals(cropBox, page.cropBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, resource, contents, rotate, mediaBox, cropBox);
    }

    @Override
    public String toString() {
        return "Page{" +
                "parent=" + parent +
                ", resource=" + resource +
                ", contents=" + contents +
                ", rotate=" + rotate +
                ", mediaBox=" + mediaBox +
                ", cropBox=" + cropBox +
                ", children=" + children +
                '}';
    }
}
