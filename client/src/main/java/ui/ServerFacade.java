package ui;

import com.google.gson.Gson;
import httprequest.LoginRequest;
import httprequest.RegisterRequest;
import httpresponse.CreateGameResponse;
import httpresponse.LoginResponse;
import httpresponse.MessageResponse;
import httpresponse.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade{
    private static final String baseURL = "http://localhost:8080";

    public static Object register(String username, String password, String email) throws IOException {
        // get the registerRequest, put in request body later
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String path = "/user";
        String RegisterURL = baseURL + path;
        URL uri = new URL(RegisterURL);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, registerRequest, method);
        // how about return a error message? still registerResponse?
        if (getResponseFromHandlers(http).getClass() == MessageResponse.class)
        {
            return (MessageResponse) getResponseFromHandlers(http);
        }
        return (RegisterResponse) getResponseFromHandlers(http);
    }

    public static LoginResponse login(String username, String password) throws IOException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String path = "/session";
        String LoginURL = baseURL + path;
        URL uri = new URL(LoginURL);
        String method = "POST";
        HttpURLConnection http = sendRequest(uri, loginRequest, method);
        LoginResponse loginResponse = (LoginResponse) getResponseFromHandlers(http);

        return loginResponse;
    }



    public static CreateGameResponse




    // make request
    private static HttpURLConnection sendRequest(URL uri, Object RequestBody, String method) throws IOException {
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.openConnection();
        http.setRequestMethod(method);
        String jsonRequestBody = gson.toJson(RequestBody); // make it as json before putting into body
        writeRequestBody(jsonRequestBody, http);
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
