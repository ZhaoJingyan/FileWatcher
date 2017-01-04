package com.watcher;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2017-1-1.
 */
class ControlCenter {

    private static LinkedBlockingDeque<String> queue;

    synchronized static void putMessage(String message){
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static String takeMessage(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
