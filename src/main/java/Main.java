import xyz.osamusasa.pdf.PdfFile;
import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.io.PdfReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xyz.osamusasa.pdf.view.PdfViewer.getPdfViewFrame;

public class Main {
    public static void main(String[] args) {
//        File f = new File(Main.class.getResource("HelloWorld.pdf").getPath());
        File f = new File(Main.class.getResource("pdf_reference_1-7.pdf").getPath());
        long start = System.currentTimeMillis();

        try {
            PdfFile pdf = PdfReader.readPdf(f);

            File file = null;
//            try {
//                Path tmpPath = Files.createTempFile(Paths.get("/tmp"), "prefix", ".suffix");
//                file = tmpPath.toFile();
//
//                // ファイルに書き込み
//                FileWriter fw = new FileWriter(file, true);
//                BufferedWriter bw = new BufferedWriter(fw);
//                PrintWriter pw = new PrintWriter(bw);
//                pw.write(indent(pdf.toString()));
//                pw.flush();
//                pw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(indent(pdf.toString()));

            getPdfViewFrame(pdf).setVisible(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | PdfFormatException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("処理時間：" + (end - start)  + "ms");
    }

    private static String indent(String str) {
        String retCode = "\n";
        String indent = "    ";
        int indentCnt = 0;
        boolean paren = false;
        int byteStream = 0;
        boolean isEscaped = false;
        StringBuilder sb = new StringBuilder(str.length());

        for (char c: str.toCharArray()) {
            if (isEscaped) {
                sb.append(c);
                isEscaped = false;
            }else if (paren && c == ')') {
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
                sb.append(indent.repeat(Math.max(indentCnt, 0)));
            } else if (c == '}' || c == ']') {
                indentCnt--;
                sb.append(retCode);
                sb.append(indent.repeat(Math.max(indentCnt, 0)));
                sb.append(c);
            } else if (paren && c == ',') {
                sb.append(c);
            } else if (c == ',') {
                sb.append(c);
                sb.append(retCode);
                sb.append(indent.repeat(Math.max(indentCnt, 0)));
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
            } else if(c == '\\') {
                isEscaped = true;
                sb.append(c);
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
