package xyz.osamusasa.pdf.element.graphics;

import java.util.Arrays;

/**
 * PDFのテキストを描画するのに必要な情報を持つクラス
 */
public class PdfText extends PageContent {

    /**
     * テキスト
     */
    private String text;

    /*
     * テキストの状態を表すパラメータ
     */

    /**
     * 文字間隔
     *
     * パラメータ：T_c
     * オペランド：charSpace
     * オペレータ：Tc
     */
    private int charSpace;

    /**
     * 単語間隔
     *
     * パラメータ：T_w
     * オペランド：wordSpace
     * オペレータ：Tw
     */
    private int wordSpace;

    /**
     * 水平間隔
     *
     * パラメータ：T_h
     * オペランド：scale
     * オペレータ：Tz
     */
    private int scale;

    /**
     * レディング
     *
     * パラメータ：T_l
     * オペランド：leading
     * オペレータ：Tl
     */
    private int leading;

    /**
     * フォント
     *
     * パラメータ：T_f
     * オペランド：font],size
     * オペレータ：Tf
     */
    private String font;

    /**
     * フォントサイズ
     *
     * パラメータ：T_fs
     * オペランド：font],size
     * オペレータ：Tf
     */
    private int size;

    /**
     * 描画モード
     *
     * パラメータ：T_mode
     * オペランド：render
     * オペレータ：Tr
     */
    private int render;

    /**
     * ベースライン調整
     *
     * パラメータ：T_rise
     * オペランド：rise
     * オペレータ：Ts
     */
    private int rise;

    /*
     * テキストの位置を表すパラメータ
     */

    /**
     * テキストマトリクス
     *
     * 次のグリフに適用する現在の位置
     */
    private int[] textMatrix;

    /**
     * テキスト行マトリクス
     *
     * 現在の行の先頭におけるテキストマトリクスの状態
     */
    private int[] textRowMatrix;


    public PdfText() {
        super();

        this.charSpace = 0;
        this.wordSpace = 0;
        this.scale = 100;
        this.leading = 0;
        this.render = 0;
        this.rise = 0;

        this.textMatrix = new int[9];
        Arrays.fill(this.textMatrix, 0);
        this.textRowMatrix = new int[9];
        Arrays.fill(this.textRowMatrix, 0);
    }


    /*
     * オペレータ
     */

    /**
     * 文字間隔をcharSpaceにする
     *
     * @param charSpace 文字間隔
     */
    public void Tc(int charSpace) {
        this.charSpace = charSpace;
    }
    /**
     * 単語間隔をwordSpaceにする
     *
     * @param wordSpace 単語間隔
     */
    public void Tw(int wordSpace) {
        this.wordSpace = wordSpace;
    }
    /**
     * sizeポイントのフォントfontが選択される
     *
     * @param font フォントの名前
     * @param size フォントサイズ
     */
    public void Tf(String font, int size) {
        this.font = font;
        this.size = size;
    }
    /**
     * 描画モードをrenderにする
     *
     * @param render 描画モード
     */
    public void Tr(int render) {
        this.render = render;
    }
    /**
     * テキストのベースラインをriseにする
     *
     * @param rise テキストのベースライン
     */
    public void Ts(int rise) {
        this.rise = rise;
    }
    /**
     * テキストの位置を次の行のオフセット(x,y)に移動する
     *
     * @param x オフセットのx座標
     * @param y オフセットのy座標
     */
    public void Td(int x, int y) {

    }
    /**
     * テキストの位置を次の行のオフセット(x,y)に移動しレディングを-yにセットする
     *
     * @param x オフセットのx座標
     * @param y オフセットのy座標
     */
    public void TD(int x, int y) {

    }
    /**
     * テキストの位置を次の行に移動する
     *
     * 0 leading Tdと等価
     */
    public void T_asterisk() {

    }
    /**
     * テキストマトリクスとテキスト行マトリクスを[a b 0 c d 0 e f 1]で更新する
     *
     * @param a a
     * @param b b
     * @param c c
     * @param d d
     * @param e e
     * @param f f
     */
    public void Tm(int a, int b, int c, int d, int e, int f) {
        this.textMatrix[0] = a;
        this.textMatrix[1] = b;
        this.textMatrix[2] = 0;
        this.textMatrix[3] = c;
        this.textMatrix[4] = d;
        this.textMatrix[5] = 0;
        this.textMatrix[6] = e;
        this.textMatrix[7] = f;
        this.textMatrix[8] = 1;

        this.textRowMatrix[0] = a;
        this.textRowMatrix[1] = b;
        this.textRowMatrix[2] = 0;
        this.textRowMatrix[3] = c;
        this.textRowMatrix[4] = d;
        this.textRowMatrix[5] = 0;
        this.textRowMatrix[6] = e;
        this.textRowMatrix[7] = f;
        this.textRowMatrix[8] = 1;
    }


    /*
     * セッター
     */
    public void setText(String text) {
        this.text = text;
    }

    /*
     * ゲッター
     */
    public String getText() {
        return text;
    }
    public String getFont() {
        return font;
    }
    public int getSize() {
        return size;
    }
    public int[] getTextMatrix() {
        return textMatrix;
    }
    public int[] getTextRowMatrix() {
        return textRowMatrix;
    }

    @Override
    public String toString() {
        return "PdfText{" +
                "textMatrix=" + Arrays.toString(textMatrix) +
                ", textRowMatrix=" + Arrays.toString(textRowMatrix) +
                '}';
    }
}
