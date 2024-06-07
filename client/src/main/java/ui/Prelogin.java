package ui;

import httpresponse.MessageResponse;
import httpresponse.RegisterResponse;

import java.io.IOException;
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
        out.println(this.help());
        out.println("Make your choice.");
        String input = scanner.nextLine();
        this.eval(input);


    }


    public void eval(String input)
    {

            switch (input)
            {
                case "Register" -> register();
            }

    }

    public void register() {
        out.println("Please set your username: ");
        String username = scanner.nextLine();
        out.println("Please set your password: ");
        String password = scanner.nextLine();
        out.println("Please set your email");
        String email = scanner.nextLine();

        try {
            Object registerReturn = ServerFacade.register(username, password, email);
            if (registerReturn instanceof RegisterResponse) {
                out.println("You successfully register the account.");
            } else {
                MessageResponse messageResponseRegister = (MessageResponse) registerReturn;
                out.println(messageResponseRegister.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
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
