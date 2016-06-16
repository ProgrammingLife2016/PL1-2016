var WIDTH = window.innerWidth;
var HEIGHT = window.innerHeight - 250;
var MINI_WIDTH = window.innerWidth;
var MINI_HEIGHT = 250;
var MAX_ZOOM_LEVEL = 100;
var CIRCLE_SIZE = 2.5;
var GENE_ANNOTATION_RECT_SIZE = 100;
var PAN_THRESHOLD = 5000;
var PAN_EXTRA_X = 50000;

var ZOOM_THRESHOLDS = function (s) {
    if (s < 5) {
        return 128;
    } else if (s < 10) {
        return 64;
    } else if (s < 20) {
        return 32;
    } else if (s < 30) {
        return 16;
    } else {
        return 1;
    }
}

var STROKE_WIDTHS = function (gens) {
    return Math.max(1, Math.log(gens - 3) / Math.log(1.2));
}

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
}

var DEFAULT_COLOR = function (edge) {
    return LINEAGE_COLORS[mode(edge.lineages)] || "#000000";
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

var ServerConnection = function() {
    var self = this;
    self.graph;
    self.previousZoomThreshold;
    self.previousDomain;
    self.minimap;
}

ServerConnection.prototype.loadGraph = function (threshold, minX, maxX, redraw) {
    var self = this;
    self.previousZoomThreshold = threshold;
    $.getJSON("/api/nodes/" + threshold + "/" + Math.round(minX) + "/" + Math.round(maxX), function (response) {
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
        }
    });
}

ServerConnection.prototype.updateGraph = function () {
    var self = this;
    var s = d3.event.scale;
    var domainX = [self.graph.xScale.invert(0), self.graph.xScale.invert(WIDTH)];
    if (self.previousZoomThreshold != ZOOM_THRESHOLDS(s) || self.previousDomain[0] - domainX[0] >= PAN_THRESHOLD || domainX[1] - self.previousDomain[1]  >= PAN_THRESHOLD) {
        self.previousZoomThreshold = ZOOM_THRESHOLDS(s);
        self.previousDomain = [domainX[0] - PAN_EXTRA_X, domainX[1] + PAN_EXTRA_X];
        self.loadGraph(ZOOM_THRESHOLDS(s), self.previousDomain[0], self.previousDomain[1], true);
    } else {
        self.graph.redraw();
    }
    self.minimap.updateMinimapRect(self.graph.xScale);
}

var Graph = function(nodes, edges, annotations) {
    var self = this;
    self.nodes = nodes;
    self.edges = edges;
    self.annotations = annotations;

    self.xScale = d3.scale.linear().domain([0, d3.max(self.nodes.map(function (n) {return n.x}))]).range([0, WIDTH]);
    self.yScale = d3.scale.linear().domain([0, d3.max(self.nodes.map(function (n) {return n.y}))]).range([HEIGHT, 0]);
    self.segmentTip = new Tip(-10, 0, function (node) {return node.data});
    self.geneTip = new Tip(10, 0, function (gene) {return gene.displayName});
    self.zoom = d3.behavior.zoom().x(self.xScale).scaleExtent([1, MAX_ZOOM_LEVEL]).on("zoom", zoomCallback);

    self.svg = new Svg("#d3", WIDTH, HEIGHT);

    self.svg.addCallback(self.zoom);
    self.svg.addCallback(self.segmentTip.tip);
    self.svg.addCallback(self.geneTip.tip);
}

Graph.prototype.draw = function () {
    var self = this;
    self.svg.drawNodes(self.nodes, self.xScale, self.yScale, self.segmentTip.tip);
    self.svg.drawEdges(self.edges, self.xScale, self.yScale);
    self.svg.drawAnnotations(self.annotations, self.xScale, self.yScale, self.geneTip.tip);
}

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
}

Graph.prototype.replace = function (nodes, edges, annotations) {
    var self = this;
    self.nodes = nodes;
    self.edges = edges;
    self.annotations = annotations;
    self.svg.clear();
    self.draw();
}

var Svg = function (domId, width, height) {
    var self = this;
    self.svg = d3.select(domId)
        .append("svg")
        .attr("width", width)
        .attr("height", height);
}

