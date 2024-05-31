package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.ServerException;
import dataaccess.sqlAuth;

public class LogoutService
{
    sqlAuth authDB = new sqlAuth();

    public LogoutService() throws DataAccessException {
    }


    public void logout (String authToken) throws DataAccessException, ServerException {
       String username = authDB.getAuth(authToken);
       if (username == null)
       {
           throw new DataAccessException("Error: unauthorized");
       }
       else
       {
           authDB.deleteAuth(authToken);
       }
    }

}
