var fs = require("fs");
var http = require('http');
var https = require('https');
var privateKey  = fs.readFileSync('ssl/key.key', 'utf8');
var certificate = fs.readFileSync('ssl/cert.cert', 'utf8');

var credentials = {key: privateKey, cert: certificate};
var express = require('express');
var bodyParser = require('body-parser')
var app = express();


// Add headers
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'POST');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    next();
});

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post('', function (req, res) {
   // First read existing users.
   var exec = require('child_process').exec;
   var compileit = 'java -cp ../ com.calpoly.incredible.Incredible ' + req.body.msg;

   exec(compileit, function(error, stdout, stderr) {
     res.end(stdout);
   });
})

var httpServer = http.createServer(app);
var httpsServer = https.createServer(credentials, app);

httpServer.listen(8081);
httpsServer.listen(8082);