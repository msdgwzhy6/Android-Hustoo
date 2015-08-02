package com.scb.administrator.a.ui;




import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ViewSwitcher;

import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.DrawerArrowDrawable;
import com.scb.administrator.a.MyApplication;
import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.FragmentAdapter;
import com.scb.administrator.a.util.AboutActivity;


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


    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    private DrawerLayout drawer;

    private ImageView login;

    public final Handler.Callback mHandlerCallback = new Handler.Callback(){


        @Override
        public boolean handleMessage(Message msg) {


            switch (msg.what){

                case 0:
                    final ViewSwitcher container = (ViewSwitcher) findViewById(R.id.view_container);
                    container.showNext();
                    ImageView view = (ImageView) container.getChildAt(0);
                    view.setImageResource(0);
                    container.removeViewAt(0);
                    mSplashing = false;
                    return true;

                default:
                    return false;

            }


        }
    };

    private   Handler mHandler = new Handler(mHandlerCallback);



    private RelativeLayout header;

    private View v;
  /*  private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_LAYOUT:
                    final ViewSwitcher container = (ViewSwitcher) findViewById(R.id.view_container);
                    container.showNext();
                    ImageView view = (ImageView) container.getChildAt(0);
                    view.setImageResource(0);
                    container.removeViewAt(0);
                    mSplashing = false;
                    break;
                default:
                    break;
            }
        }
    };
    */
    private boolean mSplashing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSplashing = true;
        mHandler.sendEmptyMessageDelayed(0, 2000);
      //  mMainHandler.sendEmptyMessageDelayed(MSG_SHOW_LAYOUT, 3000);
        Bmob.initialize(MainActivity.this, "4ef015e97fb35a0f58b00043679e2b9a");

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
        titleList.add("校园角New10");
        titleList.add("教务通知");


        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        vp.setOffscreenPageLimit(2);

        vp.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList, titleList));


        tabs.setTextColor(Color.WHITE);
        tabs.setViewPager(vp);





        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.dark_gray));
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

                int alpha = (int) (slideOffset * 252) ;
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


        if (month[6] == '3')       date="三";
        else if (month[6] == '4')  date="四";
        else if (month[6] == '5')  date="五";
        else if (month[6] == '6')  date= "六";
        else if (month[6] == '7')  date="七";
        else if (month[6] == '8')  date="八";
        else if (month[6] == '9')  date= "九";
        else if (month[6] == '1') {
            if (month[5] == '0')  date= "十";
            else if (month[5] == '1')  date= "十一";
            else if (month[5] == '2')  date= "十二";
        } else
            date=" ";


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
                .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
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

                            case R.id.nav_discussion :
                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                                intent.putExtra(Intent.EXTRA_TEXT, "我正在使用华科Hustoo，快点来吧！http://hustoo.bmob.cn/");
                                startActivity(Intent.createChooser(intent, "分享到"));

                                break;
                            case R.id.nav_setting:
                                Toast.makeText(MainActivity.this, "功能开发中，请期待.", Toast.LENGTH_SHORT).show();

                                break;

                            case R.id.nav_exit:
                                finish();
                           break;

                            case R.id.nav_home:
                                if (MyApplication.getInstance().getCurrentUser()!= null) {
                                    Intent it = new Intent();
                                    it.setClass(MainActivity.this, SchoolActivity.class);
                                    startActivity(it);
                                } else {

                                   Toast.makeText(MainActivity.this,"登录才可使用此功能",Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case  R.id.nav_friends:
                                Intent it = new Intent();
                                it.setClass(MainActivity.this, AboutActivity.class);
                                startActivity(it);
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
    public void onBackPressed() {
        if (!mSplashing) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
