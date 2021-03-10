package org.hust.cxy.mutilThread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

class SubReactor implements Runnable{
    Selector subSelector;
    ExecutorService IoExector;
    public SubReactor(Selector subSelector,ExecutorService IoExector) {
        this.subSelector = subSelector;
        this.IoExector=IoExector;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                //subSelector.selectNow();
                subSelector.select();
                Set<SelectionKey> selected=subSelector.selectedKeys();
                Iterator<SelectionKey> it=selected.iterator();
                //调用线程池执行任务
                while(it.hasNext()){
                    SocketChannel channel=(SocketChannel) it.next().channel();
                    IoExector.submit(new IOhandler(channel));
                }
                selected.clear();
            }
        }
        catch(IOException o){
            o.printStackTrace();
        }
    }
}
