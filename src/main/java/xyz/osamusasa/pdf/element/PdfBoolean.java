package xyz.osamusasa.pdf.element;

public class PdfBoolean extends PdfNamedObject {

    /**
     * 真偽値
     */
    private boolean value;

    /**
     * コンストラクタ
     *
     * @param objectNumber オブジェクト番号
     * @param generation   世代番号
     */
    public PdfBoolean(int objectNumber, int generation, boolean value) {
        super(objectNumber, generation);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PdfBoolean{" +
                "value=" + value +
                '}';
    }
}
