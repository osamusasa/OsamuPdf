package xyz.osamusasa.pdf.util;

/**
 * Byte配列に関する操作を簡潔に行うためのユーティリティクラス
 */
public class ByteArrayUtil {
    /**
     * fromからtoまでの部分配列を返す。
     *
     * @param bytes byte配列
     * @param from 開始地点（含む）
     * @param to 終了地点（含まない）
     * @return 部分配列
     */
    public static byte[] subAry(Byte[] bytes, int from, int to) {
        int _from = Math.max(from, 0);
        int _to = Math.min(to, bytes.length);

        int len = _to - _from;
        if (len < 0) {
            throw new IllegalArgumentException("from > to");
        }
        byte[] subAry = new byte[len];

        for (int i = 0, j = _from; i < len; i++, j++) {
            subAry[i] = bytes[j];
        }

        return subAry;
    }

    /**
     * fromからtoまでの部分配列を文字列として返す。
     *
     * @param bytes byte配列
     * @param from 開始地点（含む）
     * @param to 終了地点（含まない）
     * @return 部分文字列
     */
    public static String subString(Byte[] bytes, int from, int to) {
        return new String(subAry(bytes, from, to));
    }
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
        int _from = Math.max(from, 0);
        int to = _from;
        for (;to < bytes.length; to++) {
            if (bytes[to] == '\n' || bytes[to] == '\r') {
                break;
            }
        }

        if (!(_from < to)) {
            return new byte[0];
        }

        return subAry(bytes, _from, to);
    }
}
