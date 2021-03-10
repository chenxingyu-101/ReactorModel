package org.hust.cxy.mutilThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Server implements Runnable{
    ServerSocketChannel serverChannel;
    public Server() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(8888));
    }
    @Override
    public void run() {
        try {
            new AcceptReactor(serverChannel).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Server server=new Server();
        server.run();
    }
}
