package com.scb.administrator.a.entity;


import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class Notice extends DataSupport {

    private  String notice;
    private  String uri;

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
