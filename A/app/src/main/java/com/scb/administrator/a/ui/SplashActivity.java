package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.scb.administrator.a.R;
import com.scb.administrator.a.SpreadView;

public class SplashActivity extends Activity {



    private SpreadView sv,sv1,sv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        sv = (SpreadView) findViewById(R.id.sv);
        sv1 = (SpreadView) findViewById(R.id.sv1);
        sv2 = (SpreadView) findViewById(R.id.sv2);

        int statusBarHeight = getStatusHeight(SplashActivity.this);


        float scale = getResources().getDisplayMetrics().density;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth= (int) (metrics.widthPixels-(67 * scale + 0.5f));            //屏幕宽度
        int screenHeight= (int) (metrics.heightPixels-statusBarHeight-(67 * scale + 0.5f));        //屏幕高度

        ImageView  iv = (ImageView) findViewById(R.id.go);

        sv.setX(screenWidth);
        sv.setY(screenHeight);

        sv1.setX(screenWidth);
        sv1.setY(screenHeight);

        sv2.setX(screenWidth);
        sv2.setY(screenHeight);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sv1.setStart(false);
                sv.setStart(false);
                sv2.setStart(false);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                finish();
            }
        });



    }

    private int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    @Override
    protected void onResume() {
        sv1.setStart(false);
        sv.setStart(false);
        sv2.setStart(false);
        sv1.setStart(true);
        sv.setStart(true);
        sv2.setStart(true);
        sv1.setFirst(true);
        sv.setFirst(true);
        sv2.setFirst(true);
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


}
