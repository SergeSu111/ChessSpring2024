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
            case UserGameCommand.CommandType.CONNECT -> ObserveOrJoin(message, session);
            case UserGameCommand.CommandType.LEAVE -> leave(message, session);
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece(userGameCommand, session);
            case UserGameCommand.CommandType.RESIGN -> resign(userGameCommand, session);
            // how about the check and checkmate?
        }
    }

    public static void SendingErrorMessage(Session session, String errorJson) throws IOException {
     Connection connection = new Connection(null, session);
     if (connection.session.isOpen())
     {
         if (connection.session.equals(session))
         {
             connection.send(errorJson);
         }
     }

//
//       for (var connection : removeList)
//       {
//          smallGame.remove(connection);
//       }
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
                    Gson gson = new Gson();
                    String loadGameJson = gson.toJson(loadGame);
                    connection.send(loadGameJson); // toString or toJson?
                }
            }
            else
            {
                removeList.add(connection);
            }
        }
//        for (var connection : removeList)
//        {
//            smallGame.remove(connection);
//            // DO I need to put the smallGame into the connections again?
//        }
    }
    public static void ObserveOrJoin(String message, Session session)
    {

        Gson gson = new Gson();
        ConnectPlayer connectPlayer = gson.fromJson(message, ConnectPlayer.class); // make it to be userGameCommand
        try
        {
            GameData game = null;
            String authToken = connectPlayer.getAuthString();
            SQLAuth sqlAuth =  new SQLAuth();
            SQLGame sqlGame = new SQLGame();
            int gameID = connectPlayer.getGameID();
            String username = sqlAuth.getAuth(authToken);
            if (username == null) // unauthorized
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Unauthorized.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }
            try
            {
                game = sqlGame.getGame(gameID);
            }
            catch (DataAccessException e)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Game is not found.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);

            }
            if (game != null && username != null)
            {
                connectionManager.add(authToken ,session, gameID);
                if (username.equals(game.whiteUsername())) // white color
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
//                    String messageBack = notification.notificationJoinObserve(); // get the message of join game
                    notification.setMessage(username + " is joining the game with white color.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, authToken, messageJson); // send to everyone else
                    ChessGame gameCurrent = game.game(); // just get the game already set up
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID); // send the loading game to client
                }
                else if (username.equals(game.blackUsername())) // black color
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
//                    String messageBack = notification.notificationJoinObserve();
                    notification.setMessage(username + " is joining the game with black color.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, authToken, messageJson);
                    ChessGame gameCurrent = game.game(); // Do I need to set the ChessGame to be black perspective?
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID);
                }
                else // observe
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, null);
//                    String messageBack = notification.notificationJoinObserve(); // give me the observe part's message
                    notification.setMessage(username + " is observing the game.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID,authToken, messageJson);
                    ChessGame gameCurrent = game.game();
                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameCurrent);
                    SendingLoadGame(authToken, loadGame, gameID);
                }
            }

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void leave(String message, Session session)
    {
        Gson gson = new Gson();
        Leave leave = gson.fromJson(message, Leave.class);
        try
        {
            String authToken = leave.getAuthString();
            int gameID = leave.getGameID();
            SQLAuth sqlAuth =  new SQLAuth();
            SQLGame sqlGame = new SQLGame();
            String username = sqlAuth.getAuth(authToken);
            if (username == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Unauthorized.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }

            else
            {
                GameData gameCurrent = sqlGame.getGame(gameID);

                if (username.equals(gameCurrent.whiteUsername()))
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                    notification.setMessage(username + " is leaving the game.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, authToken, messageJson);
                    connectionManager.remove(gameID, authToken);
                    sqlGame.updateGame(null, ChessGame.TeamColor.WHITE, gameCurrent); // remove the user from game.
                }
                else if (username.equals(gameCurrent.blackUsername()))
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                    notification.setMessage(username + " is leaving the game.");
                     String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, authToken, messageJson);
                    connectionManager.remove(gameID, authToken);
                    sqlGame.updateGame(null, ChessGame.TeamColor.BLACK, gameCurrent);

                }
                else // observer
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, null);
                    notification.setMessage(username + " is leaving the game.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, authToken, messageJson);
                    connectionManager.remove(gameID, authToken);
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void MovePiece(UserGameCommand userGameCommand, Session session)
    {

    }

    public static void resign(UserGameCommand userGameCommand, Session session)
    {
    }




}
