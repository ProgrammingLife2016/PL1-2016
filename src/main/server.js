var http = require("http");
var express = require("express");
var url = require("url");
var ejs = require("ejs");
var fs = require('fs');

//Initialize server
var app = express();

app.get("/nodes", function(req, res) {
    res.json({"nodes": [{ "data": {"id": '1', "name": 'A', "selected": true}}, {"data": {"id": '2', "name": 'G'}}],
              "edges": [{ "data": { "source": '1', "target": '2', "group": 'pi' } }]});
});

app.use(express.static(__dirname + "/webapp"));

app.get("/api", function(req, res) {
    var obj = JSON.parse(fs.readFileSync('test_data.json', 'utf8'));
    res.json(obj);
});

http.createServer(app).listen(8000);