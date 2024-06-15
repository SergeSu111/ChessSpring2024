package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.protobuf.Enum;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.commands.websocketRequests.ConnectPlayer;
import websocket.commands.websocketRequests.Leave;
import websocket.commands.websocketRequests.MakeMove;
import websocket.commands.websocketRequests.Resign;
import websocket.messages.ServerMessage;
import websocket.messages.websocketResponse.ErrorWebsocket;
import websocket.messages.websocketResponse.LoadGame;
import websocket.messages.websocketResponse.Notification;

import java.io.IOException;
import java.security.Key;
import java.util.Collection;
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
            case UserGameCommand.CommandType.MAKE_MOVE -> MovePiece(message, session);
            case UserGameCommand.CommandType.RESIGN -> resign(message, session);
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

    // send to all others the loading game
    public static void SendingLoadGameToAllOthers(String authToken, LoadGame loadGame, int gameID) throws IOException {
        Vector<Connection> smallGame = MyConnectionManager.connections.get(gameID);
        for (Connection connection : smallGame)
        {
            if (connection.session.isOpen())
            {
                if (!connection.authToken.equals(authToken))
                {
                    Gson gson = new Gson();
                    String loadGameJson = gson.toJson(loadGame);
                    connection.send(loadGameJson);
                }
            }
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
                    connectionManager.broadcast(gameID, session, messageJson); // send to everyone else
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
                    connectionManager.broadcast(gameID, session, messageJson);
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
                    connectionManager.broadcast(gameID,session, messageJson);
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
                    connectionManager.broadcast(gameID, session, messageJson);
                    connectionManager.remove(gameID, authToken);
                    sqlGame.updateGame(null, ChessGame.TeamColor.WHITE, gameCurrent); // remove the user from game.
                }
                else if (username.equals(gameCurrent.blackUsername()))
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                    notification.setMessage(username + " is leaving the game.");
                     String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, session, messageJson);
                    connectionManager.remove(gameID, authToken);
                    sqlGame.updateGame(null, ChessGame.TeamColor.BLACK, gameCurrent);

                }
                else // observer
                {
                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, null);
                    notification.setMessage(username + " is leaving the game.");
                    String messageJson = gson.toJson(notification);
                    connectionManager.broadcast(gameID, session, messageJson);
                    connectionManager.remove(gameID, authToken);
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void MovePiece(String message, Session session)
    {
        try
        {
            GameData gameCurrent = null;
            Gson gson = new Gson();
            SQLGame sqlGame = new SQLGame();
            SQLUser sqlUser = new SQLUser();
            SQLAuth sqlAuth = new SQLAuth();
            MakeMove makeMove = gson.fromJson(message, MakeMove.class);
            int gameID = makeMove.getGameID();
            String authToken = makeMove.getAuthString();

            String username = sqlAuth.getAuth(authToken);
            if (username == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Unauthorized.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }
            try
            {
                gameCurrent = sqlGame.getGame(gameID);
            }
            catch (DataAccessException e)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Game is not found.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }

            if ( gameCurrent != null && username != null)
            {
                ChessMove chessMove = makeMove.getChessMove(); // get the chessMove
                ChessGame chessGame = gameCurrent.game(); // get the game
                Collection<ChessMove> validMoves = chessGame.validMoves(chessMove.getStartPosition());

                if (validMoves.contains(chessMove))
                {
                    if (username.equals(gameCurrent.blackUsername()))  // black user
                    {
                        if (chessGame.turn == ChessGame.TeamColor.BLACK)
                        {
                            if (!chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) && !chessGame.isInStalemate(ChessGame.TeamColor.BLACK) && chessGame.isResigned != true)
                            {
                                chessGame.makeMove(chessMove);
//                                sqlGame.updateChessGame(chessGame, gameID, gameCurrent);
                                chessGame.turn = ChessGame.TeamColor.WHITE; // CHANGE the turn
                                if (chessGame.isInCheck(ChessGame.TeamColor.WHITE))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                                    notification.setMessage(gameCurrent.whiteUsername() + " is in check.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);
                                }

                                else if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                                    notification.setMessage(gameCurrent.whiteUsername() + " is in checkmate.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson); // send to all others.
                                }
                                // for stalemate, do we need to check for both black and white?
                                else if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                                    notification.setMessage(gameCurrent.whiteUsername() + " is in stalemate.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);
                                }

                                // normal making move
                                else
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                                    notification.setMessage(username + " is making move from " + chessMove.getStartPosition() + " to " + chessMove.getEndPosition());
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);
                                    Connection connectionMover = new Connection(authToken, session);
//                            if (connectionMover.session.isOpen())
//                            {
//                                connectionMover.send(messageJson);
//                            }
                                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
                                    SendingLoadGame(authToken, loadGame, gameID); // send the updating game to myself

                                    SendingLoadGameToAllOthers(authToken, loadGame, gameID); // send the board to others.
                                }
                            }
                            else // try to make move after game over
                            {
                                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                                error.setErrorMessage("You cannot make move after game over.");
                                String errorJson = gson.toJson(error);
                                SendingErrorMessage(session, errorJson);
                            }

                        }
                        else // not the user's turn, sending error
                        {
                            ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                            error.setErrorMessage("This is not your turn, cannot move.");
                            String errorJson = gson.toJson(error);
                            SendingErrorMessage(session, errorJson);
                        }

                    }
                    else if (username.equals(gameCurrent.whiteUsername())) // white user
                    {
                        if (chessGame.turn == ChessGame.TeamColor.WHITE)
                        {
                            if (!chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) && !chessGame.isInStalemate(ChessGame.TeamColor.WHITE) && chessGame.isResigned != true)
                            {
                                chessGame.makeMove(chessMove);
//                                sqlGame.updateChessGame(chessGame, gameID, gameCurrent); // update the chessGame in db
                                chessGame.turn = ChessGame.TeamColor.BLACK;
                                if (chessGame.isInCheck(ChessGame.TeamColor.BLACK))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                                    notification.setMessage(gameCurrent.blackUsername() + " is in check.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);
                                }

                                else if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                                    notification.setMessage(gameCurrent.blackUsername() + " is in checkmate.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);

                                }

                                else if (chessGame.isInStalemate(ChessGame.TeamColor.BLACK))
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                                    notification.setMessage(gameCurrent.blackUsername() + " is in stalemate.");
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);

                                }
                                else
                                {
                                    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                                    notification.setMessage(username + " is making move from " + chessMove.getStartPosition() + " to " + chessMove.getEndPosition());
                                    String messageJson = gson.toJson(notification);
                                    connectionManager.broadcast(gameID, session, messageJson);
                                    Connection connectionMover = new Connection(authToken, session);
//                            if (connectionMover.session.isOpen())
//                            {
//                                connectionMover.send(messageJson);
//                            }
                                    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
                                    SendingLoadGame(authToken, loadGame, gameID); // send the updating game
                                    SendingLoadGameToAllOthers(authToken, loadGame , gameID); // send to others
                                }
                            }
                            else
                            {
                                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                                error.setErrorMessage("You cannot make move after game over.");
                                String errorJson = gson.toJson(error);
                                SendingErrorMessage(session, errorJson);
                            }
                        }
                        else
                        {
                            ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                            error.setErrorMessage("This is not your turn, cannot move.");
                            String errorJson = gson.toJson(error);
                            SendingErrorMessage(session, errorJson);
                        }

                    }
                    else // observer
                    {
                        ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("Observer should not make move.");
                        String errorJson = gson.toJson(error);
                        SendingErrorMessage(session, errorJson);
                    }

                }
                else // not valid move
                {
                    ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                    error.setErrorMessage("The move is not valid");
                    String errorJson = gson.toJson(error);
                    SendingErrorMessage(session, errorJson);
                }
            }

        } catch (DataAccessException | IOException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resign(String message, Session session) {
        try
        {
            GameData gameData = null;
            ChessGame chessGame = null;
            Gson gson = new Gson();
            Resign resign = gson.fromJson(message, Resign.class);
            int gameID = resign.getGameID();
            String authToken = resign.getAuthString();
            SQLGame sqlGame = new SQLGame();
            SQLUser sqlUser = new SQLUser();
            SQLAuth sqlAuth = new SQLAuth();
            String username = sqlAuth.getAuth(authToken);
            if (username == null)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Unauthorized.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }
            try
            {
                gameData = sqlGame.getGame(gameID);
                chessGame = gameData.game();
            }
            catch (DataAccessException e)
            {
                ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Game is not found.");
                String errorJson = gson.toJson(error);
                SendingErrorMessage(session, errorJson);
            }
            if (username != null && gameData != null)
            {
                if (username.equals(gameData.whiteUsername()))
                {
                    if (chessGame.isResigned != true)
                    {
                        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.WHITE);
                        notification.setMessage(username + " resigns the game.");
                        String messageJson = gson.toJson(notification);
                        connectionManager.broadcast(gameID, session, messageJson); // send to everyone else
                        Connection ResignMaker = new Connection(authToken, session);
                        if (ResignMaker.session.isOpen()) // and send to myself
                        {
                            ResignMaker.send(messageJson);
                        }

                        chessGame.isResigned = true;
                        sqlGame.updateChessGame(chessGame, gameID, gameData);
                    }
                    else
                    {
                        ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("You cannot resign after one player already resigned.");
                        String errorJson = gson.toJson(error);
                        SendingErrorMessage(session, errorJson);
                    }

                }
                else if (username.equals(gameData.blackUsername()))
                {
                    if (chessGame.isResigned != true)
                    {
                        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username, ChessGame.TeamColor.BLACK);
                        notification.setMessage(username + " resigns the game.");
                        String messageJson = gson.toJson(notification);
                        connectionManager.broadcast(gameID, session, messageJson);
                        Connection ResignMaker = new Connection(authToken, session);
                        if (ResignMaker.session.isOpen())
                        {
                            ResignMaker.send(messageJson);
                        }

                        chessGame.isResigned = true;
//                        sqlGame.updateChessGame(chessGame, gameID, gameData);
                    }
                    else
                    {
                        ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("You cannot resign after one player already resigned.");
                        String errorJson = gson.toJson(error);
                        SendingErrorMessage(session, errorJson);
                    }
                }
                else // observer
                {
                    ErrorWebsocket error = new ErrorWebsocket(ServerMessage.ServerMessageType.ERROR);
                    error.setErrorMessage("Observer cannot resign.");
                    String errorJson = gson.toJson(error);
                    SendingErrorMessage(session, errorJson);
                }
            }
        }
        catch (DataAccessException | IOException e)
        {
            throw new RuntimeException(e);
        }


    }




}
