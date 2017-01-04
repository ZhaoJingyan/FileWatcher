package com.watcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * File Information that come from parameters.db.
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FileInformation {

    private int id;

    private String name;

    private String date;

    private Connection connection;

    private String sql;

    /**
     * 构造方法
     * @param id ID
     * @param name 文件名称
     * @param date 监控日期
     * @param connection 数据库连接
     */
    FileInformation(int id, String name, String date, Connection connection) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.connection = connection;
        this.sql = "update file_info set watch_date = ? where id=" + Integer.toString(id) ;
    }

    /**
     * 获取ID
     * @return ID
     */
    int getId() {
        return id;
    }

    /**
     * 获取文件匹配名称
     * @return 文件匹配名称
     */
    String getName() {
        return name;
    }

    /**
     * 获取日期字符串
     * @return 日期字符串
     */
    String getDate() {
        return date;
    }

    /**
     * 变更监控日期
     * @param date 日期字符串
     * @return 数据库影向行数
     */
    int setDate(String date) {
        PreparedStatement statement = null;
        int result;
        try{
            statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            result = statement.executeUpdate();
            if(result >= 0)
                this.date = date;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try{
            if(statement != null)
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
