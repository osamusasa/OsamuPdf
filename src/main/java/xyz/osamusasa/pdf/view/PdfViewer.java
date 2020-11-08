package xyz.osamusasa.pdf.view;

import xyz.osamusasa.pdf.PdfFile;
import xyz.osamusasa.pdf.element.PdfStream;
import xyz.osamusasa.pdf.element.graphics.PageContent;
import xyz.osamusasa.pdf.element.graphics.PdfText;
import xyz.osamusasa.pdf.parser.GraphicsParser;
import xyz.osamusasa.pdf.parser.StreamDecoder;

import javax.swing.*;
import java.awt.*;

public class PdfViewer {

    public static Frame getPdfViewFrame(PdfFile pdf) {
        JFrame frame = new JFrame("pdf viewer"); //TODO ファイル名が欲しい
        JPanel pan = new JPanel();

        byte[] bytes = new byte[]{120,-100,115,10,-31,50,84,48,0,66,67,5,67,3,3,5,51,32,14,-55,-27,-46,15,118,51,84,48,5,50,-45,-72,-128,68,49,80,58,-92,8,68,36,-125,-120,114,46,13,-113,-44,-100,-100,124,29,-123,-16,-4,-94,-100,20,61,77,-123,-112,44,46,-41,16,46,0,96,-2,17,-126,10,13,10};
        String decode = StreamDecoder.decode(new PdfStream(null, bytes), "FlateDecode");
//        BT
//        1 0 0 1 100 600 Tm
//        /SF1 50 Tf
//        0 Ts 0 Tr 0 Tc 0 Tw
//        (Hello, World.) Tj
//        ET
        java.util.List<PageContent> list = GraphicsParser.parse(decode);
        System.out.println(list);
        Canvas canvas = new Canvas(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                g.setColor(Color.RED);
                for (PageContent pc: list) {
                    int[] ary = ((PdfText)pc).getTextMatrix();
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                    g.drawString(((PdfText) pc).getText(), ary[6], ary[7]);
                }
            }
        };

        canvas.setBounds(0, 0, 1000, 700);

        pan.add(canvas);
        frame.getContentPane().add(pan);

        frame.setBounds(100,100,1000,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
}
