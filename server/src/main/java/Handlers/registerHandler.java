package Handlers;

import HttpRequest.RegisterRequest;
import org.eclipse.jetty.server.Response;
import spark.Request;

public class registerHandler extends baseHandler
{
    registerHandler(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Object httpHandlerRequest(Request request, Response response)
    {
        // make the json request to be java request objects
        RegisterRequest body = getBody(request, RegisterRequest.class); // change the request to java object

        return null;
    }


    // return as json
}
