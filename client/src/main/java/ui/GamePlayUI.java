package ui;

import chess.ChessGame;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class GamePlayUI
{
    private static final PrintStream OUT = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final Scanner SCANNER = new Scanner(System.in);

    private final WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080", ChessGame.TeamColor.WHITE);

    private PostLogin postLogin;

    private String authToken;
    public GamePlayUI(String serverUrl, String authToken)
    {
        ServerFacade serverfacade = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public void run()
    {
        OUT.println();
        OUT.println("Welcome to your chess game. Please make your choice and enjoy the game.");

        OUT.println();
        OUT.println(help());
        String input = SCANNER.nextLine();
        while (!Objects.equals(input, "Quit"))
        {
            this.eval(input);
            input = SCANNER.nextLine();
        }
    }

    public void eval(String input)
    {
        switch (input)
        {
            case "Redraw Chess Board" -> redraw();
            case "Leave" -> leave();
            case "Make Move" -> makeMove();
            case "Resign" -> resign();
            case "Highlight Legal Moves" ->highLight();
            case "Help" -> OUT.println(help());
            default -> OUT.println(help());
        }
    }

    public static String help()
    {
        return """
               Redraw Chess Board -- Redraws the chess board upon the user’s request.
               Leave -- Removes the user from the game. The client transitions back to the Post-Login UI.
               Make Move -- Allow the user to input what move they want to make to make board update.
               Resign -- Resign the game.
               Highlight Legal Moves -- Allows the user to input the piece for which they want to highlight legal moves.
               Help - With possible commands.
                """;
    }

    public void leave() {
        // just in case
        OUT.println(RESET_BG_COLOR);
        OUT.println(RESET_TEXT_COLOR);

        Gson gson = new Gson();
        try
        {
            OUT.println("Please tell me which game you would like to leave.");
            String gameIdStr = SCANNER.nextLine();
            int gameID = Integer.parseInt(gameIdStr);
            OUT.println("Are you sure you want to leave? YES / NO");
            String answer = SCANNER.nextLine();
            if (Objects.equals(answer, "YES"))
            {
                webSocketFacade.leave(authToken, PostLogin.gamesNumber.get(gameID - 1));
                System.out.println(PostLogin.gamesNumber);
                PostLogin postlogin = new PostLogin("http://localhost:8080", authToken);
                postlogin.run();
            }
            else
            {
                System.out.println("You are still in the game.");
            }
        }
        catch (IOException E)
        {
            System.out.println(E.getMessage());
        }

    }

    public  void resign()
    {
        Gson gson = new Gson();
        try
        {
            OUT.print("Please tell me which game you would like to resign?");
            String gameIDStr = SCANNER.nextLine();
            int gameID = Integer.parseInt(gameIDStr);
            OUT.println("Are you sure you want to leave? YES / NO");
            String answer = SCANNER.nextLine();
            if (Objects.equals(answer, "YES"))
            {
                webSocketFacade.resign(authToken, PostLogin.gamesNumber.get(gameID - 1));
            }
            else
            {
                System.out.println("You are still in the game.");
            }
        }
        catch (IOException E)
        {
            System.out.println(E.getMessage());
        }
    }

    public static void redraw()
    {
    }

    public static void makeMove()
    {

    }

    public static void highLight()
    {}
}
