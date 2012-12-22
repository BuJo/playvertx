package de.jbuch.play.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.SimpleHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.deploy.Verticle;

import java.util.Date;

/**
 * Echo back everything received by TCP.
 */
public class EchoServer extends Verticle {


    public void start() {
        final EventBus eb = vertx.eventBus();
        final Logger logger = container.getLogger();

        /*
        vertx.createNetServer().connectHandler(new Handler<NetSocket>() {
            public void handle(final NetSocket socket) {
                Pump.createPump(socket, socket).start();
            }
        }).listen(1234);
        */

        NetServer server = vertx.createNetServer();

        server.connectHandler(new Handler<NetSocket>() {
            public void handle(final NetSocket sock) {

                sock.dataHandler(new Handler<Buffer>() {
                    public void handle(Buffer buffer) {
                        if (!sock.writeQueueFull()) {
                            sock.write(buffer);

                            logger.info("sending message to tcp.message");

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.putString("date", new Date().toString());
                            jsonObject.putString("text", buffer.getString(0, buffer.length()));
                            eb.publish("tcp.message", jsonObject);

                        } else {
                            sock.pause();
                            sock.drainHandler(new SimpleHandler() {
                                public void handle() {
                                    sock.resume();
                                }
                            });
                        }
                    }
                });

            }
        }).listen(1234, "localhost");
    }
}
