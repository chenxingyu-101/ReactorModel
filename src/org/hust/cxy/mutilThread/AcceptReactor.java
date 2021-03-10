package org.hust.cxy.mutilThread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcceptReactor implements Runnable {
    Selector selector;
    Selector subSelector1;
    Selector subSelector2;
    Selector subSelector3;//先写死三个，作为框架应该提供指定数量功能。
    ServerSocketChannel serverChannel;
    ExecutorService IoLoop = Executors.newCachedThreadPool();
    AcceptReactor(ServerSocketChannel serverChannel) throws IOException {
        selector=Selector.open();//获得选择器
        this.serverChannel=serverChannel;
        SelectionKey serverChannelKey=this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);//父channel注册到选择器
        serverChannelKey.attach(new AcceptorHandler());//给监听器中的ServerChannel绑定一个时间处理类
        System.out.println("开始监听连接");
        //子反应器线程也先跑起来
        subSelector1=Selector.open();
        subSelector2=Selector.open();
        subSelector3=Selector.open();
        Thread thread1=new Thread(new SubReactor(subSelector1,IoLoop));
        Thread thread2=new Thread(new SubReactor(subSelector2,IoLoop));
        Thread thread3=new Thread(new SubReactor(subSelector3,IoLoop));
        thread1.start();
        thread2.start();
        thread3.start();
        System.out.println("三个IoLoop启动");
    }
    public void run(){
        try{
            //轮询父channel，看有没有新连接。
            while(!Thread.interrupted()){
                selector.select();//设置成阻塞的，把cpu给别人用。
                Set<SelectionKey> selected=selector.selectedKeys();
                Iterator<SelectionKey> it=selected.iterator();
                //将新连接分发给子选择器。只要完成注册就行了。
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
        //随机分发SocketChannel给子选择器。
        Runnable handler=(Runnable)key.attachment();
        if(handler!=null){
            handler.run();
        }
    }
    public class AcceptorHandler implements Runnable{
        public void run(){
            try{
                SocketChannel socketChannel=serverChannel.accept();
                if(socketChannel!=null){
                    socketChannel.configureBlocking(false);
                    int random=(int)(Math.random()*3);
                    if(random==0){
                        subSelector1.wakeup();
                        socketChannel.register(subSelector1,SelectionKey.OP_READ);
                    }else if(random==1){
                        subSelector2.wakeup();
                        socketChannel.register(subSelector2,SelectionKey.OP_READ);
                    }else if(random==2){
                        subSelector3.wakeup();
                        socketChannel.register(subSelector3,SelectionKey.OP_READ);
                    }
                }
            }catch(IOException o){
                o.printStackTrace();
            }
        }
    }
}