var width = window.innerWidth - 10;
var height = window.innerHeight - 400;
var miniWidth = width/2;
var miniHeight = 350;
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
var zoom_levels = [4, 16, 32]
var previousZoom = 128;
var options;
var svg;

function newZoomLevel(s) {
    if (s < 5) {
        return zoom_levels[2];
    } else if (5 <= s && s < 7.5) {
        return zoom_levels[1];
    } else if (7.5 <= s && s < 12.5) {
        return zoom_levels[0];
    } else if (15 <= s && s < 20) {
        return zoom_levels[0];
    } else {
        return zoom_levels[0];
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
        line = d3.select("line");
        circle = d3.select("circle");
        update(128,0,1000000, true, 0)
        initializeGraph(nodes,edges);
        drawMinimap();
        drawGraph(128,0,1000000, true, 0);
        setTKKs();
        setOptions();
}

function drawGraph(threshold, minX, maxX, requestNodes, minContainersize) {
    update(threshold, minX, maxX, requestNodes, minContainersize);
    line.remove();
    circle.remove();
    line = drawLines(svg, edges);
    circle = drawNodes(svg, nodes);
    somethingIsHighlighted && (resetHighlighting() | highlightGenome(somethingIsHighlighted));
}
function update(threshold, minX, maxX, requestNodes, minContainersize) {
    console.log("/api/nodes/" + threshold + "/" + minX + "/" + maxX + "/" + requestNodes + "");
    $.ajax({
        url: "/api/nodes/" + threshold + "/" + minX + "/" + maxX + "/" + requestNodes  + "/" + minContainersize,
        async: false,
        success: function (response) {
            response = JSON.parse(response);
            nodes = response.nodes;
            edges = response.edges;
        }
    });
}

function initializeGraph(nodes, edges) {
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

    zm = d3.behavior.zoom().x(x).scaleExtent([1, maxZoomLevel]).on("zoom", zoom);
    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            getData(d.id);
            return "<strong>Segment:</strong> <span id='data" + d.id + "'>...</span>";
        });
    svg = d3.select("#d3").insert("svg",":first-child")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .call(zm)
        .call(tip);

    svg.append("rect")
        .attr("class", "overlay")
        .attr("width", width)
        .attr("height", height);


}


var rect;


function drawMinimap() {
    minimap = d3.select("#d3").insert("svg",":nth-child(2)")
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
    var requestNodes = true;
    var minContainersize = 6;
    if (zm.scale() > 10) {
        requestNodes = true;
        minContainersize = 0;
    }
    if (Math.abs(previousX[0] - x.domain()[0]) >= 10000/s || Math.abs(previousX[1] - x.domain()[1]) >= 10000/s) {
        previousX = x.domain();
        drawGraph(previousZoom, (Math.round(x.domain()[0]) - 500), (Math.round(x.domain()[1]) + 500), requestNodes, minContainersize);
    }
    if (Math.abs(previousZoom - newZoomLevel(s)) >= zoomThreshold) {
        previousZoom = newZoomLevel(s);
        drawGraph(newZoomLevel(s), Math.round(x.domain()[0]), Math.round(x.domain()[1]), requestNodes, minContainersize);

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
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});
    circle.attr("transform", transform);
}

function drawLines(svg, edges) {
    return svg.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("x1", function (d) {return x(d.x1)})
        .attr("y1", function (d) {return y(d.y1)})
        .attr("x2", function (d) {return x(d.x2)})
        .attr("y2", function (d) {return y(d.y2)})
        .attr("stroke", defaultColor)
        .attr("stroke-width", function (d) {return Math.max(1, d.genomes.length / widthFactor)});
}

