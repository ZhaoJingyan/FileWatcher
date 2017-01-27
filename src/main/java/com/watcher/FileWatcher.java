package com.watcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * File Watcher. 程序核心类
 * Created by Zhao Jinyan on 2017/1/11.
 */
public abstract class FileWatcher {

    private FilesMonitor monitor = null;

    private FilesFilter filter = null;

    private FilesTableWatcher watcher = null;

    private FileSender sender = null;

    FileWatcher() throws WatcherException {
        initialize();
    }

    // 初始化程序
    private void initialize() throws WatcherException {

        // Check resources.
        if (!Resources.isInitialized()) {
            throw new WatcherException("初始化失败!!!");
        } else {
            System.out.println("连接数据成功...");
        }

        // Instantiating
        try {
            FilesTable table = new FilesTable();
            filter = new FilesFilter(table);
            monitor = new FilesMonitor(filter.getQueue());
            watcher = new FilesTableWatcher(table);
            sender = new FileSender(watcher.getQueue(), table);
        } catch (WatcherException e) {
            close();
            throw new WatcherException("初始化进程失败!", e);
        }

    }

    // 关闭程序
    private void close() {

        System.out.println("关闭程序...");
        if (sender != null)
            sender.close();
        if (watcher != null)
            watcher.close();
        if (monitor != null)
            monitor.close();
        if (filter != null)
            filter.close();

    }

    abstract void start();

    FilesMonitor getMonitor() {
        return monitor;
    }

    FilesFilter getFilter() {
        return filter;
    }

    protected FilesTableWatcher getWatcher() {
        return watcher;
    }

    FileSender getSender() {
        return sender;
    }

    /**
     * Main Method.
     *
     * @param args Command Lines
     */
    public static void main(String[] args) throws Exception {
        FileWatcher watcher = ControlCenter.getFileWatcher();
        watcher.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String temp;
        while ((temp = reader.readLine()) != null) {
            if ("q".equals(temp)) {
                watcher.close();
                return;
            }
        }

    }

}
