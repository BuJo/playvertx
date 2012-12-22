package de.jbuch.play.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
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


        JsonObject config = new JsonObject().putString("prefix", "/eventbus");

        /*
        JsonArray inboundPermitted = new JsonArray();

        // Let through any messages sent to 'demo.orderMgr'
        JsonObject inboundPermitted1 = new JsonObject().putString("address", "demo.orderMgr");
        inboundPermitted.add(inboundPermitted1);

        // Allow calls to the address 'demo.persistor' as long as the messages
        // have an action field with value 'find' and a collection field with value
        // 'albums'
        JsonObject inboundPermitted2 = new JsonObject().putString("address", "demo.persistor")
                .putObject("match", new JsonObject().putString("action", "find")
                        .putString("collection", "albums"));
        inboundPermitted.add(inboundPermitted2);

        // Allow through any message with a field `wibble` with value `foo`.
        JsonObject inboundPermitted3 = new JsonObject().putObject("match", new JsonObject().putString("wibble", "foo"));
        inboundPermitted.add(inboundPermitted3);

        JsonArray outboundPermitted = new JsonArray();

        // Let through any messages coming from address 'ticker.mystock'
        JsonObject outboundPermitted1 = new JsonObject().putString("address", "ticker.mystock");
        outboundPermitted.add(outboundPermitted1);

        // Let through any messages from addresses starting with "news." (e.g. news.europe, news.usa, etc)
        JsonObject outboundPermitted2 = new JsonObject().putString("address_re", "news\\..+");
        outboundPermitted.add(outboundPermitted2);
*/
        JsonArray inboundPermitted = new JsonArray();
        inboundPermitted.add(new JsonObject());

        JsonArray outboundPermitted = inboundPermitted;

        vertx.createSockJSServer(server).bridge(config, inboundPermitted, outboundPermitted);

        server.listen(8081);
    }
}
