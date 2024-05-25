package service;

import HttpRequest.JoinGameRequest;
import Model.GameData;
import dataaccess.*;

public class joinGameService
{
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void joinGame (JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, ServerException, ClientException
    {
        String username = authDAO.getAuth(authToken);
        if (username == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        else
        {
            // just need to call joinGame Because joinGame will call updateGame in it.
            gameDAO.joinGame(joinGameRequest.gameID(), joinGameRequest.playerColor(), username);
        }
    }

}
