package service;

import HttpRequest.RegisterRequest;
import HttpResponse.RegisterResponse;
import Model.UserData;
import dataaccess.*;

public class registerService
{
    private final MemoryUserDAO memoryUser = new MemoryUserDAO();
    private final MemoryAuthDAO memoryAuth = new MemoryAuthDAO();
    public  RegisterResponse register(RegisterRequest registerRequest) throws DataAccessException, ClientException, ServerException{
        UserData userData = memoryUser.getUser(registerRequest.username());
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
            memoryUser.createUser(createdData);
            // create Auth for the user
            String returnedAuthToken = memoryAuth.createAuth(registerRequest.username());

            // get the RegisterResponse by the returned authToken and return to handler
            return new RegisterResponse(registerRequest.username(), returnedAuthToken);
        }

    }
}
