package xyz.osamusasa.pdf.element.document;

import java.util.ArrayList;

/**
 * PDFファイルの論理的な構造におけるノードのスーパークラス
 */
public abstract class Node {

    /**
     * 子要素のリスト
     */
    protected ArrayList<Node> children;

    /**
     * コンストラクタ
     */
    public Node () {
        children = new ArrayList<>();
    }

    /**
     * 子要素を追加する
     *
     * @param child 子要素
     * @return 追加に成功したか
     */
    public boolean addChild(Node child) {
        return children.add(child);
    }

    @Override
    public String toString() {
        return "Node{" +
                "children=" + children +
                '}';
    }
}
