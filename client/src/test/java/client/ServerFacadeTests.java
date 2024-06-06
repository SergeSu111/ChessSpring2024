package client;

import httpresponse.MessageResponse;
import httpresponse.RegisterResponse;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        ServerFacade.clear();
    }

    @BeforeEach
    public void clear() throws IOException {
        ServerFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    public void registerSuccess() throws IOException {
        RegisterResponse registerResponse = (RegisterResponse) ServerFacade.register("Serge", "sergePassword", "sjh666@byu.edu");
        assertNotEquals(null, registerResponse.authToken());
    }

    @Test
    @Order(2)
    public void registerFailed() throws IOException {
        MessageResponse messageResponse = (MessageResponse) ServerFacade.register("Serge", null, "sjh666@byu.edu");
        assertEquals("Error: bad request", messageResponse.message());
    }

    @Test
    @Order(3)
    public void loginSuccess() throws IOException {
        RegisterResponse registerResponse = (RegisterResponse) ServerFacade.register("Serge", "sergePassword", "sjh666@byu.edu");
        assertDoesNotThrow(() -> ServerFacade.login("Serge", "sergePassword"));
    }

    @Test
    @Order(4)
    public void loginFailed() throws IOException {
        RegisterResponse registerResponse = (RegisterResponse) ServerFacade.register("Serge", "sergePassword", "sjh666@byu.edu");
        MessageResponse messageResponse = (MessageResponse) ServerFacade.login("Serge", null);
        assertEquals("Error: unauthorized", messageResponse.message());
    }

}
