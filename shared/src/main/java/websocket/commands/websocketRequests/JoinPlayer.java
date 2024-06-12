package websocket.commands.websocketRequests;

import chess.ChessGame;
import websocket.commands.UserGameCommand;

public class JoinPlayer extends UserGameCommand {

    private int gameID;

    private ChessGame.TeamColor joinedColor;
    public JoinPlayer(String authToken, ChessGame.TeamColor joinedColor, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.joinedColor = joinedColor;
    }




}
