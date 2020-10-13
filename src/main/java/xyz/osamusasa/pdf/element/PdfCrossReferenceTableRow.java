package xyz.osamusasa.pdf.element;

import java.util.Objects;

/**
 * クロスリファレンステーブルの一つの行を表すクラス
 */
public class PdfCrossReferenceTableRow {
    /**
     * オブジェクトが配置されたオフセット位置
     */
    private long offset;

    /**
     * オブジェクトの世代番号
     */
    private int gen;

    /**
     * クロスリファレンスの状態
     *
     * n:使用中
     * f:未使用
     */
    private char state;

    /**
     * コンストラクタ
     *
     * @param offset オブジェクトが配置されたオフセット位置
     * @param gen オブジェクトの世代番号
     * @param state クロスリファレンスの状態
     */
    public PdfCrossReferenceTableRow(long offset, int gen, char state) {
        this.offset = offset;
        this.gen = gen;
        this.state = state;
    }

    public long getOffset() {
        return offset;
    }

    public int getGen() {
        return gen;
    }

    public char getState() {
        return state;
    }

    @Override
    public String toString() {
        return "PdfCrossReferenceTableRow{" +
                "offset=" + offset +
                ", gen=" + gen +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfCrossReferenceTableRow)) return false;
        PdfCrossReferenceTableRow that = (PdfCrossReferenceTableRow) o;
        return offset == that.offset &&
                gen == that.gen &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, gen, state);
    }
}
