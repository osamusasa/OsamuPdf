package xyz.osamusasa.pdf.element.primitive;

import java.util.Objects;

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
    private PdfReference kRoot;

    /**
     * 暗号化ディクショナリ
     */
    private PdfReference kEncrypt;

    /**
     * 文書情報(Document Information)ディクショナリ
     */
    private PdfReference kInfo;

    /**
     * ２っのバイトストリング（「<」と「>」で囲まれた）で構成されたファイル識別子
     */
    private PdfArray<PdfString> kID;

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
    public PdfTrailer(int kSize,
                      long kPrev,
                      PdfReference kRoot,
                      PdfReference kEncrypt,
                      PdfReference kInfo,
                      PdfArray<PdfString> kID,
                      long startxref) {
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

    public PdfReference getkRoot() {
        return kRoot;
    }

    public PdfReference getkEncrypt() {
        return kEncrypt;
    }

    public PdfReference getkInfo() {
        return kInfo;
    }

    @Deprecated
    public PdfArray<PdfString> getkID() {
        return kID;
    }

    /**
     * PdfTrailerに含まれるファイル識別子を返す。
     *
     * @return file identifier
     */
    public String getFileIdentifier() {
        return this.kID.get(0).getValue();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfTrailer that = (PdfTrailer) o;
        return kSize == that.kSize && kPrev == that.kPrev && startxref == that.startxref && Objects.equals(kRoot, that.kRoot) && Objects.equals(kEncrypt, that.kEncrypt) && Objects.equals(kInfo, that.kInfo) && Objects.equals(kID, that.kID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kSize, kPrev, kRoot, kEncrypt, kInfo, kID, startxref);
    }
}
