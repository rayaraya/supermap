package top.supcar.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * This class is a simple WebSocket server.
 * It run all time and contain SocketHandler {@link SocketHandler}
 * @author rayaraya
 */

public class WSServer {
    private int port;

    public WSServer(int port){
        this.port = port;
    }

    public void run(){
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");

        server.setHandler(ctx);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {new SocketHandler(), resource_handler});

        server.setHandler(handlers);

        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }
}
