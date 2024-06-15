package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import websocket.commands.websocketRequests.ConnectPlayer;
import websocket.messages.ServerMessage;
import websocket.messages.websocketResponse.ErrorWebsocket;
import websocket.messages.websocketResponse.LoadGame;
import websocket.messages.websocketResponse.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class WebSocketFacade extends Endpoint
{
    Session session;

    NotificationHandler notificationHandler;

    private static ChessGame.TeamColor color;
    public WebSocketFacade(String url, ChessGame.TeamColor color)
    {
        this.color = color;
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>()
            {
                @Override
                public void onMessage(String message)
                {
                    // make the message send from server to be back to ServerMessage
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType())
                    {
                        case ServerMessage.ServerMessageType.NOTIFICATION -> SendingNotificationBack(message);
                        case ServerMessage.ServerMessageType.ERROR -> SendingErrorBack(message);
                        case ServerMessage.ServerMessageType.LOAD_GAME -> SendingLoadGameBack(message);
                    }
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void SendingNotificationBack(String message)
    {
        Gson gson = new Gson();
        Notification notification = gson.fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }

    private static void SendingErrorBack(String message)
    {
        Gson gson = new Gson();
        ErrorWebsocket error = gson.fromJson(message, ErrorWebsocket.class);
        System.out.println(error.getErrorMessage());
    }

    private static void SendingLoadGameBack(String message)
    {
        Gson gson = new Gson();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        LoadGame loadGame = gson.fromJson(message, LoadGame.class);
        ChessGame game = loadGame.getGame();
        ChessBoard board = game.getBoard();
        if (color == ChessGame.TeamColor.WHITE)
        {
            BoardUI.callWhiteBoard(out, board); // draw the white board to console
        }
        else
        {
            BoardUI.callBlackBoard(out, board); // draw the white board to console
        }
    }

    public void ConnectPlayer(String authToken, int gameID) throws IOException // we don need to care about color, back-end check for us
    {   Gson gson = new Gson();
        ConnectPlayer connectPlayer = new ConnectPlayer(authToken, gameID);
        String connectPlayerJson = gson.toJson(connectPlayer);
        this.session.getBasicRemote().sendText(connectPlayerJson);
    }



    public ChessGame.TeamColor getColor() {
        return color;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.color = color;

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
