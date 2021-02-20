package xyz.osamusasa.pdf;

import xyz.osamusasa.pdf.element.document.DocumentTree;
import xyz.osamusasa.pdf.element.document.Page;
import xyz.osamusasa.pdf.element.primitive.*;
import xyz.osamusasa.pdf.encryption.Decrypt;
import xyz.osamusasa.pdf.encryption.Password;
import xyz.osamusasa.pdf.parser.DocumentTreeDecoder;
import xyz.osamusasa.pdf.util.ByteArrayUtil;

import java.awt.*;

/**
 * PDfファイルのデータオブジェクト
 */
public class PdfFile {

    /**
     * ヘッダーに含まれるPDF文書のバージョン
     */
    private String version;

    /**
     * PDFファイルのボディー部を表すオブジェクト
     */
    private PdfBody body;

    /**
     * PDFファイルのクロスリファレンステーブル部を表すオブジェクト
     */
    private PdfCrossReferenceTable xref;

    /**
     * PDFファイルのトレイラー部を表すオブジェクト
     */
    private PdfTrailer trailer;

    /**
     * PDFドキュメント
     */
    private DocumentTree tree;

    /**
     * Pdfのデータが暗号化してあるか
     */
    private boolean isEncrypted;

    /**
     * Pdfファイルのパスワード
     */
    private Password password;

    /**
     * コンストラクタ
     *
     * @param version バージョン
     * @param body PdfBody
     * @param xref PdfCrossReferenceTable
     * @param trailer PdfTrailer
     * @throws PdfFormatException PDFファイルとして読み込めなかった場合。
     */
    public PdfFile(
            String version,
            PdfBody body,
            PdfCrossReferenceTable xref,
            PdfTrailer trailer
    ) throws PdfFormatException {
        this.version = version;
        this.body = body;
        this.xref = xref;
        this.trailer = trailer;

        this.tree = DocumentTreeDecoder.decode(this.body, this.xref, this.trailer);

        isEncrypted = trailer.getkEncrypt() != null;
        if (isEncrypted) {
            PdfDictionary encryptDict = (PdfDictionary) body.getObject(trailer.getkEncrypt()).getValue();
            password = new Password(
                    ByteArrayUtil.toArray(encryptDict.getString("O")),
                    ((PdfInteger)encryptDict.get("P")).getValue(),
                    ByteArrayUtil.toArray(trailer.getFileIdentifier())
                    );
            // ここから
            // パスワードを使って下のgetPageContentを正しく動作するようにする
        }
    }

    /**
     * PDFファイルのページ数を返す
     *
     * @return ページ数
     */
    public int getPageLength() {
        return tree.getPageSize();
    }

    /**
     * 指定されたページのサイズを返す
     *
     * @param pos ページ
     * @return ページのサイズ
     */
    public Rectangle getPageSize(int pos) {
        //TODO
        PdfReference root = (PdfReference) trailer.getkRoot();
        PdfNamedObject catalog = body.getObject(root);
        PdfReference pageRef = (PdfReference) ((PdfDictionary)catalog.getValue()).get("Pages");
//        System.out.println("getPage");
        Page page = tree.getPage(pos);
//        System.out.println(page);

        return page.getMediaBox();
    }

    /**
     * 指定されたページのコンテンツを返す
     *
     * 暗号化してある場合は、復号したページのコンテンツを返す。
     *
     * @param pos ページ
     * @return ページのコンテンツ
     */
    public PdfStream getPageContent(int pos) {
        Page page = tree.getPage(pos);
        PdfReference ref = page.getContents();
        PdfNamedObject namedObject = body.getObject(ref);
        PdfObject obj = namedObject.getValue();
        PdfStream retObj = (PdfStream)obj;

        if (isEncrypted) {
            retObj = new PdfStream(
                    retObj.getDict(),
                    Decrypt.decrypt(
                            retObj.getValue(),
                            password,
                            namedObject.getObjectNumber(),
                            namedObject.getGeneration()
                    )
            );
        }

        return retObj;
    }

    @Override
    public String toString() {
        return "PdfFile{" +
                "version='" + version + '\'' +
                ", body=" + body +
                ", xref=" + xref +
                ", trailer=" + trailer +
                ", tree=" + tree +
                '}';
    }
}
