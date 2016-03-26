package com.scb.administrator.a;

/**
 * Created by Administrator on 2015/6/5 0005.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import android.widget.ImageView;




public class CircleImageView extends ImageView{

    public CircleImageView(Context context) {
        super(context);

    }
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    protected void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true); //防止边缘的锯齿
        paint.setColor(Color.WHITE); //这里的颜色决定了边缘的颜色

        Drawable drawable = getDrawable();
        if(drawable == null){
            return ;
        }
        if(getWidth() == 0 || getHeight() == 0){
            return ;
        }
     //先类型转换

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
		//creates a mutable copy of the image using the option specified.
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth() ;
        int h = getHeight() ;
        

        //圆形ImageView的半径为布局中的ImageView定义大小
        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);

        canvas.drawARGB(0, 0, 0, 0);
        //这才开始画圆
        canvas.drawCircle(w / 2, h / 2, w / 2 , paint);
        //x0，y0 距离左边为0 ，距离上边为0
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap sbmp;
		//以宽为标准
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),Config.ARGB_8888);


        //output不仅起到设置画布大小的作用，而是在output上画画。最后返回output
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
		//图像的抖动处理,保持好的图片效果
        paint.setDither(true);
		// 把画笔设置为透明
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
     //paint.setStyle(Paint.Style.FILL);画笔默认的style

        //第一个参数为圆心x坐标，二为圆心的y坐标，第三个参数为半径减去的数值为白边的宽度.
        //这里剪下的是图片的半径。
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2-2, paint);
        // 取两层绘制交集。显示前景色。
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}