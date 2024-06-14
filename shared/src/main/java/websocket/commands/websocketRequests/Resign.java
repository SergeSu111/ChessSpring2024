package websocket.commands.websocketRequests;

import websocket.commands.UserGameCommand;

public class Resign extends UserGameCommand {

    private int gameID;
    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
