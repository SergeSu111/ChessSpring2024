package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.ServerException;

public class logoutService
{
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();


    public void logout (String authToken) throws DataAccessException, ServerException {
       String username = memoryAuthDAO.getAuth(authToken);
       if (username == null)
       {
           throw new DataAccessException("Error: unauthorized");
       }
       else
       {
           memoryAuthDAO.deleteAuth(authToken);
       }
    }

}
