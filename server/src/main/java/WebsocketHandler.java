import chess.ChessGame;
import com.google.gson.Gson;
import com.google.protobuf.Enum;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.security.Key;

@WebSocket
public class WebsocketHandler
{
    // websocket back-end. Receiving messages from websocket facade. return the messages from it to send back to websocketFacade.
    private String keyItem;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public enum KeyItems {join, observe, move, leave, resign, check, checkmate}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) // the message is just a websocketRequest, just make it as json to pass in.
    {
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class); // make it to be userGameCommand

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.CONNECT) // if it is connect, observe or join
        {

        }
        switch (userGameCommand.getCommandType())
        {;
            case UserGameCommand.CommandType.LEAVE -> observeGame();
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece();
            case UserGameCommand.CommandType.RESIGN -> LeaveGame();
            // how about the check and checkmate?
        }
    }

    public void joinGame(String authTokenJoiner, Session session)
    {
        connectionManager.add(authTokenJoiner, session); // add the player in to websocket

    }

}
