package com.challenge.server;

import com.challenge.handler.Handler;
import org.eclipse.jetty.server.Server;

public class EmbeddedServer {
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Handler());

        server.start();
        server.join();
    }
}