package service;

import HttpResponse.LIstGameResponse;
import Model.GameData;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.ServerException;

import java.util.ArrayList;

public class listGamesService
{
    private final MemoryAuthDAO memoryAuth = new MemoryAuthDAO();

    private final MemoryGameDAO memoryGame = new MemoryGameDAO();

    public LIstGameResponse listGame(String authToken) throws DataAccessException, ServerException {
        String username =  memoryAuth.getAuth(authToken);
        if (username == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        else
        {
            ArrayList<GameData> listGames =  memoryGame.listGames(authToken);
            return new LIstGameResponse(listGames);
        }
    }

}
