// set the dimensions and margins of the diagram
let margin = {top: 100, right: 90, bottom: 50, left: 90},
    width = 960 - margin.left - margin.right,
    height = 800 - margin.top - margin.bottom;
let colors = d3.scaleOrdinal(d3.schemeCategory10);
// declares a tree layout and assigns the size
let tree = d3.tree().size([width, height]);

function init_paper() {
    let svg = d3.select("#paper")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom),
        paper = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    return paper;
}

let key = function (d) {
    return d.key;
};

export function init() {
    let paper = init_paper();

    d3.json("data/tree.json").then(function (tree_data) {
//  assigns the data to a hierarchy using parent-child relationships
            let nodes = d3.hierarchy(tree_data);
// maps the node data to the tree layout
            nodes = tree(nodes);

// adds the links between the nodes
            var link = paper.selectAll(".link")
                .data(nodes.descendants().slice(1))
                .enter().append("path")
                .attr("class", "link")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y
                        + "C" + d.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + d.parent.y;
                });

// adds each node as a group
            var node = paper.selectAll(".node")
                .data(nodes.descendants())
                .enter().append("g")
                .attr("class", function (d) {
                    return "node" +
                        (d.children ? " node--internal" : " node--leaf");
                })
                .attr("transform", function (d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });

// adds the circle to the node
            node.append("circle")
                .attr("r", 10);

// adds the text to the node
            node.append("text")
                .attr("dy", ".35em")
                .attr("y", function (d) {
                    return d.children ? -20 : 20;
                })
                .style("text-anchor", "middle")
                .text(function (d) {
                    return d.data.key;
                });
        }
    );


    return paper;
}

export function update(paper) {
    d3.json("data/tree_update.json").then(function (tree_data) {
            //  assigns the data to a hierarchy using parent-child relationships
            let nodes = d3.hierarchy(tree_data);
// maps the node data to the tree layout
            nodes = tree(nodes);
// adds the links between the nodes
            let link = paper.selectAll(".link")
                .data(nodes.descendants().slice(1))
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y
                        + "C" + d.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + d.parent.y;
                });

            let new_link = link.enter().append("path")
                .attr("class", "link")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y
                        + "C" + d.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + (d.y + d.parent.y) / 2
                        + " " + d.parent.x + "," + d.parent.y;
                })
                .merge(link)
                .transition()
                .duration(1000);
            link.exit().remove();

// adds each node as a group
            let node = paper.selectAll(".node")
                .data(nodes.descendants())
                .attr("transform", function (d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });

            let new_node = node.enter().append("g")
                .attr("class", function (d) {
                    return "node" +
                        (d.children ? " node--internal" : " node--leaf");
                })
                .attr("transform", function (d) {
                    return "translate(" + d.x + "," + d.y + ")";
                })


// adds the circle to the node
            new_node.append("circle")
                .attr("r", 10);

// adds the text to the node
            new_node.append("text")
                .attr("dy", ".35em")
                .attr("y", function (d) {
                    return d.children ? -20 : 20;
                })
                .style("text-anchor", "middle")
                .text(function (d) {
                    return d.data.key;
                });

            new_node.merge(node)
                .transition()
                .duration(1000);

            node.exit().remove();
        }
    );
}