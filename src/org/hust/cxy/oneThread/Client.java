package org.hust.cxy.oneThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    try {
                        SocketChannel socketChannel = SocketChannel.open();
                        socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        String msg1="hello";
                        //字节数组需要通过wrap()才能传入buffer
                        buffer=ByteBuffer.wrap(msg1.getBytes());
                        socketChannel.write(buffer);
                        buffer.clear();
                        String msg2="world";
                        buffer=ByteBuffer.wrap(msg2.getBytes());
                        socketChannel.write(buffer);
                    } catch (IOException o) {
                        o.printStackTrace();
                    }
                }
            }.start();
        }
        while(true){
            System.out.println("别关闭连接");
        }
    }
}
