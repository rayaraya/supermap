package top.supcar.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WSocket {
    private Session session;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        //System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            this.session = session;
            session.getRemote().sendString("helloo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onText(String message) {
        //System.out.println("message: " + message);
        try {
            this.session.getRemote().sendString("serv otvet: " + message );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        //System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }
}
