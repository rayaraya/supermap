package top.supcar.server;

import top.supcar.server.WSServer;

/**
 * The main class to run the server and load environment variables.
 * Static void main is written here.
 * @author rayaraya
 */

public class TMain {
    public static void main(String[] args) throws Exception {

        //System.out.println(args.length);
        //System.out.println(args[1]);
        //int port = Integer.parseInt(args[1]);
        int port = 7070;
        WSServer server = new WSServer(port);
        server.run();
    }
}