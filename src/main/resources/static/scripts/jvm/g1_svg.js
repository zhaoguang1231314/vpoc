import {treemap, position, object_length} from "./g1_util.js";

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

export function allocate(obj) {
    let region = d3.select('#region-' + obj.region.id);
    let {x, y} = position(region, obj.address);
    let obj_g = g.append('g')
        .attr("transform",
            "translate(" + (x + object_length / 2) + "," + (y + object_length / 2) + ")")
        .attr("id", 'obj-' + obj.id)
        .classed('region-obj-' + obj.region.id, true);

    obj_g.append("circle")
        .attr("cx", 0)
        .attr("cy", 0)
        .attr("r", object_length / 2)
        .attr("fill", colorCategory10(Math.floor(Math.random() * 10)))
        .attr("stroke", "blue");
    obj_g.append("text")
        .attr("x", -5)
        .attr("y", 5)
        .text(obj.age)
        .attr("stroke", "black");
}

export function mark(obj) {
    let obj_g = d3.select('#obj-' + obj.id);
    obj_g.select('circle')
        .transition()
        .duration(1000)
        .attr("transform", "scale(1.2)")
        .attr("fill", "white");

}

export function copy(data) {
    let obj_g = d3.select('#obj-' + data.objectBO.id);
    let region = d3.select('#region-' + data.toRegion.id);
    let {x, y} = position(region, data.objectBO.address);
    obj_g.classed('region-obj-' + data.fromRegion.id, false)
        .classed('region-obj-' + data.toRegion.id, true)
        .transition()
        .duration(1000)
        .attr("transform",
            "translate(" + (x + object_length / 2) + "," + (y + object_length / 2) + ")");
    obj_g.select('circle')
        .transition()
        .duration(1000)
        .attr("transform", "scale(0.95)")
        .attr("fill", colorCategory10(Math.floor(Math.random() * 10)));
    obj_g.select("text")
        .attr("x", -5)
        .attr("y", 5)
        .text(data.objectBO.age)
        .attr("stroke", "black");
}

export function sweep(data) {
    d3.selectAll('.region-obj-' + data.region.id)
        .transition('sweep')
        .duration(1000)
        .delay(1000)
        .remove();
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
        .classed('region', true)
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
        .classed('region_label', true)
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