package dataaccess;

public class sqlAuth implements AuthDAO
{
    /**
     * @param username
     * @return
     * @throws DataAccessException
     */

    // where I should call createDB? I think I only need to call 1 time
    // where I should create the authTable?
    @Override
    public String createAuth(String username) throws DataAccessException
    {

    }

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return AuthDAO.super.getAuth(authToken);
    }

    /**
     * @param authToken
     * @throws DataAccessException
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthDAO.super.deleteAuth(authToken);
    }

    /**
     * @throws DataAccessException
     */
    @Override
    public void clear() throws DataAccessException {
        AuthDAO.super.clear();
    }
}


