package de.jbuch.play.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.streams.Pump;
import org.vertx.java.deploy.Verticle;

/**
 * Echo back everything received by TCP.
 */
public class EchoServer extends Verticle {

    public void start() {
        vertx.createNetServer().connectHandler(new Handler<NetSocket>() {
            public void handle(final NetSocket socket) {
                Pump.createPump(socket, socket).start();
            }
        }).listen(1234);
    }
}