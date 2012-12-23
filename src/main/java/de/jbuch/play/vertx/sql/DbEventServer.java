package de.jbuch.play.vertx.sql;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.deploy.Verticle;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Pull and handle events from DB;
 */
public class DbEventServer extends Verticle {

    private EventBus eb;
    private Logger logger;

    @Override
    public void start() throws Exception {
        eb = vertx.eventBus();
        logger = container.getLogger();

        vertx.setPeriodic(2000, new Handler<Long>() {
            @Override
            public void handle(Long event) {
                requestUnhandledEvents();
            }
        });

        // testing
        vertx.setPeriodic(2000, new Handler<Long>() {
            @Override
            public void handle(Long event) {
                addRandomUnhandledEvents();
            }
        });
    }

    /**
     * Random event generator
     */
    private void addRandomUnhandledEvents() {
        int nrEvents = (int) (Math.random() * 10 + 1);
        for (int i = 0; i < nrEvents; i++) {
            int id = (int) (Math.random() * 10000 + 1);

            JsonArray arguments = new JsonArray();
            arguments.addString("objtable");
            arguments.addNumber(id);

            insertDb("insert into testevent (tablename, tableid) values (?, ?)", arguments, null);
        }
    }

    /**
     * Get unhandled events set them to inprocess and distribute them.
     */
    private void requestUnhandledEvents() {
        selectFromDb("select * from testevent where state is null", new Handler<Message<JsonObject>>() {

            @Override
            public void handle(Message<JsonObject> event) {
                if ("ok".equals(event.body.getString("status"))) {
                    JsonArray results = event.body.getArray("result");

                    logger.info("Got " + results.size() + " unhandled events.");

                    for (Object result : results) {
                        JsonObject resultobj = (JsonObject) result;

                        final String tablename = resultobj.getString("tablename");

                        final JsonObject eventobj = new JsonObject();

                        eventobj.putString("type",tablename);
                        eventobj.putNumber("id", resultobj.getInteger("tableid"));
                        eventobj.putString("creationdate", new Date(resultobj.getNumber("creationdate").longValue()).toString());

                        logger.info("change event for " + tablename + " id: " + resultobj.getInteger("tableid"));

                        setEventToInflight(resultobj.getInteger("id"), new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> event) {
                                if ("ok".equals(event.body.getString("status"))) {
                                    eb.publish("db.changeevent."+tablename, eventobj);
                                } else {
                                    logger.error("could not get dbevent: " + event.body.getString("message"));
                                }
                            }
                        });
                    }

                } else {
                    logger.error("could not get dbevent: " + event.body.getString("message"));
                }
            }
        });
    }

    private void setEventToInflight(Integer eventid, Handler<Message<JsonObject>> handler) {
        JsonArray arguments = new JsonArray();
        arguments.addNumber(eventid);

        updateDb("update testevent set state = 'i' where id = ?", arguments, handler);
    }

    private void selectFromDb(String statement, Handler<Message<JsonObject>> handler) {
        selectFromDb(statement, null, handler);
    }

    private void selectFromDb(String statement, JsonArray arguments, Handler<Message<JsonObject>> handler) {
        dbRequest("select", statement, arguments, handler);
    }

    private void updateDb(String statement, JsonArray arguments, Handler<Message<JsonObject>> handler) {
        dbRequest("update", statement, arguments, handler);
    }

    private void insertDb(String statement,JsonArray arguments, Handler<Message<JsonObject>> handler) {
        dbRequest("insert", statement, arguments, handler);
    }

    private void dbRequest(String action, String statement, JsonArray arguments, Handler<Message<JsonObject>> handler) {
        dbRequest(action, statement, arguments != null ? Arrays.asList(arguments) : null, handler);
    }

    private void dbRequest(String action, String statement, List<JsonArray> arguments, Handler<Message<JsonObject>> handler) {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.putString("action", action);
        jsonRequest.putString("stmt", statement);

        if (arguments != null && arguments.size() > 0) {
            JsonArray array = new JsonArray();
            for (JsonArray ary : arguments) {
                array.addArray(ary);
            }

            jsonRequest.putArray("values", array);
        }

        logger.info("request json: " + jsonRequest.toString());

        eb.send("de.jbuch.jdbcpersistor", jsonRequest, handler);
    }

}
