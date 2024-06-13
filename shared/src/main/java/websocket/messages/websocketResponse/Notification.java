package websocket.messages.websocketResponse;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class Notification extends ServerMessage {

    private String username;

    private ChessGame.TeamColor joinedColor;

    public Notification(ServerMessageType type, String username, ChessGame.TeamColor joinedColor) {
        super(type);
        this.username = username;
        this.joinedColor = joinedColor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String notificationJoinObserve()
    {
        if (joinedColor == null) // means observe
        {
            return this.username + " is observing the game.";
        }
        return this.username + " is joining the game as " + joinedColor + ".";
    }

    public String notificationForLeaving()
    {
        return this.username + " is leaving the game.";
    }
}
