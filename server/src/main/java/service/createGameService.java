package service;

import HttpRequest.CreateGameRequest;
import HttpResponse.CreateGameResponse;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

public class createGameService
{
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        String username = authDAO.getAuth(authToken);
        if (username == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        else
        {
            gameDAO.createGame(createGameRequest.gameName())
        }
    }
}
