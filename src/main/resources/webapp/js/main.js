$(function() { // on dom ready


    /* Settings */
    var settings = {
        animationDuration: 500, //length of the animations in ms
        zoomTreshold: 0.50, //change in percentage/100 between zoom levels for sending a new AJAX request
        startZoom: 0.1, //Zoom level at the start of the application
        nodesDir: "/api/nodes/64/0/1000000000/4", //directory at the server for the first AJAX request
    };

    /*
       Start animations
    */
    $("#options").css("height", $(document).height() - $("#nav").height());
    $("#logo").stop().animate({
        opacity: 1
    }, 800, "swing");
    $("#container").stop().animate({
        opacity: 1,
        "padding-top": 120
    }, 800, "swing");
    $("html, body").stop().animate({
        scrollTop: 0
    }, 800, "swing");
    $("#stackpane, #results").css("height", $(document).height() - $("#nav").height());
    $("#stackpane").css("top", $("#nav").height());
    $("#status").css("opacity", 0);

    /*

        Class for connection with the server

    */
    function ServerConnection() {
        this.req = {
            url: settings.nodesDir,
            data: {},
            complete: this.handleComplete,
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
        graphHandler.loadDataInGraph("");
//        $.ajax(this.req);
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
        $("#status").stop().animate({
            opacity: 1
        }, settings.animationDuration, "swing");
    }

    /*
       Load data from zoom level AJAX request to server.
    */
    ServerConnection.prototype.handleComplete = function(data) {
        console.log("Response request");
        graphHandler.requestSend = false;
        $("#status").animate({
            opacity: 0
        }, settings.animationDuration, "swing");
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
        // $("#connect").click(() => {
        //     console.log("Connecting to server...");
        //     this.retrieveDataFromServer();
        // });

        var duration = settings.animationDuration;
        var easing = "swing";
        $("#startConnection").click(() => {
            $("#d3").css("background-color", "#ECF0F1");
            $("#d3").css("display", "block");

            $("#startConnection i").attr("class", "fa fa-circle-o-notch fa-spin fa-fw fa-lg");
            console.log("Connecting to server...");
            this.retrieveDataFromServer();
        });

        $("#optionButton").click(function() {
            $header = $(this);
            $content = $("#optionsContainer");
            $content.slideToggle(duration, function() {
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
        $("#status").stop().animate({
            opacity: 0
        }, settings.animationDuration, "swing");
        graphHandler.loadDataInGraph(JSONAdapter.prototype.convert(data));
    }

    /*
      Handle error if AJAX request failed.
    */
    ServerConnection.prototype.handleError = function(jqXHR, textStatus, errorThrown) {
        graphHandler.requestSend = false;
        $("#status").animate({
            opacity: 0
        }, settings.animationDuration, "swing");
        console.log("ERROR AJAX request");
        console.log(jqXHR);
    }

    /*
      Print HTTP status code of AJAX request.
    */
    ServerConnection.prototype.handleStatusCode = function(statusCode) {
        $("#status").animate({
            opacity: 0
        }, settings.animationDuration, "swing");
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
        this.zoomTreshold = settings.zoomTreshold;
        this.requestSend = false;
        this.bindUIEvents();
        this.tree_div_height;
        this.tree_div_width;
    }

    GraphHandler.prototype.loadSettings = function() {
        var c = cookieHandler.getCookie("enableDragging");
        if (c === undefined || c[1] === "false") {
            $("#enableDragging i").attr("class", "fa fa-square-o fa-fw fa-lg");
        } else {
            $("#enableDragging i").attr("class", "fa fa-square fa-fw fa-lg");
        }
    };

    /*
      Add UI events concerning the graph view.
    */
    GraphHandler.prototype.bindUIEvents = function() {
        $(".dnaGraph").click(function() {
            $("#d3").show();
            $("#tree").hide();
            $("#rotation").hide();
            $("#tree").css("z-index", "1");
            $("#d3").css("z-index", "2");
            $("#options").css("z-index", "0");
            $("#search").css("display", "none");
            $("#tree").height(this.tree_div_height);
            $("#tree").width(this.tree_div_width);
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

            this.tree_div_height = $("#tree_display").height();
            this.tree_div_width = $("#tree_display").width();
            console.log("Window size");
            console.log($(document).width());
            console.log($(document).height());

            console.log("Div size");
            console.log(this.tree_div_width);
            console.log(this.tree_div_height);
            if ($(document).width() < this.tree_div_width) {
                console.log("Width");
                $("#tree").width($("#tree_display").width());
            }
            if ($(document).height() - $("#nav").height() < this.tree_div_height) {
                console.log("Height");
                $("#tree").height($("#tree_display").height());
            }
            window.tkks.forEach(function(tkk) {
                $("#search ul").append($("<li>").text(tkk.textContent));
            });

            if (this.fuse === undefined) {
                phyloTree.loadFuse();
            }
        });

        $("#toggleSearch").click(function() {
            if ($("#toggleSearch").data("open") === "false") {
                $("#toggleSearch i").attr("class", "fa fa-square fa-fw");
                $("#search").stop().animate({width: 260, opacity: 1}, 400, "swing");
                $("#toggleSearch").data("open", "true");
                $("#search input").focus();
            } else {
                $("#toggleSearch i").attr("class", "fa fa-square-o fa-fw");
                //phyloTree.resetHighlighting();
                $("#search").stop().animate({width: 0, opacity: 0}, 400, "swing");
                $("#toggleSearch").data("open", "false");
            }
        });

        $("#connect").click(function() {
            phyloTree.setLineageHighlighting("LIN 4");
            phyloTree.setLineageHighlighting("LIN 3");
            phyloTree.setLineageHighlighting("LIN 2");
            phyloTree.setLineageHighlighting("LIN 1");
        });

        $("#resetHighlighting").click(function() {
            phyloTree.resetHighlighting();
        });

        $("#resetButton").click(function() {
            phyloTree.resetHighlighting();
        });

        $("#selectButton").click(function() {
            if ($("#showresistance").attr("data-open") === "false") {
                $("#showresistance").attr("data-open", "true");
                $("#showresistance i").attr("class", "fa fa-square fa-fw");
            } else {
                $("#showresistance").attr("data-open", "false");
                $("#showresistance i").attr("class", "fa fa-square-o fa-fw");
            }
            phyloTree.setHL();
        });
    };

    GraphHandler.prototype.resetHighlighting = function() {
        disableHighlighting();
        $("#info").fadeOut();
        $("#info div").remove();
        $("#info").height(70);
    };

    /*
      Load data of graph containing nodes and edges in graph view.
    */
    GraphHandler.prototype.loadDataInGraph = function(response) {
        startD3();
        console.log("Done");

        var duration = settings.animationDuration;
        $("#d3").fadeIn();
        $("#tree").css("z-index", "1");
        $("#d3").css("z-index", "2");
        $("#options").css("z-index", "0");
        $("#options").fadeOut();
        //$("#status").stop().animate({opacity: 0}, duration, "swing");
        $("#viewlist").stop().animate({
            opacity: 1
        }, duration, "swing");
        $("#settings").stop().animate({
            opacity: 1
        }, duration, "swing");
        //$("#options").stop().animate({height: 0}, duration, "swing");
        //$("#container").stop().animate({height: 0}, duration, "swing");
        $("#startConnection").stop().animate({
            opacity: 0
        }, duration, "swing");
        $("#optionButton").stop().animate({
            opacity: 0
        }, duration, "swing");
    };

    GraphHandler.prototype.showGraph = function() {
        $("#d3").show();
        $("#tree").hide();
        $("#rotation").hide();
        $("#tree").css("z-index", "1");
        $("#d3").css("z-index", "2");
        $("#options").css("z-index", "0");
        $("#search").css("display", "none");
    };

    GraphHandler.prototype.showPhylotree = function() {
        $("#d3").hide();
        $("#tree").css("opacity", "1");
        $("#rotation").show();
        $("#tree").show();
        $("#tree").css("z-index", "2");
        $("#d3").css("z-index", "1");
        $("#options").css("z-index", "0");
        $("#search").css("display", "block");
        phyloTree.listItems();

        if (this.fuse === undefined) {
            phyloTree.loadFuse();
        }
    };

    GraphHandler.prototype.setSelectedGenome = function(name) {
        var listitem = $("<div>").append($("<p>").text(name));
        $("#info").prepend(listitem);
        $("#info").height($("#info").height() + 36);
        $("#info").fadeIn();
    };


    /*

      Class for rendering a Phylogentic Tree.

    */
    function PhyloGeneticTree() {
        this.animationDuration = 100;
        this.bindUIEvents();
        this.fuse = undefined;
        this.LINEAGE_COLORS = {
            "LIN 1":        "#ED00C3",
            "LIN 2":        "#0000FF",
            "LIN 3":        "#500079",
            "LIN 4":        "#FF0000",
            "LIN 5":        "#4E2C00",
            "LIN 6":        "#69CA00",
            "LIN 7":        "#FF7E00",
            "LIN animal":   "#00FF9C",
            "LIN B":        "#00FF9C",
            "LIN CANETTII": "#00FFFF"
        };
        this.resistance = {
            "XDR":                    "#D5192B",
            "MDR":                    "#4B1A1A",
            "Drug-resistant (other)": "#F5E396",
            "susceptible":            "#C9E9EE"
        };
        String.prototype.replaceAll = function(search, replace) {
            if (replace === undefined) {
                return this.toString();
            }
            return this.split(search).join(replace);
        };
    }

    PhyloGeneticTree.prototype.loadFuse = function() {
        console.log("Initialize fuse");
        var listOfGenomes = window.tkks.map(function(tkk) {
            return {
                "name": tkk.textContent,
                "compressed": tkk.textContent
            };
        });
        var options = {
            keys: ["name"],
            shouldSort: true,
            threshold: 0.1
        };
        window.fuse = new Fuse(listOfGenomes, options);
    };

    PhyloGeneticTree.prototype.listItems = function() {
        var text_items = d3.select("#tree_display").selectAll("text");
        var items = text_items[0].map(function(tkk) {
            return $("<li>").text(tkk.innerHTML);
        });
        $("#search ul").html(items);
    };

    PhyloGeneticTree.prototype.bindUIEvents = function() {
        $("#search input").focus(function() {
            console.log("Focus search bar");
        });

        $("#search input").keyup(function() {
            if ($("#search input ").val() === "") {
                phyloTree.resetHighlighting();
                phyloTree.listItems();
            }
        });
        $("#results").on("click", "li", function(e) {
            highlightGenome($(this).html());
        });

        $("#search input").on("search", function() {
            phyloTree.resetHighlighting();
            phyloTree.listItems();
        });

        $("#search input").keyup(function(e) {
            //Reset highlighting
            phyloTree.resetHighlighting();

            if (window.fuse === undefined) {
                console.log("Fuse not defined");
                phyloTree.loadFuse();
            }
            var res = window.fuse.search($("#search input").val().replace(" ", "-"));
            if (res.length == 0) return false;

            var nameMap = {};
            res.forEach(function(tkk) {
                nameMap[tkk["name"]] = 1;
            });

            d3.select("#tree_display")
              .selectAll("text")
              .style("fill", function(t) {
                  if (nameMap[t.name] == 1) {
                      return "#F00";
                  } else {
                      return "#000";
                  }
              }).attr("stroke-width", "5px");


            var items = res.map(function(match) {
                return $("<li>").text(match["name"]);
            });
            $("#search ul").html(items);
            return true;
        });

        $("#showresistance").click(function() {
            if ($("#showresistance").attr("data-open") === "false") {
                phyloTree.setHL();
                $("#showresistance").attr("data-open", "true");
                $("#showresistance i").attr("class", "fa fa-square fa-fw");
            } else {
                phyloTree.setHL();
                $("#showresistance").attr("data-open", "false");
                $("#showresistance i").attr("class", "fa fa-square-o fa-fw");
            }
        });

        $("#dr").click(function() {
            console.log("Remove");
            if ($("#dr").attr("data-open") === "true") {
                console.log("Close");
                $("[data-id=dr]").remove();
                $("#dr").attr("data-open", "false");
                $("#dr i").attr("class", "fa fa-square-o");
                $("#legend").height($("#legend").height() - 140);
                d3.select("#tree_display")
                  .selectAll("text")
                  .style("fill", function(t) {return "#444";}, "important");
            } else {
                $("#dr").attr("data-open", "true");
                $("#dr i").attr("class", "fa fa-square");
                console.log("Open");
                Object.keys(phyloTree.resistance)
                    .forEach(k => {
                        $("<div>").attr("id", "legend_item")
                            .attr("data-id", "dr")
                            .append(
                                $("<div>").attr("id", "color")
                                            .css("background-color", phyloTree.resistance[k])
                            )
                            .append($("<p>").html(k)).insertAfter("#dr");
                    });
                $("#legend").height($("#legend").height() + 140);
                $("#legend").show();
                $.getJSON("/api/metadata/genomes", function(response) {
                    console.log(response);
                    console.log("setHL");
                    d3.select("#tree_display")
                      .selectAll("text")
                      .style("fill", function(t) {
                        return phyloTree.resistance[response.data[t.name.replaceAll("-", "_")]];
                      });
                });
            }
        });

        $("#lh").click(function(){
            if ($("#lh").attr("data-open") === "true") {
                $("[data-id=lh]").remove();
                $("#lh").attr("data-open", "false");
                $("#lh i").attr("class", "fa fa-square-o");
                $("#legend").height($("#legend").height() - 290);
                d3.select("#tree_display")
                  .selectAll("path")
                  .style("stroke", function(t) {return "#999";}, "important");
            } else {
                $("#lh").attr("data-open", "true");
                $("#lh i").attr("class", "fa fa-square");
                $("#legend").height($("#legend").height() + 290);
                Object.keys(phyloTree.LINEAGE_COLORS)
                    .forEach(k => {
                        $("<div>").attr("id", "legend_item")
                            .attr("data-id", "lh")
                            .append(
                                $("<div>").attr("id", "color")
                                    .css("background-color", phyloTree.LINEAGE_COLORS[k])
                            )
                            .append($("<p>").html(k)).insertAfter("#lh");
                    });
                phyloTree.setLineageHighlighting("LIN 4");
                phyloTree.setLineageHighlighting("LIN 3");
                phyloTree.setLineageHighlighting("LIN 2");
                phyloTree.setLineageHighlighting("LIN 1");
            }
        });
    };

    PhyloGeneticTree.prototype.resetHighlighting = function() {
        window.tree.modify_selection (function (d) { return false;});
        d3.select("#tree_display")
          .selectAll("text")
            .style("fill", function(t) {return "#000";}, "important");
        d3.select("#tree_display")
          .selectAll("path")
          .style("stroke", function(p) {
              return "#999";
          }, "important");
    };

    PhyloGeneticTree.prototype.setHL = function() {
        console.log("Add HL");
        if ($("#legend").attr("data-open") === "false") {
            $("#legend").attr("data-open", "true");
            $("#legend").fadeIn();
        } else {
            $("#legend").attr("data-open", "false");
            $("#legend").fadeOut();
        }
    };

    PhyloGeneticTree.prototype.setLineageHighlighting = function(lineage) {
        // d3.select("#tree_display")
        //     .selectAll("line")
        //     .style("stroke", "#F00", "important")
        //     .style("stroke-width", "4px")
        //     .forEach(l => console.log(l));


        $.getJSON("/api/metadata/info/" + lineage.replace(" ", "-"), function(response) {
            var parentMap = {};
            window.parentMap = parentMap;
            var inLineage = (name) => response.tkkList.indexOf(name.replaceAll("-", "_")) != -1;
            var addSelection = (id) => (parentMap[id] = !parentMap[id] ? 1 : parentMap[id] + 1);
            window.links
                  .filter(link => link.source.name !== "" || link.target.name !== "")
                  .filter(link => inLineage(link.source.name) || inLineage(link.target.name))
                  .forEach(leaf => {
                      addSelection(leaf.source.id);
                      d3.select("#tree_display")
                        .selectAll("path")
                        .filter(path => path.existing_path === leaf.existing_path)
                        .style("stroke", phyloTree.LINEAGE_COLORS[lineage], "important");
                  });
            for (var i = 0; i < 8; i++) {
                Object.keys(parentMap)
                      .filter(k => parentMap[k] !== undefined && parentMap[k] >= 2)
            .map(k => window.links.find(link => link.target.id == k))
                      .filter(x => x !== undefined)
                      .forEach((x, i) => {
                          addSelection(x.source.id);
                          d3.select("#tree_display")
                            .selectAll("path")
                            .filter(path => path.existing_path === x.existing_path)
                            .style("stroke", phyloTree.LINEAGE_COLORS[lineage], "important");
                      });
            }
        });
    };


    /**

        Initialisation

    **/

    var phyloTree = new PhyloGeneticTree();
    window.graphHandler = new GraphHandler();
    var serverConnection = new ServerConnection();
    var cookieHandler = new CookieHandler();

    $(window).resize(function() {
        updateBounds();
    });

    var updateBounds = function() {
        var h = $(document).height() - $("#nav").height();
        $("#d3").css("height", h);
    };
    updateBounds();
    //initializeData();
    window.graphHandler.loadSettings();
    $("#tree").css("z-index", "0");
    $("#d3").css("z-index", "1");
    $("#options").css("z-index", "2");



    $("#optionsgraph > ul > li > a").on("click", function(e)  {
            var currentAttrValue = jQuery(this).attr('href');
            console.log(currentAttrValue);
            // Show/Hide Tabs
            $(currentAttrValue).slideDown(400).siblings().slideUp(400);

            // Change/remove current tab to active
            jQuery(this).parent('li').addClass('active').siblings().removeClass('active');

            e.preventDefault();
        });
}); // on dom ready
