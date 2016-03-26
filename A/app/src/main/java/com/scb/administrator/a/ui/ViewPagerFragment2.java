package com.scb.administrator.a.ui;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.scb.administrator.a.MyApplication;
import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.MyAdapter;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;
import com.scb.administrator.a.util.ScreenUtil;
import com.software.shell.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

public class ViewPagerFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    private SwipeRefreshLayout mSwipeLayout;

    private MyAdapter mAdapter;


    @Override
    public void onResume() {
        onRefresh();
        super.onResume();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ListView actualListView = (ListView) getActivity().findViewById(R.id.qy_list);
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly_2);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(this);


        List<QiangYu> mData = new ArrayList<>();
        mAdapter = new MyAdapter(getActivity(), mData);
        actualListView.setAdapter(mAdapter);
        Hawk.init(getActivity())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                .build();

        onRefresh();


        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommentActivity.class);
                intent.putExtra("data", mAdapter.getItem(position));
                startActivity(intent);
            }
        });



    }

    /**
     * 覆盖此函数，先通过inflater inflate函数得到view最后返回
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_demo2, container, false);

        return v;
    }

    @Override
    public void onRefresh() {
        BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
        //负号降序
        query.order("-comment");

        query.include("author");
        query.setLimit(10);


        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(getActivity(), new FindListener<QiangYu>() {


            @Override
            public void onSuccess(List<QiangYu> list) {

                Hawk.remove("10");
                Hawk.put("10", list);

                mAdapter.refresh(list);

                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
            Toast.makeText(getActivity(),"检查网络后下拉刷新",Toast.LENGTH_SHORT).show();

                List<QiangYu> value = Hawk.get("10");
                if(value!=null) {

                    mAdapter.refresh(value);
                }
                mSwipeLayout.setRefreshing(false);
            }
        });


    }
}