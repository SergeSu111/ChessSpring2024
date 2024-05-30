package dataaccess;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class sqlAuth implements AuthDAO {


    // where I should call createDB? I think I only need to call 1 time
    // where I should create the authTable? I have to write a method called createAuthTable in it, and call createStatement?

    public static void createAuthTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement(Arrays.toString(createStatements)))
            {
                preparedStatement.executeUpdate();

            }
        }
        catch (SQLException E)
        {
            throw new DataAccessException(E.getMessage());
        }
    }
    @Override
    public String createAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO username(userNameCol, authTokenCol) VALUES(?, ?)"))
            {

                String authTokenCreated = UUID.randomUUID().toString(); // get a random authToken
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authTokenCreated);
                preparedStatement.executeUpdate();
                return authTokenCreated;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        String username;
        // return a username or null
        try(var conn = DatabaseManager.getConnection())
        {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT authTokenCol, userNameCol FROM Auths WHERE authTokenCol = ?"))
            {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery())
                {
                   if (rs.next())
                   {
                       username = rs.getString("username");
                   }
                   else
                   {
                       username = null;
                   }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return username;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Auths WHERE authTokenCol = ?")) // delete that row that matches the passed in authToken
            {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void clear() throws DataAccessException {
       try (var conn = DatabaseManager.getConnection())
       {
           try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Auths"))
           {
               preparedStatement.executeUpdate();
           }
       }
       catch (SQLException e)
       {
           throw new DataAccessException(e.getMessage());
       }
    }

    // Where I should call the createStatements to make sure the table is created?
    private static final String[] createStatements =
            {
                    // the varChar is 255 or 256? They are null or not null.
                    """
                    CREATE TABLE IF NOT EXISTS Auths
                    (
                        `authTokenCol` varchar(255) NOT NULL,
                        `userNameCol` varchar(255)  NOT NULL,
                         PRIMARY KEY (authToken);
                    )
                    """
            };
}


