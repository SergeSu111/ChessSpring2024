package server;

import Handlers.loginHandler;
import Handlers.logoutHandler;
import Handlers.registerHandler;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new registerHandler(req, res).httpHandlerRequest(req, res));
        Spark.post("/session", (req, res) -> new loginHandler(req, res).httpHandlerRequest(req, res));
        Spark.delete("/session", (req, res) -> new logoutHandler(req, res).httpHandlerRequest(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
