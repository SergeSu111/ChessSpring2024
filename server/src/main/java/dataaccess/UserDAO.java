package dataaccess;

import Model.AuthData;
import Model.UserData;

public interface UserDAO {

    default AuthData createUser(UserData u) throws DataAccessException{
        return null;
    }

    default void clear() throws DataAccessException
    {
        return;
    }

    default AuthData getUser(String username) throws DataAccessException
    {
        return null;
    }

}
