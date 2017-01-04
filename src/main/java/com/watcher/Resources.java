package com.watcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Resources in the parameters.db.
 * Created by Zhao Jinyan on 2016/12/28.
 */
class Resources {

    static {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:.\\parameters.db");
            initialize();
            initialized = true;
        } catch (ClassNotFoundException | SQLException | WatcherException e) {
            e.printStackTrace();
            initialized = false;
        }

    }

    private static String pattern;

    private static Connection connection;

    private static int watchedTime;

    private static boolean initialized;

    private static String path;

    private static void initialize() throws SQLException, WatcherException {
        watchedTime = 600000;
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT NAME, VALUE FROM VARIABLE");
        while(set.next()){
            String name = set.getString("NAME");
            String value = set.getString("VALUE");
            if("PATH".equals(name)){
                path = value;
            } else if("PATTERN".equals(name)){
                pattern = value;
            } else if("WATCHED_TIME".equals(name)){
                watchedTime = Integer.parseInt(name);
            }
        }
        set.close();
        statement.close();
        if(path == null)
            throw new WatcherException("NOT SET PATH!!!");
        if(pattern == null)
            throw new WatcherException("NOT SET PATTERN!!!");
    }

    /**
     * 获取监控目录
     * @return 监控目录
     */
    static String PATH(){
        return path;
    }

    /**
     * 过去文件命中被替换的时间表达式
     * @return 正则表达式
     */
    static String PATTERN() { return pattern; }

    /**
     * 监控文件时间.
     * @return 毫秒
     */
    static int WATCHED_TIME(){
        return watchedTime;
    }

    /**
     * 确认资源事是否初始化成功.
     * @return 初始化成功返回true
     */
    static boolean isInitialized(){
        return initialized;
    }

    /**
     * Close the database connection.
     */
    static void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get File parameters from parameters.db.
     * @return All Information
     * @throws SQLException Database Exception
     */
    static List<FileInformation> getFileInformations() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT ID,NAME,WATCH_DATE FROM FILE_INFO");
        List<FileInformation> result = new ArrayList<>();
        while(set.next()){
            int id = set.getInt("ID");
            String name = set.getString("NAME");
            String watchDate = set.getString("WATCH_DATE");
            FileInformation information = new FileInformation(id, name, watchDate, connection);
            result.add(information);
        }
        set.close();
        statement.close();
        return result;
    }

}
