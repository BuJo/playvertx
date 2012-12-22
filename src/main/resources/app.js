load('vertx.js');

var webServerConf = {
    port: 8080,
    host: 'localhost'
};
var netServerConf = {
    port: 8081,
    host: "localhost",

    eventbusConfig: {prefix: "/eventbus"},
    allowIncoming: [],
    allowOutgoing: [{adress_re: "tcp\\..+"}]
};

vertx.deployModule('vertx.web-server-v1.0', webServerConf);

vertx.deployVerticle('de.jbuch.play.vertx.EchoServer', null, 1, function() {});
vertx.deployVerticle('de.jbuch.play.vertx.NetServer', netServerConf, 1, function() {});
vertx.deployVerticle('de.jbuch.play.vertx.TickerServer', null, 1, function() {});
