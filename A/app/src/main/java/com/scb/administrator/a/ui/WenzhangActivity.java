package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.Wenzhang;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class WenzhangActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView title , mainWen ,time;
    private SwipeRefreshLayout sr ;
    private SharedPreferences wenzhang;
    private String num1;
    int former;
    private ScrollView sv_wz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wenzhang);

        initView();

        Intent intent = getIntent();
        num1 = intent.getStringExtra("tittle");
        title.setText(num1);
        sv_wz = (ScrollView) findViewById(R.id.sv_wz);
        wenzhang = WenzhangActivity.this.getSharedPreferences("wenzhang", Context.MODE_PRIVATE);
        String sb = wenzhang.getString("tittle"," ");
       former = 0;

        if (sb != null) {
            if(sb.equals(num1))
            {  mainWen.setText(Html.fromHtml(wenzhang.getString("main"," ")));
                former = wenzhang.getInt("former",0);


            }
            else mainWen.setText("\n\n\n\n\n");
        }

        sr.setOnRefreshListener(this);
        onRefresh();



    }

    private void initView() {

        sr = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly_wenzhang);
        sr.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        title= (TextView) findViewById(R.id.biaoti);
        time=(TextView) findViewById(R.id.time);
        mainWen= (TextView) findViewById(R.id.mainWen);

    }


    @Override
    public void onRefresh() {
        BmobQuery<Wenzhang> query = new BmobQuery<Wenzhang>();
        query.setLimit(1);
        query.addWhereEqualTo("tittle", num1);
        query.findObjects(WenzhangActivity.this, new FindListener<Wenzhang>() {


            @Override
            public void onSuccess(List<Wenzhang> list) {


                time.setText(list.get(0).getTime());
                mainWen.setText(Html.fromHtml(list.get(0).getMain()));
                sr.setRefreshing(false);
                SharedPreferences.Editor editor = wenzhang.edit();
                editor.putString("tittle", num1);
                editor.putString("main", list.get(0).getMain());
                editor.commit();

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(WenzhangActivity.this, "请检查一下网络连接", Toast.LENGTH_SHORT).show();
                sr.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = wenzhang.edit();
        editor.putInt("former", sv_wz.getScrollY());
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sv_wz.post(new Runnable() {
            @Override
           	    public void run() {
                      // TODO Auto-generated method stub
                        sv_wz.scrollTo(0,former);
                       }
                });
        }
    }

