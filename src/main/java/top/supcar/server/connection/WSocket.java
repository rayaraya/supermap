/*
 * Copyright 2017 SUPMUP
 *
 * This file is part of Supermap.
 *
 * Supermap is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Supermap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Supermap. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package top.supcar.server.connection;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import top.supcar.server.session.ClientProcessor;


/**
 * This class uses standard WebSocket API <br>
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
