package xyz.osamusasa.pdf.element.primitive;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PDF文書のdictionaryを表すクラス
 */
public class PdfDictionary extends PdfObject {

    /**
     * Dictionary
     *
     * キーと値のペアを要素としたものです。dictionaryの要素数は０個でもかまいません。
     */
    private Map<PdfName, PdfObject> dict;

    /**
     * コンストラクタ
     *
     * @param dict Dictionary
     */
    public PdfDictionary(Map<PdfName, PdfObject> dict) {
        this.dict = dict;
    }

    /**
     * コンストラクタ
     *
     * 引数の配列の2n番目と2n+1番目の要素をペアにして、dictionaryを作る
     *
     * @param objects キーと値のペア
     * @throws IllegalArgumentException キーと値のペアになっていないとき、
     *                                  2n番目の要素がPdfNameのインスタンスでないとき
     */
    public PdfDictionary(PdfObject... objects) throws IllegalArgumentException {
        dict = new HashMap<>(objects.length/2);

        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("array length must be even.");
        }
        for (int i = 0; i < objects.length; i += 2) {
            if (!(objects[i] instanceof PdfName)) {
                throw new IllegalArgumentException("element of 2n is must be PdfName instance.");
            }
            dict.put((PdfName)objects[i], objects[i+1]);
        }
    }

    /**
     * keyと関連付けられたPdfObjectを返す
     *
     * @param key キー
     * @return PdfObject
     */
    public PdfObject get(PdfName key) {
        return dict.get(key);
    }

    /**
     * keyと関連付けられたPdfObjectを返す
     *
     * @param key キー
     * @return PdfObject
     */
    public PdfObject get(String key) {
        return get(new PdfName(key));
    }

    /**
     * keyと関連付けられたPdfStringのvalueを返す
     *
     * keyと関連付けられたPdfStringがない場合はnullを返す
     *
     * @param key キー
     * @return value
     */
    public String getString(String key) {
        PdfObject obj = get(key);
        if (!(obj instanceof PdfString)) {
            return null;
        }
        return ((PdfString)obj).getValue();
    }

    /**
     * keyと関連付けられたオブジェクトを返す
     *
     * @param key キー
     * @param crass 関連付けられたオブジェクトのクラス
     * @param <T> 関連付けられたオブジェクトの型
     * @return value
     */
    public <T> T get(String key, Class<T> crass) {
        PdfObject obj = get(key);
        if (obj == null || !(crass == obj.getClass() || crass.isInstance(obj.getClass()))) {
            return null;
        }
        return (T)obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfDictionary that = (PdfDictionary) o;
        return dict.equals(that.dict);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dict);
    }

    @Override
    public String toString() {
        return "PdfDictionary{" +
                "dict=" + dict +
                '}';
    }
}
