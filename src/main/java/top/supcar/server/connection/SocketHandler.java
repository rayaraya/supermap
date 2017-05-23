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

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import top.supcar.server.connection.WSocket;

/**
 * This class contains {@link WebSocketServletFactory} that provides <br>
 * data exchange between the servlet and clients, <br>
 * assumes the functions for a functioning servlet <br>
 * @author rayaraya
 */

public class SocketHandler extends WebSocketHandler {

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(WSocket.class);
    }
}

