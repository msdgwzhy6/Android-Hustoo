package com.scb.administrator.a.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.scb.administrator.a.R;
import com.scb.administrator.a.ui.CommentActivity;

import java.lang.ref.WeakReference;

/**
 * Created by cxm on 2016/3/15.
 */
public class MyImageGetter implements Html.ImageGetter{

    WeakReference<TextView> mTextViewReference;
    Context mContext;

    public MyImageGetter(Context context, TextView textView) {
        mContext = context.getApplicationContext();
        mTextViewReference = new WeakReference<TextView>(textView);
    }

    @Override
    public Drawable getDrawable(String url) {

        final URLDrawable urlDrawable = new URLDrawable(mContext);

        // 异步获取图片，并刷新显示内容
        //new ImageGetterAsyncTask(url, urlDrawable).execute();
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

        final WeakReference<URLDrawable> mURLDrawableReference =  new WeakReference<URLDrawable>(urlDrawable);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(url,options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {


                float w = bitmap.getWidth();

                float scale = mContext.getResources().getDisplayMetrics().density;

                float width = 210*scale;

                Matrix matrix = new Matrix();
                float bili = (float) width / w;



                matrix.postScale(bili, bili); //长和宽放大缩小的比例

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);

                Rect bounds = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

                if (mURLDrawableReference.get() != null) {
                    mURLDrawableReference.get().setBounds(bounds);
                }
                bitmapDrawable.setBounds(bounds);
                if(bitmapDrawable!=null){

                    if (mURLDrawableReference.get() != null) {
                        mURLDrawableReference.get().drawable = bitmapDrawable;
                    }
                    if (mTextViewReference.get() != null) {
                        // 加载完一张图片之后刷新显示内容
                        mTextViewReference.get().setText(mTextViewReference.get().getText());
                    }

                }


            }
        });

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        WeakReference<URLDrawable> mURLDrawableReference;
        String mUrl;

        public ImageGetterAsyncTask(String url, URLDrawable drawable) {
            mURLDrawableReference = new WeakReference<URLDrawable>(drawable);
            mUrl = url;
        }

        @Override
        protected Drawable doInBackground(String... params) {

            // 下载图片，并且使用缓存
            //显示图片的配置
            // 下载图片，并且使用缓存
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.a)
                    .showImageOnFail(R.drawable.a)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                    .displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            final Bitmap[] bitmap = {null};
            final BitmapDrawable[] bitmapDrawable = {null};
            try {

                ImageLoader.getInstance().loadImage(mUrl, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap rbitmap) {
                        bitmap[0] = rbitmap;

                        bitmapDrawable[0] = new BitmapDrawable(mContext.getResources(), bitmap[0]);

                        Rect bounds = new Rect(0, 0, bitmap[0].getWidth(), bitmap[0].getHeight());

                        if (mURLDrawableReference.get() != null) {
                            mURLDrawableReference.get().setBounds(bounds);
                        }
                        bitmapDrawable[0].setBounds(bounds);
                        ;
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });


                return bitmapDrawable[0];
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }




        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (null != result) {
                if (mURLDrawableReference.get() != null) {
                    mURLDrawableReference.get().drawable = result;
                }
                if (mTextViewReference.get() != null) {
                    // 加载完一张图片之后刷新显示内容
                    mTextViewReference.get().setText(mTextViewReference.get().getText());
                }
            }
        }
    }


    public class URLDrawable extends BitmapDrawable {
        protected Drawable drawable;

        public URLDrawable(Context context) {
            // 设置默认大小和默认图片
            Rect bounds = new Rect(0, 0, 140, 140);
            setBounds(bounds);
            drawable = context.getResources().getDrawable(R.drawable.a);
            drawable.setBounds(bounds);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}
