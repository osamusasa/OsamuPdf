package xyz.osamusasa.pdf.element.primitive;

public class PdfBoolean extends PdfObject {

    /**
     * 真偽値
     */
    private boolean value;

    /**
     * コンストラクタ
     *
     * @param value value
     */
    public PdfBoolean(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public boolean getBoolean() throws PdfPrimitiveTypeException {
        return value;
    }

    @Override
    public String toString() {
        return "PdfBoolean{" +
                "value=" + value +
                '}';
    }
}
