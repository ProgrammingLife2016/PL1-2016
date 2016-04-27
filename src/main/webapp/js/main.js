$(function(){ // on dom ready
  /*

      Adapter for converting JSON data from server to correct format for cytoscape

  */
  function JSONAdapter() {
  }

  /*
    Convert JSON data received from server.
  */
  JSONAdapter.prototype.convert = function(data) {
	var nodes = [];
    for(var i = 0; i < data.nodes.length; i++) {
        var tempNode = data.nodes[i];
        var nodeWeight = tempNode.bubble? 100 : 50;
        nodes.push({ data: { id: tempNode.id, name: getName(tempNode.data), weight: nodeWeight, faveColor: '#6FB1FC', faveShape: 'ellipse' }, position: {x: tempNode.x, y: tempNode.y} });
    }

	var edges = [];
    for(var i = 0; i < data.edges.length; i++) {
        var tempEdge = data.edges[i];
        var edgeWeight = tempNode.bubble? 100 : 50;
        edges.push({ data: { source: tempEdge.from, target: tempEdge.to}});
    }
    var elements = { nodes, edges };
    return elements;
  }

  function getName(name){
    return name.length > 3 ? name.substring(0, 3)+"..." : name;
  }

  /*

      Class for connection with the server

  */
  function ServerConnection() {
     nodesDir = "/nodes";
     this.bindUIEvents();
  }

  /*
    Create AJAX to server
  */
  ServerConnection.prototype.retrieveDataFromServer = function() {
        $.ajax({
           url: "/nodes",
           data: {},
           dataType: 'json',
           success : this.handleSucces,
           error: this.handleError,
           statusCode: {
             default: this.handleStatusCode
           }
        });
  }

  /*
    Add UI events concerning communicating with the server.
  */
  ServerConnection.prototype.bindUIEvents = function() {
      $("#connect").click(function() {
        console.log("Connecting to server...");
        serverConnection.retrieveDataFromServer();
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

        style: [{"selector":"core",
                   "style":
                   {"selection-box-color":"#AAD8FF","selection-box-border-color":"#8BB0D0","selection-box-opacity":"0.5"}},
                {"selector":"node",
                   "style":
                    {"width":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "height":"mapData(score, 0, 0.006769776522008331, 20, 60)",
                     "content":"data(name)","font-size":"12px","text-valign":"center","text-halign":"center",
                     "background-color":"#555","text-outline-color":"#555","text-outline-width":"2px","color":"#fff",
                     "overlay-padding":"6px","z-index":"10"}},
                {"selector":"node[?attr]","style":{"shape":"rectangle","background-color":"#aaa","text-outline-color":"#aaa",
                    "width":"16px","height":"16px","font-size":"6px","z-index":"1"}},
                {"selector":"node[?query]","style":{"background-clip":"none","background-fit":"contain"}},
                {"selector":"node:selected","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5",
                    "background-color":"#77828C","text-outline-color":"#77828C"}},
                {"selector":"edge","style":{"curve-style":"haystack","haystack-radius":"0.5","opacity":"0.8",
                    "line-color":"#bbb","width":"mapData(weight, 0, 1, 1, 8)","overlay-padding":"3px"}},
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

//        elements: {
//          nodes: [
//            { data: { id: '1', name: 'A', "score":0.006769776522008331 } },
//            { data: { id: '2', name: 'T', "score": 0.006769776522008331 } },
//            { data: { id: '3', name: 'TCGG', "score": 0.006769776522008331 } },
//            { data: { id: '4', name: 'AAC', "score": 0.006769776522008331 } },
//            { data: { id: '5', name: 'GTT', "score": 0.006769776522008331 } },
//            { data: { id: '6', name: 'TAGTC', "score": 0.006769776522008331 } }
//          ],
//          edges: [
//            { data: { source: '1', target: '3', group: "pi" } },
//            { data: { source: '2', target: '3', group: "pi" } },
//            { data: { source: '3', target: '4', group: "predict" } },
//            { data: { source: '3', target: '5', group: "reg" } },
//            { data: { source: '4', target: '6', group: "spd" } },
//            { data: { source: '5', target: '6', group: "coloc" } }
//          ]
//        },

        layout: {
          name: 'grid',
          padding: 10
        }
      });

      var testJson = {
                       "status": "success",
                       "nodes": [
                         {
                           "id": 1,
                           "bubble": false,
                           "data": "ACGGT",
                           "x": 0,
                           "y": 0
                         },
                         {
                           "id": 2,
                           "bubble": true,
                           "data": "bubble",
                           "x": 100,
                           "y": 100
                         },
                         {
                           "id": 3,
                           "bubble": false,
                           "data": "GCCAGT",
                           "x": -100,
                           "y": -100
                         }
                       ],
                       "edges": [
                         {
                           "from": 1,
                           "to": 2
                         },
                         {
                           "from": 2,
                           "to": 3
                         }
                       ]
                     };
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
    $("#zoomButton").click(function() {
        //console.log(cy.cytoscapeNavigator.getZoomDim());
        console.log(graphHandler.getDimensions());
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
  var serverConnection = new ServerConnection();
  var graphHandler = new GraphHandler();
}); // on dom ready

//cy.on('tap', 'node', function(){
//  try { // your browser may block popups
//    window.open( this.data('href') );
//  } catch(e){ // fall back on url change
//    window.location.href = this.data('href');
//  }
//});

//  style: cytoscape.stylesheet()
//    .selector('node')
//      .css({
//        'content': 'data(name)',
//        'text-valign': 'center',
//        'color': 'white',
//        'text-outline-width': 2,
//        'text-outline-color': '#888'
//      })
//    .selector(':selected')
//      .css({
//        'background-color': 'black',
//        'line-color': 'black',
//        'target-arrow-color': 'black',
//        'source-arrow-color': 'black',
//        'text-outline-color': 'black'
//      }),
