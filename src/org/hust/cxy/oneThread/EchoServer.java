package org.hust.cxy.oneThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class EchoServer implements Runnable{
    ServerSocketChannel serverChannel;
    public EchoServer() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(8888));
    }
    @Override
    public void run() {
        try {
            new EchoServerReactor(serverChannel).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        EchoServer echoServer=new EchoServer();
        echoServer.run();

    }
}
