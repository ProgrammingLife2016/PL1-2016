var r = 960 / 2;
var svgId = "phyloTree";

var cluster = d3.layout.cluster()
    .size([360, 1])
    .sort(null)
    .value(function(d) { return d.length; })
    .children(function(d) { return d.branchset; })
    .separation(function(a, b) { return 1; });

function project(d) {
  var r = d.y, a = (d.x - 90) / 180 * Math.PI;
  return [r * Math.cos(a), r * Math.sin(a)];
}

function cross(a, b) { return a[0] * b[1] - a[1] * b[0]; }
function dot(a, b) { return a[0] * b[0] + a[1] * b[1]; }

function step(d) {
  var s = project(d.source),
      m = project({x: d.target.x, y: d.source.y}),
      t = project(d.target),
      r = d.source.y,
      sweep = d.target.x > d.source.x ? 1 : 0;
  return (
    "M" + s[0] + "," + s[1] +
    "A" + r + "," + r + " 0 0," + sweep + " " + m[0] + "," + m[1] +
    "L" + t[0] + "," + t[1]);
}

var wrap = d3.select("#tree").append("svg")
    .attr("width", "100%")//r * 2)
    .attr("height", "100%")//r * 2)
    .attr("id", svgId)
    .style("-webkit-backface-visibility", "hidden");

// Catch mouse events in Safari.
wrap.append("rect")
    .attr("width", "100%")//r * 2)
    .attr("height", "100%")//r * 2)
    .attr("fill", "none")

var tree = wrap.append("g")
    .attr("display", "block")
    .attr("id", "GangstaShitAmattie")
    .attr("transform", "translate(" + $('html').height()/2 + "," + $('html').height()/2 + ")");

var start = null,
    rotate = 0,
    div = document.getElementById("tree");

function mouse(e) {
  return [
    e.pageX - div.offsetLeft - $('html').height()/2,//r,
    e.pageY - div.offsetTop - $('html').height()/2//r
  ];
}

wrap.on("mousedown", function() {
  wrap.style("cursor", "move");
  start = mouse(d3.event);
  d3.event.preventDefault();
});
d3.select(window)
  .on("mouseup", function() {
    if (start) {
      wrap.style("cursor", "auto");
      var m = mouse(d3.event);
      var delta = Math.atan2(cross(start, m), dot(start, m)) * 180 / Math.PI;
      rotate += delta;
      if (rotate > 360) rotate %= 360;
      else if (rotate < 0) rotate = (360 + rotate) % 360;
      start = null;
      wrap.style("-webkit-transform", null);
      tree
          .attr("transform", "translate(" + r + "," + r + ")rotate(" + rotate + ")")
        .selectAll("treetext")
          .attr("text-anchor", function(d) { return (d.x + rotate) % 360 < 180 ? "start" : "end"; })
          .attr("transform", function(d) {
            return "rotate(" + (d.x - 90) + ")translate(" + (r) + ")rotate(" + ((d.x + rotate) % 360 < 180 ? 0 : 180) + ")";
          });
    }
  })
  .on("mousemove", function() {
    if (start) {
      var m = mouse(d3.event);
      var delta = Math.atan2(cross(start, m), dot(start, m)) * 180 / Math.PI;
      wrap.style("-webkit-transform", "rotateZ(" + delta + "deg)");
    }
  });

function phylo(n, offset) {
  if (n.length != null) offset += n.length * 5000;//115;
  n.y = offset;
  if (n.children)
    n.children.forEach(function(n) {
      phylo(n, offset);
    });
}

d3.text("../genomes/340tree.rooted.TKK.nwk", function(text) {
  var x = newick.parse(text);
  var treenodes = cluster.nodes(x);
  phylo(treenodes[0], 0);

  var treelink = tree.selectAll("path.treelink")
      .data(cluster.links(treenodes))
    .enter().append("path")
      .attr("class", "treelink")
      .attr("d", step);

  var treenode = tree.selectAll("g.treenode")
      .data(treenodes.filter(function(n) { return n.x !== undefined; }))
    .enter().append("g")
      .attr("class", "treenode")
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })

  treenode.append("circle")
      .attr("r", 2.5);

  var label = tree.selectAll("text")
      .data(treenodes.filter(function(d) { return d.x !== undefined && !d.children; }))
    .enter().append("text")
      .attr("dy", ".31em")
      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + (r) + ")rotate(" + (d.x < 180 ? 0 : 180) + ")"; })
      .text(function(d) { return d.name.replace(/_/g, ' '); });

      enableZooming(svgId);
});


 function enableZooming(id){
   $(function() {
       panZoomInstance = svgPanZoom("#"+id
       , {
           zoomEnabled: true,
           disablePan: false,
           center: true,
           minZoom: 1,
           maxZoom: 4,
           dblClickZoomEnabled: false
       });

      // Initially zoom out
       panZoomInstance.zoom(1)
       panZoomInstance.disablePan();
   });
}