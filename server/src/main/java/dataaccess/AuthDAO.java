package dataaccess;

public interface AuthDAO {

    public default String createAuth(String username) throws DataAccessException
    {
        return null;
    }

    public default String getAuth(String authToken) throws DataAccessException
    {
        return null;
    }

    public default void deleteAuth(String authToken) throws DataAccessException
    {
        return;
    }

    public default void clear() throws DataAccessException
    {
        return;
    }

}
