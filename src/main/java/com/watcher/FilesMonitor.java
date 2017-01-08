package com.watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Files Monitor
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FilesMonitor extends Thread {

    static final String CLOSE = "File Monitor Close.";

    private WatchService service;

    private boolean running = true;

    private LinkedBlockingQueue<Message<String>> queue;

    /**
     * Constructor.
     * @throws IOException Java文件系统API报错
     * @throws WatcherException 初始化失败
     */
    FilesMonitor(LinkedBlockingQueue<Message<String>> queue) throws IOException, WatcherException {
        super();
        if(queue == null)
            throw new WatcherException("消息队列不能为空");
        File  file = new File(Resources.PATH());
        if(!file.exists())
            throw new WatcherException(String.format("[%s]不存在", file.getPath()));
        this.queue = queue;
        this.service = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(file.getPath());
        path.register(this.service,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY);
    }


    boolean isRunning(){
        return this.running;
    }

    private Message<String> newMessage(String fileName, String type){
        return new FileMessage(fileName, type);
    }

    @Override
    public void run() {
        try {
            while (isRunning()) {
                WatchKey key = service.take();
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    queue.put(newMessage(event.context().toString(), event.kind().toString()));
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            System.out.printf("Files Monitor 线程堵塞被终止[%s]!!!\n", e.getMessage());
        }

        ControlCenter.putMessage(CLOSE);

    }

    /**
     * 关闭线程
     */
    void close(){
        this.interrupt();
        this.running = false;
        try {
            service.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class FileMessage implements Message<String>{

        private String fileName;

        private String type;

        private long time;

        FileMessage(String fileName, String type) {
            this.fileName = fileName;
            this.type = type;
            time = new Date().getTime();
        }

        @Override
        public String data() {
            return fileName;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public long time() {
            return this.time;
        }

        public String toString(){
            return String.format("Message: {info: %s, type: %s}", fileName, type);
        }

    }

}
