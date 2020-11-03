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
        boolean paren = false;
        int byteStream = 0;
        StringBuilder sb = new StringBuilder(str.length());

        for (char c: str.toCharArray()) {
            if (paren && c == ')') {
                paren = false;
                sb.append(c);
            } else if (c == '(') {
                paren = true;
                sb.append(c);
            } else if(byteStream == 5 && c == '[') {
                paren = true;
                byteStream = 0;
                sb.append(c);
            } else if(paren && c == ']') {
                paren = false;
                sb.append(c);
            } else if (c == '{' || c == '[') {
                indentCnt++;
                sb.append(c);
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
            } else if (c == '}' || c == ']') {
                indentCnt--;
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
                sb.append(c);
            } else if (paren && c == ',') {
                sb.append(c);
            } else if (c == ',') {
                sb.append(c);
                sb.append(retCode);
                sb.append(indent.repeat(indentCnt));
            } else if(c == 'b') {
                byteStream++;
                sb.append(c);
            } else if(byteStream == 1 && c == 'y') {
                byteStream++;
                sb.append(c);
            } else if(byteStream == 2 && c == 't') {
                byteStream++;
                sb.append(c);
            } else if(byteStream == 3 && c == 'e') {
                byteStream++;
                sb.append(c);
            } else if(byteStream == 4 && c == 's') {
                byteStream++;
                sb.append(c);
            } else if(byteStream == 5 && c == '=') {
                sb.append(c);
            } else if(c == ' ') {

            } else if(c == ' ') {

            } else if(c == ' ') {

            } else if(c == ' ') {

            } else if(c == ' ') {

            } else if(c == ' ') {

            } else {
                byteStream = 0;
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
