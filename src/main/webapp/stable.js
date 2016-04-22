var width = window.innerWidth
var height = window.innerHeight

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

var force = d3.layout.force()
    .size([width, height])
    .linkDistance(50)
    .charge(0)
    .gravity(0)

var link = svg.selectAll(".link"),
    node = svg.selectAll(".node");

d3.json("graph.json", function(error, graph) {
force = force.nodes(d3.values(graph.nodes))
    .links(graph.links)
    .start();

node = node.data(graph.nodes)
    .enter().append("circle")
      .attr("class", "node")
      .attr("r", 12)
      .attr("id", "name")

link = link.data(graph.links)
    .enter().append("line").attr('marker-end','url(#arrowhead)')
      .attr("class", "link");
    });

force.on("tick", function() {
  link.attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });

  node.attr("cx", function(d) { return d.x; })
      .attr("cy", function(d) { return d.y; });
      });
