package com.watcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parameters in the parameters.db.
 * Created by Zhao Jinyan on 2016/12/28.
 */
class Parameters {

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

    final static String PATTERN = "\\{0\\}";

    private static Connection connection;

    private static boolean initialized;

    private static String path;

    private static void initialize() throws SQLException, WatcherException {
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("SELECT NAME, VALUE FROM VARIABLE");
        while(set.next()){
            String name = set.getString("NAME");
            String value = set.getString("VALUE");
            if("PATH".equals(name)){
                path = value;
            }
        }
        set.close();
        statement.close();
        if(path == null)
            throw new WatcherException("NOT SET PATH!!!");
    }

    static String PATH(){
        return path;
    }

    static boolean isInitialized(){
        return initialized;
    }

    static void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get File Information from parameters.db.
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
