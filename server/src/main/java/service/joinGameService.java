package service;

import HttpRequest.JoinGameRequest;
import Model.GameData;
import chess.ChessGame;
import dataaccess.*;

public class joinGameService
{
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void joinGame (JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, ServerException, ClientException, AlreadyTakenException
    {
        String username = authDAO.getAuth(authToken);
        if (username == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0)
        {
            throw new ClientException("Error: bad request");
        }
        else
        {
            GameData foundGame = gameDAO.getGame(joinGameRequest.playerColor(), joinGameRequest.gameID());
            if (foundGame != null)
            {
                if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE && foundGame.whiteUsername() != null || joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK && foundGame.blackUsername() != null)
                {
                    throw new AlreadyTakenException("Error: Already Taken");
                }
                else
                {
                    // just need to call joinGame Because joinGame will call updateGame in it.
                    gameDAO.joinGame(joinGameRequest.gameID(), joinGameRequest.playerColor(), username);
                }
            }


        }
    }

}
