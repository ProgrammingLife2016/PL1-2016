$( document ).ready(function() {
    var force = initForce();
    var svg = loadGraph(force);
    defineTick(force);
});

var width = 960,
    height = 600;
var svgId = "svg_container";
var link, node;
var notyList = new Array();

function initSVG(){
    var svg = d3.select("#viewport").append("svg").attr("id", svgId)
                                               .attr("width", width)
                                               .attr("height", height);
    link = svg.selectAll(".link");
    node = svg.selectAll(".node");
    return svg;
}

function enableZooming(id){
    $(function() {
        panZoomInstance = svgPanZoom("#"+id
        , {
            zoomEnabled: true,
            center: true,
            minZoom: 0.001,
            maxZoom: 1
        });

        // Initially zoom out
        panZoomInstance.zoom(0.02)
    });
}
function initForce(){
    return d3.layout.force().alpha(0).gravity(0).charge(0)
                         .linkDistance(getLinkDist)
                         .linkStrength(0.1)
                         .start();
}

function getLinkDist(d){
    h= d.target.y - d.source.y;
    w = d.target.x - d.source.x;
    return Math.sqrt(h*h + w*w);
}

function loadGraph(force){
    var svg = initSVG();
    d3.json("graph.json", function(error, graph) {
      if (error) throw error;
      force
          .nodes(graph.nodes)
          .links(graph.links)
          .start();

      link =  svg.selectAll(".link").data(graph.links)
        .enter().append("line").attr('marker-end','url(#arrowhead)')
          .attr("class", "link");
      node = svg.selectAll(".node").data(graph.nodes)
        .enter().append("circle")
          .attr("class", "node")
          .attr("label", "node")
          .attr("r", 12)
//          .attr("id", "node" + graph.nodes.length)
          .on("dblclick", dblclick)
          .on("click", click);

    enableZooming(svgId);
    })
    return svg;
}

function defineTick(force){
    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y - 250;})
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y - 250;  });
      node
          .attr("cx", function(d) { return d.x; })
          .attr("cy", function(d) { return d.y - 250;  });
    });
}

function dblclick(d) {
    d3.select(this).classed("fixed", d.fixed = false);
    notyList[d.name].close();
    notyList[d.name] = undefined;
}

function click(d) {
    d3.select(this).classed("fixed", d.fixed = true);
    if(notyList[d.name] == undefined){
        var n = noty({
            text: d.name,
            animation: {
                open: {height: 'toggle'}, // jQuery animate function property object
                close: {height: 'toggle'}, // jQuery animate function property object
                easing: 'swing', // easing
                speed: 500 // opening & closing animation speed
            }
        });
        notyList[d.name] = n;
    }
}
function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
    }
    return copy;
}