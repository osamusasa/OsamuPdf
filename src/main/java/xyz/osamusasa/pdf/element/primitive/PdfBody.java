package xyz.osamusasa.pdf.element.primitive;

import java.util.Arrays;
import java.util.List;

/**
 * PDFファイルのボディー部を表すクラス
 */
public class PdfBody {
    /**
     * インダイレクト・オブジェクトのリスト
     */
    private List<PdfNamedObject> objects;

    /**
     * コンストラクタ
     *
     * @param objects インダイレクト・オブジェクトのリスト
     */
    public PdfBody(List<PdfNamedObject> objects) {
        this.objects = objects;
    }

    /**
     * コンストラクタ
     *
     * @param objects インダイレクト・オブジェクトのリスト
     */
    public PdfBody(PdfNamedObject... objects) {
        this(Arrays.asList(objects));
    }

    /**
     * オブジェクト番号と世代番号で定まる一つのインダイレクト・オブジェクトを返す
     *
     * 存在しなかったときは、nullを返す
     *
     * @param objectNumber オブジェクト番号
     * @param generation 世代番号
     * @return 特定のインダイレクト・オブジェクト
     */
    public PdfNamedObject getObject(int objectNumber, int generation) {
        int index = objects.indexOf(new PdfNamedObject(objectNumber, generation));
        if (index == -1) {
            return null;
        } else {
            return objects.get(index);
        }
    }

    /**
     * オブジェクト番号と世代番号で定まる一つのインダイレクト・オブジェクトを返す
     *
     * 存在しなかったときは、nullを返す
     *
     * @param reference 参照オブジェクト
     * @return 特定のインダイレクト・オブジェクト
     */
    public PdfNamedObject getObject(PdfReference reference) {
        return getObject(reference.getObjectNumber(), reference.getGeneration());
    }

    @Override
    public String toString() {
        return "PdfBody{" +
                "objects=" + objects +
                '}';
    }
}
