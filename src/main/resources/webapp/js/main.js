$(function() { // on dom ready
  $("#options").css("top", $("#nav").height());
  $("#options").css("height", $(document).height() - $("#nav").height());
  $("#cy").css("top", $("#nav").height() + $("#options").height());

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

  /*
    Add UI events concerning communicating with the server.
  */
  ServerConnection.prototype.bindUIEvents = function() {
      $("#connect").click(() => {
        console.log("Connecting to server...");
        this.retrieveDataFromServer();
      });

      var duration = 400;
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
        }, 1500);
        setTimeout(function() {
            $("#options").css("display", "none");
            //$("#cy").toggle();
        }, 1500);
      });

      $("#optionButton").click(function() {
         $header = $(this);
         $content = $("#optionsContainer");
         $content.slideToggle(500, function () {
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
//           cy.autolock(false);
         } else {
           $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
           cookieHandler.setCookie("enableDragging", "false");
//           cy.autolock(true);
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

        style: [{"selector":"core",
                   "style":
                   {"selection-box-color":"#AAD8FF","selection-box-border-color":"#8BB0D0","selection-box-opacity":"0.5"}},
                {"selector":"node",
                   "style":
                    {"width":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "height":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "content":"data(name)","font-size":"12px","text-valign":"center","text-halign":"center",
                     "background-color":"#94AAC7","text-outline-color":"#94AAC7","text-outline-width":"2px","color":"#fff", //#555
                     "overlayfghjklo`-padding":"6px","z-index":"10"}},
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

        layout: {
          name: 'preset',
          padding: 10
        }
      });
      this.bindUIEvents();
      cy.userPanningEnabled( false );
  }

  GraphHandler.prototype.loadSettings = function() {
      var c = cookieHandler.getCookie("enableDragging");
      if (c === undefined || c[1] === "false") {
        cy.autolock(true);
        $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
      } else {
        cy.autolock(false);
        $("#enableDragging i").attr("class", "fa fa-square fa-fw fa-lg");
      }
      console.log("Load settings");
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
    $("#zoomButton").click(() => {
       console.log(graphHandler.getDimensions());
       $("#cy").toggle();
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

  $("#cy").cytoscapeNavigator(); // Initialize mini map
  //$("#cy").toggle();
  var graphFactory = new GraphFactory();
  var graphHandler = new GraphHandler();
  var serverConnection = new ServerConnection();
  var cookieHandler = new CookieHandler();
  graphHandler.loadSettings();
}); // on dom ready
