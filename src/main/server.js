var http = require("http");
var express = require("express");
var url = require("url");
var ejs = require("ejs");

//Initialize server
var app = express();

app.get("/nodes", function(req, res) {
    res.json({"nodes": [{ "data": {"id": '1', "name": 'A', "selected": true}}, {"data": {"id": '2', "name": 'G'}}],
              "edges": [{ "data": { "source": '1', "target": '2', "group": 'pi' } }]});
});

app.use(express.static(__dirname + "/webapp"));

http.createServer(app).listen(8000);
