package xyz.osamusasa.pdf.element;

/**
 * PDFファイルのトレイラー部を表すクラス
 */
public class PdfTrailer {
    /**
     * クロスリファレンス・テーブルのエントリ数
     */
    private int kSize;

    /**
     * 以前のクロスリファレンス・テーブルの位置を示すオフセット値
     */
    private long kPrev;

    /**
     * カタログ(Catalog)ディクショナリ
     */
    private Object kRoot;

    /**
     * 暗号化ディクショナリ
     */
    private Object kEncrypt;

    /**
     * 文書情報(Document Information)ディクショナリ
     */
    private Object kInfo;

    /**
     * ２っのバイトストリング（「<」と「>」で囲まれた）で構成されたファイル識別子
     */
    private String kID;

    /**
     * クロスリファレンスセクションの開始オフセット
     */
    private long startxref;

    /**
     * コンストラクタ
     *
     * @param kSize エントリ数
     * @param kPrev 以前のxrefのオフセット値
     * @param kRoot カタログディクショナリ
     * @param kEncrypt 暗号化ディクショナリ
     * @param kInfo 文書情報ディクショナリ
     * @param kID ファイル識別子
     * @param startxref xrefの開始オフセット値
     */
    public PdfTrailer(int kSize, long kPrev, Object kRoot, Object kEncrypt, Object kInfo, String kID, long startxref) {
        this.kSize = kSize;
        this.kPrev = kPrev;
        this.kRoot = kRoot;
        this.kEncrypt = kEncrypt;
        this.kInfo = kInfo;
        this.kID = kID;
        this.startxref = startxref;
    }

    public int getkSize() {
        return kSize;
    }

    public long getkPrev() {
        return kPrev;
    }

    public Object getkRoot() {
        return kRoot;
    }

    public Object getkEncrypt() {
        return kEncrypt;
    }

    public Object getkInfo() {
        return kInfo;
    }

    public String getkID() {
        return kID;
    }

    public long getStartxref() {
        return startxref;
    }

    @Override
    public String toString() {
        return "PdfTrailer{" +
                "kSize=" + kSize +
                ", kPrev=" + kPrev +
                ", kRoot=" + kRoot +
                ", kEncrypt=" + kEncrypt +
                ", kInfo=" + kInfo +
                ", kID='" + kID + '\'' +
                ", startxref=" + startxref +
                '}';
    }
}
