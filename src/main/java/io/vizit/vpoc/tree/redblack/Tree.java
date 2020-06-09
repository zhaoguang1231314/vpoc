package io.vizit.vpoc.tree.redblack;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Tree<K, V> {
    private Node<K, V> root;

    public Tree rotateLeft(Node x) {
        // hold xParent
        Node xParent = x.getParent();
        // hold xRight
        Node y = x.getRight();

        // y.left goes to x.right
        x.setRight(y.getLeft());

        // x goes to y's left
        y.setLeft(x);

        // find place for y: y replace x's old place
        if (x.isRoot()) {
            this.setRoot(y); // y becomes root
        } else if (x.isLeftOf(xParent)) {
            xParent.setLeft(y);
        } else {
            xParent.setRight(y);
        }
        return this;
    }

    private void setRoot(Node y) {
        this.root = y;
        y.setParent(null);
    }
}
