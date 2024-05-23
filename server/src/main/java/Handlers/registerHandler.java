package Handlers;

import HttpRequest.RegisterRequest;
import HttpResponse.MessageResponse;
import HttpResponse.RegisterResponse;
import com.google.gson.Gson;
import dataaccess.ClientException;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.registerService;
import spark.Request;
import spark.Response;

public class registerHandler extends baseHandler
{
    private final registerService registerServiceRefer = new registerService();
    public registerHandler(Request request, spark.Response response) {
        super(request, response);
    }

    @Override
    public  Object httpHandlerRequest(Request request, Response response)
    {
        Gson gson = new Gson();
        String jsonResponse;
        try
        {
            // make the json request to be java request objects
            RegisterRequest body = getBody(request, RegisterRequest.class); // change the request to java object

            // call the register service
            RegisterResponse registerResponse =  registerServiceRefer.register(body);

            // setStatus
            response.status(200);

            // set to be json
            jsonResponse = gson.toJson(registerResponse);
        }
        catch (DataAccessException ex)
        {
            response.status(403); // already taken

            //  catch the error message of DE and put it into a new response and make it a json
            jsonResponse = gson.toJson(new MessageResponse(ex.getMessage()));
        }
        catch (ClientException e)
        {
            response.status(400); // bad request

            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        }
        catch (ServerException e)
        {
            response.status(500);

            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        }

        return jsonResponse;
    }

    /**
     * @param request
     * @param response
     * @return
     */

}
