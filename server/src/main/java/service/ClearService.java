package service;

import dataaccess.*;

public class ClearService
{
    private final sqlGame gameDB = new sqlGame();
    private final sqlAuth authDB = new sqlAuth();

    private final sqlUser userDB =  new sqlUser();

    public ClearService() throws DataAccessException {
    }


    public void clear() throws DataAccessException, ServerException {
        gameDB.clear();
        authDB.clear();
        userDB.clear();
    }
}
