package org.hust.cxy.mutilThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));
                    ByteBuffer buffer=ByteBuffer.allocate(1024);
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
            }).start();
        }
        while(true){
            System.out.println("别关闭连接");
        }
    }
}
