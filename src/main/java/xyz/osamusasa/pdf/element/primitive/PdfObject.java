package xyz.osamusasa.pdf.element.primitive;

/**
 * PDF文書のオブジェクトを表すスーパークラス
 */
public abstract class PdfObject {

    public int getInt() throws PdfPrimitiveTypeException {
        throw new PdfPrimitiveTypeException("Object can't cast to int");
    }
    public boolean getBoolean() throws PdfPrimitiveTypeException {
        throw new PdfPrimitiveTypeException("Object can't cast to boolean");
    }
    public String getString() throws PdfPrimitiveTypeException {
        throw new PdfPrimitiveTypeException("Object can't cast to String");
    }
}
