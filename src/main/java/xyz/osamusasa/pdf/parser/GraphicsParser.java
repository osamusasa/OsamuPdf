package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.element.graphics.PageContent;
import xyz.osamusasa.pdf.element.graphics.PdfText;

import java.util.*;

public class GraphicsParser {

    public static List<PageContent> parse(String source) {
        List<PageContent> list = new ArrayList<>();

        String[] tokens = source.split("\\s");
        Deque<String> queue = new ArrayDeque<>();
        Iterator<String> itr = Arrays.asList(tokens).iterator();

        String token;
        while (itr.hasNext()) {
            token = itr.next();

            switch (token) {
                case "BT":
                    list.addAll(text(itr));
            }
        }


        return list;
    }

    /**
     * PdfTextをパースする
     *
     * @param itr tokens
     * @return PdfTextのリスト
     */
    private static List<PageContent> text(Iterator<String> itr) {
        List<PageContent> list = new ArrayList<>();
        Deque<String> queue = new ArrayDeque<>();
        PdfText text = new PdfText();

        String token;
        roop:
        while (itr.hasNext()) {
            token = itr.next();

            switch (token) {
                case "Tm":
                    int f = Integer.valueOf(queue.pop());
                    int e = Integer.valueOf(queue.pop());
                    int d = Integer.valueOf(queue.pop());
                    int c = Integer.valueOf(queue.pop());
                    int b = Integer.valueOf(queue.pop());
                    int a = Integer.valueOf(queue.pop());
                    text.Tm(a, b, c, d, e, f);
                    break;
                case "Tf":
                    int size = Integer.valueOf(queue.pop());
                    String font = queue.pop();
                    text.Tf(font ,size);
                    break;
                case "Ts":
                    int rise = Integer.valueOf(queue.pop());
                    text.Ts(rise);
                    break;
                case "Tr":
                    int render = Integer.valueOf(queue.pop());
                    text.Tr(render);
                    break;
                case "Tc":
                    int charSpace = Integer.valueOf(queue.pop());
                    text.Tc(charSpace);
                    break;
                case "Tw":
                    int wordSpace = Integer.valueOf(queue.pop());
                    text.Tw(wordSpace);
                    break;
                case "Tj":
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String str = queue.pop();
                        sb.insert(0, str + ' ');
                        if (str.contains("(")) {
                            break;
                        }
                    }
                    String ret = sb.toString();
                    text.setText(ret.substring(ret.indexOf('(') + 1, ret.indexOf(')')));
                    break;
                case "ET":
                    break roop;
                default:
                    queue.push(token);
            }
        }

        list.add(text);

        return list;
    }
}
