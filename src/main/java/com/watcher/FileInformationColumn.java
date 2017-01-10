package com.watcher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * Created by Zhao Jinyan on 2016/12/28.
 */
class FileInformationColumn {

    private static final DateFormat format  = new SimpleDateFormat("yyyyMMdd");

    private static final Calendar calendar = new GregorianCalendar();

    private FileInformation information;

    private String name; // File Name

    private Date watchDate;

    private String type;

    private long time;

    private static String getDateString(Date date){
        return format.format(date);
    }

    private static Date getDate(String dateString) throws ParseException {
        return format.parse(dateString);
    }

    /**
     *
     * @param information File Information
     * @throws ParseException 日期转化为字符串失败抛出异常
     */
    FileInformationColumn(FileInformation information) throws ParseException {
        this.information = information;
        this.watchDate = getDate(information.getDate());
        this.name = information.getName().replaceFirst(Resources.PATTERN(), information.getDate());
        this.type = "NULL";
        this.time = new Date().getTime();
        System.out.println(this);
    }

    /**
     * 日期加一
     */
    void addOneDay(){
        synchronized (calendar){
            calendar.setTime(this.watchDate);
            calendar.add(Calendar.DATE, 1);
            this.watchDate = calendar.getTime();
            this.time = new Date().getTime();
            information.setDate(getDateString(this.watchDate));
            this.name = information.getName().replaceFirst(Resources.PATTERN(), information.getDate());
            this.type = "NULL";
        }
    }

    /**
     * 获取文件名.
     * @return File Name
     */
    String getName() {
        return name;
    }

    /**
     * 文件此时的状态.
     * @return File Type
     */
    String getType() {
        return type;
    }

    /**
     * 设置文件状态
     * @param type File Type
     */
    void setType(String type) {
        this.type = type;
    }

    /**
     * 获取时间
     * @return Long表示的时间
     */
    long getTime() {
        return time;
    }

    void setTime(long time) {
        this.time = time;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Column: {id: %d, name: %s, type: %s, date:%s, time: %d}", information.getId(), getName(), getType(), information.getDate(), getTime());
    }
}
