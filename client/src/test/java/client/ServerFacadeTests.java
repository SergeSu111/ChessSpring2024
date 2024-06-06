package client;

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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    public void registerSuccess() throws IOException {
        RegisterResponse registerResponse = (RegisterResponse) serverFacade.register("Serge", "sergePassword", "sjh666@byu.edu");
        assertNotEquals(null, registerResponse.authToken());
    }

    @Test
    @Order(2)
    public void registerFailed() throws IOException {
        var registerResponse = serverFacade.register(null, "sergePassword", "sjh666@byu.edu");


    }




}
