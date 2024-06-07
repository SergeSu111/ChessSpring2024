package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.BLACK_KING;

public class Prelogin
{
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private final Scanner scanner = new Scanner(System.in);

    public Prelogin(String serverUrl)
    {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
    }

    public void run()
    {
        out.println(STR."\{BLACK_KING}Welcome to the Chess Game. Type Here to get started.\{BLACK_KING}");
        out.println();
        out.print(this.help());

    }

    public String help()
    {
        return """
                Register <USERNAME> <PASSWORD> <EMAIL> -- To create an account
                Login <USERNAME> <PASSWORD> -- To play chess game.
                Help -- with possible commands.
                Quit -- Exits the program.
                """;
    }

}
