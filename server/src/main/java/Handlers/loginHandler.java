package Handlers;

import HttpRequest.LoginRequest;
import HttpResponse.LoginResponse;
import HttpResponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.loginService;
import spark.Request;
import spark.Response;

public class loginHandler extends baseHandler
{
    private final loginService loginServiceRefer = new loginService();
    public loginHandler(Request request, Response response) {
        super(request, response);
    }


    @Override
    public Object httpHandlerRequest(Request request, Response response)
    {
        Gson gson = new Gson();
        String jsonResponse;
        try
        {
            // make the json request to be java request objects
            LoginRequest loginBody =  getBody(request, LoginRequest.class);

            // call the register service
            LoginResponse loginResponse = loginServiceRefer.login(loginBody);

            // setStatus
            response.status(200);

            // set to be json
            jsonResponse = gson.toJson(loginResponse);

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
