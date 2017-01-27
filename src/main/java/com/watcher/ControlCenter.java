package com.watcher;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 控制中心.
 * Created by Zhao Jinyan on 2017-1-1.
 */
class ControlCenter extends ThreadAdapter {

    private static final String NAME = "Control Center";

    private static LinkedBlockingDeque<Message> queue = new LinkedBlockingDeque<>();

    private static ControlCenter center = null;

    private static FileWatcher fileWatcher = null;

    // Default File Watcher
    private static class DefaultFileWatcher extends FileWatcher {

        DefaultFileWatcher() throws WatcherException {
            super();
        }

        // Start All Threads.
        @Override
        void start() {
            // Startup threads.
            getFilter().start();
            getMonitor().start();
            getWatcher().start();
            getSender().start();
        }

    }

    static FileWatcher getFileWatcher() throws WatcherException {
        if(center == null){
            center = new ControlCenter();
        }
        if (fileWatcher == null)
            fileWatcher = new DefaultFileWatcher();
        return fileWatcher;
    }

    static void putMessage(Message<?> message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int runThreadNum = 0;

    private ControlCenter() {
        super(NAME);
        start();
    }

    @Override
    protected void execute() throws InterruptedException {
        Message<?> message = queue.take();
        if(message instanceof ThreadAdapter.ThreadMessage){
            ThreadAdapter.ThreadMessage threadMessage = (ThreadAdapter.ThreadMessage) message;
            System.out.printf("[%s]:%s\n", ThreadAdapter.STARTED, threadMessage.data());
            if(ThreadAdapter.STARTED.equals(threadMessage.type()) && !NAME.equals(threadMessage.data())){
                runThreadNum++;
            } else if(ThreadAdapter.CLOSED.equals(threadMessage.type())){
                runThreadNum--;
                if(runThreadNum <= 0){
                    System.out.println("The Program is closed.");
                    close();
                }
            }

        } else {
            System.out.println("Error Message!");
        }
    }
}
