let broker_length = 100;
let broker_padding = 100;
let zk_length = 100;

export function layout(root) {
    if (root.depth == 0) { // zk
        root.x = 300;
        root.y = 0;
        root.length = zk_length;
    }
    if (root.depth == 1) { // brokers
        let index = root.data.index;
        root.x = index * (broker_length + broker_padding);
        root.y = zk_length + broker_padding;
        root.length = broker_length;
        root.id = index;
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