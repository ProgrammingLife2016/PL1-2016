$(function() {
    console.log("PHYLOTREE");
  d3.text ("/static/file.nwk", function (error, newick) {
      var tree = d3.layout.phylotree()
            .svg (d3.select ("#tree_display"))
            .radial (true);

      tree (d3_phylotree_newick_parser (newick))
          .layout();

      $("#layout").on("click", function (e) {
          // tree.radial ($(this).prop ("checked")).placenodes().update ();
          tree.spacing_y(tree.spacing_y() + 1).update();
          tree.spacing_x(tree.spacing_x() + 1).update();
      });
  });

  // var svg = d3.select("#tree_display")
  //     .append("svg")
  //     .attr("width", "100%")
  //     .attr("height", "100%")
  //     .call(d3.behavior.zoom().on("zoom", function () {
  //       svg.attr("transform", "translate(" + d3.event.translate + ")" + " scale(" + d3.event.scale + ")")
  //     }))
  //     .append("g");
});
