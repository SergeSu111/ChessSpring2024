package dataaccess;

import Model.AuthData;
import Model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO
{
    private static final HashSet<UserData> userDataMemory = new HashSet<>();
    @Override
    public void createUser(UserData u) throws DataAccessException {
        userDataMemory.add(u); // add the UserData into the HashSet to create in the hashSet

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {

           for (UserData singleUserData : userDataMemory)
           {
               if (singleUserData.username().equals(username))
               {
                   return singleUserData;
               }
           }
           return null;
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMemory.clear();
    }
}
