$(function() {
$('#newick_export_modal').on('show.bs.modal', function (e) {
    $('textarea[id$="nwk_export_spec"]').val(
        tree.get_newick (
            function (node) {
                var tags = [];
                selection_set.forEach (function (d) { if (node[d]) {tags.push(d)}; });
                if (tags.length) {
                    return "{" + tags.join (",") + "}";
                }
                return "";
            }
        )
    );
});

$("#newick_file").on ("change", function (e) {
    var files = e.target.files; // FileList object

    if (files.length == 1) {
      var f = files[0];
      var reader = new FileReader();

      reader.onload = (function(theFile) {
        return function(e) {
            var res = d3_phylotree_newick_parser (e.target.result);

            if (res["json"]) {
                if (!("children" in res["json"])) {
                    res["error"] = "Empty tree";
                }
            }

            var warning_div = d3.select ("#main_display").insert ("div", ":first-child");
            if (res["error"]) {
                warning_div.attr ("class", "alert alert-danger alert-dismissable")
                            .html ("<strong>Newick parser error for file " + f.name +": </strong> In file " + res["error"]);

            } else {
                default_tree_settings ();
                tree (res).svg (svg).layout();
                warning_div.attr ("class", "alert alert-success alert-dismissable")
                            .html ("Loaded a tree from  file <strong>" + f.name +": </strong>");
            }
            warning_div.append ("button")
                       .attr ("type", "button")
                       .attr ("class", "close")
                       .attr ("data-dismiss", "alert")
                       .attr ("aria-hidden", "true")
                       .html ("&times;");
        };
      })(f);

      reader.readAsText(f);
    }
});




$("#display_tree").on ("click", function (e) {
    tree.options ({'branches' : 'straight'}, true);
});

$("#mp_label").on ("click", function (e) {
    tree.max_parsimony (true);
});

$ ("[data-direction]").on ("click", function (e) {
    var which_function = $(this).data ("direction") == 'vertical' ? tree.spacing_x : tree.spacing_y;
    which_function (which_function () + (+ $(this).data ("amount"))).update();
});




$(".phylotree-layout-mode").on ("change", function (e) {
    if ($(this).is(':checked')) {
        if (tree.radial () != ($(this).data ("mode") == "radial")) {
            tree.radial (!tree.radial ()).placenodes().update ();
        }
    }
});

$(".phylotree-align-toggler").on ("change", function (e) {
    if ($(this).is(':checked')) {
        if (tree.align_tips ($(this).data ("align") == "right")) {
            tree.placenodes().update ();
        }
    }
});





function sort_nodes (asc) {
    tree.traverse_and_compute (function (n) {
            var d = 1;
            if (n.children && n.children.length) {
                d += d3.max (n.children, function (d) { return d["count_depth"];});
            }
            n["count_depth"] = d;
        });
        tree.resort_children (function (a,b) {
            return (a["count_depth"] - b["count_depth"]) * (asc ? 1 : -1);
        });
}

$("#sort_original").on ("click", function (e) {
    tree.resort_children (function (a,b) {
        return a["original_child_order"] - b["original_child_order"];
    });
});

$("#sort_ascending").on ("click", function (e) {
    sort_nodes (true);
});

$("#sort_descending").on ("click", function (e) {
    sort_nodes (false);
});

$("#and_label").on ("click", function (e) {
    tree.internal_label (function (d) { return d.reduce (function (prev, curr) { return curr[current_selection_name] && prev; }, true)}, true);
});

$("#or_label").on ("click", function (e) {
    tree.internal_label (function (d) { return d.reduce (function (prev, curr) { return curr[current_selection_name] || prev; }, false)}, true);
});


$("#filter_add").on ("click", function (e) {
    tree.modify_selection (function (d) { return d.tag || d[current_selection_name];}, current_selection_name, false, true)
        .modify_selection (function (d) { return false; }, "tag", false, false);
});

$("#filter_remove").on ("click", function (e) {
    tree.modify_selection (function (d) { return !d.tag;});
});

$("#select_all").on ("click", function (e) {
    tree.modify_selection (function (d) { return true;});
});

$("#select_all_internal").on ("click", function (e) {
    tree.modify_selection (function (d) { console.log(d); return !d3_phylotree_is_leafnode (d.target);});
});

$("#select_all_leaves").on ("click", function (e) {
    tree.modify_selection (function (d) { return d3_phylotree_is_leafnode (d.target);});
});


$("#select_none").on ("click", function (e) {
    tree.modify_selection (function (d) { return false;});
});

$("#clear_internal").on ("click", function (e) {
    tree.modify_selection (function (d) { return d3_phylotree_is_leafnode (d.target) ? d.target[current_selection_name] : false;});
});

$("#clear_leaves").on ("click", function (e) {
    tree.modify_selection (function (d) { return !d3_phylotree_is_leafnode (d.target) ? d.target[current_selection_name] : false;});
});


$("#display_dengrogram").on ("click", function (e) {
    tree.options ({'branches' : 'step'}, true);
});

//    phylotree.modify_selection = function (callback, attr, place, skip_refresh, mode) {


$("#branch_filter").on ("input propertychange", function (e) {
   var filter_value = $(this).val();

   var rx = new RegExp (filter_value,"i");

  tree.modify_selection (function (n) {
    return filter_value.length && (tree.branch_name () (n.target).search (rx)) != -1;
   },"tag");

});

$("#validate_newick").on ("click", function (e) {
    var res = d3_phylotree_newick_parser ( $('textarea[id$="nwk_spec"]').val(), true);
    if (res["error"] || ! res["json"]) {
        var warning_div = d3.select ("#newick_body").selectAll ("div  .alert-danger").data ([res["error"]])
        warning_div.enter ().append ("div");
        warning_div.html (function (d) {return d;}).attr ("class", "alert-danger");

    } else {
        default_tree_settings ();
         tree (res).svg (svg).layout();
        $('#newick_modal').modal('hide');
    }
});

function default_tree_settings () {
    tree.branch_length (null);
    tree.branch_name (null);
    tree.node_span ('equal');
    tree.options ({'draw-size-bubbles' : false}, false);
    tree.radial (true);
    tree.style_nodes (node_colorizer);
    tree.style_edges (edge_colorizer);
    tree.selection_label (current_selection_name);
}

$("#example_hiv_scaled").on ("click", function (e) {
    var hiv_tree = "";
    var res = d3_phylotree_newick_parser ( hiv_tree );
    default_tree_settings ();
    tree (res).svg (svg).layout();
});

function update_controls () {
    $("[data-mode='" + (tree.radial()      ? 'radial' : 'linear') + "']").click();
    $("[data-align='"  + (tree.align_tips () ? 'right' : 'left') + "']").click();
}

$("#example_hiv_compartments").on ("click", function (e) {

    var hiv_tree = "";
    default_tree_settings ();
    tree.radial           (true);
    tree.align_tips       (true);

    tree.branch_name      (function (data) {
           if (data.children) {
                return "";
           } else {
               bits = data.name.split ('|');
               if (bits.length >= 6) {
                 return bits[0] + " [" + bits[1] + "] " + bits[2];
               }
               return data.name;
          }
    });

    tree (d3_phylotree_newick_parser (hiv_tree)).svg (svg).layout();
    update_controls ();
});

$("#example_influenza_color").on ("click", function (e) {
    var flu_tree = "";
    var res = d3_phylotree_newick_parser ( flu_tree );
    default_tree_settings ();
    tree.branch_length (function (n) {return undefined;});
    var year_re     = new RegExp ("_virus_A_([^_]+)"),
        color_scale = d3.scale.category20();

    tree.branch_name (function (d) {
        var n = d.name.split ('_');
        if (n.length > 13) {
             return [n[9],n[10],n[12]].join (" ");
        }
        return n.join ("_");
    });

    tree.style_nodes (function (element, data) {
        var m = (tree.branch_name () (data)).split (" ");
        if (m.length > 1) {
            element.style ("fill", color_scale(m[0]));
        }
        node_colorizer (element, data);
    });


    tree.style_edges (function (element, data) {
        var m = (tree.branch_name () (data.target)).split (" ");
        if (m.length > 1) {
            element.style ("stroke", color_scale(m[0]));
        }
        edge_colorizer (element, data);
    });

    tree (res).svg (svg).layout();
});

$("#example_NGS").on ("click", function (e) {
    var NGS = "((((((((((((((cluster_0_845:0.004893613603717699,cluster_1_109:0.004840816381616456)Node13:0,cluster_3_38:0.004857835778111792)Node12:0,(cluster_6_632:0,cluster_8_53:0)Node17:0)Node11:0,cluster_21_36:0.004879782649604827)Node10:0,cluster_72_37:0.004811683649331113)Node9:0,cluster_130_63:0.004864609127112797)Node8:0,cluster_144_67:0.004856896737854473)Node7:0,cluster_164_38:0.009738706927375622)Node6:0,cluster_201_67:0.004912342010654071)Node5:0,cluster_229_35:0.004811683644837275)Node4:0,cluster_230_36:0.004893613604419758)Node3:0,cluster_295_43:0.004811683649588835)Node2:0,cluster_306_33:0.004888242085157596)Node1:0,cluster_397_62:0.004938437463580042,cluster_524_39:0.0049142477452619)";
    var res = d3_phylotree_newick_parser ( NGS );
    var re = new RegExp("_([0-9]+)$"),
        l10 = Math.log (10);
    default_tree_settings ();
    tree.node_span (function (a) { var m = re.exec (a.name); try {return Math.sqrt(parseFloat (m[1]))} catch (e) {} return null;});
    tree.options ({'draw-size-bubbles' : true}, false);

    tree (res).svg (svg).layout();
});


$("#example_GVZ").on ("click", function (e) {
    default_tree_settings ();

    function GVZ_colorizer (element, data) {
        element.style ('stroke-width', 4)
               .style ('stroke-linejoin', 'round')
               .style ('stroke-linecap', 'round');
    }
    var GVZ = "(((A300_20090507_rt_PGM:0.00763,((A300_20090507_rt_miSeqR2:0.0,A300_20090507_rt_miSeqR1:0.0):0.00055,A300:0.00055)0.979:0.00055)1.000:0.03978,(A202_20081001_rt_PGM:0.00582,((A202_20081001_rt_miSeqR2:0.0,A202_20081001_rt_miSeqR1:0.0):0.00055,A202:0.00055)0.988:0.00054)0.984:0.01883)0.868:0.00764,((A158_20080617_rt_miSeqR2:0.0,A158_20080617_rt_miSeqR1:0.0,A158_20080617_rt_PGM:0.0,A158:0.0):0.03547,(((A207_20081020_rt_miSeqR2:0.0,A207_20081020_rt_miSeqR1:0.0,A207:0.0):0.00055,A207_20081020_rt_PGM:0.00055)1.000:0.03874,(A124:0.00055,((A124_20080207_rt_miSeqR2:0.0,A124_20080207_rt_miSeqR1:0.0):0.00055,A124_20080207_rt_PGM:0.00383)0.910:0.00054)0.980:0.01829)0.025:0.00507)0.737:0.00225,((((A326_20090902_rt_miSeqR2:0.0,A326_20090902_rt_miSeqR1:0.0,A326_20090902_rt_PGM:0.0):0.03017,((A364_20100120_rt_PGM:0.00585,((A364_20100120_rt_miSeqR2:0.0,A364_20100120_rt_miSeqR1:0.0):0.00055,A364:0.00055)0.829:0.00357)0.996:0.02199,(((A297_20090526_rt_miSeqR2:0.0,A297_20090526_rt_miSeqR1:0.0,A297:0.0):0.00055,A297_20090526_rt_PGM:0.00189)1.000:0.04029,((A313:0.00055,((A313_20090715_rt_miSeqR2:0.0,A313_20090715_rt_miSeqR1:0.0):0.00053,A313_20090715_rt_PGM:0.00577)0.891:0.00055)1.000:0.04241,(A318:0.00055,((A318_20090728_rt_miSeqR2:0.0,A318_20090728_rt_miSeqR1:0.0):0.00053,A318_20090728_rt_PGM:0.00582)0.884:0.00055)0.999:0.03273)0.917:0.01184)0.658:0.00196)0.923:0.00824)0.525:0.00054,((((A144_20080513_rt_miSeqR2:0.0,A144_20080513_rt_miSeqR1:0.0,A144:0.0):0.00055,A144_20080513_rt_PGM:0.00188)0.998:0.02715,(A157:0.00055,((A157_20080617_rt_miSeqR2:0.0,A157_20080617_rt_miSeqR1:0.0):0.00055,A157_20080617_rt_PGM:0.00194)0.825:0.00053)1.000:0.03065)0.912:0.01106,(((A137_20080318_rt_miSeqR2:0.0,A137_20080318_rt_miSeqR1:0.0,A137:0.0):0.00054,A137_20080318_rt_PGM:0.00575)1.000:0.05302,((A302_20090604_rt_miSeqR2:0.0,A302_20090604_rt_miSeqR1:0.0,A302:0.0):0.00054,A302_20090604_rt_PGM:0.00574)1.000:0.03598)0.745:0.00848)0.465:0.00054)0.785:0.00274,((A312_20090702_rt_miSeqR2:0.0,A312_20090702_rt_miSeqR1:0.0,A312:0.0):0.00055,A312_20090702_rt_PGM:0.00571)0.999:0.02550)0.884:0.00629)";
    var res = d3_phylotree_newick_parser ( GVZ );
    var colorizer = d3.scale.category10 ();

    tree.branch_name (function (d) {
        var n = d.name.split ('_');
        if (n.length > 1) {
             return n[0] + "(" + n[n.length-1] + ")";
        }
        return n + " (Sanger)";
    });

    tree.node_span ('equal');
    tree.options ({'draw-size-bubbles' : false}, false);
    tree.font_size (14);
    tree.scale_bar_font_size (12);
    tree.node_circle_size (4);
    tree.spacing_x (16, true);
    tree.style_edges (GVZ_colorizer);

    tree.options ({'draw-size-bubbles' : false}, false);
    tree (res).svg (svg).layout();
});





function node_colorizer (element, data) {
try{
   var count_class = 0;

    selection_set.forEach (function (d,i) { if (data[d]) {count_class ++; element.style ("fill", color_scheme(i), i == current_selection_id ?  "important" : null);}});

    if (count_class > 1) {

    } else {
        if (count_class == 0) {
            element.style ("fill", null);
       }
    }
}
catch (e) {

}

}

function edge_colorizer (element, data) {
   //console.log (data[current_selection_name]);
    try {
        var count_class = 0;

        selection_set.forEach (function (d,i) {
            if (data[d]) {
                count_class ++;
                element.style ("stroke", color_scheme(i), i == current_selection_id ?  "important" : null);
            }
        });

        if (count_class > 1) {
            element.classed ("branch-multiple", true);
        } else
        if (count_class == 0) {
                element.style ("stroke", null)
                    .classed ("branch-multiple", false);
        }
    }
    catch (e) {}

}

var valid_id = new RegExp ("^[\\w]+$");

$("#selection_name_box").on ("input propertychange", function (e) {
   var name = $(this).val();

   var accept_name = (selection_set.indexOf (name) < 0) &&
                     valid_id.exec (name) ;

   d3.select ("#save_selection_button").classed ("disabled", accept_name ? null : true );
});

$("#selection_rename > a").on ("click", function (e) {

    d3.select ("#save_selection_button")
           .classed ("disabled",true)
           .on ("click", function (e) { // save selection handler
                var old_selection_name = current_selection_name;
                selection_set[current_selection_id] = current_selection_name = $("#selection_name_box").val();

                if (old_selection_name != current_selection_name) {
                    tree.update_key_name (old_selection_name, current_selection_name);
                    update_selection_names (current_selection_id);
                }
                send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                             {'detail' : ['save', this]}));
           });

    d3.select ("#cancel_selection_button")
               .classed ("disabled",false)
               .on ("click", function (e) { // save selection handler
                    $("#selection_name_box").val(current_selection_name);
                    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['cancel', this]}));
              });

    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['rename', this]}));
    e.preventDefault    ();
});

