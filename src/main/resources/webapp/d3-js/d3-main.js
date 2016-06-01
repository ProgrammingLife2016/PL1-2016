var miniHeight = 250;
var width = $("#d3").width();
var height = $(document).height() - $("#nav").height() - miniHeight;

var miniWidth = width;
var maxZoomLevel = 100;

var colorFactor;
var widthFactor;

var nodes;
var edges;
var x;
var y;

var miniX;
var miniY;


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
    var svg = d3.select("#d3").append("svg")
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
        .attr("stroke", function (d) {
           var x = d.gens * colorFactor;
           return "rgb(" + (77 - x) + "," + (202 - x) + "," + (207 - x) + ")"
         })
        .attr("stroke-width", function (d) {return Math.max(1, d.gens / widthFactor)});

    circle = svg.selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 5)
        .attr("transform", "translate(-9999, -9999)")
        .attr("fill", "#94AAC7")
        .on("mouseover", tip.show)
        .on("mouseout", tip.hide);
}

var rect;

function drawMinimap() {
    minimap = d3.select("#d3").append("svg")
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
        .attr("stroke", function (d) {
           var x = d.gens * colorFactor;
           return "rgb(" + (198 - x) + "," + (211 - x) + "," + (209 - x) + ")"
        })
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
