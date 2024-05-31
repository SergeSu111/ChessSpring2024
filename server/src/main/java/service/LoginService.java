package service;

import dataaccess.*;
import httprequest.LoginRequest;
import httpresponse.LoginResponse;
import model.UserData;

public class LoginService
{
    private final sqlUser userDB = new sqlUser();
    private final sqlAuth  authDB = new sqlAuth();

    public LoginService() throws DataAccessException {
    }

    public LoginResponse login(LoginRequest loginRequest) throws DataAccessException, ServerException {
        UserData userData = userDB.getUser(loginRequest.username());
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
            String authToken = authDB.createAuth(userData.username());
            return new LoginResponse(userData.username(), authToken);
        }
    }

}
