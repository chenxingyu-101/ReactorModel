package org.hust.cxy.oneThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverChannel;
    EchoServerReactor(ServerSocketChannel serverChannel) throws IOException {
        selector=Selector.open();//获得选择器
        this.serverChannel=serverChannel;
        SelectionKey serverChannelKey=this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);//父channel注册到选择器
        serverChannelKey.attach(new AcceptorHandler());//给监听器中的ServerChannel绑定一个时间处理类
        System.out.println("开始监听...");
    }
    //轮询和分发事件
    public void run(){
        try{
            //轮询事件,服务器再此处循环，永不结束。
            while(!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selected=selector.selectedKeys();
                Iterator<SelectionKey> it=selected.iterator();
                //分发事件
                while(it.hasNext()){
                    dispatch(it.next());
                }
                selected.clear();//选择器轮询到的key清空
            }
        }
        catch(IOException o){
            o.printStackTrace();
        }
    }
    void dispatch(SelectionKey key){
        //分发事件给对应的handler
        Runnable handler=(Runnable)key.attachment();
        if(handler!=null){
            handler.run();
        }
    }
    public class AcceptorHandler implements Runnable{
        public void run(){
            try{
                System.out.println("AcceptorHandler");
                SocketChannel socketChannel=serverChannel.accept();
                if(socketChannel!=null){
                    socketChannel.configureBlocking(false);
                    SelectionKey socketChannelKey=socketChannel.register(selector,SelectionKey.OP_READ);
                    socketChannelKey.attach(new EchoHandler(selector,socketChannel));
                }
            }catch(IOException o){
                o.printStackTrace();
            }
        }
    }
}
