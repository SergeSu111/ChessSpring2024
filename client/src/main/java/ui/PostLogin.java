package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import httpresponse.CreateGameResponse;
import httpresponse.LIstGameResponse;
import httpresponse.MessageResponse;
import model.GameData;
import org.junit.platform.commons.function.Try;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLogin
{
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final Scanner scanner = new Scanner(System.in);

    private Prelogin prelogin;

    private final String authToken;
    public PostLogin(String serverUrl, String authToken)
    {
       ServerFacade serverfacade = new ServerFacade(serverUrl);
       this.authToken = authToken;
    }

    public void run()
    {
        out.println();
        out.println(STR."\{BLACK_KING}Welcome to your chess game account. Please make your choice\{BLACK_KING}");
        out.println();
        out.println(help());
        String input = scanner.nextLine();
        while (!Objects.equals(input, "Quit"))
        {
            this.eval(input);
            input = scanner.nextLine();
        }

    }

    public void eval(String input)
    {
        switch (input)
        {
            case "Create Game" -> createGame();
            case "List Games" -> listGame();
            case "Join Game" -> joinGame();
            case "Observe" -> observeGame();
            case "Log out" ->logOut();
            case "Quit" -> quit();
            case "Help" -> out.println(help());
            default -> out.println(help());
        }

    }
    public static String help()
    {
        return """
               Create Game <Name> -- Create a new chess game.
               List Games -- List all the games.
               Join Game <GameID> <UR PlayerColor> -- Join a current game.
               Observe <GameID> -- Observe a current game.
               Log out -- Logout your account.
               Quit -- Exits your chess game.
               Help - With possible commands.
                """;
    }

    public void quit()
    {
        out.println("Your game is exit."); // the print is not showing up in console.
        System.exit(0);
    }

    public void createGame()
    {
        out.println("Please type the game name you want to create.");
        String gameName = scanner.nextLine();
        try
        {
            Object createGameReturn = ServerFacade.createGame(gameName, authToken);
            if (createGameReturn instanceof CreateGameResponse)
            {
                CreateGameResponse createGameResponseReturn = (CreateGameResponse)createGameReturn;
                int gameID = createGameResponseReturn.gameID();
                out.println(STR."You successfully created a chess game. the game id is: \{gameID}");
            }
            else
            {
                MessageResponse messageResponse = (MessageResponse) createGameReturn;
                out.println(messageResponse.message());
            }
        }
        catch (IOException E)
        {
           out.println(E.getMessage());
        }
    }

    public void listGame()
    {
        try
        {
            Object listGameReturn = ServerFacade.listGame(authToken);
            if (listGameReturn instanceof LIstGameResponse lIstGameResponseReturn)
            {
                ArrayList<GameData> listGames = lIstGameResponseReturn.games();
                for (GameData listGame : listGames)
                {
                    out.println(listGame);
                    out.println();
                    out.println();
                }
            }
            else
            {
                MessageResponse messageResponse = (MessageResponse) listGameReturn;
                out.println(messageResponse.message());
            }
            out.println(help());
        }
        catch (IOException E)
        {
            out.println(E.getMessage());
        }
    }

    public void joinGame()
    {
        Gson gson = new Gson();
        out.println("Please tell me which game you would like to join.");
        String gameIdStr = scanner.nextLine();
        int gameID = Integer.parseInt(gameIdStr);
        out.println("Please tell me what color you would like to join");
        String playerColor = scanner.nextLine();
        if (!Objects.equals(playerColor, "WHITE") && !playerColor.equals("BLACK"))
        {
           out.println("BAD Request. Color should be all capital.");
        }
        else
        {
            ChessGame.TeamColor playerColorChanged = gson.fromJson(playerColor, ChessGame.TeamColor.class);
            try
            {
                MessageResponse messageResponseJoinGame = ServerFacade.joinGame(playerColorChanged, gameID, authToken);
                if (!Objects.equals(messageResponseJoinGame.message(), ""))
                {
                    out.println(messageResponseJoinGame.message());
                }
                else
                {
                    out.println("You successfully join the game");
                    if (playerColorChanged == ChessGame.TeamColor.BLACK)
                    {
                        BoardUI.callWhiteBoard(out);
                        out.println(SET_BG_COLOR_BLACK);
                        out.println(SET_TEXT_COLOR_BLACK);
                        out.println(EMPTY);
                        out.println(EMPTY);
                        BoardUI.callBlackBoard(out);
                    }
                    else
                    {
                        BoardUI.callBlackBoard(out);
                        out.println(SET_BG_COLOR_BLACK);
                        out.println(SET_TEXT_COLOR_BLACK);
                        out.println(EMPTY);
                        out.println(EMPTY);
                        BoardUI.callWhiteBoard(out);
                    }


                }
            }
            catch (IOException e)
            {
                out.println(e.getMessage());
            }
        }
    }

    public void observeGame()
    {
        // for observe. I do not have endpoint for that.
        // directly call the board?
        // Everytime before I call the callBoard, I should make it to be 1.
        out.println("You successfully observe the game");
        BoardUI.callBlackBoard(out);
        out.println(SET_BG_COLOR_BLACK);
        out.println(SET_TEXT_COLOR_BLACK);
        out.println(EMPTY);
        out.println(EMPTY);
        BoardUI.callWhiteBoard(out);
    }

    public void logOut()
    {
        try
        {
           MessageResponse messageResponseLogOut = ServerFacade.logout(authToken);
           if (!Objects.equals(messageResponseLogOut.message(), "")) // error
           {
               out.println(messageResponseLogOut.message());
           }
           else
           {
               out.println("You successfully logout the game.");
               prelogin.run();
           }
        }
        catch (IOException e)
        {
            out.println(e.getMessage());
        }
    }



}