$("#selection_delete > a").on ("click", function (e) {

    tree.update_key_name (selection_set[current_selection_id], null)
    selection_set.splice (current_selection_id, 1);

    if (current_selection_id > 0) {
        current_selection_id --;
    }
    current_selection_name = selection_set[current_selection_id];
    update_selection_names (current_selection_id)
    $("#selection_name_box").val(current_selection_name)


    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['save', this]}));
    e.preventDefault    ();

});

$("#selection_new > a").on ("click", function (e) {

    d3.select ("#save_selection_button")
               .classed ("disabled",true)
               .on ("click", function (e) { // save selection handler
                    current_selection_name = $("#selection_name_box").val();
                    current_selection_id = selection_set.length;
                    selection_set.push (current_selection_name);
                    update_selection_names (current_selection_id);
                    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['save', this]}));
              });

     d3.select ("#cancel_selection_button")
               .classed ("disabled",false)
               .on ("click", function (e) { // save selection handler
                    $("#selection_name_box").val(current_selection_name);
                    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['cancel', this]}));
              });

    send_click_event_to_menu_objects (new CustomEvent (selection_menu_element_action,
                                 {'detail' : ['new', this]}));
    e.preventDefault    ();

});

function send_click_event_to_menu_objects (e) {
    $("#selection_new, #selection_delete, #selection_rename, #save_selection_name, #selection_name_box, #selection_name_dropdown").get().forEach (
        function (d) {
            d.dispatchEvent (e);
        }
    );
}

