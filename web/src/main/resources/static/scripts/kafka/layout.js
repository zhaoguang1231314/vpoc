let node_width = 200;
let node_height = 100;
let x_padding = 200;
let y_padding = 300;

export function layout(root) {
    if (root.depth == 0) {
        root.x = 0;
        root.y = 0;
    }
    if (root.depth == 1) {
        let index = root.data.index;
        root.width = node_width;
        root.height = node_height;
        // zk
        if (root.data.key == 'zk') {
            root.x = 400;
            root.y = 100;
            root.id = 'zk';
            return root;
        }
        // brokers
        root.x = index * (node_width + x_padding);
        root.y = 100 + y_padding;
        root.id = 'broker-'+index;
    }

    if (root.depth > 1) { // object
        console.log("should not be here");
        return root;
    }
    if (root.children == null) {
        return root;
    }
    root.decendents = root.children;
    for (let i = 0; i < root.children.length; i++) {
        let child = layout(root.children[i]);
        root.decendents.concat(child.decendents);
    }
    return root;
}