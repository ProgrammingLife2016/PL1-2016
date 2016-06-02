$(function() { // on dom ready

  /* Settings */
  var settings = {
     animationDuration: 500, //length of the animations in ms
     zoomTreshold: 0.50, //change in percentage/100 between zoom levels for sending a new AJAX request
     startZoom: 0.1, //Zoom level at the start of the application
     nodesDir: "/api/nodes", //directory at the server for the first AJAX request
  };

  /*
     Start animations
  */
  $("#options").css("height", $(document).height() - $("#nav").height());
  $("#logo").stop().animate({opacity: 1}, 800, "swing");
  $("#container").stop().animate({opacity: 1, "padding-top": 120}, 800,"swing");
  $("html, body").stop().animate({ scrollTop: 0}, 800, "swing");
  $("#stackpane, #results").css("height", $(document).height() - $("#nav").height());
  $("#stackpane").css("top", $("#nav").height());
  $("#status").css("opacity", 0);

  /*
    Factory for creating Graph objects.
  */
  function GraphFactory() {
    this.nodeTemplate = {
      data: {
        id: -1,
        name: "-",
        weight: 0,
        faveColor: '#6FB1FC',
        position: { x: 0, y: 0 }
      }
    };
  }

  GraphFactory.prototype.createNode = function(node) {
    var getName = name => name.length > 4 ? name.substring(0, 4) + "..." : name;
    var sh = ((node.bubble === "true") ? "rectangle" : "circle");
    var gens = (node.genomes !== undefined ? node.genomes.join(",") : "");
    var isBubble = (node.isBubble ? "true" : "false");
    return {
      data: {
        id: node.id,
        name: node.id,
        weight: node.bubble ? 100 : 50,
        faveColor: '#6FB1FC',
        faveShape: 'ellipse',
        refs: gens,
        bubble: isBubble,
        containersize: node.containersize,
        shape: sh
      },
      position: {
        x: node.x + 550,
        y: node.y + 450
      }
    };
  }

  GraphFactory.prototype.createEdge = function(edge) {
    return {
      data: {
        source: edge.from,
        target: edge.to,
        group: "normal"
      }
    };
  }

  /*

      Adapter for converting JSON data from server to correct format for cytoscape

  */
  function JSONAdapter() {}

  /*
    Convert JSON data received from server.
  */
  JSONAdapter.prototype.convert = function(data) {
    if (data.hasOwnProperty("nodes") && data.hasOwnProperty("edges")) {
        var nodes = data.nodes.map(n => graphFactory.createNode(n));
        var edges = data.edges.map(e => graphFactory.createEdge(e));
        return { nodes, edges };
    } else {
        console.log("ERROR [JSONAdapter.prototype.convert]: Data has wrong format.");
        return {};
    }
  }

  /*

      Class for connection with the server

  */
  function ServerConnection() {
     this.req = {
         url: settings.nodesDir,
         data: {},
         complete : this.handleComplete,
         error: this.handleError,
         statusCode: {
            default: this.handleStatusCode
         }
     };
     this.bindUIEvents();
  }

  /*
    Create AJAX to server
  */
  ServerConnection.prototype.retrieveDataFromServer = function() {
      console.log(this.req);
      $.ajax(this.req);
  }

  ServerConnection.prototype.sendZoomlevel = function(z, minX, maxX) {
      var request = {
            url: "/api/nodes/" + z + "/" + minX + "/" + maxX,
            data: {},
            error: this.handleError,
            complete: this.handleComplete,
            statusCode: {
               default: this.handleStatusCode
            }
      };
      $.ajax(request);
      console.log("Send zoomlevel " + z + " to server");
      $("#status").stop().animate({opacity: 1}, settings.animationDuration, "swing");
  }

  /*
     Load data from zoom level AJAX request to server.
  */
  ServerConnection.prototype.handleComplete = function(data) {
     console.log("Response zoom level request");
     graphHandler.requestSend = false;
     $("#status").animate({opacity: 0}, settings.animationDuration, "swing");
     if (data["status"] === "error") {
        console.log("Failed zoom level request");
     } else if (data.responseText === "") {
        console.log("ERROR: response AJAX request is empty");
     } else {
        graphHandler.loadDataInGraph(JSON.parse(data.responseText));
     }
  }

  /*
    Add UI events concerning communicating with the server.
  */
  ServerConnection.prototype.bindUIEvents = function() {
      $("#connect").click(() => {
        console.log("Connecting to server...");
        this.retrieveDataFromServer();
      });

      var duration = settings.animationDuration;
      var easing = "swing";
      $("#startConnection").click(() => {
        $("#d3").css("background-color", "#ECF0F1");
        $("#d3, .cytoscape-navigator").css("display", "block");

        $("#startConnection i").attr("class", "fa fa-circle-o-notch fa-spin fa-fw fa-lg");
        console.log("Connecting to server...");
        this.retrieveDataFromServer();

        //$.getJSON("/api/nodes", function (response) { });

        setTimeout(function(){
            //$("#status").stop().animate({opacity: 1}, duration, "swing");
            $("#viewlist").stop().animate({opacity: 1}, duration, "swing");
            $("#settings").stop().animate({opacity: 1}, duration, "swing");
            $("#options").stop().animate({height: 0}, duration, "swing");
            $("#container").stop().animate({height: 0}, duration, "swing");
            $("#startConnection").stop().animate({opacity: 0}, duration, "swing");
            $("#optionButton").stop().animate({opacity: 0}, duration, "swing");
            $("#d3").stop().animate({ opacity: 1 }, duration, "swing");

            $("#tree").css("z-index", "1");
            $("#d3").css("z-index", "2");
            $("#options").css("z-index", "0");
        }, duration);

        setTimeout(function() {
            $("#options").css("display", "none");
        }, duration);
      });

      $("#optionButton").click(function() {
         $header = $(this);
         $content = $("#optionsContainer");
         $content.slideToggle(duration, function () {
            if ($content.is(":visible")) {
               $("#optionButton i").attr("class", "fa fa-arrow-right fa-fw");
            } else {
               $("#optionButton i").attr("class", "fa fa-arrow-down fa-fw");
            }
         });
      });

      $("#enableDragging").click(() => {
         var c = cookieHandler.getCookie("enableDragging");
         if (c === undefined || c[1] === "false") {
           $("#enableDragging i").attr("class", "fa fa-square fa-fw fa-lg");
           cookieHandler.setCookie("enableDragging", "true");
         } else {
           $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
           cookieHandler.setCookie("enableDragging", "false");
         }
         graphHandler.loadSettings();
      });
  }

  /*
    Load data if AJAX request was succesfull.
  */
  ServerConnection.prototype.handleSucces = function(data, textStatus, jqXHR) {
      console.log(jqXHR);
      console.log(data);
      $("#status").stop().animate({opacity: 0}, settings.animationDuration, "swing");
      graphHandler.loadDataInGraph(JSONAdapter.prototype.convert(data));
  }

  /*
    Handle error if AJAX request failed.
  */
  ServerConnection.prototype.handleError = function(jqXHR ,textStatus, errorThrown) {
      graphHandler.requestSend = false;
      $("#status").animate({opacity: 0}, settings.animationDuration, "swing");
      console.log("ERROR AJAX request");
      console.log(jqXHR);
  }

  /*
    Print HTTP status code of AJAX request.
  */
  ServerConnection.prototype.handleStatusCode = function(statusCode) {
      $("#status").animate({opacity: 0}, settings.animationDuration, "swing");
      console.log("Status code AJAX request: " + statusCode)
  }

  /*
    Class for setting and getting the cookie values.
  */
  function CookieHandler() {}

  CookieHandler.prototype.getCookie = function(name) {
    return document.cookie
                   .replace(" ", "")
                   .split(";")
                   .map(c => c.split("="))
                   .find(c => c[0] === name);

  }

  CookieHandler.prototype.setCookie = function(name, value) {
    document.cookie = name + "=" + value;
  }

  /*

     Class for rendering the graph.

  */
  function GraphHandler() {
      highlight_node = {
        "background-color": "#555" ,
        "text-outline-color": "#555"
      };
      normal_node = {
        "background-color": "#94AAC7",
        "text-outline-color": "#94AAC7"
      };

      highlight_edge = {
        "line-color": "#777",
        "line-style": "dashed",
        "width": "3px"
      };
      normal_edge = {
        "line-color": "#777",
        "line-style": "normal",
        "width": "1px"
      };
      this.zoomTreshold = settings.zoomTreshold;
      this.requestSend = false;
      console.log("Start zoom: " + this.zoom);
      this.bindUIEvents();
  }

  GraphHandler.prototype.loadSettings = function() {
      var c = cookieHandler.getCookie("enableDragging");
      if (c === undefined || c[1] === "false") {
        $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
        console.log("autolock(true)");
      } else {
        $("#enableDragging i").attr("class", "fa fa-square fa-fw fa-lg");
        console.log("autolock(false)");
      }
  }

  /*
    Add UI events concerning the graph view.
  */
  GraphHandler.prototype.bindUIEvents = function() {
    $("#zoomButton").click(function() {
       graphHandler.highlightGenome("G 2");
       $("#status").stop().animate({opacity: 0}, settings.animationDuration, "swing");
    });

    $(".dnaGraph").click(function() {
       $("#d3").show();
       $("#tree").hide();
       $("#rotation").hide();
       $("#tree").css("z-index", "1");
       $("#d3").css("z-index", "2");
       $("#options").css("z-index", "0");
       $("#search").css("display", "none");
    });

    $(".phylogeneticTree").click(function() {
       console.log("Phylogenetic tree");
       $("#d3").hide();
       $("#tree").css("opacity", "1");
       $("#rotation").show();
       $("#tree").show();
       $("#tree").css("z-index", "2");
       $("#d3").css("z-index", "1");
       $("#options").css("z-index", "0");
       $("#search").css("display", "block");
       for (var i = 0; i < window.tkks.length; i++) {
          $("#search ul").append($("<li>").text(window.tkks[i].textContent));
       }

       if (this.fuse === undefined) {
           phyloTree.loadFuse();
       }

       //Center phylogenetic tree in view
       var el = document.getElementsByClassName("svg-pan-zoom_viewport")[0];
       var tr = el.getAttribute("transform");
       var values = tr.split('(')[1].split(')')[0].split(',');
       var wrap = d3.select(".svg-pan-zoom_viewport > g")
                    .attr("transform", "matrix(0.809726453085347,0,0,0.809726453085347," + values[4] + ",120)");

    });

    $("#resetHighlighting").click(function() {
       graphHandler.resetHighlighting();
    });

    $("#resetButton").click(function() {
       graphHandler.resetHighlighting();
    });

    $("#selectButton").click(function() {
      graphHandler.resetHighlighting();
      graphHandler.showPhylotree();
    });
  }

  GraphHandler.prototype.resetHighlighting = function() {
    disableHighlighting();
    $("#info").fadeOut();
  }

  /*
    Load data of graph containing nodes and edges in graph view.
  */
  GraphHandler.prototype.loadDataInGraph = function(response) {
      nodes = response.nodes;
      edges = response.edges;
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
      drawGraph();
      drawMinimap();
  }

  GraphHandler.prototype.showGraph = function() {
       $("#d3").show();
       $("#tree").hide();
       $("#rotation").hide();
       $("#tree").css("z-index", "1");
       $("#d3").css("z-index", "2");
       $("#options").css("z-index", "0");
       $("#search").css("display", "none");
  }

  GraphHandler.prototype.showPhylotree = function() {
       $("#d3").hide();
       $("#tree").css("opacity", "1");
       $("#rotation").show();
       $("#tree").show();
       $("#tree").css("z-index", "2");
       $("#d3").css("z-index", "1");
       $("#options").css("z-index", "0");
       $("#search").css("display", "block");
       for (var i = 0; i < window.tkks.length; i++) {
          $("#search ul").append($("<li>").text(window.tkks[i].textContent));
       }

       if (this.fuse === undefined) {
           phyloTree.loadFuse();
       }

       //Center phylogenetic tree in view
//       var el = document.getElementsByClassName("svg-pan-zoom_viewport")[0];
//       var tr = el.getAttribute("transform");
//       var values = tr.split('(')[1].split(')')[0].split(',');
//       var wrap = d3.select(".svg-pan-zoom_viewport > g")
//                    .attr("transform", "matrix(0.809726453085347,0,0,0.809726453085347," + values[4] + ",120)");
  }

  GraphHandler.prototype.setSelectedGenome = function(name) {
       $("#info p").html(name);
       $("#info").fadeIn();
  }


  /*

    Class for rendering a Phylogentic Tree.

  */
  function PhyloGeneticTree() {
      this.animationDuration = 100;
      this.bindUIEvents();
      this.fuse = undefined;
  }

  PhyloGeneticTree.prototype.loadFuse = function() {
      console.log("Initialize fuse");
      var listOfGenomes = window.tkks.map(function(tkk) {
          return {
             "name": tkk.textContent,
             "compressed": tkk.textContent.replace(" ", "").replace("-", "")
          };
      });
      var options = {
          keys: ["name"],
          shouldSort: true
      };
      window.fuse = new Fuse(listOfGenomes, options);
  }

  PhyloGeneticTree.prototype.bindUIEvents = function() {
      $("#search input").focus(function() {
         console.log("Focus search bar");
      });

      $("#search input").keyup(function(e) {
         //Reset higlighting
         window.tkks
               .filter(function(tkk) {return tkk !== undefined; })
               .forEach(function(tkk) {
                   $(tkk).css("fill", "#222");
               });

         if (window.fuse === undefined) {
            console.log("Fuse not defined");
            phyloTree.loadFuse();
         }
         var res = window.fuse.search($("#search input").val());
         if (res.length == 0) return false;

         var nameMap = {};
         res.forEach(function(tkk) { nameMap[tkk["name"]] = 1; });
         window.tkks
               .filter(function(tkk) {return nameMap[tkk.textContent] === 1;})
               .forEach(function(tkk) { $(tkk).css("fill", "#05dbe4"); });

         var items = res.map(function(match) {
             return $("<li>").text(match["name"]);
         });
         $("#search ul").html(items);
      });
  }

  /**

      Initialisation

  **/

  var graphFactory = new GraphFactory();
  var phyloTree = new PhyloGeneticTree();
  window.graphHandler = new GraphHandler();
  var serverConnection = new ServerConnection();
  var cookieHandler = new CookieHandler();

  $(window).resize(function () {
      updateBounds();
  });

  var updateBounds = function () {
      var h = $(document).height() - $("#nav").height();
      $("#d3").css("height", h);
  };
  updateBounds();

  graphHandler.loadSettings();
  $("#tree").css("z-index", "0");
  $("#d3").css("z-index", "1");
  $("#options").css("z-index", "2");
}); // on dom ready
