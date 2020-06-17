// set the dimensions and margins of the diagram
let color = d3.scaleOrdinal(d3.schemeCategory10);

const Region = Object.freeze({
    EDEN: Symbol("eden"),
    SURVIVOR: Symbol("survivor"),
    OLD: Symbol("old")
});

let treeData = {
    "size": "90",
    "children": [
        {
            "size": "30",
            "children": [
                {
                    "size": "5"
                },
                {
                    "size": "5"
                },
                {
                    "size": "5"
                }
            ]
        }
    ]
};

// set the dimensions and margins of the diagram
var margin = {top: 40, right: 90, bottom: 50, left: 90},
    width = 660 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// declares a tree layout and assigns the size
var treemap = d3.tree()
    .size([width, height]);

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
    .data(nodes.descendants())
    .enter().append("g")
    .attr("class", function (d) {
        return "node" +
            (d.children ? " node--internal" : " node--leaf");
    })
    .attr("transform", function (d) {
        return "translate(" + 300 + "," + 100 + ")";
    });
let tree_height = g.select(".node").data()[0].height;

// adds the circle to the node
node.append("rect")
    .attr("x", d => {
        return -d.x
    })
    .attr("y", d => {
        return 0;
    })
    .attr("width", d => {
        return d.x * 2
    })
    .attr("height", d => {
        return height * d.height / tree_height;
    })
    .attr("fill", "none")
    .attr("stroke", "blue");

// adds the text to the node
node.append("text")
    .attr("dy", ".35em")
    .attr("y", function (d) {
        return d.children ? -20 : 20;
    })
    .style("text-anchor", "middle")
    .text(function (d) {
        return d.data.size;
    });
