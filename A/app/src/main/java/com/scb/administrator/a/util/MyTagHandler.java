package com.scb.administrator.a.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.scb.administrator.a.R;


import org.xml.sax.XMLReader;

import java.util.Locale;

/**
 * Created by cxm on 2016/3/15.
 */
public class MyTagHandler implements Html.TagHandler {

    private Context mContext;

    public MyTagHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        // 处理标签<img>
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len-1, len, ImageSpan.class);
            String imgURL = images[0].getSource();

            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(mContext, imgURL), len-1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {

        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 进行图片点击之后的处理
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

            final Dialog processDialog = new Dialog(context,R.style.processDialog);
            processDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            processDialog.setCancelable(true);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_phtoto, null);
            final SubsamplingScaleImageView sv = (SubsamplingScaleImageView) layout.findViewById(R.id.imageView);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(url, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {


                    sv.setImage(ImageSource.bitmap(bitmap));
                    processDialog.setContentView(layout);
                    processDialog.show();



                }
            });





        }
    }
}