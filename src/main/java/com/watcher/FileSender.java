package com.watcher;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 文件发送器。
 * Created by Zhao Jinyan on 2016-12-30.
 */
class FileSender extends Thread {

    static final String CLOSE = "file sender closed.";

    private LinkedBlockingDeque<String> queue;

    private FilesTable table;

    private boolean running = true;

    /**
     * 构造方法.
     * @param queue 消息接收队列
     * @throws WatcherException 初始化失败异常
     */
    FileSender(LinkedBlockingDeque<String> queue, FilesTable table) throws WatcherException {
        super();
        if(queue == null)
            throw new WatcherException("接收队列为空!!!");
        if(table == null)
            throw new WatcherException("文件列表为空!!!");
        this.queue = queue;
        this.table = table;
    }

    boolean isRunning(){
        return this.running;
    }

    @Override
    public void run() {
        try{
            while(isRunning()){
                String name = queue.take();
                String path = Resources.PATH() + '\\' + name;
                System.out.printf("准备发送文件%s\n", path);
                if(send(path) == 0)
                    table.updateFile(name);
                else
                    System.out.println("发送失败");
            }
        }catch(InterruptedException e){
            System.out.printf("File Sender 线程堵塞被终止[%s]!!!\n", e.getMessage());
        }

        ControlCenter.putMessage(CLOSE);

    }

    /**
     * 通过执行bat脚本发送文件.
     * @param path 文件完整路径
     * @return bat返回值
     * @throws InterruptedException 进程堵塞中断
     */
    private int send(String path) throws InterruptedException{
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(".\\CopyFile.bat " + path);
            ProcessWatch watch = new ProcessWatch(process);
            watch.start();
            process.waitFor();
            watch.setOver(true);
            int exitVal = process.exitValue();
            if(exitVal == 0){
                System.out.printf("%s 发送成功[%d]...\n", path, exitVal);
            }
            return exitVal;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("%s 发送失败!!!\n", path);
            return 1;
        }

    }

    /**
     * 关闭线程
     */
    void close(){
        this.interrupt();
        this.running = false;
    }

}