function update_selection_names (id, skip_rebuild) {

    skip_rebuild = skip_rebuild || false;
    id = id || 0;


    current_selection_name = selection_set[id];
    current_selection_id = id;

    if (!skip_rebuild) {
        d3.selectAll (".selection_set").remove();

        d3.select ("#selection_name_dropdown")
          .selectAll (".selection_set")
          .data (selection_set)
          .enter()
          .append ("li")
          .attr ("class", "selection_set")
          .append ("a")
          .attr ("href", "#")
          .text (function (d) { return d;})
          .style ("color", function (d,i) {return color_scheme(i);})
          .on ("click", function (d,i) {update_selection_names (i,true);});

    }


    d3.select ("#selection_name_box")
      .style ("color",  color_scheme(id))
      .property ("value", current_selection_name);

    tree.selection_label (selection_set[id]);
}

var width  = 800, //$(container_id).width(),
    height = 600, //$(container_id).height()
    selection_set = ['Foreground'],
    current_selection_name = $("#selection_name_box").val(),
    current_selection_id = 0,
    max_selections       = 10;
    color_scheme = d3.scale.category10(),
    selection_menu_element_action = "phylotree_menu_element_action";

var tree = d3.layout.phylotree("body")
    .size([height, width])
    .separation (function (a,b) {return 0;})
    .count_handler (function (count) {
            $("#selected_branch_counter").text (function (d) {return count[current_selection_name];});
            $("#selected_filtered_counter").text (count.tag);
        }
    );
    //.node_span (function (a) {if (a.children && a.children.length) return 1; return isNaN (parseFloat (a["attribute"]) * 100) ? 1 : parseFloat (a["attribute"]) * 100; });

