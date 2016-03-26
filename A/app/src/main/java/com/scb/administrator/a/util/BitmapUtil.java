package com.scb.administrator.a.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.scb.administrator.a.R;

/**
 * ͼƬ���ع�����
 *
 * @author gaozhibin
 *
 */
public class BitmapUtil {
    private static final String TAG = "BtimapUtil";




    public static Bitmap getBitmap(String address,Context context) throws Exception{

        // 1.通过Url对象封装url对象
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(3000);

        // 2.判断是否有缓存文件
        File cacheFile = new File(context.getCacheDir(),
                URLEncoder.encode(address));// 缓存文件
        if (cacheFile.exists())// 判断是否有缓存
            conn.setIfModifiedSince(cacheFile.lastModified());// 发送缓存文件的最后修改时间

        // 3.获取状态码，根据状态吗来判断接下来的操作。读取文件？还是写缓存，写缓存的时候记着用多个线程
        int code = conn.getResponseCode();
        if (code == 200) {
            byte[] data = Util.read(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);// 转化为图片
            weiteCache(cacheFile, bm);
            return bm;
        } else if(code==304){
            return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());//读取本地数据，并转为图片来显示
        }

//        如果不成功，抛异常，给我们自己看的
        throw new NetworkErrorException("访问服务器出错："+code);


    }
    private static void weiteCache(final File cacheFile, final Bitmap bm) {
        // 使用一个新的线程来写，这样的好处就是在页面打开的时候不会因为写缓存而等待时间
        new Thread() {
            public void run() {
                try {
                    FileOutputStream fos = new FileOutputStream(cacheFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);//指定格式 存储到本地
                    fos.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }.start();

    }

    public static Bitmap downloadBitmap(String url,Context context){


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        final Bitmap[] rbitmap = {null};

        ImageLoader.getInstance().loadImage(url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    rbitmap[0] =  bitmap;
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });










        return rbitmap[0];
    }


}