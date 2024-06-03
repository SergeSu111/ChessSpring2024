package service;

import httprequest.RegisterRequest;
import httpresponse.RegisterResponse;
import model.UserData;
import dataaccess.*;

public class RegisterService
{
    private final sqlUser userDB = new sqlUser();
    private final sqlAuth authDB = new sqlAuth();

    public RegisterService() throws DataAccessException {
    }

    public  RegisterResponse register(RegisterRequest registerRequest) throws DataAccessException, ClientException, ServerException{
        sqlUser.createUserTable();
        sqlAuth.createAuthTable();
        UserData userData = userDB.getUser(registerRequest.username());
        if (userData != null)
        {
            throw new DataAccessException("Error: already taken");
        }
        if (registerRequest.password() == null)
        {
            throw new ClientException("Error: bad request");
        }
        else // else the userDate is not in db
        {
            // CREATE one
            UserData createdData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDB.createUser(createdData);
            // create Auth for the user
            String returnedAuthToken = authDB.createAuth(registerRequest.username());

            // get the RegisterResponse by the returned authToken and return to handler
            return new RegisterResponse(registerRequest.username(), returnedAuthToken);
        }

    }
}
