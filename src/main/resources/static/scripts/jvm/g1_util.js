let object_length = 30;
let region_length = object_length * 5;
let heap_length = region_length * 5;

export function treemap(root) {
    if (root.depth == 0) { // heap
        root.x = 0;
        root.y = 0;
        root.length = heap_length;
    }
    if (root.depth == 1) { // region
        root.x = root.data.index % 5 * region_length;
        root.y = Math.floor(root.data.index / 5) * region_length;
        root.length = region_length;
    }
    if (root.depth == 2) { // object
        root.x = root.parent.x + root.data.index % 5 * object_length;
        root.y = root.parent.y + Math.floor(root.data.index / 5) * object_length;
        root.length = object_length;
    }
    if (root.depth > 2) { // object
        console.log("should not be here");
        return root;
    }
    if (root.children == null) {
        return root;
    }
    root.decendents = root.children;
    for (let i = 0; i < root.children.length; i++) {
        let child = treemap(root.children[i]);
        root.decendents.concat(child.decendents);
    }
    return root;
}