Svg.prototype.addCallback = function (callback) {
    var self = this;
    self.svg.call(callback);
}

Svg.prototype.drawNodes = function (nodes, xScale, yScale, tip) {
    var self = this;
    self.svgNodes = self.svg.selectAll("circle")
         .data(nodes)
         .enter()
         .append("circle")
         .attr("r", CIRCLE_SIZE)
         .on("mouseover", tip.show)
         .on("mouseout", tip.hide);
    self.positionNodes(self.svgNodes, xScale, yScale);
}

Svg.prototype.drawEdges = function (edges, xScale, yScale) {
    var self = this;
    self.svgEdges = self.svg.selectAll("line")
        .data(edges)
        .enter()
        .append("line")
        .attr("stroke-width", function (d) {return STROKE_WIDTHS(d.genomes.length)})
        .attr("stroke", function (d) {return DEFAULT_COLOR(d)});
    self.positionEdges(self.svgEdges, xScale, yScale);
}

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
}

Svg.prototype.positionNodes = function (svgNodes, xScale, yScale) {
    svgNodes
         .attr("cx", function (d) {return xScale(d.x)})
         .attr("cy", function (d) {return yScale(d.y)});
}

Svg.prototype.positionEdges = function (svgEdges, xScale, yScale) {
    svgEdges
        .attr("x1", function (d) {return xScale(d.x1)})
        .attr("y1", function (d) {return yScale(d.y1)})
        .attr("x2", function (d) {return xScale(d.x2)})
        .attr("y2", function (d) {return yScale(d.y2)});
}

Svg.prototype.positionAnnotations = function (svgAnnotations, xScale, yScale) {
    svgAnnotations
        .attr("x", function (d) {return xScale(d.startx)})
        .attr("y", function (d) {return yScale(Math.max(d.starty, d.endy)) - GENE_ANNOTATION_RECT_SIZE / 2})
        .attr("width", function (d) {return Math.abs(xScale(d.endx) - xScale(d.startx))})
        .attr("height", function (d) {return GENE_ANNOTATION_RECT_SIZE});
}

Svg.prototype.clear = function () {
    var self = this;
    self.svgNodes.remove();
    self.svgEdges.remove();
    self.svgAnnotations.remove();
}

Svg.prototype.drawMinimapRect = function (miniXScale, graphXScale) {
    var self = this;
    self.minimapRect = self.svg
        .append("rect")
        .attr("class", "minimapRect")
    self.positionMinimapRect(miniXScale, graphXScale);
}

Svg.prototype.positionMinimapRect = function (miniXScale, graphXScale) {
    var self = this;
    self.minimapRect
        .attr("x", miniXScale(graphXScale.invert(0)))
        .attr("y", 0)
        .attr("width", miniXScale(graphXScale.invert(WIDTH)) - miniXScale(graphXScale.invert(0)))
        .attr("height", MINI_HEIGHT);
}

var Tip = function(offsetX, offsetY, text) {
    var self = this;
    self.tip = d3.tip()
        .attr("class", "d3-tip")
        .offset(offsetX, offsetY)
        .html(text);
}

var Minimap = function(nodes, edges) {
    var self = this;
    self.edges = edges;
    self.xScale = d3.scale.linear().domain([0, d3.max(nodes.map(function (n) {return n.x}))]).range([0, MINI_WIDTH]);
    self.yScale = d3.scale.linear().domain([0, d3.max(nodes.map(function (n) {return n.y}))]).range([MINI_HEIGHT, 0]);
    self.svg = new Svg("#d3", MINI_WIDTH, MINI_HEIGHT);
}

Minimap.prototype.draw = function (graphXScale) {
    var self = this;
    self.svg.drawEdges(self.edges, self.xScale, self.yScale);
    self.svg.drawMinimapRect(self.xScale, graphXScale);
}

Minimap.prototype.updateMinimapRect = function (graphXScale) {
    var self = this;
    self.svg.positionMinimapRect(self.xScale, graphXScale)
}

var serverConnection;
function startD3() {
    serverConnection = new ServerConnection();
    serverConnection.loadGraph(128, 0, 100000000);
}

function zoomCallback() {
    serverConnection.updateGraph();
}
