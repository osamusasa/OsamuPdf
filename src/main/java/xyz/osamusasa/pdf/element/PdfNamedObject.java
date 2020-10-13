package xyz.osamusasa.pdf.element;

import java.util.Objects;

/**
 * PDF文書のインダイレクト・オブジェクトを表すクラス
 */
public class PdfNamedObject extends PdfObject {
    /**
     * オブジェクト番号
     */
    private int objectNumber;

    /**
     * 世代番号
     */
    private int generation;

    /**
     * オブジェクト
     */
    private PdfObject value;

    /**
     * コンストラクタ
     *
     * @param objectNumber オブジェクト番号
     * @param generation 世代番号
     * @deprecated
     */
    public PdfNamedObject(int objectNumber, int generation) {
        this.objectNumber = objectNumber;
        this.generation = generation;
    }

    /**
     * コンストラクタ
     *
     * @param objectNumber オブジェクト番号
     * @param generation 世代番号
     * @param value オブジェクト
     */
    public PdfNamedObject(int objectNumber, int generation, PdfObject value) {
        this.objectNumber = objectNumber;
        this.generation = generation;
        this.value = value;
    }

    public int getObjectNumber() {
        return objectNumber;
    }

    public int getGeneration() {
        return generation;
    }

    public PdfObject getValue() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfNamedObject)) return false;
        PdfNamedObject pdfObject = (PdfNamedObject) o;
        return objectNumber == pdfObject.objectNumber &&
                generation == pdfObject.generation;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(objectNumber, generation);
    }

    @Override
    public String toString() {
        return "PdfNamedObject{" +
                "objectNumber=" + objectNumber +
                ", generation=" + generation +
                ", value=" + value +
                '}';
    }
}
