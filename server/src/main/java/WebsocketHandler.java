import chess.ChessGame;
import com.google.gson.Gson;
import com.google.protobuf.Enum;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.security.Key;

@WebSocket
public class WebsocketHandler
{
    // websocket back-end. Receiving messages from websocket facade. return the messages from it to send back to websocketFacade.
    private Connection connection;
    private String keyItem;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public enum KeyItems {join, observe, move, leave, resign, check, checkmate}
    @OnWebSocketMessage
    public void onMessage(Session session, String message)
    {
        for (var item : KeyItems.values())
        {
            String itemJson = item.toString();
            if (message.contains(itemJson))
            {
                keyItem = itemJson;
            }
        }
        Gson gson = new Gson();
        KeyItems itemBack = gson.fromJson(keyItem, KeyItems.class);
        switch (itemBack)
        {
            case itemBack == KeyItems.join -> joinGame();
            case itemBack == KeyItems.observe -> observeGame();
            case itemBack == KeyItems.move -> MovePiece();
            case itemBack == KeyItems.leave -> LeaveGame();
            case itemBack == KeyItems.resign -> Resign();
            case itemBack == KeyItems.check -> checkGame();
            case itemBack == KeyItems.checkmate -> CheckMateGame();
        }
    }

}
