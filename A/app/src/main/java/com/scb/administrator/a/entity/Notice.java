package com.scb.administrator.a.entity;




/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class Notice  {

    private  String notice;
    private  String uri;
    private  String time;

    public String getTime() {
        return time;
    }

    public Notice(String notice, String uri ,String time) {
        this.notice = notice;
        this.time = time;
        this.uri = uri;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Notice(String notice, String uri) {
        this.notice = notice;
        this.uri = uri;

    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
