package service;

import HttpRequest.RegisterRequest;
import HttpResponse.RegisterResponse;
import Model.UserData;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

public class registerService
{
    MemoryUserDAO memoryUser = new MemoryUserDAO();
    MemoryAuthDAO memoryAuth = new MemoryAuthDAO();
    public RegisterResponse register(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = memoryUser.getUser(registerRequest.username());
        if (userData != null)
        {
            throw new DataAccessException("Error: already taken");
        }
        else // else the userDate is not in db
        {
          // CREATE one
            UserData createdData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            memoryUser.createUser(createdData);
            // create Auth for the user
            memoryAuth.createAuth(registerRequest.username());
            RegisterResponse returnedRegisterResponse = new RegisterResponse()
        }




    }
}
