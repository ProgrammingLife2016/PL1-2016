var width = window.innerWidth - 10;
var height = window.innerHeight - 10;
var maxZoomLevel = 100;

var nodes;
var edges;
var x;
var y;

$.getJSON("/api/nodes", function (response) {
    nodes = response.nodes;
    edges = response.edges;
    x = d3.scale.linear()
        .domain([0, max(nodes, "x")])
        .range([0, width]);

    y = d3.scale.linear()
        .domain([0, max(nodes, "y")])
        .range([height, 0]);
    drawGraph();
});

var circle;
var line;
var zm;

function drawGraph() {
    zm = d3.behavior.zoom().x(x).y(y).scaleExtent([1, maxZoomLevel]).on("zoom", zoom);
    var tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            getData(d.id);
            return "<strong>Segment:</strong> <span id='data" + d.id + "'>...</span>";
        });
    var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .call(zm)
        .call(tip);

    svg.append("rect")
        .attr("class", "overlay")
        .attr("width", width)
        .attr("height", height);

    line = svg.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("x1", function (d) {return x(d.x1)})
        .attr("y1", function (d) {return y(d.y1)})
        .attr("x2", function (d) {return x(d.x2)})
        .attr("y2", function (d) {return y(d.y2)})
        .attr("stroke", function (d) {var x = d.gens * 2; return "rgb(" + (135 - x) + "," + (206 - x) + "," + (250 - x) + ")"})
        .attr("stroke-width", function (d) {return Math.max(1, d.gens / 10)});

    circle = svg.selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 2.5)
        .attr("transform", "translate(-9999, -9999)")
        .on("mouseover", tip.show)
        .on("mouseout", tip.hide);
}

function zoom() {
    line.attr("x1", function (d) {return x(d.x1)})
        .attr("y1", function (d) {return y(d.y1)})
        .attr("x2", function (d) {return x(d.x2)})
        .attr("y2", function (d) {return y(d.y2)})
        .attr("stroke-width", function (d) {return Math.max(1, d.gens / 10 / zm.scale())});
    if (zm.scale() > 20) {
        circle.attr("transform", transform);
    } else {
        circle.attr("transform", "translate(-9999, -9999)");
    }
}

function transform(d) {
    return "translate(" + x(d.x) + "," + y(d.y) + ")";
}

function max(data, dim) {
    return Math.max(...data.map(function (d) {return d[dim]}));
}

function getData(id) {
    $.get("/api/data/" + id, function (response) {
        $("#data" + id).html(response);
        console.log(response);
        console.log($("#data" + id));
    });
}
