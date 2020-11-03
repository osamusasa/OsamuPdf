package xyz.osamusasa.pdf.element;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PDFファイルのクロスリファレンステーブル部を表すクラス
 */
public class PdfCrossReferenceTable {
    private int start;
    private int len;
    private List<PdfCrossReferenceTableRow> crossReferences;

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

    public Iterable<PdfCrossReferenceTableRow> getRowsInUse() {
        return new Iterable<PdfCrossReferenceTableRow>() {
            @Override
            public Iterator<PdfCrossReferenceTableRow> iterator() {
                return new CrossReferenceTableIterator();
            }
        };
    }

    @Override
    public String toString() {
        return "PdfCrossReferenceTable{" +
                "start=" + start +
                ", len=" + len +
                ", crossReferences=" + crossReferences +
                '}';
    }

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
