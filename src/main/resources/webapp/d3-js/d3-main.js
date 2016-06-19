var WIDTH = window.innerWidth;
var HEIGHT = window.innerHeight - 450;
var MINI_WIDTH = window.innerWidth / 2;
var MINI_HEIGHT = 350;
var MAX_ZOOM_LEVEL = 100;
var GENE_ANNOTATION_RECT_SIZE = 100;
var PAN_THRESHOLD = 5000;
var PAN_EXTRA_X = 50000;
var adjustment;
var SELECTORS = [];

/**
 * @return {number}
 */
var ZOOM_THRESHOLDS = function (s) {
    if (s < 5) {
        return 64;
    } else if (s < 7.5) {
        return 16;
    } else {
        return 4;
    }
};
/**
 * @return {number}
 */
var STROKE_WIDTHS = function (gens) {
    return Math.max(1, Math.log(gens - 3) / Math.log(1.2));
};

var LINEAGE_COLORS = {
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
};

var DEFAULT_COLOR = function (edge) {
    if (edge.highlight && edge.highlight.length > 0) {
        return SELECTORS[SELECTORS.length - 1].color;
    } else return LINEAGE_COLORS[edge.lineages] || "#000000";
};

/**
 * @return {string}
 */
var NODE_TYPE = function (node) {
    switch (node.containersize) {
    case 3:
        return "triangle-up";
    case 4:
        return "diamond";
    default:
        return "circle";
    }
};
/**
 * @return {number}
 */
var NODE_SIZE = function (d) {
    return d.segmentsize / 10;
};
/**
 * @return {string}
 */
var NODE_COLOR = function (node) {
    switch (node.containersize) {
    case 3:
        return "orange";
    case 4:
        return "green";
    default:
        return "pink";
    }
};

/**
 * @return {number}
 */
var MIN_CONTAINERSIZES = function (s) {
    if (s < 5) {
        return 32;
    } else if (s < 7.5) {
        return 16;
    } else {
        return 0;
    }
};

