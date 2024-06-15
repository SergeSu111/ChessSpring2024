package ui;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;
import websocket.messages.websocketResponse.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint
{
    Session session;

    NotificationHandler notificationHandler;

    public WebSocketFacade(String url)
    {
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
                        case ServerMessage.ServerMessageType.NOTIFICATION -> SendingNotification(message);
                        case ServerMessage.ServerMessageType.ERROR ->
                        case ServerMessage.ServerMessageType.LOAD_GAME ->
                    }
                }

            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void SendingNotification(String message)
    {
        Gson gson = new Gson();
        Notification notification = gson.fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
