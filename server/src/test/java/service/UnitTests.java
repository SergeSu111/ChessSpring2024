package service;

import HttpRequest.LoginRequest;
import HttpRequest.RegisterRequest;
import HttpResponse.LoginResponse;
import HttpResponse.RegisterResponse;
import dataaccess.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


public class UnitTests
{

    private final RegisterRequest registerRequestTest = new RegisterRequest("Serge", "Serge666", "sjh666@byu.edu");

    private final LoginRequest loginRequest = new LoginRequest("Serge", "Serge666");
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private final MemoryUserDAO userDAO = new MemoryUserDAO();

    private final MemoryGameDAO gameDAO = new MemoryGameDAO();

    private final registerService registerServiceTest = new registerService();
    private final loginService loginServiceTest = new loginService();


    @Test
    @Order(1)
    public void clear() throws ServerException, DataAccessException {
        clearService clearServiceTest = new clearService();
        assertDoesNotThrow(clearServiceTest::clear);
    }

    @Test
    @Order(2)
    public void registerSuccess() throws ServerException, ClientException, DataAccessException {

        RegisterResponse registerResponseTest = registerServiceTest.register(registerRequestTest);
        String username = registerResponseTest.username();

        assertEquals(registerResponseTest.username(), username);
    }

    @Test
    @Order(3)
    public void registerFailed() throws ServerException, ClientException, DataAccessException {
        RegisterRequest missingPassword = new RegisterRequest("lala", null, "sjh66@byu.edu");
        ClientException clientException = assertThrows(ClientException.class, () ->  registerServiceTest.register(missingPassword));
        assertEquals(clientException.getMessage(), "Error: bad request");
    }

    @Test
    @Order(4)
    public void loginSuccess() throws ServerException, DataAccessException, ClientException {
       RegisterResponse registerResponseTest = registerServiceTest.register(new RegisterRequest("Su", "123", "Junhao.S@Outlook.com"));
       LoginResponse loginResponse = loginServiceTest.login(new LoginRequest("Su", "123"));
       String username = registerResponseTest.username();
       assertEquals(username, loginResponse.username());
    }




}
