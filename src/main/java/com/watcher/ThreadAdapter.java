package com.watcher;

/**
 * 线程适配器, 所有线程的基类.
 * Created by Zhao Jinyan on 2017/1/8.
 */
abstract class ThreadAdapter extends Thread {

    private boolean running = true;

    private CloseMessage closeMessage;

    // Close Thread Message
    private static class CloseMessage implements Message<String> {

        private String name;

        CloseMessage(String name){
            this.name = name;
        }

        @Override
        public String data() {
            return name;
        }

        @Override
        public String type() {
            return "close";
        }

        @Override
        public long time() {
            return 0;
        }
    }

    /**
     * Constructor.
     * @param name Thread Name
     */
    ThreadAdapter(String name){
        super(name);
        closeMessage = new CloseMessage(name);
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        try{
            while(isRunning())
                execute();
        } catch (InterruptedException e){
            System.out.printf("%s线程堵塞被终止[%s]!!!\n", this.getName(), e.getMessage());
        }
        ControlCenter.putMessage(closeMessage.data());
    }

    /**
     * Close the thread.
     */
    void close(){
        running = false;
        this.interrupt();
    }

    /**
     * 线程的主体逻辑.
     * @throws InterruptedException 中断
     */
    protected abstract void execute() throws InterruptedException;

    /**
     * 判断线程是否关闭
     * @return 如果线程关闭, 则返回false
     */
    final boolean isRunning(){
        return this.running;
    }

}
