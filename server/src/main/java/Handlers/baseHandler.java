package Handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import org.eclipse.jetty.server.Response;
import spark.Request;

public abstract class  baseHandler {
    // if the web API requires an authToken, write the logic to valid the authToken in base handler class

    // use spark to create the request and response classes.
    private Request request;
    private Response response;
    baseHandler(Request request, Response response)
    {
        this.request = request;
        this.response = response;
    }

    public abstract Object httpHandlerRequest(Request request, Response response);

    protected String validAuthToken(Request request, Response response) throws DataAccessException {
        // get the token
        String authToken = request.headers("Authorization"); // get the authToken in the header of the request

        // how can I validate the authToken?
        if (authToken != null)
        {
            MemoryAuthDAO newMemoryAuthDao = new MemoryAuthDAO();
            var getUsername = newMemoryAuthDao.getAuth(authToken);
            if (getUsername != null)
            {
                return authToken; // which means the authToken is in db.
            }

        }
        return "Your authToken is not in db.";
    }

    protected static <T> T getBody (Request request, Class<T> aClass)
    {
        Gson gson = new Gson();
        // get the request parameters as json
        String requestJ = request.body();
       T tBody =  gson.fromJson(requestJ, aClass); // transfer the requestBody to be the Generic class type
        if (tBody != null)
        {
            return tBody;
        }
        else
        {
            throw new RuntimeException("You do not have request body");
        }

    }






}
