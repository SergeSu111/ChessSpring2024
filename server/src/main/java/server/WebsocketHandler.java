package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.protobuf.Enum;
import dataaccess.UserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.commands.websocketRequests.ConnectPlayer;

import java.security.Key;

@WebSocket
public class WebsocketHandler
{
    // websocket back-end. Receiving messages from websocket facade. return the messages from it to send back to websocketFacade.
    private final MyConnectionManager connectionManager = new MyConnectionManager();


    public enum KeyItems {join, observe, move, leave, resign, check, checkmate}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) // the message is just a websocketRequest, just make it as json to pass in.
    {
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class); // make it to be userGameCommand

        switch (userGameCommand.getCommandType())
        {
            case UserGameCommand.CommandType.CONNECT -> ObserveOrJoin(userGameCommand, session);
            case UserGameCommand.CommandType.LEAVE -> observeGame();
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece();
            case UserGameCommand.CommandType.RESIGN -> LeaveGame();
            // how about the check and checkmate?
        }
    }

    public static void ObserveOrJoin(UserGameCommand userGameCommand, Session session)
    {
        ConnectPlayer connectPlayer = (ConnectPlayer)userGameCommand;
        String authToken = connectPlayer.getAuthString();

    }


}
