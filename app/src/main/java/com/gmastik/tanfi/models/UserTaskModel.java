package com.gmastik.tanfi.models;

public class UserTaskModel {
    private String title,time_line,date,desc;

    public UserTaskModel() {
    }

    public UserTaskModel(String title, String time_line, String date, String desc) {
        this.title = title;
        this.time_line = time_line;
        this.date = date;
        this.desc = desc;
    }

    public String getTime_line() {
        return time_line;
    }

    public void setTime_line(String time_line) {
        this.time_line = time_line;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
