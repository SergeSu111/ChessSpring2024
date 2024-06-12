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

import javax.management.Notification;
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
            case UserGameCommand.CommandType.LEAVE -> observeGame();
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece();
            case UserGameCommand.CommandType.RESIGN -> LeaveGame();
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
            if (username != null) // authorized
            {
                if (connectPlayer.getJoinedColor() != null)
                {
                    if (sqlGame.getGame(connectPlayer.getJoinedColor(), connectPlayer.getGameID()) != null) // get the game
                    {
                        GameData targetGame = sqlGame.getGame(connectPlayer.getJoinedColor(), connectPlayer.getGameID());
                        if (targetGame.blackUsername() == null && connectPlayer.getJoinedColor() == ChessGame.TeamColor.BLACK)
                        {
                            Notification notification = new Notification()
                        }
                    }
                }
                else // observe Game
                {

                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }


}
