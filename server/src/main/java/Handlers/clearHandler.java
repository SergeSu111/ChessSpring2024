package Handlers;

import HttpResponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.clearService;
import spark.Request;
import spark.Response;

public class clearHandler extends baseHandler
{
    clearService clearServiceRefer = new clearService();
    clearHandler(Request request, Response response) {
        super(request, response);
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object httpHandlerRequest(Request request, Response response){
        Gson gson = new Gson();
        String body;
        try
        {
            clearServiceRefer.clear();
            body = new Gson().toJson(new MessageResponse("")); //no message
            response.status(200);

        }
        catch(DataAccessException e)
        {
            body = gson.toJson(new MessageResponse(e.getMessage()));
        }
        catch (ServerException e)
        {
            body = new Gson().toJson(new MessageResponse(e.getMessage()));
            response.status(500);
        }
        response.type("application/type");
        return body;
    }
}
