package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfDictionary;

/**
 * ページのリソース
 */
public class PageResource extends Node {

    private PdfDictionary value;

    /**
     * コンストラクタ
     *
     * @param dictionary PdfDictionary
     */
    public PageResource(PdfDictionary dictionary) {
        this.value = dictionary;
    }
}
