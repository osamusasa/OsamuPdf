package xyz.osamusasa.pdf.element.document;

import xyz.osamusasa.pdf.element.primitive.PdfDictionary;

import java.util.Objects;

/**
 * ドキュメント情報辞書
 */
public class DocInfoDictionary extends Node {

    /**
     * ドキュメントのタイトル
     */
    private String title;

    /**
     * ドキュメントの主題
     */
    private String subject;

    /**
     * ドキュメントに関連付けられるキーワード
     */
    private String keywords;

    /**
     * ドキュメントの著者名
     */
    private String author;

    /**
     * ドキュメントの作成日時
     */
    private String creationDate;

    /**
     * ドキュメントの最終更新日時
     */
    private String modDate;

    /**
     * ドキュメントがもともとほかのフォーマットで作成されたものである場合、
     * それを作成したプログラムの名前
     */
    private String creator;

    /**
     * ドキュメントがもともとほかのフォーマットで作成されたものである場合、
     * それをPDFに変換したプログラムの名前
     */
    private String producer;

    /**
     * コンストラクタ
     *
     * @param source source
     */
    public DocInfoDictionary(PdfDictionary source) {
        super();
        this.title = source.getString("Title");
        this.subject = source.getString("Subject");
        this.keywords = source.getString("Keywords");
        this.author = source.getString("Author");
        this.creationDate = source.getString("CreationDate");
        this.modDate = source.getString("ModDate");
        this.creator = source.getString("Creator");
        this.producer = source.getString("Producer");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocInfoDictionary)) return false;
        DocInfoDictionary that = (DocInfoDictionary) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(keywords, that.keywords) &&
                Objects.equals(author, that.author) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(modDate, that.modDate) &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(producer, that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subject, keywords, author, creationDate, modDate, creator, producer);
    }

    @Override
    public String toString() {
        return "DocInfoDictionary{" +
                "title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", keywords='" + keywords + '\'' +
                ", author='" + author + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", creator='" + creator + '\'' +
                ", producer='" + producer + '\'' +
                ", children=" + children +
                '}';
    }
}
