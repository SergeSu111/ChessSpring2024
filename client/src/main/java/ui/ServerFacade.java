package ui;

import httprequest.RegisterRequest;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private static final String baseURL = "http://localhost:8080";
    private HttpURLConnection httpURLConnection;
    public ServerFacade() throws MalformedURLException {
    }


    public static Object register( String username, String password, String email) throws MalformedURLException {
        // get the registerRequest, put in request body later
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String path = "/user";
        String RegisterURL = baseURL + path;
        URL url = new URL(RegisterURL);

    }


    // make request



    // get response

}
