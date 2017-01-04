package com.watcher;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Files Table(文件列表).
 *
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FilesTable {

    private List<FileInformationColumn> columns;

    private final Object lock = new Object();

    /**
     * 构造方法。
     * @throws SQLException 数据库访问异常
     * @throws WatcherException 初始化失败
     * @throws ParseException 字符串格式化转换失败
     */
    FilesTable() throws SQLException, WatcherException, ParseException {
        columns = new ArrayList<>();
        List<FileInformation> informations = Resources.getFileInformations();
        if (informations == null || informations.size() == 0)
            throw new WatcherException("没有文件信息...");
        for (FileInformation information : informations) {
            columns.add(new FileInformationColumn(information));
        }
    }

    /**
     * 当被监控的文件目录发生变化时，就是调用此方法。
     * 此方法会过滤无关文件变化消息（message），根据有效的文件变化消息更新文件列表。
     * @param message 文件变化消息，其中包含文件名，和事件类型
     * @return 如果返回为false，说明message被过滤
     */
    boolean put(Message message) {
        boolean result = false;
        synchronized (lock) {
            FileInformationColumn column = getColumns(message.info());
            if (column != null) {
                if (column.getType().equals("NULL")) {
                    column.setType(message.type());
                    column.setTime(message.time());
                    System.out.printf("%s 开始监控...\n", message.info());
                    result = true;
                }
                if(column.getType().equals("ENTRY_CREATE") || column.getType().equals("ENTRY_MODIFY")){
                    column.setType(message.type());
                    column.setTime(message.time());
                    System.out.printf("%s 刷新信息\n", message.info());
                    result = true;
                }
            }
        }
        return result;
    }

    private FileInformationColumn getColumns(String name) {
        if (name == null)
            return null;
        for (FileInformationColumn column : columns) {
            if (name.equals(column.getName()))
                return column;
        }
        return null;
    }

    /**
     * 刷新文件列表.
     * @param time 比对时间（就是当前时间）
     * @param adapter 文件处理适配器，用于回调文件处理逻辑
     * @throws InterruptedException 堵塞中断异常
     */
    void refresh(long time, FileDisposeAdapter adapter) throws InterruptedException {
        if(adapter == null)
            return;
        synchronized (lock) {
            for (FileInformationColumn column : columns){
                if("ENTRY_CREATE".equals(column.getType()) || "ENTRY_MODIFY".equals(column.getType())){
                    if(time - column.getTime() >= Resources.WATCHED_TIME()){
                        System.out.printf("%s 10分钟无变化，开始处理...\n", column.getName());
                        column.setType("DISPOSE");
                        adapter.dispose(column.getName());
                    }
                }
            }
        }
    }

    /**
     * 更新文件信息.
     * @param name File Name
     */
    void updateFile(String name){
        if(name == null)
            return;
        synchronized (lock){
            for(FileInformationColumn column : columns){
                if(name.equals(column.getName())){
                    column.addOneDay();
                    return;
                }
            }
        }
    }

}
