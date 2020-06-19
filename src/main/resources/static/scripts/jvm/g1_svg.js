import {treemap, position, object_length} from "./g1_util.js";

let colorCategory10 = d3.scaleOrdinal(d3.schemeCategory10);
let colorPastel2 = d3.scaleOrdinal(d3.schemePastel2);
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

export function allocate(obj) {
    let region = d3.select('#region-' + obj.region.id);
    let {x, y} = position(region, obj.address);
    g.append("circle")
        .attr("id", 'obj-' + obj.id)
        .attr("cx", x + object_length / 2)
        .attr("cy", y + object_length / 2)
        .attr("r", object_length / 2)
        .attr("fill", colorCategory10(Math.floor(Math.random() * 10)))
        .attr("stroke", "blue");
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

    g.selectAll(".region")
        .data(root.decendents)
        .enter()
        .append("rect")
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
            if (i % 2 == 0) {
                // region.setRegionType(Region.RegionType.EDEN);
                color = colorPastel2(0);
            } else {
                // region.setRegionType(Region.RegionType.OLD);
                color = colorPastel2(1);
            }
            if (i == 11 || i == 13) {
                // region.setRegionType(Region.RegionType.SURVIVOR);
                color = colorPastel2(2);
            }
            if (i == 24) {
                // region.setRegionType(Region.RegionType.HUMONGOUS);
                color = colorPastel2(3);
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
        .style("text-anchor", "middle")
        .text(function (d) {
            let i = d.id;
            let text = 'E';
            if (i % 2 == 1) {
                text = 'O';
            }
            if (i == 11 || i == 13) {
                text = 'S';
            }
            if (i == 24) {
                text = 'H';
            }
            return text;
        });
}