package ui;

import httpresponse.LoginResponse;
import httpresponse.MessageResponse;
import httpresponse.RegisterResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.BLACK_KING;

public class Prelogin
{
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final Scanner scanner = new Scanner(System.in);

    public Prelogin(String serverUrl)
    {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
    }

    public void run()
    {
        out.println(STR."\{BLACK_KING}Welcome to the Chess Game. Type Here to get started.\{BLACK_KING}");
        out.println();
        out.println(help());
        out.println("Make your choice.");
        String input = scanner.nextLine();
        while (!Objects.equals(input, "QUIT"))
        {
            this.eval(input);
            input = scanner.nextLine();
        }
    }


    public void eval(String input)
    {
            switch (input)
            {
                case "Register" -> register();
                case "Login" -> login();
                case "Help" -> out.println(help());
                case "Quit" -> quit();
                default -> out.println(help());
            }

    }

    public static void register() {
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
                RegisterResponse registerResponseReturned = (RegisterResponse) registerReturn;
                String authToken = registerResponseReturned.authToken();
                out.println();
                PostLogin postlogin = new PostLogin("http://localhost:8080", authToken);
                postlogin.run();
            } else {
                MessageResponse messageResponseRegister = (MessageResponse) registerReturn;
                out.println(messageResponseRegister.message());
                out.println(help());
            }
        } catch (IOException e) {
           out.println(e.getMessage());
        }
    }

    public static void login()
    {
        out.println("Please type your username.");
        String username = scanner.nextLine();
        out.println("Please type your password");
        String password = scanner.nextLine();

        try
        {
            Object loginReturn = ServerFacade.login(username, password);
            if (loginReturn instanceof LoginResponse)
            {
                LoginResponse loginResponseReturn = (LoginResponse)loginReturn;
                String authToken = loginResponseReturn.authToken();
                out.println("You successfully login the account.");
                // turn to postLogin. do this later
                PostLogin postLogin = new PostLogin("http://localhost:8080", authToken);
                postLogin.run();
            }
            else
            {
                MessageResponse messageResponse = (MessageResponse) loginReturn;
                out.println(messageResponse.message());
            }
            out.println(help());
        }
        catch (IOException E)
        {
            out.println(E.getMessage());
        }
    }

    public static String help()
    {
        return """
                Register <USERNAME> <PASSWORD> <EMAIL> -- To create an account
                Login <USERNAME> <PASSWORD> -- To play chess game.
                Help -- with possible commands.
                Quit -- Exits your chess game.
                """;
    }

    public void quit()
    {
        out.println("Your game is exit."); // the print is not showing up in console.
        System.exit(0);
    }



}
