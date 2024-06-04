package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import dataaccess.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    // before each test we can clean up all tables to not affect other tests

    private final sqlUser sqlUserRefer = new sqlUser();
    private final sqlAuth sqlAuthRefer = new sqlAuth();
    private final sqlGame sqlGameRefer = new sqlGame();

    private final UserData userData = new UserData("Serge", "Serge111", "sjh66@byu.edu");
    private final UserData userDataFailed = new UserData("", "Serge", "sjh66@byu.edu");

    public UnitTests() throws DataAccessException {
    }

    @BeforeEach
    public void createDB() throws DataAccessException {
        DatabaseManager.createDatabase();
        sqlUser.createUserTable();
        sqlAuth.createAuthTable();
        sqlGame.createGamesTable();


    }

    @BeforeEach
    @Test
    public void clear() throws DataAccessException {
        sqlUserRefer.clear();
        sqlAuthRefer.clear();
        sqlGameRefer.clear();
        assertDoesNotThrow(sqlUserRefer::clear);
        assertDoesNotThrow(sqlAuthRefer::clear);
        assertDoesNotThrow(sqlGameRefer::clear);
    }

    @Test
    @Order(1)
    public void registerSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> sqlUserRefer.createUser(userData));
    }

    @Test
    @Order(2)
    public void registerFailed() throws DataAccessException {
        sqlUserRefer.createUser(userData);
        assertThrows(DataAccessException.class, () -> sqlUserRefer.createUser(userData));
    }

    @Test
    @Order(3)
    public void loginSuccess() throws DataAccessException {
        sqlUserRefer.createUser(userData);
        UserData returnedUserData = sqlUserRefer.getUser(userData.username());
        assertEquals(userData.username(), returnedUserData.username());
    }

    @Test
    @Order(4)
    public void loginFailed() throws DataAccessException {
        sqlUserRefer.createUser(userData);
        UserData returnedUserData = sqlUserRefer.getUser(userData.username());
        assertNotEquals(returnedUserData, userDataFailed);
    }

    @Test
    @Order(5)
    public void createGameSuccess() throws DataAccessException {
        sqlUserRefer.createUser(userData);
        assertDoesNotThrow(() -> sqlGameRefer.createGame("game1"));
    }

    @Test
    @Order(6)
    public void createGameFailed() throws DataAccessException {
        sqlUserRefer.createUser(userData);
        assertThrows(DataAccessException.class, () -> sqlGameRefer.createGame(null));


    }

}
