package service;

import chess.ChessPiece;
import dataaccess.*;

public class clearService
{
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    MemoryUserDAO userDAO = new MemoryUserDAO();


    void clear() throws DataAccessException, ServerException {
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
    }
}
