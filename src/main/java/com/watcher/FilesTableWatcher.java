package com.watcher;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 文件列表监控器。每隔30秒刷新一次FilesTable.
 * Created by Zhao Jinyan on 2016-12-29.
 */
class FilesTableWatcher extends Thread implements FileDisposeAdapter {

    static final String CLOSE = "Files table watcher closed.";

    private LinkedBlockingDeque<String> queue;

    private FilesTable table;

    private boolean running = true;

    /**
     * 构造方法
     * @param table 文件列表
     * @throws WatcherException 初始化失败
     */
    FilesTableWatcher(FilesTable table) throws WatcherException{
        super();
        if(table == null)
            throw new WatcherException("文件列表为空!!!");
        this.table = table;
        this.queue = new LinkedBlockingDeque<>();
    }

    boolean isRunning(){
        return running;
    }

    @Override
    public void run() {
        try {
            while(isRunning()){
                Thread.sleep(30000);
                long time = new Date().getTime();
                table.refresh(time, this);
            }
        } catch (InterruptedException e) {
            System.out.printf("Files Table Watcher 线程堵塞被终止[%s]!!!\n", e.getMessage());
        }

        ControlCenter.putMessage(CLOSE);
    }

    /**
     * 关闭线程
     */
    void close(){
        this.interrupt();
        this.running = false;
    }

    @Override
    public void dispose(String name) throws InterruptedException {
        queue.put(name);
    }

    LinkedBlockingDeque<String> getQueue() {
        return queue;
    }
}
