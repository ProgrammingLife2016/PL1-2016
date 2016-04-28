$(function() { // on dom ready
  $("#cy").css("top", $("#nav").height());
  $("#logo").stop().animate({opacity: 1},800,"swing");

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
        faveShape: 'ellipse'
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
    var nodes = data.nodes.map(n => graphFactory.createNode(n));
    var edges = data.edges.map(e => graphFactory.createEdge(e));
    return { nodes, edges };
  }

  /*

      Class for connection with the server

  */
  function ServerConnection() {
     nodesDir = "/nodes";
     this.req = {
                   url: "",
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
      this.req["url"] = nodesDir;
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
  }

  /*
    Load data if AJAX request was succesfull.
  */
  ServerConnection.prototype.handleSucces = function(data, textStatus, jqXHR) {
      console.log(textStatus);
      console.log(jqXHR);
      graphHandler.loadDataInGraph(data);
  }

  /*
    Handle error if AJAX request failed.
  */
  ServerConnection.prototype.handleError = function(jqXHR ,textStatus, errorThrown) {
      console.log("ERROR AJAX request");
      console.log(jqXHR);
      console.log("- HTTP status code : " + jqXHR.status);
      console.log("- response Text    : " + jqXHR.responseText);
      console.log("- Error thrown     : " + errorThrown);
  }

  /*
    Print HTTP status code of AJAX request.
  */
  ServerConnection.prototype.handleStatusCode = function(statusCode) {
      console.log("Status code AJAX request: " + statusCode)
  }


  /*

     Class for rendering the graph.

  */
  function GraphHandler() {
      cy = cytoscape({
        container: $('#cy')[0],
        boxSelectionEnabled: false,
        autounselectify: true,
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
                     "overlay-padding":"6px","z-index":"10"}},
                {"selector":"node[?attr]","style":{"shape":"rectangle","background-color":"#aaa","text-outline-color":"#aaa",
                    "width":"16px","height":"16px","font-size":"6px","z-index":"1"}},
                {"selector":"node[?query]","style":{"background-clip":"none","background-fit":"contain"}},
                {"selector":"node:selected","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5",
                    "background-color":"#77828C","text-outline-color":"#77828C"}},
                {"selector":"edge","style":{"target-arrow-shape": "triangle"}},
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
                {"selector":"edge[group=\"user\"]","style":{"line-color":"#f0ec86"}}],

        layout: {
          name: 'grid',
          padding: 10
        }
      });

      var testJson = data;
      console.log(testJson)
      cy.add(JSONAdapter.prototype.convert(testJson));
      this.bindUIEvents();
  }

  /*
    Get dimensions of total graph view and coordinates of zoom view.
  */
  GraphHandler.prototype.getDimensions = function() {
     return {
         totalWidth: cy.width(),
         totalHeight: cy.height(),
         zoomLocation: {
            minX: 0,
            maxX: 0,
            minY: 0,
            maxY: 0
         }
     };
  }

  /*
    Add UI events concerning the graph view.
  */
  GraphHandler.prototype.bindUIEvents = function() {
    $("#zoomButton").click(() => {
        //console.log(cy.cytoscapeNavigator.getZoomDim());
        console.log(graphHandler.getDimensions());
//        $.ajax({
//           url: "/api",
//           data: {},
//           dataType: 'json',
//           success : (data) => cy.add(JSONAdapter.prototype.convert(data)),
//           error: () => console.log("#error"),
//        });
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
  function PhyloGeneticTree() {
  }

  PhyloGeneticTree.prototype.hideTree = function() { cy.css("display", "none"); }
  PhyloGeneticTree.prototype.showTree = function() { cy.css("display", "block"); }

  $('#cy').cytoscapeNavigator(); // Initialize mini map
  var graphFactory = new GraphFactory();
  var serverConnection = new ServerConnection();
  var graphHandler = new GraphHandler();
}); // on dom ready
