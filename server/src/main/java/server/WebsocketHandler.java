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
import websocket.commands.websocketRequests.Leave;
import websocket.messages.ServerMessage;
import websocket.messages.websocketResponse.ErrorWebsocket;
import websocket.messages.websocketResponse.LoadGame;
import websocket.messages.websocketResponse.Notification;

import java.io.IOException;
import java.security.Key;
import java.util.Objects;
import java.util.Vector;

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

    public static void SendingErrorMessage(String authToken, ErrorWebsocket error, int gameID) throws IOException {
       Vector<Connection> smallGame = MyConnectionManager.connections.get(gameID);
        Vector<Connection> removeList = new Vector<>();
       for (Connection connection : smallGame)
       {
           if (connection.session.isOpen())
           {
               if (connection.authToken.equals(authToken))
               {
                   connection.send(error.getErrorMessage());
               }
           }
           else
           {
               removeList.add(connection);
           }
       }

       for (var connection : removeList)
       {
          smallGame.remove(connection);
       }
    }


    public static void SendingLoadGame(String authToken, LoadGame loadGame, int gameID) throws IOException {
        Vector<Connection> smallGame = MyConnectionManager.connections.get(gameID);
        Vector<Connection> removeList = new Vector<>();
        for (Connection connection : smallGame)
        {
            if (connection.session.isOpen())
            {
                if (connection.authToken.equals(authToken)) // only send to myself
                {
                    connection.send(loadGame.toString()); // toString or toJson?
                }
            }
            else
            {
                removeList.add(connection);
            }
        }
        for (var connection : removeList)
        {
            smallGame.remove(connection);
            // DO I need to put the smallGame into the connections again?
        }
    }
    public static void ObserveOrJoin(UserGameCommand userGameCommand, Session session)
    {
        Gson gson = new Gson();
        ConnectPlayer connectPlayer = gson.fromJson(String.valueOf(userGameCommand), ConnectPlayer.class);
        try
        {
            String authToken = connectPlayer.getAuthString();
            SQLAuth sqlAuth =  new SQLAuth();
            SQLGame sqlGame = new SQLGame();
            int gameID = connectPlayer.getGameID();
            String username = sqlAuth.getAuth(authToken);
            connectionManager.add(authToken ,session, gameID);
            if (username == null) // unauthorized
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR, "Unauthorized.");
                SendingErrorMessage(authToken, error, gameID);
            }
            GameData game = sqlGame.getGame(gameID);
            if (game == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR, "Game is not existed.");
                SendingErrorMessage(authToken, error, gameID);
            }
            if (game != null && username != null)
            {
                if (username.equals(game.whiteUsername())) // white color
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                    String message = notification.notificationJoinObserve(); // get the message of join game
                    connectionManager.broadcast(gameID, authToken, message); // send to everyone else
                    ChessGame gameCurrent = game.game(); // just get the game already set up
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID); // send the loading game to client
                }
                else if (username.equals(game.blackUsername())) // black color
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                    String message = notification.notificationJoinObserve();
                    connectionManager.broadcast(gameID, authToken, message);
                    ChessGame gameCurrent = game.game(); // Do I need to set the ChessGame to be black perspective?
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID);
                }
                else // observe
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, null);
                    String message = notification.notificationJoinObserve(); // give me the observe part's message
                    connectionManager.broadcast(gameID,authToken, message);
                    ChessGame gameCurrent = game.game();
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID);
                }
            }

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void leave(UserGameCommand userGameCommand, Session session)
    {
        Gson gson = new Gson();
        Leave leave = gson.fromJson(String.valueOf(userGameCommand), Leave.class);
        try
        {
            String authToken = leave.getAuthString();
            int gameID = leave.getGameID();
            SQLAuth sqlAuth =  new SQLAuth();
            SQLGame sqlGame = new SQLGame();
            String username = sqlAuth.getAuth(authToken);
            if (username == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR, "Unauthorized.");
                SendingErrorMessage(authToken, error, gameID);
            }
            else
            {
                GameData gameCurrent = sqlGame.getGame(gameID);

                if (username.equals(gameCurrent.whiteUsername()))
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                    connectionManager.broadcast(gameID, authToken, notification.notificationForLeaving());
                    connectionManager.remove(gameID, authToken);
                    // Do I need to remove the user from database?
                }
                else if (username.equals(gameCurrent.blackUsername()))
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                    connectionManager.broadcast(gameID, authToken, notification.notificationForLeaving());
                    connectionManager.remove(gameID, authToken);
                    // Do I need to remove the user from database?
                }
                else // means the user who wants to leave is observer?
                {

                }
            }



        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void MovePiece(UserGameCommand userGameCommand, Session session)
    {}

    public static void resign(UserGameCommand userGameCommand, Session session)
    {
    }




}
