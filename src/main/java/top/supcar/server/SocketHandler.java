package top.supcar.server;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * This class contains {@link WebSocketServletFactory} that provides
 * data exchange between the servlet and clients,
 * assumes the functions for a functioning servlet.
 * @author rayaraya
 */

public class SocketHandler extends WebSocketHandler {

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(WSocket.class);
    }
}

