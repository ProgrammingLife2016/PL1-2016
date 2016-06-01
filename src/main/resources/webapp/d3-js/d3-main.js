var width = window.innerWidth - 10;
var height = window.innerHeight - 300;
var miniWidth = width;
var miniHeight = 250;
var maxZoomLevel = 100;

var colorFactor;
var widthFactor;

var nodes;
var edges;
var x;
var y;

var miniX;
var miniY;

$.getJSON("/api/nodes", function (response) {
    nodes = response.nodes;
    edges = response.edges;
    x = d3.scale.linear()
        .domain([0, max(nodes, "x")])
        .range([0, width]);

    y = d3.scale.linear()
        .domain([0, max(nodes, "y")])
        .range([height, 0]);

    miniX = d3.scale.linear()
        .domain([0, max(nodes, "x")])
        .range([0, miniWidth]);
    miniY = d3.scale.linear()
        .domain([0, max(nodes, "y")])
        .range([miniHeight, 0]);

    if (nodes.length > 9000) {
        colorFactor = 2;
        widthFactor = 10;
    } else {
        colorFactor = 15;
        widthFactor = 2;
    }
    drawGraph();
    drawMinimap();
});

var circle;
var line;
var zm;

var minimap;

function drawGraph() {
    zm = d3.behavior.zoom().x(x).scaleExtent([1, maxZoomLevel]).on("zoom", zoom);
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
        .attr("stroke", function (d) {var x = d.gens * colorFactor; return "rgb(" + (135 - x) + "," + (206 - x) + "," + (250 - x) + ")"})
        .attr("stroke-width", function (d) {return Math.max(1, d.gens / widthFactor)});

    circle = svg.selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 2.5)
        .attr("transform", "translate(-9999, -9999)")
        .on("mouseover", tip.show)
        .on("mouseout", tip.hide);
}

var rect;

function drawMinimap() {
    minimap = d3.select("body").append("svg")
        .attr("width", miniWidth)
        .attr("height", miniHeight)
        .append("g");

    minimap.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("x1", function (d) {return miniX(d.x1)})
        .attr("y1", function (d) {return miniY(d.y1)})
        .attr("x2", function (d) {return miniX(d.x2)})
        .attr("y2", function (d) {return miniY(d.y2)})
        .attr("stroke", function (d) {var x = d.gens * colorFactor; return "rgb(" + (255- x) + "," + (127 - x) + "," + (0) + ")"})
        .attr("stroke-width", function (d) {return Math.max(1, d.gens / widthFactor)});

    rect = minimap
        .append("rect")
        .attr("class", "minimapRect")
        .attr("x", 0)
        .attr("y", 0)
        .attr("width", miniWidth)
        .attr("height", miniHeight);
}

function zoom() {
    var t = d3.event.translate;
    var s = d3.event.scale;
    if (t[0] > 0) {
        t[0] = 0;
    } else if (t[0] < - width * (s - 1)) {
        t[0] = - width * (s - 1);
    }
    console.log("t = " + t + ", s = " + s);
    zm.translate(t);
    var visibleX1 = miniX(x.invert(0));
    var visibleX2 = miniX(x.invert(width));
    var visibleY1 = miniY(y.invert(0));
    var visibleY2 = miniY(y.invert(height));
    rect.attr("x", visibleX1)
        .attr("y", visibleY1)
        .attr("width", visibleX2 - visibleX1)
        .attr("height", visibleY2 - visibleY1);
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
