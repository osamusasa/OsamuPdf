package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfStream;

/**
 * コンテントストリーム
 */
public class ContentStream extends Node {

    private PdfStream value;

    public ContentStream(PdfStream value) {
        this.value = value;
    }
}
