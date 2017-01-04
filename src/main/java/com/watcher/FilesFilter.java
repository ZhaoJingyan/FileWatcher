package com.watcher;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Files Filter.
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FilesFilter extends Thread {

    static final String CLOSE = "Files Filter closed.";

    private FilesTable table;

    private LinkedBlockingQueue<Message> queue;

    private boolean running = true;

    FilesFilter(FilesTable table) throws WatcherException {
        super();
        if (table == null)
            throw new WatcherException("文件列表不能为空!");
        this.table = table;
        queue = new LinkedBlockingQueue<>();
    }

    boolean isRunning() {
        return this.running;
    }

    @Override
    public void run() {
        try {
            while (isRunning()) {
                Message message = this.queue.take();
                //System.out.println(message.toString());
                //System.out.println(String.format("队列长度: %d",queue.size()));
                if (!table.put(message)) {
                    System.out.printf("%s 非监控对象...\n", message.info());
                }
            }
        } catch (InterruptedException e) {
            System.out.printf("Files Filter 线程堵塞被终止[%s]!!!\n", e.getMessage());
        }
        ControlCenter.putMessage(CLOSE);
    }

    void close() {
        this.interrupt();
        this.running = false;
    }

    LinkedBlockingQueue<Message> getQueue() {
        return this.queue;
    }

}
