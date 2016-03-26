package com.scb.administrator.a;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.scb.administrator.a.entity.User;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/7/5 0005.
 */
public class MyApplication  extends Application {



    private static MyApplication myApplication = null;
    public static MyApplication getInstance(){
        return myApplication;
    }

    public User getCurrentUser() {
        User user = BmobUser.getCurrentUser(myApplication, User.class);
        if(user!=null){
            return user;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        Bmob.initialize(getApplicationContext(), "Your_Bmob_App_Key");
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }


}