function drawNodes(svg, nodes) {
    return svg.selectAll("path")
        .data(nodes)
        .enter()
        .append("path")
        .attr("d", d3.svg.symbol()
            .type(function(d) {
                if (d.containersize == 3){
                    return "triangle-up";
                } else if (d.containersize == 4){
                    return "diamond";
                } else {
                    return "circle";
                }
            })
            .size("500")
            )

        .attr("fill", function(d) {
            if (d.containersize == 3){
                return "orange";
            } else if (d.containersize == 4) {
                return "green";
            } else if (d.containersize == 1) {
                return "pink";
            }
        })
        .attr("transform", "translate(-9999, -9999)")
        .on("mouseover", tip.show)
        .on("mouseout", tip.hide);
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
    var strUser = $(".tkks").chosen().val();
    console.log(strUser + " " + $("#baseindex").val());
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

var currentSelection = {};

function setOptions() {
    $.getJSON("/api/metadata/options", function(response) {
        options = response.options;
        $.each(response.options, function(key, value){
                currentSelection[key] = [];
                $(".metadata").append("<option value=\"" + key + "\">" + key + "</option>");
        });
        var my_options = $(".metadata option");
        my_options.sort(function(a,b) {
            if (a.text > b.text) return 1;
            else if (a.text < b.text) return -1;
            else return 0
        });
        $(".metadata").empty().append(my_options);

        $(".metadata").chosen({ search_contains: true });

    });

    $(".metadata").on('change', function(event, params){
        $.each(params, function(key, value){
            if (key == "selected") {
                $("#characteristics").append("<div class=\"search_item\"><span>" + value + ": </span><select multiple id =\"" + value + "\" data-placeholder=\"Select " + value + "\" ></select></div>");
                $("#"+value).on('change', function(event, params) {
                    updateCharacteristic(event, params);
                });
                for (var i = 0; i < options[value].length; i++) {
                    $("#"+value).append("<option value=\"" + options[value][i] + "\">" + options[value][i] + "</option>" );
                }

                var my_options = $("#"+value+" option");
                my_options.sort(function(a,b) {
                    if (a.text > b.text) return 1;
                    else if (a.text < b.text) return -1;
                    else return 0
                });
                $("#" + value).empty().append(my_options);

                $("#" + value).chosen({ search_contains: true, width: "95%" });
            } else if (key == "deselected") {
                $("#"+value).parent().remove();
            }
        });
    });
}
function updateCharacteristic(event, params) {
    $.each(params, function(key, value){
        if (key == "selected") {
            currentSelection[event.currentTarget.id].push(value);
        } else {
            currentSelection[event.currentTarget.id].splice(currentSelection[event.currentTarget.id].indexOf(value), 1);
        }
        var selectedGenomes = genomes;
        for (var i = 0; i < Object.keys(currentSelection).length; i++) {
            var search_query = "";
            var search_term = Object.keys(currentSelection)[i];
            for (var j = 0; j < currentSelection[search_term].length; j++) {
                    var search_value = currentSelection[search_term][j]
                    if (j == 0) {

                        search_query = "//*["+search_term+"=\"" + search_value +"\"]";
                    } else {
                        search_query += "|//*["+search_term+"=\"" + search_value +"\"]";
                    }
                    console.log(search_query);
            }
            if ( search_query != "") {
              selectedGenomes = JSON.search(selectedGenomes, search_query);
            }

        }
        $("#selectedTKKs>option").remove();
        for(var key in selectedGenomes) {
            $("#selectedTKKs").append("<option value=" + selectedGenomes[key].specimen_id  + ">" +selectedGenomes[key].specimen_id + "</option>");
        }
        console.log(selectedGenomes.length);

    });

}
function setTKKs() {
    $("#optionsgraph").css("display", "block");
    $("#baseindex").keyup(function(e){
        if(e.keyCode == 13) {
            jumpToBaseGetFromDOM();
        }
    });
    for (var i = 0; i < window.tkks.length; i++) {
      $(".tkks").append( "<option value=\"" + window.tkks[i].textContent + "\">" + window.tkks[i].textContent+ "</option>" );
    }
    $(".tkks").chosen({ search_contains: true });
}
