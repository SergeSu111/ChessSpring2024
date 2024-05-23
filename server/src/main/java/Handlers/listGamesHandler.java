package Handlers;

import HttpResponse.LIstGameResponse;
import HttpResponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.listGamesService;
import spark.Request;
import spark.Response;

public class listGamesHandler extends baseHandler
{

    private final listGamesService listGameServiceRefer = new listGamesService();
    listGamesHandler(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Object httpHandlerRequest(Request request, Response response) {
        String jsonResponse;
        String authToken = request.headers("Authorization");
        Gson gson = new Gson();
        try
        {
           LIstGameResponse lIstGameResponse = listGameServiceRefer.listGame(authToken);
           response.status(200);
            jsonResponse = gson.toJson(lIstGameResponse);

        } catch (ServerException e) {
            response.status(500);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(401);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        }
        return jsonResponse;
    }
}
