package websocket.commands.websocketRequests;

import chess.ChessGame;
import websocket.commands.UserGameCommand;

public class ConnectPlayer extends UserGameCommand {

    private int gameID;

    public ConnectPlayer(String authToken, int gameID) {
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
