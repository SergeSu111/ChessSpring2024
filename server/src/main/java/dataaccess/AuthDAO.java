package dataaccess;

import Model.AuthData;

public interface AuthDAO {

    public default void createAuth(String username) throws DataAccessException
    {
        return;
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
