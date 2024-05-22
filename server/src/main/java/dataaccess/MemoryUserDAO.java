package dataaccess;

import Model.AuthData;
import Model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO
{
    HashSet<UserData> userDataMemory = new HashSet<>();
    @Override
    public AuthData createUser(UserData u) throws DataAccessException {
        userDataMemory.add(u); // add the UserData into the HashSet to create in the hashSet

    }

    @Override
    public AuthData getUser(String username) throws DataAccessException {
       try
       {
           for (UserData singleUserData : userDataMemory)
           {
               if (singleUserData.username().equals(username))
               {
                   // call getAuth
               }
           }
       }
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMemory.clear();
    }
}
