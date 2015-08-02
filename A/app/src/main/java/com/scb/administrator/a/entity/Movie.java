package com.scb.administrator.a.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/5 0005.
 */
public class Movie  extends BmobObject {
    String movieName;
    String movieUri;
    String toUri;

    public String getToUri() {
        return toUri;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieUri() {
        return movieUri;
    }
}
