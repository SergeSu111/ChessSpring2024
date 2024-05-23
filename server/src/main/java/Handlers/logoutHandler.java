package Handlers;

import HttpResponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.loginService;
import service.logoutService;
import spark.Request;
import spark.Response;

public class logoutHandler extends baseHandler
{
    private final logoutService logoutServiceRefer = new logoutService();

    public logoutHandler(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Object httpHandlerRequest(Request request, Response response) {
        Gson gson = new Gson();
        String jsonResponse;

        // get the request authToken from header
        String authToken = request.headers("Authorization");
        try
        {
            // call the register service
            logoutServiceRefer.logout(authToken);
            // setStatus
            jsonResponse = new Gson().toJson(new MessageResponse("")); //no message
            response.status(200);

        } catch (ServerException e)
        {
            response.status(500);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));

        } catch (DataAccessException e) {
           response.status(401);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        }
        response.type("application/json");
        return jsonResponse;
    }
}
