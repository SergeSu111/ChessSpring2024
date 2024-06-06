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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade{
    private static String httpURL;
    public ServerFacade(String httpURL)
    {
        ServerFacade.httpURL = httpURL;
    }
    public static Object register(String username, String password, String email) throws IOException {
        // get the registerRequest, put in request body later
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String path = "/user";
        URL uri = new URL(httpURL + path);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, registerRequest, method, null);

        // how about return a error message? still registerResponse?

        return getResponseFromHandlers(http, RegisterResponse.class);
    }

    public static Object login(String username, String password) throws IOException {
//        LoginRequest loginRequest = new LoginRequest(username, password);
//        String path = "/session";
//        URL uri = new URL(httpURL + path);
//        String method = "POST";
//        HttpURLConnection http = sendRequest(uri, loginRequest, method, null);
////        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
//        {
//            return (MessageResponse) getResponseFromHandlers(http);
//        }
//        LoginResponse loginResponse = (LoginResponse) getResponseFromHandlers(http);
//        return loginResponse;
        return null;
    }

    public static MessageResponse logout(String authToken) throws IOException {  // Because logout is always returned Message response "" or some error message
//        String path = "/session";
//        URL uri = new URL(httpURL + path);
//        String method = "DELETE";
//        HttpURLConnection http = sendRequest(uri, null, method, authToken);
//        return (MessageResponse) getResponseFromHandlers(http);
        return null;
    }

    public static MessageResponse clear() throws IOException
    {
//        String path = "/db";
//        URL uri = new URL(httpURL + path);
//        String method = "DELETE";
//        HttpURLConnection http = sendRequest(uri, null, method, null);
//        return (MessageResponse) getResponseFromHandlers(http);
        return null;
    }

    public static Object createGame(String gameName, String authToken) throws IOException {
//        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
//        String path = "/game";
//        URL uri = new URL(httpURL + path);
//        String method = "POST";
//        HttpURLConnection http = sendRequest(uri, createGameRequest, method, authToken);
//        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
//        {
//            return (MessageResponse)getResponseFromHandlers(http);
//        }
//        return (CreateGameResponse)getResponseFromHandlers(http);
        return null;
    }

    public static Object listGame(String authToken) throws IOException {
//        String path = "/game";
//        URL uri = new URL(httpURL + path);
//        String method = "GET";
//        HttpURLConnection http = sendRequest(uri, null, method, authToken);
//        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
//        {
//            return (MessageResponse)getResponseFromHandlers(http);
//        }
//        return (LIstGameResponse)getResponseFromHandlers(http);
        return null;
    }

    public static Object joinGame(ChessGame.TeamColor playerColor, int gameID, String authToken) throws IOException {
//        String path = "/game";
//        URL uri = new URL(httpURL + path);
//        String method = "PUT";
//        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
//        HttpURLConnection http = sendRequest(uri, joinGameRequest, method, authToken);
//        return (MessageResponse) getResponseFromHandlers(http);
        return null;
    }


    // make request
    private static HttpURLConnection sendRequest(URL uri, Object RequestBody, String method, String authToken) throws IOException {
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.openConnection();
        http.setRequestMethod(method);
        String jsonRequestBody = gson.toJson(RequestBody); // make it as json before putting into body
        if (authToken != null)
        {
            http.addRequestProperty("authorization", authToken);
        }
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
    private static Object getResponseFromHandlers(HttpURLConnection http, Type type) throws IOException
    {
        Object responseBody;
        Gson gson = new Gson();
        if (http.getResponseCode() == 200)
        {
            try (InputStream respBody = http.getInputStream())
            {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = gson.fromJson(inputStreamReader, type);
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
