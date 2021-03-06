$(function() {
//var r = 1000;
var r = $('html').height() / 2;//960 / 2;
console.log("r: " + r);
var svgId = "phyloTree";
var panX = 0, panY = 0;

$('#rotation').css('top', r-$('#rotation').height()+'px');

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

var wrap = d3.select("#tree")
//    .append("svg")
//    .attr("id", "zoomContainer")
//    .attr("width", r * 2)
//    .attr("height", r * 2)
    .append("svg")
    .attr("width", $(document).width())//r * 2)
    .attr("height", r * 3)
    .attr("align", "center")
    .attr("id", svgId)
    .style("-webkit-backface-visibility", "hidden");

// Catch mouse events in Safari.
wrap.append("rect")
    .attr("width", r * 2)
    .attr("height", r * 2)
    .attr("fill", "none")

var tree = wrap.append("g")
    .attr("transform", "translate(" + $(document).width()/3 + "," + $(document).height()/4 + ")")


var start = null,
    rotate = 0,
    div = document.getElementById("tree");

function mouse(e) {
//            console.log(panX + ", " + panY);
  return [
    e.pageX - div.offsetLeft - r + panX/2,
    e.pageY - div.offsetTop - r + panY/2
  ];
}

d3.select('#rotation').on("mousedown", function() {//wrap.on("mousedown", function() {
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
              .attr("transform", "translate(" + (r ) + "," + (r ) + ")rotate(" + rotate + ")")
            .selectAll("text")
              .attr("text-anchor", function(d) { return (d.x + rotate) % 360 < 180 ? "start" : "end"; })
              .attr("transform", function(d) {
                return "rotate(" + (d.x - 90) + ")translate(" + (r - (r/480)) + ")rotate(" + ((d.x + rotate) % 360 < 180 ? 0 : 180) + ")";
              });
        }
  })
  .on("mousemove", function() {
    if (start) {
      var m = mouse(d3.event);
//      start[0] += panX + Math.acos(delta);
//      start[1] += panY + Math.asin(delta);
//      m[0] += panX + Math.acos(delta);
//      m[1] += panY + Math.asin(delta);
      var delta = Math.atan2(cross(start, m), dot(start, m)) * 180 / Math.PI;
//      tree.attr("transform", "translate( "+(r -panX)+", "+(r - panY)+")");
//      wrap.style("-webkit-transform", "rotateZ(" + delta + "deg)");
      tree.attr("transform", "translate( "+(r )+", "+(r )+")rotate(" + (rotate + delta) + ")");
      //$('.svg-pan-zoom_viewport')
    }
  });

function phylo(n, offset) {
  if (n.length != null) offset += n.length * 5000*r/480;//115;
  n.y = offset;
  if (n.children) {
      n.children.forEach(function(n) {
          phylo(n, offset);
      });
  }
}
window.genomes = [];
function getMetadata(name) {
    var value = $.getJSON("/api/metadata/info/"+name, function (response) {
        if(response.subject.length != 0){
            window.genomes.push(response["subject"][0]);
            $("#selectedTKKs").append("<option value=" + response["subject"][0].specimen_id  + ">" +response["subject"][0].specimen_id + "</option>");
        }
    });
}
d3.text("/static/file.nwk", function(text) {
  var x = newick.parse(text);
  var treenodes = cluster.nodes(x);
  phylo(treenodes[0], 0);
  var classes =  setClassNames(treenodes);

  var treelink = tree.selectAll("path.treelink")
      .data(cluster.links(treenodes))
    .enter().append("path")
//      .attr("class", "treelink")
      .attr("d", step)
      .attr("class", getClassName)
      .on("mouseenter", function() {
          var classes = getProperClassFormat($(this).attr("class"));//"."+$(this).attr("class").split(' ').slice(1).join(', .');
          $(classes).css('stroke', '#dd5f70');
      })
      .on("mouseleave", function() {
          var classes = getProperClassFormat($(this).attr("class"));//"."+$(this).attr("class").split(' ').slice(1).join(', .');
          $(classes).css('stroke', '#ccc');
      }) ;

  var treenode = tree.selectAll("g.treenode")
      .data(treenodes.filter(function(n) { return n.x !== undefined; }))
    .enter().append("g")
      .attr("class", "treenode")
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })

  treenode.append("circle")
      .attr("r", 2.5);
  window.tree = tree;
  var label = tree.selectAll("text")
      .data(treenodes.filter(function(d) { return d.x !== undefined && !d.children; }))
    .enter().append("text")

      .attr("dy", ".31em")
      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + (r -(r/480)) + ")rotate(" + (d.x < 180 ? 0 : 180) + ")"; })
      .text(function(d) { return d.name.replace('-', '_').replace('-', '_'); })
      .attr("class", function(d) {
            getMetadata(d.name.replace('-', '_').replace('-', '_'));
            var str = $("."+d.name).attr("class");
            return str.replace("treelink", "");
            })
      .on("mouseover", function() {
          if ($(this).css("fill") === "rgb(0, 0, 0)") {
              colorTKKs(this, '#dd5f70', '#dd5f70');
          }
      })
      .on("mouseout", function() {
          if ($(this).css("fill") === "rgb(221, 95, 112") {
              colorTKKs(this, '#ccc', '#000');
          }
      })
      .on("mouseleave", function() {
          colorTKKs(this, '#ccc', '#000');
      })
      .on("mousedown", function(e) {
          console.log("Select TKK");
          console.log(e.name);
          highlight(e.name);
      });
      enableZooming(svgId);
    window.tkks = $("text").toArray().map(function(tkk) {return tkk;});
});

 var highlight = function(name) {
      var color = highlightLineage(name);
 }

 function enableZooming(id){
   $(function() {
       panZoomInstance = svgPanZoom("#"+id
       , {
           zoomEnabled: true,
           disablePan: true,
           center: true,
           minZoom: 0.5,
           maxZoom: 4,
           dblClickZoomEnabled: false
       });

      // Initially zoom out
       panZoomInstance.zoom(0.6);
       panZoomInstance.setOnPan(function(){
            panX = panZoomInstance.getPan().x;
            panY = panZoomInstance.getPan().y
       });

       panZoomInstance.setOnZoom(function(){
            var delta = Math.abs(1/panZoomInstance.getZoom());
            $('circle').attr('r', 2.5 * delta);
            $('circle').css('stroke-width', 1.5 * delta + "px");
            $('.treelink').css('stroke-width', 1.5 * delta + "px");
       });
   });
}
function setClassNames(tree) {
    var count = 0;
    // iterate over properties, increment if a non-prototype property
    tree.reverse().forEach(function(obj){
            obj.id = count;
            if(obj.children != undefined){
                obj.className = getChildrenIDsAndClasses(obj.children) ;
            }
            count++;
         });

//    tree.forEach(function(obj){
//                var childrenIds = "";
//                if(obj.children != undefined)
//                    childrenIds = getChildrenIDs(obj.children);
//                obj.className = "treelink"  + childrenIds;//+ obj.id
//             });
}

function getChildrenIDsAndClasses(children) {
    var ids = '';
    children.forEach(function(obj) {
        ids += " " + obj.id;
        if(obj.className != undefined)
            ids += obj.className;
    });
    return ids;
}

function getClassName(d) {
    if( d.target.name != "" &&  d.target.name != undefined)
        return  "treelink" + d.source.className + " " + d.target.name;//
    return  "treelink" + d.source.className;//
}

function getProperClassFormat(str){
    if(str != undefined)
        return "."+str.split(' ').slice(1).join(', .');
    return '.dummy';
}

function colorTKKs(obj, colorLine, colorText){
    var str = $(obj).text().replace(/\s/g, '_');
    str = getProperClassFormat($("."+str).attr("class"));//$("."+str).attr("class")
    $(str).css('stroke', colorLine);
    $('text'+str.replace(/,\s./g, ", text.")).css('fill', colorText);
}

  //tree.selectAll("text")
});