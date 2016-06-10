var width = window.innerWidth - 10;
var height = window.innerHeight - 300;
var miniWidth = width;
var miniHeight = 250;
var maxZoomLevel = 100;
var zoomThreshold = 1;

var colorFactor;
var widthFactor;

var nodes;
var edges;
var x;
var y;

var somethingIsHighlighted = false;

var miniX;
var miniY;

var previousZoom = 128;

function newZoomLevel(s) {
    if (s < 10) {
        return 128;
    } else if (5 <= s && s < 20) {
        return 4;
    } else {
        return 1;
    }
}

var lineageColors = {
    "LIN 1": "#ed00c3",
    "LIN 2": "#0000ff",
    "LIN 3": "#500079",
    "LIN 4": "#ff0000",
    "LIN 5": "#4e2c00",
    "LIN 6": "#69ca00",
    "LIN 7": "#ff7e00",
    "LIN animal": "#00ff9c",
    "LIN B": "#00ff9c",
    "LIN CANETTII": "#00ffff"
}

function startD3() {
    $.getJSON("/api/nodes/128/0/100000000", function (response) {
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
            colorFactor = 20;
            widthFactor = 1;
        }
        drawGraph();
        drawMinimap();
        d3.select("#jump").on("click", jumpToBaseGetFromDOM);
    });
}

var circle;
var line;
var zm;
var svg;
var tip;

var minimap;

function drawGraph() {
    zm = d3.behavior.zoom().x(x).scaleExtent([1, maxZoomLevel]).on("zoom", zoom);
    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            getData(d.id);
            return "<strong>Segment:</strong> <span id='data" + d.id + "'>...</span>";
        });
    svg = d3.select("#d3").append("svg")
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
        .attr("stroke", defaultColor)
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});

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
        .attr("stroke", defaultColor)
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});

    rect = minimap
        .append("rect")
        .attr("class", "minimapRect")
        .attr("x", 0)
        .attr("y", 0)
        .attr("width", miniWidth)
        .attr("height", miniHeight);
}
var previousX = [0, 1000000];
function zoom(beginX) {
    var t = d3.event.translate;
    var s = d3.event.scale;

    if (beginX) {
        t[0] = beginX;
        t[1] = beginX + 1000;
    }

    if (Math.abs(previousX[0] - x.domain()[0]) >= 1500 || Math.abs(previousX[1] - x.domain()[1]) >= 1500) {
        previousX = x.domain();
        $.ajax({
                    url: "/api/nodes/" + previousZoom + "/" + (Math.round(x.domain()[0]) - 500) + "/" + (Math.round(x.domain()[1]) + 500),
                    async: false,
                    success: function (response) {
                        response = JSON.parse(response);
                        nodes = response.nodes;
                        edges = response.edges;

                        line.remove();
                        circle.remove();
                        line = svg.selectAll("line")
                            .data(edges)
                            .enter()
                            .append("line")
                            .attr("x1", function (d) {return x(d.x1)})
                            .attr("y1", function (d) {return y(d.y1)})
                            .attr("x2", function (d) {return x(d.x2)})
                            .attr("y2", function (d) {return y(d.y2)})
                            .attr("stroke", defaultColor)
                            .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});

                        circle = svg.selectAll("circle")
                            .data(nodes)
                            .enter()
                            .append("circle")
                            .attr("r", 2.5)
                            .attr("transform", "translate(-9999, -9999)")
                            .on("mouseover", tip.show)
                            .on("mouseout", tip.hide);
                        }
                });
    }
    if (Math.abs(previousZoom - newZoomLevel(s)) >= zoomThreshold) {
        previousZoom = newZoomLevel(s);
        console.log(previousZoom);
        console.log(newZoomLevel(s));
        $.ajax({
            url: "/api/nodes/" + newZoomLevel(s) + "/" + Math.round(x.domain()[0]) + "/" + Math.round(x.domain()[1]),
            async: false,
            success: function (response) {
                response = JSON.parse(response);
                nodes = response.nodes;
                edges = response.edges;

                line.remove();
                circle.remove();
                line = svg.selectAll("line")
                    .data(edges)
                    .enter()
                    .append("line")
                    .attr("x1", function (d) {return x(d.x1)})
                    .attr("y1", function (d) {return y(d.y1)})
                    .attr("x2", function (d) {return x(d.x2)})
                    .attr("y2", function (d) {return y(d.y2)})
                    .attr("stroke", defaultColor)
                    .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});

                circle = svg.selectAll("circle")
                    .data(nodes)
                    .enter()
                    .append("circle")
                    .attr("r", 2.5)
                    .attr("transform", "translate(-9999, -9999)")
                    .on("mouseover", tip.show)
                    .on("mouseout", tip.hide);

                somethingIsHighlighted && (resetHighlighting() | highlightGenome(somethingIsHighlighted));
                }
        });
    }
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
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / zm.scale() * 2)});
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

function highlightGenome(genome) {
    somethingIsHighlighted = genome;
    window.graphHandler.setSelectedGenome(genome);
    window.graphHandler.showGraph();
    line.attr("stroke", function (d) {
        d.highlighted = d.genomes.map(function (x) {return x.split("_").join(" ")}).indexOf(genome.split("_").join(" ")) != -1;
        if (d.highlighted) {
            return "#eeee00";
        } else {
            return defaultColor(d);
        }
    })
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / zm.scale() * 2)});
}

function disableHighlighting() {
    somethingIsHighlighted = false;
    line.attr("stroke", function (d) {
        d.highlighted = false;
        return defaultColor(d);
    })
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / 10 / zm.scale())});
}

function defaultColor(d) {
    return lineageColors[mode(d.lineages)] || "#000000";
}

function highlightLineage(genome) {
    $.get("/api/lineage/" + genome.split(" ").join("_"), function(lineage) {
        window.graphHandler.setSelectedGenome(genome, lineageColors[lineage], lineage);
        window.graphHandler.showGraph();
        line.attr("stroke", function (d) {
            if (d.lineages.indexOf(lineage) != -1) {
                d.lineageHighlighted = true;
                d.currentColor = lineageColors[lineage];
                return lineageColors[lineage];
            } else {
                return defaultColor(d);
            }
        });
        actuallyHighlightGenome(genome);
    });
}

function jumpToBaseGetFromDOM() {
    var e = document.getElementById("fuzzOptionsList");
    var strUser = e.options[e.selectedIndex].value;
    console.log(strUser + " " + $("#baseindex").text());
    $.getJSON("/api/metadata/navigate/" + strUser + "/" + $("#baseindex").val(), function (response) {
        var dx = 10;
        var dy = 10;
        var x = response.x + 5;
        var y = 10;
        var scale = Math.max(1, Math.min(8, 0.9 / Math.max(dx / width, dy / height)));
        var translate = [width / 2 - scale * x, height / 2 - scale * y];

        svg.transition()
            .duration(750)
            .call(zm.translate(translate).scale(scale).event);
    });
}

function jumpToBase(genome, index) {
    $.ajax({
        url: "/api/metadata/navigate/" + genome + "/" + index,
        async: false,
        success: function (response) {
            return JSON.parse(response).x;
        }
    });
}
function mode(array) {
	if (array.length == 0) {
		return null;
	}
	var modeMap = {};
	var maxEl = array[0]
    var maxCount = 1;
    for (var i = 0; i < array.length; i++) {
		var el = array[i];
		if (modeMap[el] == null) {
			modeMap[el] = 1;
		} else {
			modeMap[el]++;
	    }
		if (modeMap[el] > maxCount) {
			maxEl = el;
			maxCount = modeMap[el];
		}
	}
    return maxEl;
}