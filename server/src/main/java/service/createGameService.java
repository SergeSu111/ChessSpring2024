package service;

import HttpRequest.CreateGameRequest;
import HttpResponse.CreateGameResponse;
import dataaccess.*;

public class createGameService
{
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException, ClientException, ServerException {
        String username = authDAO.getAuth(authToken);
        if (username == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        else
        {
            int gameID = gameDAO.createGame(createGameRequest.gameName());
            return new CreateGameResponse(gameID);
        }
    }
}
