package com.scb.administrator.a.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2015/7/7 0007.
 */
public class User extends BmobUser {

    public static final String TAG = "User";

    private String signature;
    private String avatar;
    private BmobRelation favorite;


    public BmobRelation getFavorite() {
        return favorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.favorite = favorite;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
