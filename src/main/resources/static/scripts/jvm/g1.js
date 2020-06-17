// set the dimensions and margins of the diagram
let color = d3.scaleOrdinal(d3.schemeCategory10);

const Region = Object.freeze({
    EDEN: Symbol("eden"),
    SURVIVOR: Symbol("survivor"),
    OLD: Symbol("old")
});

let treeData = {
    key: 625,
    index: 0,
    children: []
};

for (let i = 0; i < 25; i++) {
    let region = {
        key: i * 10,
        index: i
    }
    treeData.children.push(region);
}

// set the dimensions and margins of the diagram
var margin = {top: 40, right: 90, bottom: 50, left: 90},
    width = 900 - margin.left - margin.right,
    height = 900 - margin.top - margin.bottom;

let object_length = 30;
let region_length = object_length * 5;
let heap_length = region_length * 5;

function treemap(root) {
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
        root.x = root.data.index % 5 * object_length;
        root.y = Math.floor(root.data.index / 5) * object_length;
        root.length = object_length;
    }
    if (root.depth > 2) { // object
        console.log("should not be here");
        return root;
    }
    if (root.children == null) {
        return root;
    }
    root.children.forEach(child => treemap(child));
    return root;
}

//  assigns the data to a hierarchy using parent-child relationships
var hierarchy = d3.hierarchy(treeData);

// maps the node data to the tree layout
let nodes = treemap(hierarchy);

// append the svg obgect to the body of the page
// appends a 'group' element to 'svg'
// moves the 'group' element to the top left margin
var svg = d3.select("#paper")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom),
    g = svg.append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

// adds each node as a group
var node = g.selectAll(".node")
    .data(nodes.children)
    .enter().append("g")
    .attr("class", function (d) {
        return "node" +
            (d.children ? " node--internal" : " node--leaf");
    });
// .attr("transform", function (d) {
//     return "translate(" + 300 + "," + 100 + ")";
// });

// adds the circle to the node
node.append("rect")
    .attr("x", d => {
        return d.x
    })
    .attr("y", d => {
        return d.y;
    })
    .attr("width", d => {
        return d.length;
    })
    .attr("height", d => {
        return d.length;
    })
    .attr("fill", "none")
    .attr("stroke", "blue");

// // adds the text to the node
// node.append("text")
//     .attr("dy", ".35em")
//     .attr("y", function (d) {
//         return d.children ? -20 : 20;
//     })
//     .style("text-anchor", "middle")
//     .text(function (d) {
//         return d.data.key;
//     });
