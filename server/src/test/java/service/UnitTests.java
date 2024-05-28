package service;

import Handlers.clearHandler;
import Handlers.loginHandler;
import Handlers.logoutHandler;
import Handlers.registerHandler;
import HttpRequest.RegisterRequest;
import HttpResponse.RegisterResponse;
import Model.UserData;
import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.asserts.Assertion;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTests
{

    private final RegisterRequest registerRequestTest = new RegisterRequest("Serge", "Serge666", "sjh666@byu.edu");
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private final MemoryUserDAO userDAO = new MemoryUserDAO();

    private final MemoryGameDAO gameDAO = new MemoryGameDAO();

    private final registerService registerServiceTest = new registerService();
    @Test
    public void clear() throws ServerException, DataAccessException {
        clearService clearServiceTest = new clearService();
        assertDoesNotThrow(clearServiceTest::clear);
    }

    @Test
    public void registerSuccess() throws ServerException, ClientException, DataAccessException {



        RegisterResponse registerResponseTest = registerServiceTest.register(registerRequestTest);
        String username = registerResponseTest.username();

        assertEquals(registerResponseTest.username(), username);
    }

    @Test
    public void registerFailed() throws ServerException, ClientException, DataAccessException {
        RegisterResponse registerResponseTest = registerServiceTest.register(registerRequestTest);
        assertEquals(registerResponseTest, "Error: already taken");

    }


}
