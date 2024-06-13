package websocket.commands.websocketRequests;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMove extends UserGameCommand {

    private int gameID;

    private ChessMove chessMove;

    public MakeMove(String authToken, int gameID, ChessMove chessMove) {
        super(authToken);
        this.gameID = gameID;
        this.chessMove = chessMove;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }

    public void setChessMove(ChessMove chessMove) {
        this.chessMove = chessMove;
    }
}
