$(function() { // on dom ready
  $("#options").css("top", $("#nav").height());
  $("#options").css("height", $(document).height() - $("#nav").height());
  $("#logo").stop().animate({opacity: 1}, 800,"swing");
  $("#container").stop().animate({opacity: 1, "padding-top": 120}, 800,"swing");
  $("html, body").stop().animate({ scrollTop: 0}, "swing");

  /*
    Factory for creating Graph objects.
  */
  function GraphFactory() {
    this.nodeTemplate = {
      data: {
        id: -1, name: "-", weight: 0, faveColor: '#6FB1FC', faveShape: 'ellipse'
      },
      position: { x: 0, y: 0 }
    };

    console.log(this.nodeTemplate);
  }

  GraphFactory.prototype.createNode = function(node) {
    var getName = name => name.length > 4 ? name.substring(0, 4) + "..." : name;
    this.nodeTemplate["data"]["id"] = node.id;
    this.nodeTemplate["data"]["name"] = getName(node.data);
    this.nodeTemplate["data"]["weight"] = node.bubble ? 100 : 50;
    this.nodeTemplate["position"]["x"] = node.x + 550;
    this.nodeTemplate["position"]["y"] = node.y + 450;
    return {
      data: {
        id: node.id,
        name: getName(node.data),
        weight: node.bubble ? 100 : 50,
        faveColor: '#6FB1FC',
        faveShape: 'ellipse'
      },
      position: {
        x: node.x + 550,
        y: node.y + 450
      }
    };
//     return this.nodeTemplate;
  }

  GraphFactory.prototype.createEdge = function(edge) {
    var edgeWeight = edge.bubble ? 100 : 50;
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
  function JSONAdapter() {
  }

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
                   url: "/api/nodes",
                   data: {},
                   dataType: 'json',
                   success : this.handleSucces,
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
      console.log("Request");
      console.log(this.req);
      $.ajax(this.req);
  }

  ServerConnection.prototype.sendZoomlevel = function(z) {
      var r = {
            url: "/api/nodes/" + z,
            error: this.handleError,
            statusCode: {
               default: this.handleStatusCode
            }
      };
      $.ajax(r);
      console.log("Send zoomlevel");
  }

  /*
    Add UI events concerning communicating with the server.
  */
  ServerConnection.prototype.bindUIEvents = function() {
      $("#connect").click(() => {
        console.log("Connecting to server...");
        this.retrieveDataFromServer();
      });

      var duration = 500;
      var easing = "swing";
      $("#startConnection").click(() => {
        $("#cy").css("background-color", "#ECF0F1");
        $("#startConnection i").attr("class", "fa fa-circle-o-notch fa-spin fa-fw fa-lg");
        console.log("Connecting to server...");
        this.retrieveDataFromServer();
        setTimeout(function(){
            $("#options").stop().animate({height: 0}, duration, "swing");
            $("#container").stop().animate({height: 0}, duration, "swing");
            $("#startConnection").stop().animate({opacity: 0}, duration, "swing");
            $("#optionButton").stop().animate({opacity: 0}, duration, "swing");
            $("#cy").stop().animate({
              top: $("#nav").height(),
              opacity: 1
              }, duration, "swing");
            $(".cytoscape-navigator").stop().animate({opacity: 1}, duration, "swing");
        }, duration);
        setTimeout(function() {
            $("#options").css("display", "none");
            $("#tree").toggle();
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
           cy.autolock(false);
         } else {
           $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
           cookieHandler.setCookie("enableDragging", "false");
           cy.autolock(true);
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
      graphHandler.loadDataInGraph(JSONAdapter.prototype.convert(data));
  }

  /*
    Handle error if AJAX request failed.
  */
  ServerConnection.prototype.handleError = function(jqXHR ,textStatus, errorThrown) {
      console.log("ERROR AJAX request");
      console.log(jqXHR);
  }

  /*
    Print HTTP status code of AJAX request.
  */
  ServerConnection.prototype.handleStatusCode = function(statusCode) {
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
      cy = cytoscape({
        container: $('#cy')[0],
        hideEdgesOnViewport : true,
        hideLabelsOnViewport : true,
        textureOnViewport : true,
        wheelSensitivity: 0.1,
        minZoom: 0.1,
        zoom: 0.1,

        style: [{"selector":"core",
                   "style":
                   {"selection-box-color":"#AAD8FF","selection-box-border-color":"#8BB0D0","selection-box-opacity":"0.5"}},
                {"selector":"node",
                   "style":
                    {"width":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "height":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "content":"data(name)","font-size":"12px","text-valign":"center","text-halign":"center",
                     "background-color":"#94AAC7","text-outline-color":"#94AAC7","text-outline-width":"2px","color":"#fff", //#555
                     "overlay-padding":"6px","z-index":"10"}},
                {"selector":"node[?attr]","style":{"shape":"rectangle","background-color":"#aaa","text-outline-color":"#aaa",
                    "width":"16px","height":"16px","font-size":"6px","z-index":"1"}},
                {"selector":"node[?querzy]","style":{"background-clip":"none","background-fit":"contain"}},
                {"selector":"node:selected","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5",
                    "background-color":"#77828C","text-outline-color":"#77828C"}},
                {"selector":"edge","style":{"target-arrow-shape": "triangle", "background-color": "#F00"}},
                {"selector":"node.unhighlighted","style":{"opacity":"0.2"}},
                {"selector":"edge.unhighlighted","style":{"opacity":"0.05"}},
                {"selector":".highlighted","style":{"z-index":"999999"}},
                {"selector":"node.highlighted","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5",
                    "background-color":"#394855","text-outline-color":"#394855",
                    "shadow-blur":"12px","shadow-color":"#000","shadow-opacity":"0.8",
                    "shadow-offset-x":"0px","shadow-offset-y":"4px"}},
                {"selector": "node", "style": {"color": "#ECF0F1"}},
                {"selector":"edge.filtered","style":{"opacity":"0"}},
                {"selector":"edge[group=\"coexp\"]","style":{"line-color":"#d0b7d5"}},
                {"selector":"edge[group=\"normal\"]","style":{"line-color":"#CCC"}},
                {"selector":"edge[group=\"coloc\"]","style":{"line-color":"#a0b3dc"}},
                {"selector":"edge[group=\"gi\"]","style":{"line-color":"#90e190"}},
                {"selector":"edge[group=\"path\"]","style":{"line-color":"#9bd8de"}},
                {"selector":"edge[group=\"pi\"]","style":{"line-color":"#eaa2a2"}},
                {"selector":"edge[group=\"predict\"]","style":{"line-color":"#f6c384"}},
                {"selector":"edge[group=\"spd\"]","style":{"line-color":"#dad4a2"}},
                {"selector":"edge[group=\"spd_attr\"]","style":{"line-color":"#D0D0D0"}},
                {"selector":"edge[group=\"reg\"]","style":{"line-color":"#D0D0D0"}},
                {"selector":"edge[group=\"reg_attr\"]","style":{"line-color":"#D0D0D0"}},
                {"selector":"edge[group=\"user\"]","style":{"line-color":"#f0ec86"}},
                {"selector":"edge","style":{"target-arrow-color": "#777", "target-arrow-shape": "triangle", "line-color": "#777"}},
               ],

        layout: { name: 'preset', padding: 10 },

      });

      this.zoom = cy.zoom();
      this.zoomTreshold = 0.20;
      console.log("Start zoom: " + this.zoom);

      cy.panzoom({
            zoomFactor: 0.05, // zoom factor per zoom tick
            zoomDelay: 45, // how many ms between zoom ticks
            minZoom: 0.1, // min zoom level
            maxZoom: 10, // max zoom level
            fitPadding: 50, // padding when fitting
            panSpeed: 10, // how many ms in between pan ticks
            panDistance: 10, // max pan distance per tick
            panDragAreaSize: 75, // the length of the pan drag box in which the vector for panning is calculated (bigger = finer control of pan speed and direction)
            panMinPercentSpeed: 0.25, // the slowest speed we can pan by (as a percent of panSpeed)
            panInactiveArea: 8, // radius of inactive area in pan drag box
            panIndicatorMinOpacity: 0.5, // min opacity of pan indicator (the draggable nib); scales from this to 1.0
            zoomOnly: false, // a minimal version of the ui only with zooming (useful on systems with bad mousewheel resolution)

            // icon class names
            sliderHandleIcon: 'fa fa-minus',
            zoomInIcon: 'fa fa-plus',
            zoomOutIcon: 'fa fa-minus',
            resetIcon: 'fa fa-expand'
      });

      this.bindUIEvents();
//      cy.userPanningEnabled( false );
//cy.on('zoom', function(evt){
      $(document).scroll(function(event) {
          currentMousePos.x = event.pageX;
          currentMousePos.y = event.pageY;
          $('.cytoscape-navigatorView').offset().top = currentMousePos.x + $('body').offset().top / 2;
          $('.cytoscape-navigatorView').offset().left = currentMousePos.x + $('body').offset().left * 2;
      });
//        $(document).mouseleave(function(event) { });
//        $(document).mouseout(function(event) { });
//});
//      cy.on('layoutready', function(evt){
//          var upperBoundary = $('.cytoscape-navigatorView').offset().top;
//          var lowerBoundary = $('.cytoscape-navigatorView').height() + upperBoundary;
//          var leftBoundary = $('.cytoscape-navigatorView').offset().left;
//          var rightBoundary = $('.cytoscape-navigatorView').width() + leftBoundary;
//
//            cy.on('pan', function(evt){
//                  console.log("left: " + $('.cytoscape-navigatorView').offset().left +
//                              " top: " + $('.cytoscape-navigatorView').offset().top);
//                  console.log("width: " + $('.cytoscape-navigatorView').width() +
//                              " height: " + $('.cytoscape-navigatorView').height());
//                  console.log("upperBoundary: " + upperBoundary +
//                                  " lowerBoundary: " + lowerBoundary );
//                  console.log("leftBoundary: " + leftBoundary +
//                              " rightBoundary: " + rightBoundary);
//
//            });
//      });
  }

  GraphHandler.prototype.loadSettings = function() {
      var c = cookieHandler.getCookie("enableDragging");
      if (c === undefined || c[1] === "false") {
        cy.autolock(true);
        $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
        console.log("autolock(true)");
      } else {
        cy.autolock(false);
        $("#enableDragging i").attr("class", "fa fa-square fa-fw fa-lg");
        console.log("autolock(false)");
      }
  }

  /*
    Get dimensions of total graph view and coordinates of zoom view.
  */
  GraphHandler.prototype.getDimensions = function() {
       var left = $('.cytoscape-navigatorView').css('left');
       left = parseInt(left.substring(0, left.length -3));
       var top = $('.cytoscape-navigatorView').css('top');
       top = parseInt(top.substring(0, top.length -3));
       console.log("minX: " + left + " " + "maxX: " + (left + cy.width()) + "minY: " + top + "maxY: " + (top + cy.height()));
       return {
           totalWidth: cy.width(),
           totalHeight: cy.height(),
           zoomLocation: {
              minX: left,
              maxX: left + cy.width(),
              minY: top,
              maxY: top + cy.height()
           }
       };
    }

  /*
    Add UI events concerning the graph view.
  */
  GraphHandler.prototype.bindUIEvents = function() {
    $("#zoomButton").click(function() {
       console.log(graphHandler.getDimensions());
       $("#cy").toggle();
    });

    cy.on("zoom", () => {
       var z = cy.zoom();
       console.log("zooming: " + z)
       console.log("current zoom: " + this.zoom);
       var p = Math.abs(z - this.zoom) / this.zoom;
       console.log("p= " + p);
       if (p > this.zoomTreshold) {
          serverConnection.sendZoomlevel(z);
          this.zoom = z;
       }
    });

    $(".phylogeneticTree").click(function() {
       console.log("Phylogenetic tree");
       $("#cy").toggle();
       $(".cytoscape-navigator").toggle();
       $("#tree").css("display", "block");
       $("#rotation").toggle();
    });
  }

  /*
    Load data of graph containing nodes and edges in graph view.
  */
  GraphHandler.prototype.loadDataInGraph = function(elements) {
      console.log(elements);
      if (elements.hasOwnProperty("nodes") && elements.hasOwnProperty("edges")) {
          window.elements = elements;
          cy.load(elements);
      } else {
          console.log("ERROR [GraphHandler.prototype.loadDataInGraph]: Data has wrong format.");
      }
  }

  GraphHandler.prototype.hideGraph = function() { cy.css("display", "none"); }
  GraphHandler.prototype.showGraph = function() { cy.css("display", "block"); }

  /*

    Class for rendering a Phylogentic Tree.

  */
  function PhyloGeneticTree() {}

  PhyloGeneticTree.prototype.hideTree = function() { cy.css("display", "none"); }
  PhyloGeneticTree.prototype.showTree = function() { cy.css("display", "block"); }

  /**

      Initialisation

  **/

  $("#cy").cytoscapeNavigator(); // Initialize mini map
  //$("#cy").toggle();
  var graphFactory = new GraphFactory();
  var graphHandler = new GraphHandler();
  var serverConnection = new ServerConnection();
  var cookieHandler = new CookieHandler();


  $(window).resize(function () {
      updateBounds();
  });

  var updateBounds = function () {
      var bounds = cy.elements().boundingBox();
      //$('#cyContainer').css('height', bounds.h + 300);
      var h = $(document).height() - $("#nav").height() - $(".cytoscape-navigator").height();
      console.log("Height: " + h);
      $("#cy").css("height", h);
      $("#cy").css("top", $("#nav").height() + $("#options").height());
      cy.center();
      cy.resize();
      //fix the Edgehandles
      //$('#cy').cytoscapeEdgehandles('resize');
  };
  updateBounds();

  graphHandler.loadSettings();
//  cy.autolock(false);
//  cy.boxSelectionEnabled(true);
  cy.on("ready", function () {
     this.zoom = cy.zoom(0.1);
     console.log("Zooming: " + cy.zoom())
  });
  cy.zoom(0.1);

}); // on dom ready
