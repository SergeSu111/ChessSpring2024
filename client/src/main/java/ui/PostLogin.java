package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.BLACK_KING;

public class PostLogin
{
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final Scanner scanner = new Scanner(System.in);

    private final String authToken;
    public PostLogin(String serverUrl, String authToken)
    {
       ServerFacade serverfacade = new ServerFacade(serverUrl);
       this.authToken = authToken;
    }

    public void run()
    {
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
            case "List Games" -> login();
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
            if (createGameReturn)
        }
    }



}
