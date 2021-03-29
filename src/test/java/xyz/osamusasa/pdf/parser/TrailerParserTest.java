package xyz.osamusasa.pdf.parser;

import org.junit.jupiter.api.Test;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.PdfArray;
import xyz.osamusasa.pdf.element.primitive.PdfReference;
import xyz.osamusasa.pdf.element.primitive.PdfString;
import xyz.osamusasa.pdf.element.primitive.PdfTrailer;

import static org.junit.jupiter.api.Assertions.*;

class TrailerParserTest {

    @Test
    void readTrailer() {
        String src = "0000005845 00000 n trailer << /Size 18 /Root 12 0 R " +
                "/Info 17 0 R /ID [ <12533dd32ccc13a61660100248240502> <12533dd32ccc13a61660100248240502> ] >> " +
                "startxref 6189 %%EOF";

        try {
            PdfTrailer result = TrailerParser.readTrailer(src);

            PdfArray<PdfString> id = new PdfArray<>();
            id.add(new PdfString("12533dd32ccc13a61660100248240502"));
            id.add(new PdfString("12533dd32ccc13a61660100248240502"));

            PdfTrailer actual = new PdfTrailer(
                    18,
                    -1,
                    new PdfReference(12, 0),
                    null,
                    new PdfReference(17, 0),
                    id,
                    6189);

            assert result != null;
            assertEquals(result.getkSize(), actual.getkSize());
            assertEquals(result.getkPrev(), actual.getkPrev());
            assertEquals(result.getkRoot(), actual.getkRoot());
            assertEquals(result.getkEncrypt(), actual.getkEncrypt());
            assertEquals(result.getkInfo(), actual.getkInfo());
            assertEquals(result.getkID(), actual.getkID());
            assertEquals(result.getStartxref(), actual.getStartxref());
            assertEquals(result, actual);

        } catch (PdfFormatException e) {
            e.printStackTrace();
            assert false;
        }
    }
}