package io.vizit.vpoc.tree.redblack;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Node<K extends Comparable, V> implements Comparable<Node<K, V>> {
    @EqualsAndHashCode.Include
    @NotNull
    private K key;
    private V value;
    private boolean red = true;
    private Node<K, V> parent = null;
    private Node<K, V> left = null;
    private Node<K, V> right = null;

    public void setLeft(Node<K, V> left) {
        this.left = left;
        this.left.parent = this;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
        this.right.parent = this;
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

    @Override
    public int compareTo(Node<K, V> o) {
        return this.key.compareTo(o);
    }
}
