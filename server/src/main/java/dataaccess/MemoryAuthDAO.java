package dataaccess;

import Model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO
{
    HashSet<AuthData> authDataInMemory = new HashSet<>();
    @Override
    public String createAuth(String username) throws DataAccessException {
        String newAuthToken = UUID.randomUUID().toString(); // get the new AuthToken
        AuthData newAuthData = new AuthData(newAuthToken, username); // create a new AuthData
        authDataInMemory.add(newAuthData); // put this new authData into authDataInMemory
        return newAuthData.authToken();
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException
    {
        for (AuthData singleAuthMemory : authDataInMemory)
        {
            if (singleAuthMemory.authToken().equals(authToken))
            {
                return singleAuthMemory.username();
            }
        }
        return null; // does not find the auth.
    }

    /**
     * @param authToken
     * @throws DataAccessException
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        // if the authToken in a singleAuth, just remove that singleAuthMemory
        authDataInMemory.removeIf(singleAuthMemory -> singleAuthMemory.authToken().equals(authToken));
    }

    /**
     * @throws DataAccessException
     */
    @Override
    public void clear() throws DataAccessException {
        authDataInMemory.clear();
    }
}
