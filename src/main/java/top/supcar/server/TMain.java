package top.supcar.server;

import top.supcar.server.WSServer;

public class TMain {
    public static void main(String[] args) throws Exception {

        //System.out.println(args.length);
        //System.out.println(args[1]);
        int port = Integer.parseInt(args[1]);
        WSServer server = new WSServer(port);
        server.run();
    }
}