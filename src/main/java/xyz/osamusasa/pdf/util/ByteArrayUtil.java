package xyz.osamusasa.pdf.util;

/**
 * Byte配列に関する操作を簡潔に行うためのユーティリティクラス
 */
public class ByteArrayUtil {
    /**
     * 指定された位置から、改行コードまでの部分配列を返す。
     *
     * 改行コードが見つからなかった場合は、終わりまでの配列を返す。
     * 返す配列に、改行コードは含まれない。
     *
     * @param bytes byte配列
     * @param from 開始地点
     * @return 部分配列
     */
    public static byte[] subAryUntilReturn(Byte[] bytes, int from) {
        int to = from;
        for (;to < bytes.length; to++) {
            if (bytes[to] == '\n' || bytes[to] == '\r') {
                to--;
                break;
            }
        }

        if (!(from < to)) {
            return new byte[0];
        }

        byte[] subAry = new byte[to - from + 1];
        for (int i = from; i <= to; i++) {
            subAry[i] = bytes[i];
        }

        return subAry;
    }
}
