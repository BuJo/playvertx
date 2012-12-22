package de.jbuch.play.vertx;

import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;


/**
 * Basic HTTP server
 */
public class NetServer extends Verticle {

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        JsonObject config = container.getConfig();

        JsonObject serverConfig = config.getObject("eventbusConfig");
        JsonArray inboundPermitted = config.getArray("allowIncoming");
        JsonArray outboundPermitted = config.getArray("allowOutgoing");

        vertx.createSockJSServer(server).bridge(serverConfig, inboundPermitted, outboundPermitted);

        server.listen(config.getInteger("port"));
    }
}