var test_string = "(((EELA:0.150276,CONGERA:0.213019):0.230956,(EELB:0.263487,CONGERB:0.202633):0.246917):0.094785,((CAVEFISH:0.451027,(GOLDFISH:0.340495,ZEBRAFISH:0.390163):0.220565):0.067778,((((((NSAM:0.008113,NARG:0.014065):0.052991,SPUN:0.061003,(SMIC:0.027806,SDIA:0.015298,SXAN:0.046873):0.046977):0.009822,(NAUR:0.081298,(SSPI:0.023876,STIE:0.013652):0.058179):0.091775):0.073346,(MVIO:0.012271,MBER:0.039798):0.178835):0.147992,((BFNKILLIFISH:0.317455,(ONIL:0.029217,XCAU:0.084388):0.201166):0.055908,THORNYHEAD:0.252481):0.061905):0.157214,LAMPFISH:0.717196,((SCABBARDA:0.189684,SCABBARDB:0.362015):0.282263,((VIPERFISH:0.318217,BLACKDRAGON:0.109912):0.123642,LOOSEJAW:0.397100):0.287152):0.140663):0.206729):0.222485,(COELACANTH:0.558103,((CLAWEDFROG:0.441842,SALAMANDER:0.299607):0.135307,((CHAMELEON:0.771665,((PIGEON:0.150909,CHICKEN:0.172733):0.082163,ZEBRAFINCH:0.099172):0.272338):0.014055,((BOVINE:0.167569,DOLPHIN:0.157450):0.104783,ELEPHANT:0.166557):0.367205):0.050892):0.114731):0.295021)";
var container_id = '#tree_container';
//var test_string = "(a : 0.1, (b : 0.11, (c : 0.12, d : 0.13) : 0.14) : 0.15)";

