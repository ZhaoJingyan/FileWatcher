package com.watcher;

/**
 * 文件处理适配器.
 * Created by Zhao Jinyan on 2016-12-30.
 *
 * @see com.watcher.FilesTable#refresh(long, FileDisposeAdapter)
 */
interface FileDisposeAdapter {

    /**
     * 当文件列表刷新时，本方法会被调用。
     *
     * @param name 文件名称
     * @throws InterruptedException 线程堵塞中断
     */
    void dispose(String name) throws InterruptedException;

}
