package com.watcher;

/**
 * 线程消息.
 * Created by Zhao Jinyan on 2016/12/28.
 */
interface Message<T> {

    /**
     * Data in the message.
     *
     * @return information
     */
    T data();

    /**
     * Message type.
     *
     * @return Message Type
     */
    String type();

    /**
     * 消息生成时间
     *
     * @return 时间(毫秒)
     * @see java.util.Date#getTime()
     */
    long time();

}
