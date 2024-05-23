package server;

import Handlers.*;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new registerHandler(req, res).httpHandlerRequest(req, res));
        Spark.post("/session", (req, res) -> new loginHandler(req, res).httpHandlerRequest(req, res));
        Spark.delete("/session", (req, res) -> new logoutHandler(req, res).httpHandlerRequest(req, res));
        Spark.get("/game", (req, res) -> new listGamesHandler(req, res).httpHandlerRequest(req, res));
        Spark.post("/game", (req, res) -> new createGameHandler(req, res).httpHandlerRequest(req, res));
        Spark.put("/game", (req, res) -> new joinGameHandler(req, res).httpHandlerRequest(req, res));
        Spark.delete("/db", (req, res) -> new clearHandler(req, res).httpHandlerRequest(req, res));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
