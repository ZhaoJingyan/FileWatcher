package com.watcher;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 文件列表监控器。每隔30秒刷新一次FilesTable.
 * Created by Zhao Jinyan on 2016-12-29.
 */
class FilesTableWatcher extends ThreadAdapter implements FileDisposeAdapter {

    static final String NAME = "Files Table Watcher";

    private LinkedBlockingDeque<String> queue;

    private FilesTable table;

    /**
     * 构造方法
     * @param table 文件列表
     * @throws WatcherException 初始化失败
     */
    FilesTableWatcher(FilesTable table) throws WatcherException{
        super(NAME);
        if(table == null)
            throw new WatcherException("文件列表为空!!!");
        this.table = table;
        this.queue = new LinkedBlockingDeque<>();
    }

    @Override
    protected void execute() throws InterruptedException {
        Thread.sleep(30000);
        long time = new Date().getTime();
        table.refresh(time, this);
    }

    @Override
    public void dispose(String name) throws InterruptedException {
        queue.put(name);
    }

    LinkedBlockingDeque<String> getQueue() {
        return queue;
    }
}
