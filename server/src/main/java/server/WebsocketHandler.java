package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.protobuf.Enum;
import dataaccess.DataAccessException;
import dataaccess.SQLAuth;
import dataaccess.SQLGame;
import dataaccess.UserDAO;
import model.GameData;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.commands.websocketRequests.ConnectPlayer;
import websocket.messages.ServerMessage;
import websocket.messages.websocketResponse.ErrorWebsocket;
import websocket.messages.websocketResponse.Notification;

import java.io.IOException;
import java.security.Key;

@WebSocket
public class WebsocketHandler
{
    // websocket back-end. Receiving messages from websocket facade. return the messages from it to send back to websocketFacade.
    private static final MyConnectionManager connectionManager = new MyConnectionManager();

    public enum KeyItems {join, observe, move, leave, resign, check, checkmate}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) // the message is just a websocketRequest, just make it as json to pass in.
    {
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class); // make it to be userGameCommand

        switch (userGameCommand.getCommandType())
        {
            case UserGameCommand.CommandType.CONNECT -> ObserveOrJoin(userGameCommand, session);
            case UserGameCommand.CommandType.LEAVE -> leave(userGameCommand, session);
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece(userGameCommand, session);
            case UserGameCommand.CommandType.RESIGN -> resign(userGameCommand, session);
            // how about the check and checkmate?
        }
    }

    public static void ObserveOrJoin(UserGameCommand userGameCommand, Session session)
    {
        ConnectPlayer connectPlayer = (ConnectPlayer)userGameCommand;
        try
        {
            String authToken = connectPlayer.getAuthString();
            SQLAuth sqlAuth =  new SQLAuth();
            SQLGame sqlGame = new SQLGame();
            String username = sqlAuth.getAuth(authToken);
            connectionManager.add(authToken, session);
            int gameID = connectPlayer.getGameID();
            if (username == null) // unauthorized
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR, "Unauthorized.");
                for (Connection connection : MyConnectionManager.connections.values())
                {
                    if (connection.authToken.equals(authToken))
                    {
                        connection.send(error.getErrorMessage());
                    }
                }
            }
            GameData game = sqlGame.getGame(gameID);
            if (game == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR, "Game is not existed.");
                for (Connection connection : MyConnectionManager.connections.values())
                {
                    if (connection.session.isOpen())
                    {
                        if (connection.authToken.equals(authToken))
                        {
                            connection.send(error.getErrorMessage());
                        }
                    }
                }
            }

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void leave(UserGameCommand userGameCommand, Session session)
    {

    }

    public static void MovePiece(UserGameCommand userGameCommand, Session session)
    {}

    public static void resign(UserGameCommand userGameCommand, Session session)
    {
    }




}
