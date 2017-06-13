package com.example.admin.timetable;

import java.io.Serializable;

/**
 * Created by admin on 2016/12/8.
 */
public class Course implements Serializable {
    private String name;
    private String teacher;
    private String email_ftp;
    private int week;
    private int start;
    private int end;
    private String address;
    private String ddl;
    private String ddl_time;
    private int lamp;
    Course() {
        name = "";
        teacher = "";
        email_ftp = "";
        week = 0;
        start = 0;
        end = 0;
        address = "";
        ddl = "";
        ddl_time = "";
        lamp = 0;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public String getAddress() {
        return address;
    }

    public String getDdl() {
        return ddl;
    }

    public String getEmail_ftp() {
        return email_ftp;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getWeek() {
        return week;
    }

    public int getLamp() {
        return lamp;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public void setEmail_ftp(String email_ftp) {
        this.email_ftp = email_ftp;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDdl_time() {
        return ddl_time;
    }

    public void setDdl_time(String ddl_time) {
        this.ddl_time = ddl_time;
    }

    public void setLamp(int lamp) {
        this.lamp = lamp;
    }

}
