package com.watcher;

import java.util.Scanner;

/**
 * Created by Administrator on 2017-1-1.
 */
class ProcessWatch extends Thread {

    private Process process;

    private boolean over;

    ProcessWatch(Process process){
        super();
        this.process = process;
        over = false;
    }

    @Override
    public void run() {
        if(process == null)
            return;
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
    }

    public void setOver(boolean over) {
        this.over = over;
    }
}
