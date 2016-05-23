var width = window.innerWidth - 10;
var height = window.innerHeight - 10;

var nodes;
var edges;
var x_scale;
var y_scale;

$.getJSON("/api/nodes", function (response) {
    nodes = response.nodes;
    edges = response.edges;
    x_scale = d3.scale.linear()
        .domain([0, max(nodes, "x")])
        .range([0, width]);

    y_scale = d3.scale.linear()
        .domain([0, max(nodes, "y")])
        .range([height, 0]);
    drawGraph();
});


var svg = d3.select("body").append("svg");

svg.attr("width", width)
    .attr("height", height);

svg.append("rect")
    .attr("class", "overlay")
    .attr("width", width)
    .attr("height", height);

function max(data, dim) {
    return Math.max(...data.map(function (d) {return d[dim]}));
}


function drawGraph() {
    svg.selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 2.5)
        .attr("transform", transform);

    svg.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("x1", function (d) {return x_scale(d.x1)})
        .attr("y1", function (d) {return y_scale(d.y1)})
        .attr("x2", function (d) {return x_scale(d.x2)})
        .attr("y2", function (d) {return y_scale(d.y2)})
        .attr("stroke", "#87cefa")
        .attr("stroke-width", "1");

}

function transform(d) {
    return "translate(" + x_scale(d.x) + "," + y_scale(d.y) + ")";
}