//window.setInterval (function () {});

var svg = d3.select(container_id).append("svg")
    .attr("width", width)
    .attr("height", height);



function selection_handler_name_box (e) {
    var name_box = d3.select (this);
     switch (e.detail[0]) {
        case 'save':
        case 'cancel':
            name_box.property ("disabled", true)
                    .style ("color",  color_scheme(current_selection_id));

            break;
        case 'new':
            name_box.property ("disabled", false)
                    .property ("value", "new_selection_name")
                    .style ("color",  color_scheme(selection_set.length));
            break;
        case 'rename':
           name_box.property ("disabled", false);
           break;
    }

}

function selection_handler_new (e) {
    var element = d3.select (this);
    $(this).data('tooltip', false);
    switch (e.detail[0]) {
        case 'save':
        case 'cancel':
            if (selection_set.length == max_selections) {
                element.classed ("disabled", true);
                    $(this).tooltip ({'title' : 'Up to ' + max_selections + ' are allowed', 'placement' : 'left'});
            } else {
                element.classed ("disabled", null);
            }
            break;
        default:
            element.classed ("disabled", true);
            break;

    }
}

function selection_handler_rename (e) {
    var element = d3.select (this);
    element.classed ("disabled", (e.detail[0] == "save" || e.detail[0] == "cancel") ? null : true);
}

