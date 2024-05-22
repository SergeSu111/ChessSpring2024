package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public interface GameDAO {
    public default String createGame(String gameName) throws DataAccessException {
        return null;
    }

    public default GameData getGame(ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        return null;
    }

    public default ArrayList<ChessGame> listGames(String authToken) throws DataAccessException
    {
        return null;
    }

    public default void updateGame(String username, ChessGame.TeamColor playerColor, String gameID) throws DataAccessException
    {
        return;
    }

    public default void clear() throws DataAccessException
    {
        return;
    }



}
