package xyz.osamusasa.pdf.util;

import java.util.Arrays;

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
     * 指定された位置から、ホワイトスペースまでの部分配列を返す。
     *
     * ホワイトスペースが見つからなかった場合は、終わりまでの配列を返す。
     * 返す配列に、ホワイトスペースは含まれない。
     *
     * @param bytes byte配列
     * @param from 開始地点
     * @return 部分配列
     */
    public static byte[] subAryUntilWhiteSpace(Byte[] bytes, int from) {
        int _from = Math.max(from, 0);
        int to = _from;
        for (;to < bytes.length; to++) {
            if (Character.isWhitespace(bytes[to])) {
                break;
            }
        }

        if (!(_from < to)) {
            return new byte[0];
        }

        return subAry(bytes, _from, to);
    }

    /**
     * 指定された位置から、ホワイトスペースまでの部分配列を返す。
     *
     * ホワイトスペースが見つからなかった場合は、終わりまでの配列を返す。
     * 返す配列に、ホワイトスペースは含まれない。
     *
     * @param bytes byte配列
     * @param from 開始地点
     * @return 部分配列
     */
    public static byte[] subAryUntilWhiteSpace(byte[] bytes, int from) {
        int _from = Math.max(from, 0);
        int to = _from;
        for (;to < bytes.length; to++) {
            if (Character.isWhitespace(bytes[to])) {
                break;
            }
        }

        if (!(_from < to)) {
            return new byte[0];
        }

        return Arrays.copyOfRange(bytes, _from, to);
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

    /**
     * 引数の16進数の文字列表現をバイト配列にパースする
     *
     * @param src 16進数の文字列表現
     * @return バイト配列
     */
    public static byte[] toArray(String src) {
        if (src.length() % 2 != 0) {
            //TODO
            throw new IllegalArgumentException("入力の文字列は偶数でないといけません。（将来的に改善予定）");
        }

        byte[] ret = new byte[src.length() / 2];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte)(int)Integer.valueOf(src.substring(i*2, (i+1)*2), 16);
        }

        return ret;
    }

    /**
     * この文字列が指定されたchar値のシーケンスを含む場合に限りtrueを返します。
     *
     * @param src バイト配列
     * @param s 検索するシーケンス
     * @return この文字列がsを含む場合はtrue。そうでない場合はfalse
     */
    public static boolean contains(byte[] src, CharSequence s) {
        for (int start = 0; start < src.length; start++) {
            if (src[start] != s.charAt(0)) {
                continue;
            }

            int sameLength = 0;

            for (int i = 0; i < s.length(); i++) {
                if (src[start + i] == s.charAt(i)) {
                    sameLength++;
                } else {
                    break;
                }
            }

            if (sameLength == s.length()) {
                return true;
            }
        }

        return false;
    }

    /**
     * このバイト配列の指定されたインデックス以降の部分文字列が、指定された接頭辞で始まるかどうかを判定します。
     *
     * @param src バイト配列
     * @param prefix 接頭辞
     * @param toffset このバイト配列の比較を開始する位置
     * @return 引数によって表される文字シーケンスが、インデックスtoffsetで始まるこのオブジェクトの部分文字列の接頭辞である場合はtrue、
     *         そうでない場合はfalse。 toffsetが負の値の場合、あるいはバイト配列の長さより大きい場合、結果はfalse。
     */
    public static boolean startsWith(byte[] src, String prefix, int toffset) {
        if (toffset < 0) {
            return false;
        }

        if (src.length < prefix.length() + toffset) {
            return false;
        }


        int sameResult = 0;
        for (int j = 0; j < prefix.length(); j++) {
            if (src[j + toffset] == prefix.charAt(j)) {
                sameResult++;
            } else {
                break;
            }
        }

        return sameResult == prefix.length();
    }

    /**
     * このバイト配列が、指定された接尾辞で終るかどうかを判定します。
     *
     * @param src バイト配列
     * @param suffix 接尾辞
     * @return 引数によって表される文字シーケンスが、バイト配列によって表される文字シーケンスの接尾辞である場合はtrue、そうでない場合はfalse。
     *         引数が空の文字列の場合の結果はtrueになる。
     */
    public static boolean endWith(byte[] src, String suffix) {
        if (suffix.isEmpty()) {
            return true;
        }

        if (src.length < suffix.length()) {
            return false;
        }

        for (int i = src.length - 1, j = suffix.length() - 1; j >= 0; i--, j--) {
            if (src[i] != suffix.charAt(j)) {
                return false;
            }
        }

        return true;
    }
}
