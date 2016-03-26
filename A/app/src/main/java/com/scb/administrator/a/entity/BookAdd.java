package com.scb.administrator.a.entity;

/**
 * Created by Administrator on 2015/9/23 0023.
 */
public class BookAdd {
    private String o;
    private String  t ;
    private String thr;

    public BookAdd(String o, String t, String thr) {
        this.o = o;
        this.t = t;
        this.thr = thr;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getThr() {
        return thr;
    }

    public void setThr(String thr) {
        this.thr = thr;
    }
}
