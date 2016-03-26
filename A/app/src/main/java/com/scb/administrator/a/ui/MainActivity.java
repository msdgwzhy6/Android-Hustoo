package com.scb.administrator.a.ui;




import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import static android.view.Gravity.START;


import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.DrawerArrowDrawable;
import com.scb.administrator.a.MyApplication;
import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.FragmentAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;


public class MainActivity extends FragmentActivity {


    private TextView tv;





    private PagerSlidingTabStrip tabs;
    /**
     * 页面list *
     */
   private List<Fragment> fragmentList = new ArrayList<>();
    /**
     * 页面title list *
     */
  private  List<String> titleList = new ArrayList<>();

private  SharedPreferences skinSettingPreference;
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    private DrawerLayout drawer;
private int skin_type;
    private ImageView login,iv;
    int[]  skinResources = {  R.drawable.back2,R.drawable.back8
    };







    private RelativeLayout header;

    private View v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);













       skinSettingPreference = MainActivity.this.getSharedPreferences("skin",  Context.MODE_PRIVATE);
        String key = "skin_type";
       skin_type = skinSettingPreference.getInt(key, 0);

        iv = (ImageView) findViewById(R.id.back);
     try {
         Drawable d = this.getResources().getDrawable(skinResources[skin_type]);
         iv.setImageDrawable(d);
     }catch (Throwable e) {
         e.printStackTrace();

     }



        initView();

        if (MyApplication.getInstance().getCurrentUser() != null) {
            // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段

            imageInit(MyApplication.getInstance().getCurrentUser().getAvatar());
            tv.setText(MyApplication.getInstance().getCurrentUser().getUsername());
        }


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyApplication.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            }
        });


        fragmentList.add(new ViewPagerFragment1());
        fragmentList.add(new ViewPagerFragment2());
        fragmentList.add(new ViewPagerFragment3());


        titleList.add("首页");
        titleList.add("校园角Hot10");
        titleList.add("教务通知");


        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        vp.setOffscreenPageLimit(2);

        vp.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList, titleList));

   tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setTextColor(Color.WHITE);
        tabs.setViewPager(vp);







        drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        if(skin_type==0)
        drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.white));
        else
            drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.dark_gray));
        final ImageView  imageView = (ImageView) findViewById(R.id.drawer_indicator);

        imageView.setImageDrawable(drawerArrowDrawable);

        NavigationView navigationView =
                (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                offset = slideOffset;

                int alpha = (int) (slideOffset * 255) ;
                v.getBackground().setAlpha(alpha);
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }


            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }


        });

        //设置箭头图片的点击监�?
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });


    }














    private void initView() {

        TextView month, yue;
        month = (TextView) findViewById(R.id.month);
        yue = (TextView) findViewById(R.id.yue);

        Typeface typeFace = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/zz.ttf");
        month.setTypeface(typeFace);
        yue.setTypeface(typeFace);

        month.setText(getMonth());
         tv = (TextView) findViewById(R.id.tv_name);
        tv.setTypeface(typeFace);
        header = (RelativeLayout) findViewById(R.id.header);

           login = (ImageView) findViewById(R.id.iv_login);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        v = findViewById(R.id.title);
        v.getBackground().setAlpha(0);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


    }

    private String getMonth() {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());
        char[] month = date.toCharArray();


       if (month[5] == '1') {
            if (month[6] == '0')  date= "十";
            else if (month[6] == '1')  date= "十一";
            else if (month[6] == '2')  date= "十二";
        }
        else if (month[6] == '1')  date= "一";
        else if (month[6] == '2')  date= "二";
        else if (month[6] == '3')       date="三";
        else if (month[6] == '4')  date="四";
        else if (month[6] == '5')  date="五";
        else if (month[6] == '6')  date= "六";
        else if (month[6] == '7')  date="七";
        else if (month[6] == '8')  date="八";
        else if (month[6] == '9')  date= "九";
        else date=" ";


        return date;
    }


  private void imageInit(String Imageuri) {


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(Imageuri, login, options);

    }






    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()){


                            case R.id.nav_setting:
                                SharedPreferences.Editor editor = skinSettingPreference.edit();
                                String key  = "skin_type";

                                if(skin_type==0) {
                                    skin_type = 1;
                                    drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.dark_gray));
                                }
                                else
                                {  skin_type= 0;
                                    drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.white));
                                   }
                                editor.putInt(key, skin_type);
                                editor.commit();

                                try {
                                    Drawable d = MainActivity.this.getResources().getDrawable(skinResources[skin_type]);
                                    iv.setImageDrawable(d);
                                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(iv);


                                }catch (Throwable e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "换肤失败.", Toast.LENGTH_SHORT).show();

                                }finally {
                                    drawer.closeDrawer(START);
                                }


                                break;



                            case R.id.nav_book:
                                startActivity(new Intent(MainActivity.this, BookActivity.class));

                                break;

                            case R.id.nav_home:

                                startActivity(new Intent(MainActivity.this, SchoolActivity.class));



                                break;

                            case  R.id.nav_friends:



                                startActivity(new Intent(MainActivity.this, AboutActivity.class));

                                break;

                        }
                        menuItem.setChecked(true);

                        return true;
                    }
                });
    }


    @Override
    protected void onResume() {
        //currentUser = BmobUser.getCurrentUser(MainActivity.this,User.class);
        if (MyApplication.getInstance().getCurrentUser() != null) {
            // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段

            imageInit(MyApplication.getInstance().getCurrentUser().getAvatar());

        }
        else {
            tv.setText("注册/登录");
            login.setImageResource(R.drawable.ic_default_avatar_lite);
        }
        super.onResume();
    }



    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
