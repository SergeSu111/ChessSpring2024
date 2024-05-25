package service;

import HttpRequest.LoginRequest;
import HttpResponse.LoginResponse;
import Model.UserData;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.ServerException;

public class loginService
{
    private final MemoryUserDAO memoryUser = new MemoryUserDAO();
    private final MemoryAuthDAO memoryAuth = new MemoryAuthDAO();

    public LoginResponse login(LoginRequest loginRequest) throws DataAccessException, ServerException {
        UserData userData = memoryUser.getUser(loginRequest.username());
        if (userData == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        if (loginRequest.password() == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        if (!loginRequest.password().equals(userData.password()))
        {
            throw new DataAccessException("Error: unauthorized");
        }
        else
        {
            String authToken = memoryAuth.createAuth(userData.username());
            return new LoginResponse(userData.username(), authToken);
        }
    }

}
