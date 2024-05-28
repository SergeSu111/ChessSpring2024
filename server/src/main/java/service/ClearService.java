package service;

import dataaccess.*;

public class ClearService
{
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    MemoryUserDAO userDAO = new MemoryUserDAO();


    public void clear() throws DataAccessException, ServerException {
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
    }
}
