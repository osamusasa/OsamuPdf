package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfReference;
import xyz.osamusasa.pdf.element.primitive.PdfTrailer;

/**
 * トレーラ辞書
 *
 * 相互参照テーブルをもとに、すべてのオブジェクトにアクセスできる
 * ドキュメントツリーのルート
 */
public class TrailerDictionary extends Node {

    /**
     * このファイルの相互参照テーブル内に格納されたエントリの総数
     */
    private int size;

    /**
     * ドキュメントカタログへの間接参照
     */
    private PdfReference root;

    /**
     * ドキュメント情報辞書への間接参照
     */
    private PdfReference info;

    /**
     * ワークフロー内でこのファイルを識別するために用いられる固有文字列
     *
     * ２つの文字が格納された配列で、最初の文字はファイルが最初に生成されたときに決定され、
     * 次の文字はファイルが更新されたときにワークフローシステムによって決定される。
     */
    private String[] id;

    /**
     * コンストラクタ
     */
    private TrailerDictionary() {
        super();
        size = 0;
        root = null;
        info = null;
        id = new String[]{null, null};
    }

    /**
     * コンストラクタ
     *
     * @param trailer PdfTrailer
     */
    public TrailerDictionary(PdfTrailer trailer) {
        this();
        this.size = trailer.getkSize();
    }
}
