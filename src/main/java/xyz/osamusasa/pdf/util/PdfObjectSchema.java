package xyz.osamusasa.pdf.util;

import xyz.osamusasa.pdf.element.primitive.*;

import java.util.Map;

/**
 * PdfObjectのスキーマクラス
 */
public class PdfObjectSchema {

    public static Map<String, Object> docInfoDictionarySchema;
    public static Map<String, Object> docCatalogSchema;
    public static Map<String, Object> pageSchema;

    static {
        docInfoDictionarySchema = Map.of(
                "Title", false,
                "Subject", false,
                "Keywords", false,
                "Author", false,
                "CreationDate", false,
                "ModDate", false,
                "Creator", false,
                "Producer", false
        );
        docCatalogSchema = Map.of(
                "Type", new PdfName("Catalog"),
                "Pages", false,
                "PageLabels", false,
                "Names", false,
                "Dests", false,
                "ViewerPreferences", false,
                "PageLayout", false,
                "PageMode", false,
                "OutLines", false,
                "Metadata", false
        );
        pageSchema = Map.of(
                "Type", new PdfName("Page"),
                "Parent", true,
                "Resources", false,
                "Contents", false,
                "Rotate", false,
                "MediaBox", true,
                "CropBox", false
        );
    }


    /**
     * PDFオブジェクトがスキーマの条件を満たしているか。
     *
     * ret < 0 : 必須のキーがオブジェクトに含まれていない
     * ret = 0 : スキーマに必須指定のキーがない かつ すべての必須でないキーが
     *           オブジェクトに含まれていない
     * ret < 0 : オブジェクトに含まれているキーの数
     *
     * @param object PDFオブジェクト
     * @param schema スキーマ
     *               String -> キーの名称
     *               Object -> Boolean か PdfObject のインスタンス
     *                  Boolean   -> 必須であるか
     *                      true : 必須
     *                      false : 任意
     *                  PdfObject -> 必須であり かつ 値がPdfObjectと一致
     * @return スキーマの条件を満たしているキーの数
     */
    public static int valid(PdfNamedObject object, Map<String, Object> schema) {
        PdfObject obj = object.getValue();

        if (!(obj instanceof PdfDictionary)) {
            return -1;
        }

        PdfDictionary dict = (PdfDictionary)obj;
        int cnt = 0;
        for (String key: schema.keySet()) {
            Object cond = schema.get(key);
            if (dict.get(key) != null) {
                // PdfDictionaryにスキーマの指定するキーがあった場合
                if (!(cond instanceof Boolean)) {
                    // スキーマの条件が真偽値でない場合 -> PdfDictionaryの値と一致
                    if (cond.equals(dict.get(key))) {
                        cnt++;
                    } else {
                        // 一致しない場合は必須を満たさないので、-1で終了
                        return -1;
                    }
                } else {
                    // スキーマの条件が真偽値の場合、真偽にかかわらずカウントを１増やす
                    cnt++;
                }

                continue;
            }

            // PdfDictionaryにスキーマの指定するキーがない場合
            if (cond instanceof Boolean) {
                // スキーマの条件が真偽値の場合
                if ((Boolean) cond) {
                    // 必須の場合は-1で終了
                    return -1;
                }
            } else {
                // スキーマの条件が真偽値でない場合、必須を満たさないので-1で終了
                return -1;
            }
        }

        return cnt;
    }
}
