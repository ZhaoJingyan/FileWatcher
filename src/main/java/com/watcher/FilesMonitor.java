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
class FilesMonitor extends ThreadAdapter {

    private static final String NAME = "Files Monitor";

    private WatchService service;

    private LinkedBlockingQueue<Message<String>> queue;

    /**
     * Constructor.
     *
     * @throws WatcherException 初始化失败
     */
    FilesMonitor(LinkedBlockingQueue<Message<String>> queue) throws WatcherException {
        super(NAME);
        if (queue == null)
            throw new WatcherException("消息队列不能为空");
        File file = new File(Resources.PATH());
        if (!file.exists())
            throw new WatcherException(String.format("[%s]不存在", file.getPath()));
        this.queue = queue;
        try {
            this.service = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(file.getPath());
            path.register(this.service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new WatcherException("调用文件监控API失败!", e);
        }

    }

    private Message<String> newMessage(String fileName, String type) {
        return new FileMessage(fileName, type);
    }

    @Override
    protected void execute() throws InterruptedException {
        WatchKey key = service.take();
        List<WatchEvent<?>> events = key.pollEvents();
        for (WatchEvent<?> event : events) {
            queue.put(newMessage(event.context().toString(), event.kind().toString()));
        }
        key.reset();
    }

    /**
     * 关闭线程
     */
    void close() {
        super.close();
        try {
            service.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class FileMessage implements Message<String> {

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

        public String toString() {
            return String.format("Message: {info: %s, type: %s}", fileName, type);
        }

    }

}
