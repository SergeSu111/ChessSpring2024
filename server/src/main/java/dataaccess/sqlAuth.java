package dataaccess;

import java.sql.SQLException;
import java.util.UUID;

public class sqlAuth implements AuthDAO {
    /**
     * @param username
     * @return
     * @throws DataAccessException
     */

    // where I should call createDB? I think I only need to call 1 time
    // where I should create the authTable?
    @Override
    public String createAuth(String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chess"); // to make sure doing with chess db.
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth(authToken) VALUES (?)"))
            {
               String authToken = UUID.randomUUID().toString(); // get a random authToken
                preparedStatement.setString(1, authToken);

                preparedStatement.executeUpdate();
                return authToken;
            }
        }
    }

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return AuthDAO.super.getAuth(authToken);
    }

    /**
     * @param authToken
     * @throws DataAccessException
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthDAO.super.deleteAuth(authToken);
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
                        `authToken` varchar(255) NOT NULL,
                        `username` varchar(255) NOT NULL,
                         primary key ("authToken);
                    )
                    """
            };
}


