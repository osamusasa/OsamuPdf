import xyz.osamusasa.pdf.PdfFile;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.io.PdfReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File f = new File(Main.class.getResource("HelloWorld.pdf").getPath());

        try {
            PdfFile pdf = PdfReader.readPdf(f);

            System.out.println(indent(pdf.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | PdfFormatException e) {
            e.printStackTrace();
        }
    }

    private static String indent(String str) {
        String retCode = "\n";
        String indent = "    ";
        int indentCnt = 0;
        StringBuilder sb = new StringBuilder(str.length());

        for (char c: str.toCharArray()) {
            if (c == '{' || c == '[') {
                indentCnt++;
                sb.append(c);
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
            } else if (c == '}' || c == ']') {
                indentCnt--;
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
                sb.append(c);
            } else if (c == ',') {
                sb.append(c);
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
            } else if(c == ' ') {

            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
