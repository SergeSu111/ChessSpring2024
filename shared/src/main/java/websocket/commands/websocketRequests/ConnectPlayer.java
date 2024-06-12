package websocket.commands.websocketRequests;

import chess.ChessGame;
import websocket.commands.UserGameCommand;

public class ConnectPlayer extends UserGameCommand {

    private int gameID;

    private ChessGame.TeamColor joinedColor;

    public ConnectPlayer(String authToken, int gameID, ChessGame.TeamColor joinedColor) {
        super(authToken);
        this.gameID = gameID;
        this.joinedColor = joinedColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getJoinedColor() {
        return joinedColor;
    }

    public void setJoinedColor(ChessGame.TeamColor joinedColor) {
        this.joinedColor = joinedColor;
    }
}
