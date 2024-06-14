package websocket.commands.websocketRequests;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMove extends UserGameCommand {

    private int gameID;

    private ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ChessMove getChessMove() {
        return move;
    }

    public void setChessMove(ChessMove chessMove) {
        this.move = move;
    }
}