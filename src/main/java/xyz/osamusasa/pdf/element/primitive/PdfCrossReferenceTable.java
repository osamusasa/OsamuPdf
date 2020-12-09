package xyz.osamusasa.pdf.element.primitive;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PDFファイルのクロスリファレンステーブル部を表すクラス
 */
public class PdfCrossReferenceTable {
    /**
     * サブセクションに含まれている最初のオブジェクト番号
     */
    private int start;

    /**
     * オブジェクトの数量
     */
    private int len;

    /**
     * クロスリファレンス行
     */
    private List<PdfCrossReferenceTableRow> crossReferences;

    /**
     * コンストラクタ
     *
     * @param start サブセクションに含まれている最初のオブジェクト番号
     * @param len オブジェクトの数量
     * @param crossReferences クロスリファレンス行
     */
    public PdfCrossReferenceTable(int start, int len, List<PdfCrossReferenceTableRow> crossReferences) {
        this.start = start;
        this.len = len;
        this.crossReferences = crossReferences;
    }

    public int getStart() {
        return start;
    }

    public int getLen() {
        return len;
    }

    public List<PdfCrossReferenceTableRow> getCrossReferences() {
        return crossReferences;
    }

    /**
     * 使用中であるクロスリファレンスの行を返すイテレーションを返す
     *
     * @return イテレーション
     */
    public Iterable<PdfCrossReferenceTableRow> getRowsInUse() {
        return CrossReferenceTableIterator::new;
    }

    @Override
    public String toString() {
        return "PdfCrossReferenceTable{" +
                "start=" + start +
                ", len=" + len +
                ", crossReferences=" + crossReferences +
                '}';
    }

    /**
     * 使用中であるクロスリファレンスの行を返すイテレーション
     */
    class CrossReferenceTableIterator implements Iterator<PdfCrossReferenceTableRow> {
        int pos;

        CrossReferenceTableIterator() {
            pos = getStart();
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return skipNotUsedRow();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public PdfCrossReferenceTableRow next() {
            if (!skipNotUsedRow()) {
                throw new NoSuchElementException("no more pdf cross reference table rows");
            }

            PdfCrossReferenceTableRow ret = crossReferences.get(pos);
            pos++;
            return ret;
        }

        boolean skipNotUsedRow() {
            while (true) {
                if ( !(pos < getLen()) ) {
                    return false;
                }

                if (crossReferences.get(pos).getState() == 'n') {
                    return true;
                } else if (crossReferences.get(pos).getState() == 'f'){
                    pos++;
                } else {
                    return false;
                }
            }
        }
    }
}