function selection_handler_save_selection_name (e) {
    var element = d3.select (this);
    element.style ("display", (e.detail[0] == "save" || e.detail[0] == "cancel") ? "none" : null);
}

function selection_handler_name_dropdown (e) {
    var element = d3.select (this).selectAll (".selection_set");
    element.classed ("disabled", (e.detail[0] == "save" || e.detail[0] == "cancel") ? null : true);
}

function selection_handler_delete (e) {
    var element = d3.select (this);
    $(this).tooltip('destroy');
    switch (e.detail[0]) {
        case 'save':
        case 'cancel':
            if (selection_set.length == 1) {
                element.classed ("disabled", true);
                    $(this).tooltip ({'title' : 'At least one named selection set <br> is required;<br>it can be empty, however', 'placement' : 'bottom', 'html': true});
            } else {
                element.classed ("disabled", null);
            }
            break;
        default:
            element.classed ("disabled", true);
            break;

    }}


$( document ).ready( function () {
    default_tree_settings();
    tree(test_string).svg (svg).layout();
    // $("#selection_new").get(0).addEventListener(selection_menu_element_action,selection_handler_new,false);
    // $("#selection_rename").get(0).addEventListener(selection_menu_element_action,selection_handler_rename,false);
    // $("#selection_delete").get(0).addEventListener(selection_menu_element_action,selection_handler_delete,false);
    // $("#selection_delete").get(0).dispatchEvent (new CustomEvent (selection_menu_element_action,
    //                              {'detail' : ['cancel', null]}));
    // $("#selection_name_box").get(0).addEventListener(selection_menu_element_action,selection_handler_name_box,false);
    // $("#save_selection_name").get(0).addEventListener(selection_menu_element_action,selection_handler_save_selection_name,false);
    // $("#selection_name_dropdown").get(0).addEventListener(selection_menu_element_action,selection_handler_name_dropdown,false);
    update_selection_names();

    // jQuery.get("/static/file.nwk", function(data) {
    //     var res = d3_phylotree_newick_parser (data);
    //     default_tree_settings ();
    //     tree.branch_length (function (n) {return undefined;});
    //     var year_re     = new RegExp ("_virus_A_([^_]+)"),
    //         color_scale = d3.scale.category20();

    //     tree.branch_name (function (d) {
    //         var n = d.name.split ('_');
    //         if (n.length > 13) {
    //             return [n[9],n[10],n[12]].join (" ");
    //         }
    //         return n.join ("_");
    //     });

    //     tree.style_nodes (function (element, data) {
    //         var m = (tree.branch_name () (data)).split (" ");
    //         if (m.length > 1) {
    //             element.style ("fill", color_scale(m[0]));
    //         }
    //         node_colorizer (element, data);
    //     });


    //     tree.style_edges (function (element, data) {
    //         var m = (tree.branch_name () (data.target)).split (" ");
    //         if (m.length > 1) {
    //             element.style ("stroke", color_scale(m[0]));
    //         }
    //         edge_colorizer (element, data);
    //     });

    //     tree (res).svg (svg).layout();
    // });

    d3.text ("/static/file.nwk", function (error, newick) {
        var res = d3_phylotree_newick_parser (newick);

        if (res["json"]) {
            if (!("children" in res["json"])) {
                res["error"] = "Empty tree";
            }
        }

        var warning_div = d3.select ("#main_display").insert ("div", ":first-child");
        if (res["error"]) {
            warning_div.attr ("class", "alert alert-danger alert-dismissable")
                        .html ("<strong>Newick parser error for file " + f.name +": </strong> In file " + res["error"]);

        } else {
            default_tree_settings ();
            tree.radial(true);
            tree.align_tips(true);
            tree (res)
                .svg (d3.select("#tree_display"))
                .layout();
            window.tree = tree;
            //warning_div.attr ("class", "alert alert-success alert-dismissable") .html ("Loaded a tree from  file <strong>" + f.name +": </strong>");

            var notTKK = ["root", "start"];
            window.tkks =
                tree.get_nodes()
                    .filter(function(node) {
                        return node.name !== "" && notTKK.indexOf(node.name) == -1;
                    })
                    .map(function(node) {
                        return {"textContent": node.name};
                    });
        }
        warning_div.append ("button")
                   .attr ("type", "button")
                   .attr ("class", "close")
                   .attr ("data-dismiss", "alert")
                   .attr ("aria-hidden", "true")
                   .html ("&times;");

        $("#layout").on("click", function (e) {
            // tree.radial ($(this).prop ("checked")).placenodes().update ();
            tree.spacing_y(tree.spacing_y() + 1).update();
            tree.spacing_x(tree.spacing_x() + 1).update();
        });
    });

});
});
