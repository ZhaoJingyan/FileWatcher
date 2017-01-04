package com.watcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Application Start.
 * Created by Zhao Jinyan on 2016/12/28.
 */
public class Main {

    private static FilesMonitor monitor = null;

    private static FilesFilter filter = null;

    private static FilesTable table = null;

    private static FilesTableWatcher watcher = null;

    private static FileSender sender = null;

    // 初始化程序
    private static void initialize(){

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
    private static void close(){

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
            if(FileSender.CLOSE.equals(message))
                senderClosed = true;
            if(FilesTableWatcher.CLOSE.equals(message))
                watcherClosed = true;
            if(FilesMonitor.CLOSE.equals(message))
                monitorClosed = true;
            if(FilesFilter.CLOSE.equals(message))
                filterClosed = true;
            if(senderClosed && watcherClosed && monitorClosed && filterClosed){
                Resources.close();
                System.out.println("程序关闭.");
            }
        }

    }

    /**
     * Main Method.
     *
     * @param args Command Lines
     */
    public static void main(String[] args) {

        // Initialize the Program.
        initialize();

        // Startup threads.
        filter.start();
        monitor.start();
        watcher.start();
        sender.start();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = reader.readLine();
                if("q".equals(input)){
                    close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("控制台输入异常");
            Resources.close();
            System.exit(-1);
        }

    }

}
