package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class sqlGame implements GameDAO
{
    private static final String[] createStatements =
            {
                    //the primary key is gameID I guess?
                    """
                    CREATE TABLE IF NOT EXISTS Games
                    (
                        `gameIDCol` INT NOT NULL,
                        `whiteUserNameCol` varchar(255),
                        `blackUserNameCol` varchar(255),
                        `gameNameCol` varchar(255) NOT NULL,
                        `ChessGameCol` varchar(255) NOT NULL,
                         PRIMARY KEY (gameIDCol);
                    )
                    """
            };
    public sqlGame() throws DataAccessException {
        createGamesTable();
    }

    public static void createGamesTable() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement(Arrays.toString(createStatements)))
            {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException
    {
        if (gameName == null)
        {
            return 0;
        }
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Games(gameIDCol, whiteUserNameCol, blackUserNameCol, gameNameCol, ChessGameCol) VALUES (?,?,?,?,?)"))
            {
                ChessGame newGame = new ChessGame();
                String jsonGame = gson.toJson(newGame);
                Random random = new Random();
                int randomInt = random.nextInt(10000); // get the random number between 0 - 10000
                preparedStatement.setInt(1, randomInt);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, jsonGame);

                preparedStatement.executeUpdate();
                return randomInt;
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @param playerColor
     * @param gameID
     * @return
     * @throws DataAccessException
     */
    @Override
    public GameData getGame(ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        return GameDAO.super.getGame(playerColor, gameID);
    }

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        return GameDAO.super.listGames(authToken);
    }

    /**
     * @param username
     * @param playerColor
     * @param targetGame
     * @throws DataAccessException
     */
    @Override
    public void updateGame(String username, ChessGame.TeamColor playerColor, GameData targetGame) throws DataAccessException {
        GameDAO.super.updateGame(username, playerColor, targetGame);
    }

    /**
     * @param gameID
     * @param playerColor
     * @param username
     * @throws DataAccessException
     */
    @Override
    public void joinGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        GameDAO.super.joinGame(gameID, playerColor, username);
    }

    /**
     * @throws DataAccessException
     */

    @Override
    public void clear() throws DataAccessException {
        GameDAO.super.clear();
    }
}
