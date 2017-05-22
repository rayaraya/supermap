package top.supcar.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


/**
 * This class uses standard WebSocket API.
 * @author rayaraya
 */

@WebSocket
public class WSocket {
    private Session session;
    private ClientProcessor clientProcessor;
    @OnWebSocketConnect
    public void onConnect(Session session) {
        try {
            this.session = session;
            clientProcessor = new ClientProcessor(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onText(String message) {
        System.out.println("message: " + message);
        try {
          clientProcessor.handleMsg(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
        clientProcessor.stop();
    }
}
