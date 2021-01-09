package xyz.osamusasa.pdf.parser;

import xyz.osamusasa.pdf.PdfFormatException;
import xyz.osamusasa.pdf.element.document.*;
import xyz.osamusasa.pdf.element.primitive.*;
import xyz.osamusasa.pdf.util.PdfObjectSchema;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * ドキュメントツリーのデコーダ
 */
public class DocumentTreeDecoder {

    /**
     * 引数の情報からドキュメントツリーを構築する
     *
     * @param body PDFファイルのボディー部
     * @param xref PDFファイルのクロスリファレンステーブル部
     * @param trailer PDFファイルのトレイラー部
     * @return ドキュメントツリー
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public static DocumentTree decode(
            PdfBody body,
            PdfCrossReferenceTable xref,
            PdfTrailer trailer
    ) throws PdfFormatException {
        ArrayList<Node> entirePageList = new ArrayList<>();

        TrailerDictionary root = new TrailerDictionary(trailer);

        DocInfoDictionary docInfoDictionary = null;
        DocCatalog docCatalog = null;

        for (PdfNamedObject obj: body.getObjects()) {
            if (PdfObjectSchema.valid(obj, PdfObjectSchema.docInfoDictionarySchema) > 0) {
                docInfoDictionary = new DocInfoDictionary((PdfDictionary) obj.getValue());
                root.addChild(docInfoDictionary);
                break;
            }
        }
        if (docInfoDictionary == null) {
            throw new PdfFormatException("ドキュメント情報辞書がありません");
        }

        for (PdfNamedObject obj: body.getObjects()) {
            if (PdfObjectSchema.valid(obj, PdfObjectSchema.docCatalogSchema) > 0) {
                docCatalog = new DocCatalog((PdfDictionary) obj.getValue());
                docInfoDictionary.addChild(docCatalog);
                break;
            }
        }
        if (docCatalog == null) {
            throw new PdfFormatException("ドキュメントカタログがありません");
        }

        PageList pageList = new PageList((PdfDictionary) body.getObject(docCatalog.getPages()).getValue());
        docCatalog.addChild(pageList);

        Deque<PageList> pageListDeque = new ArrayDeque<>();
        pageListDeque.add(pageList);

        while (!pageListDeque.isEmpty()) {
            PageList pages = pageListDeque.poll();
            for(PdfReference reference: pages.getChildReferences()) {
                PdfDictionary value = (PdfDictionary) body.getObject(reference).getValue();

                if ("Pages".equals(value.getString("Type"))) {
                    pageListDeque.add(new PageList(value));
                } else if ("Page".equals(value.getString("Type"))) {
                    Page page = new Page(value);
                    pageList.addChild(page);
                    entirePageList.add(page);

                    if (page.getContents() != null) {
                        page.addChild(new PageContent((PdfStream) body.getObject(page.getContents()).getValue()));
                    }
                    if (page.getResource() != null) {
                        page.addChild(new PageResource(page.getResource()));
                    }
                } else {
                    throw new PdfFormatException("ページリストまたはページではありません");
                }
            }
        }

        return new DocumentTree(body, xref, trailer, root, entirePageList);
    }
}
