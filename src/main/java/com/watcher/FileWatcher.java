package com.watcher;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * File Watcher. 程序核心类
 * Created by Zhao Jinyan on 2017/1/11.
 */
public abstract class FileWatcher {

    private FilesMonitor monitor = null;

    private FilesFilter filter = null;

    private FilesTable table = null;

    private FilesTableWatcher watcher = null;

    private FileSender sender = null;

    FileWatcher(){
        initialize();
    }

    // 初始化程序
    private void initialize(){

        // Check resources.
        if(!Resources.isInitialized()){
            System.out.println("初始化失败!!!");
            return;
        } else {
            System.out.println("连接数据成功...");
        }

        // Instantiating the Files Table.
        try {
            table = new FilesTable();
        } catch (SQLException | WatcherException | ParseException e) {
            e.printStackTrace();
            System.out.println("初始化文件列表失败");
            close();
        }

        // Instantiating the File Filter.
        try {
            filter = new FilesFilter(table);
        } catch (WatcherException e) {
            e.printStackTrace();
            System.out.println("初始化文件过滤器失败");
            close();
        }

        // Instantiating the File Monitor.
        try {
            monitor = new FilesMonitor(filter.getQueue());
        } catch (IOException | WatcherException e) {
            e.printStackTrace();
            System.out.println("初始化文件监控器失败!!!");
            close();
        }

        // Instantiating the Files Table Watcher
        try {
            watcher = new FilesTableWatcher(table);
        } catch (WatcherException e) {
            e.printStackTrace();
            System.out.println("初始化文件列表监控器失败!!!");
            close();
        }

        // Instantiating the File Sender
        try {
            sender = new FileSender(watcher.getQueue(), table);
        } catch (WatcherException e) {
            e.printStackTrace();
            System.out.println("初始化文件发送器失败!!!");
            close();
        }

    }

    // 关闭程序
    private void close(){

        System.out.println("关闭程序...");
        boolean senderClosed = true;
        if(sender != null && sender.isRunning()){
            senderClosed = false;
            sender.close();
        }
        boolean watcherClosed = true;
        if(watcher != null && watcher.isRunning()){
            watcherClosed = false;
            watcher.close();
        }
        boolean monitorClosed = true;
        if(monitor != null && monitor.isRunning()){
            monitorClosed = false;
            monitor.close();
        }
        boolean filterClosed = true;
        if(filter != null && filter.isRunning()){
            filterClosed = false;
            filter.close();
        }

        String message;
        while((message = ControlCenter.takeMessage()) != null){
            if(FileSender.NAME.equals(message))
                senderClosed = true;
            if(FilesTableWatcher.NAME.equals(message))
                watcherClosed = true;
            if(FilesMonitor.NAME.equals(message))
                monitorClosed = true;
            if(FilesFilter.NAME.equals(message))
                filterClosed = true;
            if(senderClosed && watcherClosed && monitorClosed && filterClosed){
                Resources.close();
                System.out.println("程序关闭.");
                System.exit(0);
            }
        }
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
    public static void main(String[] args) {
        FileWatcher watcher = ControlCenter.getFileWatcher();
        watcher.start();
    }

}
