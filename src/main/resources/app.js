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
    allowOutgoing: [{adress_re: "tcp\\..+"}, {address_re: "db.changeevent\\..+"}]
};
var tickerConf = {
    tick: 2000
};
var persistorConf = {
    address  : "de.jbuch.jdbcpersistor",

    // JDBC connection settings
    driver   : "org.postgresql.Driver",
    url      : "jdbc:postgresql:playvertx",
    username : "playvertx",
    password : "playvertx",

    // Pool settings
    minpool  : 5,
    maxpool  : 20,
    acquire  : 5,

    // Defaults
    batchtimeout       : 5000,
    transactiontimeout : 10000
};

vertx.deployModule('vertx.web-server-v1.0', webServerConf);
vertx.deployModule('com.bloidonia.jdbc-persistor-v1.1', persistorConf);

vertx.deployVerticle('de.jbuch.play.vertx.EchoServer', null, 1, function() {});
vertx.deployVerticle('de.jbuch.play.vertx.NetServer', netServerConf, 1, function() {});
vertx.deployVerticle('de.jbuch.play.vertx.TickerServer', tickerConf, 1, function() {});
vertx.deployVerticle('de.jbuch.play.vertx.sql.DbEventServer', tickerConf, 1, function() {});
