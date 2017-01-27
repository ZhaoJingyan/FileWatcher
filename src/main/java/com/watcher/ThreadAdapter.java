package com.watcher;

/**
 * 线程适配器, 所有线程的基类.
 * Created by Zhao Jinyan on 2017/1/8.
 */
abstract class ThreadAdapter extends Thread {

    static final String ERROR = "error";

    static final String CLOSED = "closed";

    static final String STARTED = "started";

    private boolean running = true;

    // Close Thread Message
    static class ThreadMessage implements Message<String> {

        private String name;

        private String type;

        ThreadMessage(String name, String type){
            this.name = name;
            this.type = type;
        }

        @Override
        public String data() {
            return name;
        }

        @Override
        public String type() {
            return type;
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
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        ControlCenter.putMessage(new ThreadMessage(this.getName(), STARTED));
        try{
            while(isRunning())
                execute();
        } catch (InterruptedException e){
            ControlCenter.putInformation(String.format("%s线程堵塞被终止[%s]!!!", this.getName(), e.getMessage()));
        }
        ControlCenter.putMessage(new ThreadMessage(this.getName(), CLOSED));
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
