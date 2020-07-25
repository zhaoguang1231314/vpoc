import {layout} from "./layout.js";

let colorCategory10 = d3.scaleOrdinal(d3.schemeCategory10);
let colorPastel2 = d3.scaleOrdinal(d3.schemePastel2);
let margin = {top: 40, right: 90, bottom: 50, left: 150},
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
        key: 625,
        index: 0,
        children: []
    };

    for (let i = 0; i < 3; i++) {
        let broker = {
            key: i,
            index: i,
            children: []
        }
        treeData.children.push(broker);
    }

    let hierarchy = d3.hierarchy(treeData);
    let root = layout(hierarchy);

    g.selectAll(".broker")
        .data(root.decendents)
        .enter()
        .append("rect")
        .classed('broker', true)
        .attr("id", d => {
            return 'region-' + d.id
        })
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
        .attr("fill", d => {
            let i = d.id;
            let color = colorPastel2(0);
            if (i == 11 || i == 13) {
                color = colorPastel2(2);
            } else if (i == 24) {
                color = colorPastel2(3);
            } else if (i % 2 == 0 && i < 10) {
                color = colorPastel2(0);
            } else {
                color = colorPastel2(1);
            }
            return color;
        })
        .attr("stroke", "blue");

    g.selectAll(".region_label")
        .data(root.decendents)
        .enter()
        .append("text")
        .attr("x", d => {
            return d.x + d.length / 2;
        })
        .attr("y", d => {
            return d.y + d.length / 2;
        })
        .classed('region_label', true)
        .style("text-anchor", "middle")
        .text(function (d) {
            let i = d.id;
            let text = 'E';
            if (i == 11 || i == 13) {
                text = 'S';
            } else if (i == 24) {
                text = 'H';
            } else if (i % 2 == 0 && i < 10) {
                text = 'E';
            } else {
                text = 'O';
            }
            return text;
        });
}