package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.primitive.*;
import xyz.osamusasa.pdf.util.ByteArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfNamedObjectParser {
    /**
     * ソースから次に読み込む位置を示す。
     */
    private int pos;

    /**
     * ソース
     */
    private Byte[] source;

    /**
     * コンストラクタ
     *
     * @param source バイト配列
     */
    private PdfNamedObjectParser(Byte[] source) {
        this.source = source;
        this.pos = 0;
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @return 解析したインダイレクト・オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfNamedObject parse() throws PdfFormatException {
        String firstLine = new String(ByteArrayUtil.subAryUntilReturn(source, 0));
        String[] firstLines = firstLine.split(" ");
        if (!(firstLines.length == 3 && "obj".equals(firstLines[2]))) {
            throw new PdfFormatException("named object needs to start \"obj\" keyword");
        }
        int objectNumber = Integer.valueOf(firstLines[0]);
        int generation = Integer.valueOf(firstLines[1]);

        pos = firstLine.length();

        PdfObject object = s1();

        return new PdfNamedObject(objectNumber, generation, object);
    }

    /**
     * ソースを解析して、インダイレクト・オブジェクトを生成する。
     *
     * @param source ソース
     * @return 解析したインダイレクト・オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public static PdfNamedObject parse(Byte[] source) throws PdfFormatException {
        PdfNamedObjectParser parser = new PdfNamedObjectParser(source);
        return parser.parse();
    }

    /**
     * すべてのオブジェクトへの分岐
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s1() throws PdfFormatException {
//        System.out.println("s1(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        switch (source[pos]) {
            case '(': {
                return s11();
            }
            case '<': {
                pos++;
                return s2();
            }
            case '/': {
                pos++;
                return s5();
            }
            case '[': {
                return s6();
            }
            case 's': {

            }
            case 't':
            case 'f': {
                return s13();
            }
            case 'n': {
                return s15();
            }
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                return s8();
            }
            case ' ':
            case '\r':
            case '\n': {
                pos++;
                return s1();
            }
        }
        return null;
    }

    /**
     * バイナリorディクショナリorストリーム
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s2() throws PdfFormatException {
//        System.out.println("s2(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        switch (source[pos]) {
            case '<': {
                pos++;
                return s3();
            }
        }
        return s14();
    }

    /**
     * ディクショナリorストリーム
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s3() throws PdfFormatException {
//        System.out.println("s3(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        Map<PdfName, PdfObject> dict = new HashMap<>();

        skipWhiteSpace();
        while (source[pos] != '>') {
            PdfName name = (PdfName) s4();
            PdfObject object = s1();
            dict.put(name, object);
            skipWhiteSpace();
        }

        pos++;
        if (source[pos] != '>') {
            throw new PdfFormatException("dictionaryは\">>\"で終わります。");
        } else {
            pos++;
        }

        skipWhiteSpace();

        if (match("stream")) {
            return new PdfStream(new PdfDictionary(dict), s10());
        } else {
            return new PdfDictionary(dict);
        }
    }

    /**
     * PdfNameが始まる
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s4() throws PdfFormatException {
//        System.out.println("s4(" + source[pos] + "," + (char)(byte)source[pos] + ")");

        skipWhiteSpace();

        if (source[pos] == '/') {
            pos++;
            return s5();
        } else {
            printErrMsg();
            throw new PdfFormatException("PdfNameは/から始まります");
        }
    }

    /**
     * PdfNameの内容
     *
     * @return オブジェクト
     */
    private PdfObject s5() {
//        System.out.println("s5(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        int start = pos;
        while (isNameChar(source[pos])) {
            pos++;
        }
        return new PdfName(ByteArrayUtil.subString(source, start, pos));
    }

    /**
     * arrayが始まる
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s6() throws PdfFormatException {
//        System.out.println("s6(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        skipWhiteSpace();

        if (source[pos] == '[') {
            pos++;
            return s7();
        } else {
            throw new PdfFormatException("PdfArrayは[から始まります");
        }
    }

    /**
     * arrayの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s7() throws PdfFormatException {
//        System.out.println("s7(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        List<PdfObject> list = new ArrayList<>();

        skipWhiteSpace();
        while (source[pos] != ']') {
            PdfObject object = s1();

            if (object == null) {
                printErrMsg();
                throw new PdfFormatException("配列の内容がnullです");
            }

            list.add(object);
            skipWhiteSpace();
        }

        pos++;

        return new PdfArray(list);
    }

    /**
     * PdfNumberが始まる
     *
     * インダイレクト・オブジェクトへの参照の可能性もあるので、
     * ２つ先のオブジェクトまで先読みして、参照でなければposを戻す
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s8() throws PdfFormatException {
//        System.out.println("s8(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        PdfObject o1 = s9();

        skipWhiteSpace();

        if (!isNumberChar(source[pos])) {
            return o1;
        }

        int current = pos;
        PdfObject o2 = s9();

        skipWhiteSpace();

        if (source[pos] == 'R') {
            pos++;
            return new PdfReference((PdfInteger) o1, (PdfInteger) o2);
        }

        pos = current;

        return o1;
    }

    /**
     * PdfNumberの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s9() throws PdfFormatException {
//        System.out.println("s9(" + source[pos] + "," + (char)(byte)source[pos] + ")");
        int start = pos;
        boolean isInteger = true;

        while (isNumberChar(source[pos])) {
            if (isInteger && !isIntegerChar(source[pos])) {
                isInteger = false;
            }

            pos++;
        }

        if (!(isWhiteSpaceChar(source[pos]) || source[pos] != ']' || source[pos] != '>') || start == pos) {
            printErrMsg();
            throw new PdfFormatException("PdfNumber is consist of [0~9,+,-,.]");
        }

        try {
            if (isInteger) {
                return new PdfInteger(Integer.valueOf(ByteArrayUtil.subString(source, start, pos)));
            } else {
                return new PdfReal(Double.valueOf(ByteArrayUtil.subString(source, start, pos)));
            }
        } catch (NumberFormatException e) {
            printErrMsg();
            e.printStackTrace();
            throw new PdfFormatException("PdfNumberではありません");
        }
    }

    /**
     * ストリームオブジェクトのバイト列
     *
     * @return バイト列
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private byte[] s10() throws PdfFormatException {
        pos += "stream".length();
        skipWhiteSpace(); //TODO 改行を一つスキップする

        int start = pos;

        while (true) {
            if (!(pos < source.length)) {
                throw new PdfFormatException("endstreamがありません");
            } else if (source[pos] != 'e') {
                pos++;
            } else if (match("endstream")) {
                break;
            } else {
                pos++;
            }
        }

        byte[] ary = new byte[ pos - start ];
        for (int i = start, j = 0; i < pos; i++, j++) {
            ary[j] = source[i];
        }

        return ary;
    }

    /**
     * PdfStringが始まる
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s11() throws PdfFormatException {
        skipWhiteSpace();

        if (source[pos] != '(') {
            throw new PdfFormatException("stringは(で始まります。");
        }

        pos++;
        return s12();
    }

    /**
     * PdfStringの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s12() throws PdfFormatException {
        int start = pos;
        boolean isEscaped = false;

        while (true) {
            if ( !(pos < source.length) ) {
                printErrMsg();
                throw new PdfFormatException(")が見つかりません");
            }

            if (isEscaped) {
                pos++;
                isEscaped = false;
                continue;
            }

            if (source[pos] == '\\') {
                isEscaped = true;
                pos++;
                continue;
            }

            if (source[pos] == ')') {
                pos++;
                break;
            }

            pos++;
        }

        String value = ByteArrayUtil.subString(source, start, pos);
        value = value
                .replace("\\(", "(")
                .replace("\\)", ")")
                .replace("\\n", "\n");

        return new PdfString(value);
    }

    /**
     * PdfBooleanの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s13() throws PdfFormatException {
        if ("true".equals(ByteArrayUtil.subString(source, pos, pos+"true".length()))) {
            pos += "true".length();
            return new PdfBoolean(true);
        }
        if ("false".equals(ByteArrayUtil.subString(source, pos, pos+"false".length()))) {
            pos += "false".length();
            return new PdfBoolean(false);
        }

        printErrMsg();
        throw new PdfFormatException("booleanではありません");
    }

    /**
     * PdfString(バイナリデータ)の内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s14() throws PdfFormatException {
        int start = pos;

        while (true) {
            if ( !(pos < source.length) ) {
                printErrMsg();
                throw new PdfFormatException(">が見つかりません");
            }

            if (source[pos] == '>') {
                pos++;
                break;
            }

            pos++;
        }

//        System.err.println("<<" + source[pos] + ">>---------------------------------------------------------------");
//        printErrMsg();
//        System.err.println("[" + ByteArrayUtil.subString(source, start, pos-1)+"]");

        return new PdfString(ByteArrayUtil.subString(source, start, pos-1), true);
    }

    /**
     * PdfNullの内容
     *
     * @return オブジェクト
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    private PdfObject s15() throws PdfFormatException {
        if ("null".equals(ByteArrayUtil.subString(source, pos, pos+"null".length()))) {
            pos += "null".length();
            return new PdfNull();
        }

        printErrMsg();
        throw new PdfFormatException("nullではありません");
    }


    /**
     * ホワイトスペースであるか
     *
     * ' '、'\r'、'\n'であればtrue
     *
     * @param c 文字
     * @return ホワイトスペースであるか
     */
    private static boolean isWhiteSpaceChar(byte c) {
        return c == ' ' || c == '\r' || c == '\n';
    }

    /**
     * PdfNameを構成する文字であるか。
     *
     * nameは、NULL以外のASCII文字を連続して並べたもの
     * また、'<'、'['も除く
     *
     * @param c 文字
     * @return PdfNameを構成する文字であるか
     */
    private boolean isNameChar(byte c) {
        //                                '/'        '<'        '>'        '['        ']'
        return 33 <= c && c <= 126 && c != 47 && c != 60 && c != 62 && c != 91 && c != 93;
    }

    /**
     * PdfNumberを構成する文字であるか。
     *
     * [0~9,+,-,.]
     *
     * @param c 文字
     * @return PdfNumberを構成する文字であるか。
     */
    private static boolean isNumberChar(byte c) {
        return (48 <= c && c <= 57) || c == 43 || c == 45 || c == 46;
    }

    /**
     * PdfIntegerを構成する文字であるか。
     *
     * @param c 文字
     * @return PdfIntegerを構成する文字であるか。
     */
    private static boolean isIntegerChar(byte c) {
        return 48 <= c && c <= 57;
    }

    /**
     * ホワイトスペースでない文字が出るまでposを進める
     *
     * ' '、'\r'、'\n'をスキップする
     */
    private void skipWhiteSpace() {
        while (source[pos] == ' ' || source[pos] == '\r' || source[pos] == '\n') {
            pos++;
        }
    }

    /**
     * 現在の位置から指定された文字が続くか
     *
     * @param sequence 文字列
     * @return 現在の位置から指定された文字列が続くか
     */
    private boolean match(String sequence) {
        for (int i = pos, j = 0; i < source.length && j < sequence.length(); i++, j++) {
            if ( (char)(byte)source[i] != sequence.charAt(j) ) {
                return false;
            }
        }

        return true;
    }

    /**
     * 現在のsourceとposからエラーメッセージを作成する
     *
     * posの前後100文字に加え、posの位置を示す"^"を挿入する。
     *
     * @return エラーメッセージ
     */
    private String makeErrMsg() {
        int range = 100;
        int from = pos - range;
        int to = pos + range;
        int subject = range;
        if (from < 0) {
            subject += from + 1;
            from = 0;
        }
        if (to > source.length) {
            subject += source.length - to + 1;
            to = source.length;
        }

        String errMsg = ByteArrayUtil.subString(source, from, to);

        int idx;
        if ((idx = errMsg.indexOf("\r\n", subject)) != -1) {
            idx += "\r\n".length();
        } else if ((idx = errMsg.indexOf("\r", subject)) != -1) {
            idx += "\r".length();
        } else if ((idx = errMsg.indexOf("\n", subject)) != -1) {
            idx += "\n".length();
        } else {
            idx = errMsg.length();
        }
        int lineEnd = idx;

        if ((idx = errMsg.lastIndexOf("\r\n", subject-1)) != -1) {
            idx += "\r\n".length();
        } else if ((idx = errMsg.lastIndexOf("\r", subject-1)) != -1) {
            idx += "\r".length();
        } else if ((idx = errMsg.lastIndexOf("\n", subject-1)) != -1) {
            idx += "\n".length();
        } else {
            idx = errMsg.length();
        }
        int lineStart = idx;

        int spaceCnt = Math.max(subject - lineStart, 0);

        return
                "char: '" + (char)(byte)source[pos] + "'\n"
                + errMsg.substring(0, lineEnd)
                + " ".repeat(spaceCnt)
                + "^\n"
                + errMsg.substring(lineEnd);
    }

    /**
     * 現在のsourceとposからエラーメッセージを作成し、標準エラーに出力する
     */
    private void printErrMsg() {
        System.err.println(makeErrMsg());
    }
}
