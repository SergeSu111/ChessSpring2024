package handlers;

import httprequest.JoinGameRequest;
import httpresponse.MessageResponse;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.ClientException;
import dataaccess.DataAccessException;
import dataaccess.ServerException;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends BaseHandler
{
    JoinGameService joinGameServiceRefer = new JoinGameService();
    public JoinGameHandler(Request request, Response response) throws DataAccessException {
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
        String jsonResponse;
        try {
            String authToken = request.headers("Authorization");
            JoinGameRequest joinGameRequest = getBody(request, JoinGameRequest.class);
            joinGameServiceRefer.joinGame(joinGameRequest, authToken);
            jsonResponse = new Gson().toJson(new MessageResponse("")); //no message
            response.status(200);

        } catch (ServerException e) {
            response.status(500);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (ClientException e) {
            response.status(400);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (DataAccessException e) {
            response.status(401);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        } catch (AlreadyTakenException e)
        {
            response.status(403);
            jsonResponse = gson.toJson(new MessageResponse(e.getMessage()));
        }
        response.type("application/json");
        return jsonResponse;
    }
}
