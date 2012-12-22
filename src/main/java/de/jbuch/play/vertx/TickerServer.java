package de.jbuch.play.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

import java.util.Date;


/**
 * Ticks
 */
public class TickerServer extends Verticle {

    @Override
    public void start() throws Exception {
        final EventBus eb = vertx.eventBus();

        vertx.setPeriodic(5000, new Handler<Long>() {

            @Override
            public void handle(Long event) {
                eb.publish("tcp.ticker", new JsonObject().putString("date", new Date().toString()));
            }
        });
    }
}
