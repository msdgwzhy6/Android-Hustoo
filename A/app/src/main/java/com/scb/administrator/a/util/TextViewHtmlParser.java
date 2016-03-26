package com.scb.administrator.a.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.scb.administrator.a.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class TextViewHtmlParser implements Html.ImageGetter{

    TextView mTextView;
    private Context mContext;
    int width;
    ProgressBar pb;
    public TextViewHtmlParser(Context c) {
        this.mContext=c;
    }
    public void setTextViewHtml(TextView textview,String htmlStr,ProgressBar pb){
        htmlStr=regStr(htmlStr);
        this.mTextView = textview;
        this.pb = pb;
        mTextView.setText(Html.fromHtml(htmlStr,this, null));
    }

    //对字符串进行一些操作
    private String regStr(String str){

        return str;
    }
    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();

        float scale = mContext.getResources().getDisplayMetrics().density;
        float pad = (20*scale*4);
          width = (int) (ScreenUtil.getScreenWidth(mContext)-pad);

        //预先设置图片(默认图片)加载前的宽高
        urlDrawable.setBounds(0, 0, width,0);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
                .extraForDownloader(null)
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                        //  .decodingOptions(null)
                .displayer(new SimpleBitmapDisplayer()) // default
                        .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .handler(new Handler()) // default
                .build();

        //返回一个实例
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(source,options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                //缩放图片宽为全屏

                    float w = bitmap.getWidth();


                    Matrix matrix = new Matrix();
                    float bili = (float) width / w;



                    matrix.postScale(bili, bili); //长和宽放大缩小的比例

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                urlDrawable.bitmap = bitmap;
                urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mTextView.invalidate();
                mTextView.setText(mTextView.getText()); // 解决图文重叠
                if(pb!=null) pb.setVisibility(View.GONE);
            }
        });


        return urlDrawable;
    }

}


