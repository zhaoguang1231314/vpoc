package io.vizit.vpoc.tree.redblack;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Node<K, V> {
    @EqualsAndHashCode.Include
    private K key;
    private V value;
    private Color color;
    private Node<K, V> parent;
    private Node<K, V> left;
    private Node<K, V> right;

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
     * check if parent is the left child of this node.
     *
     * @param parent
     * @return
     */
    public boolean isLeftOf(Node parent) {
        return this == parent.getLeft();
    }
}
