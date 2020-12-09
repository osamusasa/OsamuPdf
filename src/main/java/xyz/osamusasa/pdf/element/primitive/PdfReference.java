package xyz.osamusasa.pdf.element.primitive;

import java.util.Objects;

public class PdfReference extends PdfObject {
    /**
     * オブジェクト番号
     */
    private int objectNumber;

    /**
     * 世代番号
     */
    private int generation;

    /**
     * コンストラクタ
     *
     * @param objectNumber オブジェクト番号
     * @param generation 世代番号
     */
    public PdfReference(int objectNumber, int generation) {
        this.objectNumber = objectNumber;
        this.generation = generation;
    }

    /**
     * コンストラクタ
     *
     * @param objectNumber オブジェクト番号
     * @param generation 世代番号
     */
    public PdfReference(PdfInteger objectNumber, PdfInteger generation) {
        this.objectNumber = objectNumber.getValue();
        this.generation = generation.getValue();
    }

    public int getObjectNumber() {
        return objectNumber;
    }

    public int getGeneration() {
        return generation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfReference that = (PdfReference) o;
        return objectNumber == that.objectNumber &&
                generation == that.generation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectNumber, generation);
    }

    @Override
    public String toString() {
        return "PdfReference(" + objectNumber + ", " + generation + ')';
    }
}
