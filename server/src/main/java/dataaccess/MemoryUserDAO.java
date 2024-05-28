package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO
{
    private static final HashSet<UserData> UserDataMemory = new HashSet<>();
    @Override
    public void createUser(UserData u) throws DataAccessException {
        UserDataMemory.add(u); // add the UserData into the HashSet to create in the hashSet

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {

           for (UserData singleUserData : UserDataMemory)
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
        UserDataMemory.clear();
    }
}
