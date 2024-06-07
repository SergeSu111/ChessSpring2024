package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.BLACK_KING;

public class Prelogin
{
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private final ServerFacade serverFacade;

    private final Scanner scanner = new Scanner(System.in);

    public Prelogin(String serverUrl)
    {
        serverFacade = new ServerFacade(serverUrl);
    }

    public void run()
    {
        out.println(STR."\{BLACK_KING}Welcome to the Chess Game. Type Here to get started.\{BLACK_KING}");
    }

}
