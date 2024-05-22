package dataaccess;

import Model.AuthData;
import Model.UserData;

public interface UserDAO {

    default void createUser(UserData u) throws DataAccessException{
        return;
    }

    default void clear() throws DataAccessException
    {
        return;
    }

    default UserData getUser(String username) throws DataAccessException
    {
        return null;
    }

}
