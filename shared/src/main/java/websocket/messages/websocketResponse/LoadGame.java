package websocket.messages.websocketResponse;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class LoadGame extends ServerMessage {

    private ChessGame game;
    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
