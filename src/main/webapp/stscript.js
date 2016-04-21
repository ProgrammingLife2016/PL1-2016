var width = 960,
    height = 500;

var force =
d3.layout.force().alpha(0).gravity(0).charge(0).linkDistance(50).start()
//d3.layout.force()
//    .size([width, height])
//    .charge(-200).linkStrength(10)
//    .gravity(0).alpha(0).theta(0).friction(0.5)
//    .linkDistance(40).start()

var drag = force.drag()
    .on("dragstart", dragstart);

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

var link = svg.selectAll(".link"),
    node = svg.selectAll(".node");

//$.ajax({
//    url: "graph.json",
//    success: function (data) {
//        var obj = JSON.parse(data);
//    }
//});
//var nodelabels = svg.selectAll(".nodelabel")
//         .data(dataset.nodes)
//         .enter()
//         .append("text")
//         .attr({"x":function(d){return d.x;},
//                "y":function(d){return d.y;},
//                "class":"nodelabel",
//                "stroke":"black"})
//         .text(function(d){return d.name;});
var nodelabels;
var dataset;
var i = 0;
d3.json("graph.json", function(error, graph) {
  if (error) throw error;
  dataset = clone(graph)
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
      .attr("r", 12)
      .attr("id", "node" + i++)
      .on("dblclick", dblclick)
      .call(drag);

//  nodelabels = svg.selectAll(".nodelabel")
//   .data(dataset.nodes)
//   .enter()
//   .append("text")
//   .attr({"x":function(d){return d.x;},
//          "y":function(d){return d.y;},
//          "class":"nodelabel",
//          "stroke":"black"})
//   .text(function(d){return d.name;});
//   console.log(nodelabels)
});

force.on("tick", function() {
  link.attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });

  node.attr("cx", function(d) { return d.x; })
      .attr("cy", function(d) { return d.y; });
//  console.log(node)
//  console.log(nodelabels)
//  nodelabels.attr("x", function(d) { return d.x; })
//                  .attr("y", function(d) { return d.y; });
});

function dblclick(d) {
  d3.select(this).classed("fixed", d.fixed = false);
}

function dragstart(d) {
  d3.select(this).classed("fixed", d.fixed = true);
}
function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
    }
    return copy;
}

document.getElementById("node0")