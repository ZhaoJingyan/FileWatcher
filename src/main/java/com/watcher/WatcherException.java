package com.watcher;

/**
 * Application Exception.
 * Created by Zhao Jinyan on 2016-12-29.
 */
class WatcherException extends Exception {

    WatcherException(String message) {
        super(message);
    }

    WatcherException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
