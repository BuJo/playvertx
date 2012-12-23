# vert.x Playground

Playground for learning various aspects of vert.x.  This includes upstream
modules as well as self-created verticles.
It also includes support for testing.  It uses thucydides to be able to
nicely handle frontend tests.

## Verticles

* DbEventServer: generates db.changeevent... messages
* EchoServer: opens TCP port 1234 - generates tcp.message messages
* NetServer: opens TCP port 8081 - handles bridging the eventbus to browser
* TickerServer: generates tcp.ticker messages
* Browser: can subscribe to the mentioned eventbus addresses

## Setup

Starting vert.x correctly in all cases can be a little tricky.  When vert.x
is started by itself (`vertx run app.js`) it does not allow the classpath
to contain the application itself.  When running it in embedded mode, it
needs to be included.

    mvn clean package
    cd target/classes
    vertx run app.js

Note that the DbEventServer needs a database, take care that the jdbc
implementation can be found somewhere.

