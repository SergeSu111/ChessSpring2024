package ui;

import com.google.gson.Gson;
import httprequest.RegisterRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ServerFacade{
    private static final String baseURL = "http://localhost:8080";
    private HttpURLConnection httpURLConnection;

    public ServerFacade() throws MalformedURLException
    {
    }

    public static Object register( String username, String password, String email) throws IOException {
        // get the registerRequest, put in request body later
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String path = "/user";
        String RegisterURL = baseURL + path;
        URL uri = new URL(RegisterURL);
        HttpURLConnection http = sendRequest(uri, registerRequest);


    }


    // make request
    private static HttpURLConnection sendRequest(URL uri, Object RequestBody) throws IOException {
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.openConnection();
        http.setRequestMethod("POST");
        String jsonRequestBody = gson.toJson(RequestBody); // make it as json before putting into body
        writeRequestBody(jsonRequestBody, http);


    }

    // write body
    private static void writeRequestBody(String jsonRequestBody, HttpURLConnection http) throws IOException {
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

}
