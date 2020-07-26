import {layout} from "./layout.js";

let colorCategory10 = d3.scaleOrdinal(d3.schemeCategory10);
let colorPastel2 = d3.scaleOrdinal(d3.schemePastel2);
let margin = {top: 40, right: 90, bottom: 50, left: 50},
    width = 1200 - margin.left - margin.right,
    height = 1000 - margin.top - margin.bottom;

let svg = d3.select("#paper")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom),
    g = svg.append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");


export function init() {
    let treeData = {
        key: "kafka",
        index: 0,
        children: []
    };
    let zk = {
        key: "zk",
        index: 0,
        children: []
    }
    treeData.children.push(zk);

    for (let i = 0; i < 3; i++) {
        let broker = {
            key: "broker-" + i,
            index: i,
            children: []
        }
        treeData.children.push(broker);
    }

    let hierarchy = d3.hierarchy(treeData);
    let root = layout(hierarchy);

    g.selectAll(".node")
        .data(root.decendents)
        .enter()
        .append("rect")
        .classed('node', true)
        .attr("id", d => {
            return d.id
        })
        .attr("x", d => {
            return d.x
        })
        .attr("y", d => {
            return d.y;
        })
        .attr("width", d => {
            return d.width;
        })
        .attr("height", d => {
            return d.height;
        })
        .attr("fill", d => {
            if (d.data.key == 'zk') {
                return colorPastel2(0);
            }
            let i = d.data.index;
            return colorPastel2(i+1);
        })
        .attr("stroke", "blue");

    g.selectAll(".node_label")
        .data(root.decendents)
        .enter()
        .append("text")
        .attr("x", d => {
            return d.x + d.width / 2;
        })
        .attr("y", d => {
            return d.y + d.height / 2;
        })
        .classed('node_label', true)
        .style("text-anchor", "middle")
        .text(function (d) {
            return d.id;
        });
}