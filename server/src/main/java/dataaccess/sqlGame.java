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

    @Override
    public GameData getGame(ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        String whiteUserName, blackUserName, gameName, ChessGame;
        Gson gson = new Gson();
        GameData getGameData;
       try (var conn = DatabaseManager.getConnection())
       {
           try (var preparedStatement = conn.prepareStatement("SELECT gameIDCol, whiteUserNameCol, blackUserNameCol, gameNameCol, ChessGameCol FROM Games WHERE gameIDCol = ?"))
           {
               preparedStatement.setInt(1, gameID);
               try (var rs = preparedStatement.executeQuery())
               {
                   if (rs.next())
                   {
                       whiteUserName = rs.getString("whiteUserNameCol");
                       blackUserName = rs.getString("blackUserNameCol");
                       gameName = rs.getString("gameNameCol");
                       ChessGame = rs.getString("ChessGameCol");
                       ChessGame getGame = gson.fromJson(ChessGame, chess.ChessGame.class);
                       getGameData = new GameData(gameID, whiteUserName, blackUserName, gameName, getGame);
                       return getGameData;
                   }
                   else
                   {
                       return null;
                   }
               } catch (SQLException e) {
                   throw new DataAccessException(e.getMessage());
               }
           }
       }
       catch (SQLException e)
       {
           throw new DataAccessException(e.getMessage());
       }
    }

    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException
    {
        String whiteUserName, blackUserName, gameName, ChessGame;
        int gameID;
        Gson gson = new Gson();
        ArrayList<GameData> returnedGames =new ArrayList<>();
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Games")) {
                try (var rs = preparedStatement.executeQuery())
                {
                    while (rs.next()) // I think not if, because we need all gameData in db.
                    {
                        gameID = rs.getInt("gameIdCol");
                        whiteUserName = rs.getString("whiteUserNameCol");
                        blackUserName = rs.getString("blackUserNameCol");
                        gameName = rs.getString("gameNameCol");
                        ChessGame = rs.getString("ChessGameCol");
                        ChessGame getGame = gson.fromJson(ChessGame, chess.ChessGame.class);
                        returnedGames.add(new GameData(gameID, whiteUserName, blackUserName, gameName, getGame));
                    }
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
            catch (SQLException e)
            {
                throw new DataAccessException(e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return returnedGames;
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
