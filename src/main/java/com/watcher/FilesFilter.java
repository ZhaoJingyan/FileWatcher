package com.watcher;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Files Filter.
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FilesFilter extends ThreadAdapter {

    public static final String NAME = "Files Filter closed.";

    private FilesTable table;

    private LinkedBlockingQueue<Message<String>> queue;

    FilesFilter(FilesTable table) throws WatcherException {
        super(NAME);
        if (table == null)
            throw new WatcherException("文件列表不能为空!");
        this.table = table;
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    protected void execute() throws InterruptedException {
        Message<String> message = this.queue.take();
        if (!table.put(message)) {
            ControlCenter.putInformation(String.format("%s 非监控对象...", message.data()));
        }
    }

    /**
     * 消息队列
     * @return 消息队列
     */
    LinkedBlockingQueue<Message<String>> getQueue() {
        return this.queue;
    }

}
