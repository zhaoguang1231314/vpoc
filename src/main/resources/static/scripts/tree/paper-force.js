// set the dimensions and margins of the diagram
let margin = {top: 100, right: 90, bottom: 50, left: 90},
    width = 960 - margin.left - margin.right,
    height = 800 - margin.top - margin.bottom;
let colors = d3.scaleOrdinal(d3.schemeCategory10);

function init_paper() {
    let svg = d3.select("#paper")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom),
        paper = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    return paper;
}


export function init() {
    let paper = init_paper();

    d3.json("data/tree.json").then(function (tree_data) {
            let root = d3.hierarchy(tree_data);
            let links = root.links();
            let nodes = root.descendants();

            let simulation = d3.forceSimulation(nodes)
                .force("link", d3.forceLink(links).id(d => d.id).distance(100).strength(1))
                .force("charge", d3.forceManyBody().strength(-50))
                .force("x", d3.forceX())
                .force("y", d3.forceY());

            //Create nodes as circles
            let circles = paper.selectAll("circle")
                .data(nodes)
                .enter()
                .append("circle")
                .attr("r", 10)
                .style("fill", function (d, i) {
                    return colors(i);
                });

            //Create edges as lines
            let edges = paper.selectAll("line")
                .data(links)
                .enter()
                .append("line")
                .style("stroke", "#ccc")
                .style("stroke-width", 1);

            //Every time the simulation "ticks", this will be called
            simulation.on("tick", function () {

                edges.attr("x1", function (d) {
                    return d.source.x;
                })
                    .attr("y1", function (d) {
                        return d.source.y;
                    })
                    .attr("x2", function (d) {
                        return d.target.x;
                    })
                    .attr("y2", function (d) {
                        return d.target.y;
                    });

                circles.attr("cx", function (d) {
                    return d.x;
                })
                    .attr("cy", function (d) {
                        return d.y;
                    });

            });
        }
    );


    return paper;
}

export function update(paper) {
    d3.json("data/tree_update.json").then(function (tree_data) {
        }
    );
}