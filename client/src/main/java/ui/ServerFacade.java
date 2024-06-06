package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import httprequest.CreateGameRequest;
import httprequest.JoinGameRequest;
import httprequest.LoginRequest;
import httprequest.RegisterRequest;
import httpresponse.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade{
    private static String baseURL = "http://localhost:";

    ServerFacade(String port)
    {
        baseURL += port;
    }
    public static Object register(String username, String password, String email) throws IOException {
        // get the registerRequest, put in request body later
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String path = "/user";
        URL uri = new URL(baseURL + path);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, registerRequest, method);
        // how about return a error message? still registerResponse?
        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
        {
            return (MessageResponse) getResponseFromHandlers(http);
        }
        return (RegisterResponse) getResponseFromHandlers(http);
    }

    public static Object login(String username, String password) throws IOException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String path = "/session";
        URL uri = new URL(baseURL + path);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, loginRequest, method);
        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
        {
            return (MessageResponse) getResponseFromHandlers(http);
        }
        return (LoginResponse) getResponseFromHandlers(http);
    }

    public static MessageResponse logout() throws IOException {  // Because logout is always returned Message response "" or some error message
        String path = "/session";
        URL uri = new URL(baseURL + path);
        String method = "DELETE";
        HttpURLConnection http = sendRequest(uri, null, method);
        return (MessageResponse) getResponseFromHandlers(http);
    }

    public static MessageResponse clear() throws IOException
    {
        String path = "/db";
        URL uri = new URL(baseURL + path);
        String method = "DELETE";
        HttpURLConnection http = sendRequest(uri, null, method);
        return (MessageResponse) getResponseFromHandlers(http);
    }

    public static Object createGame(String gameName) throws IOException {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        String path = "/game";
        URL uri = new URL(baseURL + path);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, createGameRequest, method);
        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
        {
            return (MessageResponse)getResponseFromHandlers(http);
        }
        return (CreateGameResponse)getResponseFromHandlers(http);
    }

    public static Object listGame() throws IOException {
        String path = "/game";
        URL uri = new URL(baseURL + path);
        String method = "GET";
        HttpURLConnection http = sendRequest(uri, null, method);
        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
        {
            return (MessageResponse)getResponseFromHandlers(http);
        }
        return (LIstGameResponse)getResponseFromHandlers(http);
    }

    public static Object joinGame(ChessGame.TeamColor playerColor, int gameID) throws IOException {
        String path = "/game";
        URL uri = new URL(baseURL + path);
        String method = "PUT";
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        HttpURLConnection http = sendRequest(uri, joinGameRequest, method);
        return (MessageResponse) getResponseFromHandlers(http);
    }


    // make request
    private static HttpURLConnection sendRequest(URL uri, Object RequestBody, String method) throws IOException {
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.openConnection();
        http.setRequestMethod(method);
        String jsonRequestBody = gson.toJson(RequestBody); // make it as json before putting into body
        writeRequestBody(jsonRequestBody, http);
        http.connect();
        return http; // return the http that already had the request
    }

    // write body
    private static void writeRequestBody(String jsonRequestBody, HttpURLConnection http) throws IOException {
        // I think I also need to set the authToken into Body right? But authToken should be set in header.
        if (!jsonRequestBody.isEmpty())
        {
            http.setDoOutput(true); // I can write request into body
            try (var outputStream = http.getOutputStream())
            {
                outputStream.write(jsonRequestBody.getBytes()); // put it into body
            }
        }
    }
    // get response
    private static Object getResponseFromHandlers(HttpURLConnection http) throws IOException
    {
        Object responseBody;
        Gson gson = new Gson();
        if (http.getResponseCode() == 200)
        {
            try (InputStream respBody = http.getInputStream())
            {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = gson.fromJson(inputStreamReader, Map.class);
            }
        }
        else
        {
            try(InputStream respBody = http.getInputStream())
            {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                 responseBody = gson.fromJson(inputStreamReader, MessageResponse.class);
            }

        }
        return responseBody;
    }

}
