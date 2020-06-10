package io.vizit.vpoc.tree.redblack;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Tree<K extends Comparable, V> {
    private Node<K, V> root;

    public Tree<K, V> rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.getRight();
        // y.left goes to x.right
        x.setRight(y.getLeft());
        findPlace4Y((Node<K, V>) x, (Node<K, V>) y);

        // x goes to y's left
        y.setLeft(x);

        return this;
    }

    public Tree<K, V> rotateRight(Node<K, V> x) {
        Node<K, V> y = x.getLeft();
        // y.right goes to x.left
        x.setLeft(y.getRight());

        // find place for y: y replace x's old place
        findPlace4Y(x, y);

        // x goes to y's right
        y.setRight(x);
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
        return insertFixup(z);
    }

    private void findPlace4Y(Node<K, V> x, Node<K, V> y) {
        Node<K, V> xParent = x.getParent();
        if (x.isRoot()) {
            this.setRoot(y); // y becomes root
        } else if (x.isLeftChild()) {
            xParent.setLeft(y);
        } else {
            xParent.setRight(y);
        }
    }

    private Tree<K, V> insertFixup(Node<K, V> z) {
        while (z != null && !z.isRoot() && z.getParent().isRed()) {
            if (z.getParent().isLeftChild()) {
                if (z.getUncle().isRed()) { // case 1: both parent and uncle is red
                    flipColor(z);
                    z = z.getGrandpa(); // fixup grandpa
                } else { // case 2: uncle is black
                    if (z.isRightChild()) {
                        rotateLeft(z.getParent()); // rotate z's parent left
                    }
                    flipColor(z);
                    rotateRight(z.getGrandpa());
                }
            } else { // case 2
                if (z.getUncle().isRed()) {
                    flipColor(z);
                    z = z.getGrandpa();
                } else {
                    if (z.isLeftChild()) {
                        rotateRight(z.getParent());
                    }
                    flipColor(z);
                    rotateLeft(z.getGrandpa());
                }
            }
        }
        this.root.black();
        return this;
    }

    private void flipColor(Node<K, V> z) {
        z.getParent().black();
        z.getUncle().black();
        z.getGrandpa().red();
    }

    private void setRoot(Node y) {
        this.root = y;
        y.setParent(null);
    }
}
