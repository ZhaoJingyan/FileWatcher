package com.watcher;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 控制中心.
 * Created by Zhao Jinyan on 2017-1-1.
 */
class ControlCenter {

    private static LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>();

    private static FileWatcher fileWatcher = null;

    private static class DefaultFileWatcher extends FileWatcher {

        DefaultFileWatcher() {
            super();
        }

        @Override
        void start() {
            // Startup threads.
            getFilter().start();
            getMonitor().start();
            getWatcher().start();
            getSender().start();
        }

    }

    static FileWatcher getFileWatcher() {
        if (fileWatcher == null)
            fileWatcher = new DefaultFileWatcher();
        return fileWatcher;
    }

    synchronized static void putMessage(String message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static String takeMessage() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
