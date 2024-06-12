package websocket.messages.websocketResponse;

import websocket.messages.ServerMessage;

public class ErrorWebsocket extends ServerMessage {

    private String errorMessage;

    public ErrorWebsocket(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }



}
