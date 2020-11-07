package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.element.PdfStream;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class StreamDecoder {

    /**
     * 圧縮されたストリームのバイト列を指定された方式でデコードする
     *
     * /ASCIIHexDecode  未対応
     * /ASCII85Decode   未対応
     * /LZWDecode       未対応
     * /FlateDecode     対応済み
     * /RunLengthDecode 未対応
     * /CCITTFaxDecode  未対応
     * /JBIG2Decode     未対応
     * /DCTDecode       未対応
     * /JPXDecode       未対応
     *
     * @param stream ストリームデータ
     * @param filter 圧縮方式
     * @return デコードされた文字列
     */
    public static String decode(PdfStream stream, String filter) {
        switch (filter) {
        case "FlateDecode": return flateDecode(stream);
        }

        throw new UnsupportedOperationException("filter:" + filter + " is not supported");
    }

    /**
     * Flate圧縮されたデータを展開する
     *
     * @param stream ストリームデータ
     * @return デコードされた文字列
     */
    private static String flateDecode(PdfStream stream) {
        try {
            Inflater decompresser = new Inflater();
            decompresser.setInput(stream.getValue());
            byte[] result = new byte[100];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            return new String(result, 0, resultLength);
        } catch (DataFormatException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("圧縮形式がFlate圧縮でありません");
        }
    }
}
