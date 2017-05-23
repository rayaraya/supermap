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

package top.supcar.server;

import top.supcar.server.connection.WSServer;

/**
 * The main class to run the server and load environment variables <br>
 * Static void main is written here <br>
 * @author rayaraya
 */


public class TMain {
    public static void main(String[] args) throws Exception {

        //System.out.println(args.length);
        //System.out.println(args[1]);
        int port;
        if(args.length > 0) {
            port =Integer.parseInt(args[1]);
        }else{
            port = 7070;
        }

        WSServer server = new WSServer(port);
        server.run();
    }
}