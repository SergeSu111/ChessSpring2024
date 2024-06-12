package server;

import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MyConnectionManager
{
    // big websocket space. ConcurrentHashMap is better than hashmap
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session)
    {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken)
    {
        connections.remove(authToken);
    }

    public void broadcast(String senderAuthToken, String notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values())
        {
            if (c.session.isOpen())
            {
                if (!c.authToken.equals(senderAuthToken)) // we need to send the message to them
                {
                    c.send(notification); // send one by one to all others
                }
            }
            else
            {
                removeList.add(c); // remove who someone is leaving like leave game quit logout.
            }
        }

        for (var c : removeList)
        {
            connections.remove(c.authToken); // remove from websocket family
        }
    }
}
