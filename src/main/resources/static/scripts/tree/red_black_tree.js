const Colors = Object.freeze({
    RED: {name: "red"},
    GREEN: {name: "black"}
});

class Node {
    constructor(key, value, color, parent) {
        this.key = key;
        this.value = value;
        this.color = color;
        this.parent = parent;
        this.right = null;
        this.left = null;
    }

    set_right(x) {
        this.right = x;
        x.parent = this;
    }

    is_root() {
        return this.parent == null;
    }
}

class Red_black_tree {
    constructor(root) {
        this.root = root;
    }

    set_root(x) {
        this.root = x;
        x.parent = null;
    }

    left_rotate(x) {
        let y = x.right;
        x.set_right(y.left);
        if (x.is_root()) {
            x.set_root(y);
        } else {
            let x_parent = x.parent;
            if (x.key == x_parent.left.key) {

            }
        }

    }


}