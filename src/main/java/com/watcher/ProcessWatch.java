package com.watcher;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 刷新bat执行时的控制台输出流.
 * Created by Zhao Jinyan on 2017-1-1.
 */
class ProcessWatch extends Thread {

    private Process process = null;

    private boolean running = true;

    private boolean over;

    private Lock lock;

    private Condition condition;

    ProcessWatch(){
        super();
        over = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    boolean isRunning(){
        return running;
    }

    @Override
    public void run() {
        try{
            while (isRunning()){
                refresh();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void refresh() throws InterruptedException{
        lock.lockInterruptibly();
        try{
            if(process == null)
                condition.await();
            Scanner scanner = new Scanner(process.getInputStream());
            while(true){
                if(over)
                    break;
                String before = null;
                while(scanner.hasNext()){
                    String temp = scanner.nextLine();
                    if(before == null || !temp.equals(before)){
                        System.out.println(temp);
                        before = temp;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void close(){
        running = false;
        interrupt();
    }

    void setOver(boolean over) {
        this.over = over;
        this.process = null;
    }

    void setProcess(Process process) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            this.process = process;
            this.over = false;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
