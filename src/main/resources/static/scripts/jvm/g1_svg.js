import {treemap} from "./g1_util.js";

const Region = Object.freeze({
    EDEN: Symbol("eden"),
    SURVIVOR: Symbol("survivor"),
    OLD: Symbol("old")
});
let color = d3.scaleOrdinal(d3.schemeCategory10);
let margin = {top: 40, right: 90, bottom: 50, left: 90},
    width = 900 - margin.left - margin.right,
    height = 900 - margin.top - margin.bottom;

let svg = d3.select("#paper")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom),
    g = svg.append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

export function gc() {

}

export function init_heap() {
// set the dimensions and margins of the diagram
    let treeData = {
        key: 625,
        index: 0,
        children: []
    };

    for (let i = 0; i < 25; i++) {
        let region = {
            key: i,
            index: i,
            children: []
        }
        treeData.children.push(region);
    }

    let hierarchy = d3.hierarchy(treeData);
    let root = treemap(hierarchy);

    var node = g.selectAll(".node")
        .data(root.decendents)
        .enter()
        .append("rect")
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
}


// .attr("transform", function (d) {
//     return "translate(" + 300 + "," + 100 + ")";
// });

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