function mode(array) {
    if (array.length == 0) {
        return null;
    }
    var modeMap = {};
    var maxEl = array[0];
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

var ServerConnection = function() {
    var self = this;
    self.graph;
    self.previousZoomThreshold;
    self.previousDomain;
    self.previousScale = 1;
    self.minimap;
};

ServerConnection.prototype.loadGraph = function (threshold, minX, maxX, minContainersize, redraw) {
    var self = this;
    self.previousZoomThreshold = threshold;
    var tkksToHighlight = "";
    for (var i = 0; i < SELECTORS.length; i++) {
        for(var j = 0; j < SELECTORS[i].genomes.length; j++) {
            tkksToHighlight += "/"+SELECTORS[i].genomes[j] + "-" + SELECTORS[i].id;
        }
    }

    $.getJSON("/api/nodes/" + threshold + "/" + Math.round(minX) + "/" + Math.round(maxX) + "/" + minContainersize, function (response) {
        console.log("/api/nodes/" + threshold + "/" + Math.round(minX) + "/" + Math.round(maxX) + "/" + minContainersize);
        var nodes = response.nodes;
        var edges = response.edges;
        var annotations = response.annotations;
        if (redraw) {
            self.graph.replace(nodes, edges, annotations);
        } else {
            self.previousDomain = [0, d3.max(nodes.map(function (n) {return n.x}))];
            self.graph = new Graph(nodes, edges, annotations);
            self.graph.draw();
            self.minimap = new Minimap(nodes, edges);
            self.minimap.draw(self.graph.xScale);
            setOptions();
            setTKKs();
            initializeHighlighting();
        }
    });
};

ServerConnection.prototype.updateGraph = function () {
    var self = this;
    var s = d3.event.scale;
    self.previousScale = s;
    var domainX = [self.graph.xScale.invert(0), self.graph.xScale.invert(WIDTH)];
    if (self.previousZoomThreshold != ZOOM_THRESHOLDS(s) || self.previousDomain[0] - domainX[0] >= PAN_THRESHOLD || domainX[1] - self.previousDomain[1]  >= PAN_THRESHOLD) {
        self.previousZoomThreshold = ZOOM_THRESHOLDS(s);
        self.previousDomain = [domainX[0] - PAN_EXTRA_X, domainX[1] + PAN_EXTRA_X];
        self.loadGraph(ZOOM_THRESHOLDS(s), self.previousDomain[0], self.previousDomain[1], MIN_CONTAINERSIZES(s), true);
    } else {
        self.graph.redraw();
    }
    self.minimap.updateMinimapRect(self.graph.xScale);
};

ServerConnection.prototype.jumpToBase = function (genome, base) {
    var self = this;
    $.getJSON("/api/metadata/navigate/" + genome + "/" + base, function (response) {
        var scale = 100;
        var translate = [- response.x * scale, - response.y * scale];

        self.graph.svg.svg.transition()
            .duration(750)
            .call(self.graph.zoom.translate(translate).scale(scale).event);
    });
};

var Graph = function(nodes, edges, annotations) {
    var self = this;
    self.nodes = nodes;
    self.edges = edges;
    self.annotations = annotations;

    self.xScale = d3.scale.linear().domain([0, d3.max(self.edges.map(function (e) {return e.x2}))]).range([0, WIDTH]);
    self.yScale = d3.scale.linear().domain([0, d3.max(self.edges.map(function (e) {return e.y2}))]).range([HEIGHT, 0]);
    self.geneTip = new Tip(10, 0, function (gene) {return gene.displayname});
    self.zoom = d3.behavior.zoom().x(self.xScale).scaleExtent([1, MAX_ZOOM_LEVEL]).on("zoom", zoomCallback);

    self.svg = new Svg("#d3graph", WIDTH, HEIGHT);

    self.svg.addCallback(self.zoom);
    self.svg.addCallback(self.geneTip.tip);
};

Graph.prototype.draw = function () {
    var self = this;
    self.svg.drawEdges(self.edges, self.xScale, self.yScale);
    self.svg.drawAnnotations(self.annotations, self.xScale, self.yScale, self.geneTip.tip);
    self.svg.drawNodes(self.nodes, self.xScale, self.yScale);
};

Graph.prototype.redraw = function () {
    var self = this;
    var t = d3.event.translate;
    var s = d3.event.scale;
    if (t[0] > 0) {
        t[0] = 0;
    } else if (t[0] < - WIDTH * (s - 1)) {
        t[0] = - WIDTH * (s - 1);
    }
    self.zoom.translate(t);
    self.svg.positionNodes(self.svg.svgNodes, self.xScale, self.yScale);
    self.svg.positionEdges(self.svg.svgEdges, self.xScale, self.yScale);
    self.svg.positionAnnotations(self.svg.svgAnnotations, self.xScale, self.yScale);
};

Graph.prototype.replace = function (nodes, edges, annotations) {
    var self = this;
    self.nodes = nodes;
    self.edges = edges;
    self.annotations = annotations;
    self.svg.clear();
    self.draw();
};

var Svg = function (domId, width, height) {
    var self = this;
    self.svg = d3.select(domId)
        .append("svg")
        .attr("width", width)
        .attr("height", height);
};

Svg.prototype.addCallback = function (callback) {
    var self = this;
    self.svg.call(callback);
};

Svg.prototype.drawNodes = function (nodes, xScale, yScale) {
    var self = this;
    self.svgNodes = self.svg.selectAll("path")
         .data(nodes)
         .enter()
         .append("path")
         .attr("d", d3.svg.symbol().type(NODE_TYPE).size(NODE_SIZE))
         .attr("fill", NODE_COLOR)
         .on("click", inspectSegment);
    self.positionNodes(self.svgNodes, xScale, yScale);
};

Svg.prototype.drawEdges = function (edges, xScale, yScale) {
    var self = this;
    self.svgEdges = self.svg.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("stroke-width", function (d) {return STROKE_WIDTHS(d.genomes)})
        .attr("stroke", function (d) {return DEFAULT_COLOR(d)});
    self.positionEdges(self.svgEdges, xScale, yScale);
};

Svg.prototype.drawAnnotations = function (annotations, xScale, yScale, tip) {
    var self = this;
    self.svgAnnotations = self.svg.selectAll("rect")
        .data(annotations)
        .enter()
        .append("rect")
        .attr("class", "geneRect")
        .on("mouseover", tip.show)
        .on("mouseout", tip.hide);
    self.positionAnnotations(self.svgAnnotations, xScale, yScale);
};

Svg.prototype.positionNodes = function (svgNodes, xScale, yScale) {
    svgNodes.attr("transform", function (d) {return "translate(" + xScale(d.x) + "," + yScale(d.y) + ")"});
};

Svg.prototype.positionEdges = function (svgEdges, xScale, yScale) {
    svgEdges
        .attr("x1", function (d) {return xScale(d.x1)})
        .attr("y1", function (d) {return yScale(d.y1)})
        .attr("x2", function (d) {return xScale(d.x2)})
        .attr("y2", function (d) {return yScale(d.y2)});
};

Svg.prototype.positionAnnotations = function (svgAnnotations, xScale, yScale) {
    svgAnnotations
        .attr("x", function (d) {return xScale(d.startx)})
        .attr("y", function (d) {return yScale(Math.max(d.starty, d.endy)) - GENE_ANNOTATION_RECT_SIZE / 2})
        .attr("width", function (d) {return Math.abs(xScale(d.endx) - xScale(d.startx))})
        .attr("height", function (d) {return GENE_ANNOTATION_RECT_SIZE});
};

Svg.prototype.clear = function () {
    var self = this;
    self.svgNodes.remove();
    self.svgEdges.remove();
    self.svgAnnotations.remove();
};

Svg.prototype.drawMinimapRect = function (miniXScale, graphXScale) {
    var self = this;
    self.minimapRect = self.svg
        .append("rect")
        .attr("class", "minimapRect");
    self.positionMinimapRect(miniXScale, graphXScale);
};

Svg.prototype.positionMinimapRect = function (miniXScale, graphXScale) {
    var self = this;
    self.minimapRect
        .attr("x", miniXScale(graphXScale.invert(0)))
        .attr("y", 0)
        .attr("width", miniXScale(graphXScale.invert(WIDTH)) - miniXScale(graphXScale.invert(0)))
        .attr("height", MINI_HEIGHT);
};

var Tip = function(offsetX, offsetY, text) {
    var self = this;
    self.tip = d3.tip()
        .attr("class", "d3-tip")
        .offset([offsetX, offsetY])
        .html(text);
};

var Minimap = function(nodes, edges) {
    var self = this;
    self.edges = edges;
    self.xScale = d3.scale.linear().domain([0, d3.max(edges.map(function (e) {return e.x2}))]).range([0, MINI_WIDTH]);
    self.yScale = d3.scale.linear().domain([0, d3.max(edges.map(function (e) {return e.y2}))]).range([MINI_HEIGHT, 0]);
    self.svg = new Svg("#d3minimap", MINI_WIDTH, MINI_HEIGHT);
};

Minimap.prototype.draw = function (graphXScale) {
    var self = this;
    self.svg.drawEdges(self.edges, self.xScale, self.yScale);
    self.svg.drawMinimapRect(self.xScale, graphXScale);
};

Minimap.prototype.updateMinimapRect = function (graphXScale) {
    var self = this;
    self.svg.positionMinimapRect(self.xScale, graphXScale)
};

var SegmentInspector = function () {
    var self = this;
    self.segmentInspector = $("#segmentinspector");
};

SegmentInspector.prototype.display = function (commonStart, differences, commonEnd) {
    var self = this;
    var html = "";
    var longestDiff = d3.max(differences.map(function (d) {return d.length}));
    for (var i = 0; i < differences.length; i++) {
        html += "<p>" + commonStart + "<span>" + differences[i] + new Array(longestDiff - differences[i].length + 1).join("&nbsp;") + "</span>" + commonEnd;
    }
    self.segmentInspector.html(html);
};

var serverConnection;
function startD3() {
    serverConnection = new ServerConnection();
    serverConnection.loadGraph(64, 0, 100000000, 64, false);
}

function zoomCallback() {
    serverConnection.updateGraph();
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
    console.log($('.metadata'))

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
                    var search_value = currentSelection[search_term][j];
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
        $("#selectedTKKs").find(">option").remove();
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
            serverConnection.jumpToBase($(".tkks").chosen().val(), $("#baseindex").val());
        }
    });
    for (var i = 0; i < window.tkks.length; i++) {
      $(".tkks").append( "<option value=\"" + window.tkks[i].textContent + "\">" + window.tkks[i].textContent+ "</option>" );
    }



    $("#d3minimap").append($("#optionsgraph").remove());
    $(".tkks").chosen({ search_contains: true });
    $.getJSON("/api/metadata/annotations", function (response) {
        for (var i = 0; i < response["annotations"].length; i++) {
              $(".annotations").append( "<option value=\"" + response.annotations[i].displayname + "\">" + response.annotations[i].displayname + "</option>" );
        }
        $(".annotations").chosen({ search_contains: true });
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


    $("#optionsgraph").find("> ul > li > a").on("click", function(e) {
        var currentAttrValue = jQuery(this).attr('href');
        $(currentAttrValue).slideDown(400).siblings().slideUp(400);
        $(this).parent('li').addClass('active').siblings().removeClass('active');
        e.preventDefault();
    });
}

function jumpToBaseGetFromDOM() {
    serverConnection.jumpToBase($(".tkks").chosen().val(), $("#baseindex").val());
}

function initializeHighlighting() {
    $("ol.highlighted").sortable({
      group: 'highlighted',
      pullPlaceholder: false,
      // animation on drop
      onDrop: function  ($item, container, _super) {
        var $clonedItem = $('<li/>').css({height: 0});
        $item.before($clonedItem);
        $clonedItem.animate({'height': $item.height()});

        $item.animate($clonedItem.position(), function  () {
          $clonedItem.detach();
          _super($item, container);
        });
      },

      // set $item relative to cursor position
      onDragStart: function ($item, container, _super) {
        var offset = $item.offset(),
            pointer = container.rootGroup.pointer;

        adjustment = {
          left: pointer.left - offset.left,
          top: pointer.top - offset.top
        };

        _super($item, container);
      },
      onDrag: function ($item, position) {
        $item.css({
          left: position.left - adjustment.left,
          top: position.top - adjustment.top
        });
      }
    });
}

function highLightGenomesFromMetadata() {
    setMetadataHighlighting($("#selectedTKKs").val());
    serverConnection.loadGraph(serverConnection.previousZoomThreshold, serverConnection.graph.xScale.invert(0), serverConnection.graph.xScale.invert(WIDTH), MIN_CONTAINERSIZES(serverConnection.previousScale), true);
}

function setMetadataHighlighting(selectedgenes) {
    var obj = [];
    obj["id"] = SELECTORS.length + 1;
    obj["color"] = getRandomColor();
    obj["genomes"] = selectedgenes;
    for (var i = 0; i < selectedgenes.length; i++) {
        $("#4").find(">ol").append("<li value=\"" + selectedgenes[i] + "\">" + selectedgenes[i]+"</li>")
    }
    SELECTORS = [];
    SELECTORS.push(obj);
}

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function inspectSegment(node) {
    var segmentInspector = new SegmentInspector();
    $.ajax({
        url: "/api/data/mutation/" + node.id,
        async: false,
        success: function (response) {
            response = JSON.parse(response);
            segmentInspector.display("..." + response.startdata.substr(response.startdata.length - 50), response.contdata, response.enddata);
        }
    });
}