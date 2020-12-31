package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ページリスト
 */
public class PageList extends Node {

    /**
     * 親ノード
     */
    private PdfReference parent;

    /**
     * このノードの真下にある子ノード群
     */
    private List<PdfReference> kids;

    /**
     * このコードの配下にあるページの数
     * ただしページリストは除く
     */
    private int count;

    /**
     * コンストラクタ
     *
     * @param source source
     */
    public PageList(PdfDictionary source) throws PdfFormatException {
        super();
        parent = source.get("Parent", PdfReference.class);
        /*
         * TODO
         *  kids = source.get("Kids", PdfArray.class);
         *  のようにしたい。そのために、PdfArrayを読み込むときに
         *  kidsタグはPdfReferenceで読み込むようにできるか。
         */
        PdfArray list = source.get("Kids", PdfArray.class);
        kids = new ArrayList<>();
        for (Object object: list) {
            if (!(object instanceof PdfReference)) {
                throw new PdfFormatException("ページリストのKidsはPdfReferenceのリスト");
            }
            kids.add((PdfReference) object);
        }
        count = source.get("Count", PdfInteger.class).getValue();
    }

    /**
     * このノードの直下にあるページのリファレンスのリストを取得する。
     *
     * @return このノードの直下にあるページのリファレンスのリスト
     */
    public List<PdfReference> getChildReferences() {
        return kids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageList)) return false;
        PageList pageList = (PageList) o;
        return count == pageList.count &&
                Objects.equals(parent, pageList.parent) &&
                Objects.equals(kids, pageList.kids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, kids, count);
    }

    @Override
    public String toString() {
        return "PageList{" +
                "parent=" + parent +
                ", kids=" + kids +
                ", count=" + count +
                ", children=" + children +
                '}';
    }
}
