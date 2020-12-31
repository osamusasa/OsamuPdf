package xyz.osamusasa.pdf.util;

import org.junit.jupiter.api.Test;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PdfObjectSchemaTest {

    @Test
    void valid() throws PdfFormatException {
        PdfNamedObject pdfNamedObject= new PdfNamedObject(
                1,
                1,
                new PdfDictionary(
                        new PdfName("first"),
                        new PdfInteger(3),
                        new PdfName("second"),
                        new PdfString("str")
                )
        );
        Map<String, Object> schema1 = new HashMap<>();
        schema1.put("first", true);
        schema1.put("second", false);
        schema1.put("third", false);

        Map<String, Object> schema2 = new HashMap<>();
        schema2.put("third", true);

        Map<String, Object> schema3 = new HashMap<>();
        schema3.put("first", new PdfInteger(3));

        Map<String, Object> schema4 = new HashMap<>();
        schema4.put("first", new PdfInteger(2));

        assertEquals(2, PdfObjectSchema.valid(pdfNamedObject, schema1));
        assertEquals(-1, PdfObjectSchema.valid(pdfNamedObject, schema2));
        assertEquals(1, PdfObjectSchema.valid(pdfNamedObject, schema3));
        assertEquals(-1, PdfObjectSchema.valid(pdfNamedObject, schema4));
    }
}