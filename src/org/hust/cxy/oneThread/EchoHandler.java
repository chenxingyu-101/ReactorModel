package org.hust.cxy.oneThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Logger;

public class EchoHandler implements Runnable{
    SocketChannel channel;
    ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

    EchoHandler(Selector selector, SocketChannel socketChannel){
        channel=socketChannel;
    }
    void readAndWrite() throws IOException {
        while (channel.read(byteBuffer)>0){//它一次写，我一次读
            byteBuffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder=charset.newDecoder();
            CharBuffer charBuffer=decoder.decode(byteBuffer);
            System.out.println(charBuffer.toString());
            byteBuffer.clear();
        }
    }
    @Override
    public void run() {
        try {
            System.out.println("IOHandler");
            readAndWrite();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
