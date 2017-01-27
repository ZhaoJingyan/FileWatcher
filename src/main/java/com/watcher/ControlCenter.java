package com.watcher;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 控制中心.
 * Created by Zhao Jinyan on 2017-1-1.
 */
class ControlCenter extends ThreadAdapter {

    private static final String INFO = "info";

    private static final String NAME = "Control Center";

    private static LinkedBlockingDeque<Message> queue = new LinkedBlockingDeque<>();

    private static ControlCenter center = null;

    private static FileWatcher fileWatcher = null;

    private static class LogMessage implements Message<String> {

        private String information;

        private String type;

        LogMessage(String information, String type) {
            this.information = information;
            this.type = type;
        }

        @Override
        public String data() {
            return information;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public long time() {
            return 0;
        }
    }

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

    static void putInformation(String information){
        try{
            queue.put(new LogMessage(information, INFO));
        }catch(InterruptedException e){
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
            System.out.printf("[%10s]:%s\n", threadMessage.type(), threadMessage.data());
            if(ThreadAdapter.STARTED.equals(threadMessage.type()) && !NAME.equals(threadMessage.data())){
                runThreadNum++;
            } else if(ThreadAdapter.CLOSED.equals(threadMessage.type())){
                runThreadNum--;
                if(runThreadNum <= 0){
                    System.out.println("The Program is closed.");
                    close();
                }
            } else if(ThreadAdapter.ERROR.equals(threadMessage.type())){
                System.out.println("The Program is error!");
                close();
            }

        } else if(message instanceof LogMessage){
            LogMessage logMessage = (LogMessage) message;
            System.out.printf("[%10s]:%s\n", logMessage.type(), logMessage.data());
        } else {
            System.out.println("Error Message!");
        }
    }
}
