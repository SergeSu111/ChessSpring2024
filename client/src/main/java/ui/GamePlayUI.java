package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class GamePlayUI
{
    private static final PrintStream OUT = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final Scanner SCANNER = new Scanner(System.in);

    private WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080", ChessGame.TeamColor.WHITE);

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
            case "Redraw Chess Board" -> Redraw();
            case "Leave" -> leave();
            case "Make Move" -> MakeMove();
            case "Resign" -> Resign();
            case "Highlight Legal Moves" ->HighLight();
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

    public static void leave()
    {}

    public static void Resign()
    {
    }

    public static void Redraw()
    {
    }

    public static void MakeMove()
    {

    }

    public static void HighLight()
    {}
}
