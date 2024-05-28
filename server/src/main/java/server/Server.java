package server;

import handlers.*;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegisterHandler(req, res).httpHandlerRequest(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler(req, res).httpHandlerRequest(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler(req, res).httpHandlerRequest(req, res));
        Spark.get("/game", (req, res) -> new ListGamesHandler(req, res).httpHandlerRequest(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler(req, res).httpHandlerRequest(req, res));
        Spark.put("/game", (req, res) -> new JoinGameHandler(req, res).httpHandlerRequest(req, res));
        Spark.delete("/db", (req, res) -> new ClearHandler(req, res).httpHandlerRequest(req, res));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
