package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Arrays;

public class sqlUser implements UserDAO
{
    /**
     * @param u
     * @throws DataAccessException
     */
    private static final String[] createStatements =
            {
                    // email is varchar or text?
                    """
                    CREATE TABLE IF NOT EXISTS Users
                    (
                        `passwordCol` varchar(255) NOT NULL,
                        `usernameCol` varchar(255)  NOT NULL,
                        `emailCol` varchar(255) NOT NULL,
                         PRIMARY KEY (authToken);
                    )
                    """
            };

    public static void createUserTable() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement(Arrays.toString(createStatements)))
            {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void createUser(UserData u) throws DataAccessException
    {
        try (var conn = DatabaseManager.getConnection())
        {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Users"))
        }
    }

    /**
     * @throws DataAccessException
     */
    @Override
    public void clear() throws DataAccessException {
        UserDAO.super.clear();
    }

    /**
     * @param username
     * @return
     * @throws DataAccessException
     */
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return UserDAO.super.getUser(username);
    }

}
