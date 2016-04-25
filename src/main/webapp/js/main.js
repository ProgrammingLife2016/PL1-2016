$(function(){ // on dom ready

var cy = cytoscape({
  container: $('#cy')[0],

  boxSelectionEnabled: false,
  autounselectify: true,

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

  elements: {
    nodes: [
      { data: { id: '1', name: 'A', "score":0.006769776522008331 } },
      { data: { id: '2', name: 'T', "score": 0.006769776522008331 } },
      { data: { id: '3', name: 'TCGG', "score": 0.006769776522008331 } },
      { data: { id: '4', name: 'AAC', "score": 0.006769776522008331 } },
      { data: { id: '5', name: 'GTT', "score": 0.006769776522008331 } },
      { data: { id: '6', name: 'TAGTC', "score": 0.006769776522008331 } }
    ],
    edges: [
      { data: { source: '1', target: '3', group: "pi" } },
      { data: { source: '2', target: '3', group: "pi" } },
      { data: { source: '3', target: '4', group: "predict" } },
      { data: { source: '3', target: '5', group: "reg" } },
      { data: { source: '4', target: '6', group: "spd" } },
      { data: { source: '5', target: '6', group: "coloc" } }
    ]
  },

  layout: {
    name: 'grid',
    padding: 10
  }
});

// Initialize mini map
$('#cy').cytoscapeNavigator();

//cy.on('tap', 'node', function(){
//  try { // your browser may block popups
//    window.open( this.data('href') );
//  } catch(e){ // fall back on url change
//    window.location.href = this.data('href');
//  }
//});

}); // on dom ready
