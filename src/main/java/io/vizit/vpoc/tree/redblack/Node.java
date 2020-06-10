package io.vizit.vpoc.tree.redblack;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Node<K extends Comparable, V> implements Comparable<Node<K, V>> {
    @EqualsAndHashCode.Include
    @NotNull
    private final K key;
    private V value;
    private boolean red = true;
    private Node<K, V> parent = null;
    private Node<K, V> left = null;
    private Node<K, V> right = null;

    public void setLeft(Node<K, V> left) {
        this.left = left;
        if (this.left != null) {
            this.left.parent = this;
        }
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
        if (this.right != null) {
            this.right.parent = this;
        }
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    /**
     * check if it is the left child.
     *
     * @return
     */
    public boolean isLeftChild() {
        if (this.isRoot()) {
            throw new RuntimeException("This is a root node.");
        }
        return this == this.parent.getLeft();
    }

    /**
     * check if it is the right child.
     *
     * @return
     */
    public boolean isRightChild() {
        return !this.isLeftChild();
    }

    @Override
    public int compareTo(Node<K, V> o) {
        return this.key.compareTo(o.key);
    }

    public Node<K, V> getGrandpa() {
        return this.getParent().getParent();
    }

    public Node<K, V> getUncle() {
        if (this.isLeftChild()) {
            return this.getParent().getRight();
        }
        return this.getParent().getLeft();
    }

    public void black() {
        this.setRed(false);
    }

    public void red() {
        this.setRed(true);
    }
}
