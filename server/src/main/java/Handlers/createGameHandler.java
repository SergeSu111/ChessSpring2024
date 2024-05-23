package Handlers;

import HttpRequest.CreateGameRequest;
import HttpResponse.CreateGameResponse;
import HttpResponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.ClientException;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.createGameService;
import spark.Request;
import spark.Response;

public class createGameHandler extends baseHandler{

    createGameService createGameServiceRefer = new createGameService();
    public createGameHandler(Request request, Response response) {
        super(request, response);
    }

    /**
     * @param request
     * @param response
     * @return
     */

    @Override
    public Object httpHandlerRequest(Request request, Response response) {
        Gson gson = new Gson();
        String createdGameJsonReturn;
        try
        {
            // convert to createGameRequest class
            CreateGameRequest createGameRequest = getBody(request, CreateGameRequest.class);

            // get the authToken
            String authToken = request.headers("Authorization");
            CreateGameResponse createGameResponse = createGameServiceRefer.createGame(createGameRequest, authToken);
            int gameID = createGameResponse.gameID();
            createdGameJsonReturn = gson.toJson(gameID);
            response.status(200);

        } catch (ServerException e)
        {
            response.status(500);
            createdGameJsonReturn = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (ClientException e)
        {
            response.status(400);
            createdGameJsonReturn = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (DataAccessException e)
        {
            response.status(401);
            createdGameJsonReturn = gson.toJson(new MessageResponse(e.getMessage()));
        }
        response.type("application/json");
        return createdGameJsonReturn;
    }
}
