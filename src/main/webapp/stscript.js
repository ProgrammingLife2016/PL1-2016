//var width = 960,
//    height = 500;
var margin = {top: -5, right: -5, bottom: -5, left: -5},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

var baseYmin = 295,
    baseYmax = 305,
    baseLinkDist = 80,
    shiftDist = 30;

var force =
d3.layout.force().alpha(0).gravity(0).charge(0)
.linkDistance(function(d){
        h = d.target.y - d.source.y;
        w = d.target.x - d.source.x;
        return Math.sqrt(h*h + w*w);
    })
    .linkStrength(0.1)
.start()

var zoom = d3.behavior.zoom()
    .scaleExtent([1, 10])
    .on("zoom", zoomed)

function zoomed() {
  container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);


var link = svg.selectAll(".link"),
    node = svg.selectAll(".node");

var notyList = new Array();

var nodelabels;
var dataset;
var i = 0;

function setDataset(val, callback){
    dataset = val;
    callback();
}

function printDataset(){
    console.log("Dataset: " + dataset);
}

d3.json("graph.json", function(error, graph) {
  if (error) throw error;
  setDataset(clone(graph), printDataset);
  force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();
  link = link.data(graph.links)
    .enter().append("line").attr('marker-end','url(#arrowhead)')
      .attr("class", "link");

  node = node.data(graph.nodes)
    .enter().append("circle")
      .attr("class", "node")
      .attr("label", "node")
      .attr("r", 12)
      .attr("id", "node" + i++)
      .on("dblclick", dblclick)
      .on("click", click);
})


force.on("tick", function() {
  link.attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y;})
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y;  });
  node
      .attr("cx", function(d) { return d.x; })
      .attr("cy", function(d) { return d.y;  });
});


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
