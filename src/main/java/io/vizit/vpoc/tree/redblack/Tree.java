package io.vizit.vpoc.tree.redblack;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Tree<K extends Comparable, V> {
    private Node<K, V> root;

    public Tree<K, V> rotateLeft(Node<K, V> x) {
        // hold xParent
        Node<K, V> xParent = x.getParent();
        // hold xRight
        Node<K, V> y = x.getRight();

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

    public Tree<K, V> rotateRight(Node<K, V> x) {
        // hold xParent
        Node<K, V> xParent = x.getParent();
        // hold xRight
        Node<K, V> y = x.getLeft();

        // y.left goes to x.right
        x.setLeft(y.getRight());

        // x goes to y's left
        y.setRight(x);

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

    public Tree<K, V> insert(Node<K, V> z) {
        Node<K, V> y = null;
        Node<K, V> x = this.root;

        while (x != null) {
            y = x;
            if (z.compareTo(x) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        if (y == null) {
            this.setRoot(z);
        } else if (z.compareTo(y) < 0) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }



        return this;
    }


    private void setRoot(Node y) {
        this.root = y;
        y.setParent(null);
    }
}
