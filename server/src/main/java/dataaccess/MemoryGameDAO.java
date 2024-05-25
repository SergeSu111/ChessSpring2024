package dataaccess;

import Model.GameData;
import chess.ChessBoard;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO{

    private static final HashSet<GameData> gameDataMemory = new HashSet<>();
    /**
     * @param gameName
     * @return
     * @throws DataAccessException
     */

    private GameData usedGame;
    @Override
    public int createGame(String gameName) throws DataAccessException
    {
        Random random = new Random();
        int randomInt = random.nextInt(10000); // get the random number between 0 - 10000
        GameData newGame = new GameData(randomInt, null, null, gameName, new ChessGame(new ChessBoard(), ChessGame.TeamColor.WHITE));
        gameDataMemory.add(newGame);
        return randomInt;
    }

    /**
     * @param playerColor
     * @param gameID
     * @return
     * @throws DataAccessException
     */
    @Override
    public GameData getGame(ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        for (GameData singleGame : gameDataMemory)
        {
            if (singleGame.gameID() == gameID)
            {
                return singleGame;
            }
        }
        return null;
    }

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        return new ArrayList<>(gameDataMemory);
    }


    @Override
    public void updateGame(String username, ChessGame.TeamColor playerColor, GameData targetGame) throws DataAccessException {

        GameData temGame;
        if (playerColor.equals(ChessGame.TeamColor.WHITE))
        {
            temGame = new GameData(usedGame.gameID(), username, usedGame.blackUsername(), usedGame.gameName(), usedGame.game()); // create a temporary GameData. because we cannot change GameData's property
            targetGame = temGame;
            gameDataMemory.add(targetGame);
        }
        else if(playerColor.equals(ChessGame.TeamColor.BLACK))
        {
           temGame = new GameData(usedGame.gameID(), usedGame.whiteUsername(), username, usedGame.gameName(), usedGame.game());
           targetGame = temGame;
           gameDataMemory.add(targetGame); // then add a new updated one
        }

    }

    /**
     * @param gameID
     * @param playerColor
     * @param username
     * @throws DataAccessException
     */
    @Override
    public void joinGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        GameData foundGame = getGame(playerColor, gameID);
        usedGame = foundGame;
        gameDataMemory.remove(foundGame); // we removed the old one first.
        updateGame(username, playerColor, foundGame);

    }

    /**
     * @throws DataAccessException
     */
    @Override
    public void clear() throws DataAccessException {
        gameDataMemory.clear();
    }


}
