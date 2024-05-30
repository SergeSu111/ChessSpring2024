package dataaccess;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class sqlAuth implements AuthDAO {
    /**
     * @param username
     * @return
     * @throws DataAccessException
     */

    // where I should call createDB? I think I only need to call 1 time
    // where I should create the authTable? I have to write a method called createAuthTable in it, and call createStatement?
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

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public String getAuth(String authToken) throws DataAccessException {
        // return a username or null
        try(var conn = DatabaseManager.getConnection())
        {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT authTokenCol, userNameCol FROM Auth WHERE authTokenCol = ?"))
            {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery())
                {
                   if (rs.next())
                   {
                       return rs.getString("username");
                   }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @param authToken
     * @throws DataAccessException
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Auths WHERE authTokenCol = ?")) // delete that row that matches the passed in authToken
            {
                preparedStatement.setString(1, authToken);
            }
        }
    }

    /**
     * @throws DataAccessException
     */
    @Override
    public void clear() throws DataAccessException {
        AuthDAO.super.clear();
    }

    // Where I should call the createStatements to make sure the table is created?
    private final String[] createStatements =
            {
                    // the varChar is 255 or 256? They are null or not null.
                    """
                    CREATE TABLE IF NOT EXISTS Auth
                    (
                        `authTokenCol` varchar(255) NOT NULL,
                        `userNameCol` varchar(255)  NOT NULL,
                         PRIMARY KEY (authToken);
                    )
                    """
            };
    `
}


