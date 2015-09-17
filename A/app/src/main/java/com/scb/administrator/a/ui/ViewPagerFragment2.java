package com.scb.administrator.a.ui;


import android.content.Intent;
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
    private ListView actualListView;
    private FloatingActionButton bt;
    private List<QiangYu> mData;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置


    @Override
    public void onResume() {
        onRefresh();
        super.onResume();
    }

    @Override
    public void onDestroy() {

      ;
        super.onDestroy();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
  //添加你的bmob的Key
        Bmob.initialize(getActivity(), KEY);
        actualListView = (ListView) getActivity().findViewById(R.id.qy_list);
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly_2);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(this);


        mData = new ArrayList<>();
        mAdapter = new MyAdapter(getActivity(),mData);
        actualListView.setAdapter(mAdapter);

        onRefresh();
        bt = (FloatingActionButton) getActivity().findViewById(R.id.w_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (MyApplication.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),CreateActivity.class);
                    startActivity(intent);
                } else   Toast.makeText(getActivity(),"请先登录帐号",Toast.LENGTH_SHORT).show();

            }
        });

        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommentActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);
            }
        });

       actualListView.setOnScrollListener(new OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {
               switch (scrollState) {
                   // 当不滚动时
                   case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                       scrollFlag = false;
                       // 判断滚动到底部
                       if (actualListView.getLastVisiblePosition() == (actualListView
                               .getCount() - 1)) {
                          // toTopBtn.setVisibility(View.VISIBLE);

                           bt.hide();
                       }
                       // 判断滚动到顶部
                       if (actualListView.getFirstVisiblePosition() == 0) {
                          // toTopBtn.setVisibility(View.GONE);
                           bt.show();
                       }

                       break;
                   case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                       scrollFlag = true;
                       break;
                   case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                       scrollFlag = true;
                       break;
               }
           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

               if (scrollFlag
                       && ScreenUtil.getScreenViewBottomHeight(actualListView) <= ScreenUtil
                       .getScreenHeight(getActivity())){
                   if (firstVisibleItem < lastVisibleItemPosition){
                       // 上滑
                       //toTopBtn.setVisibility(View.VISIBLE);
                       bt.show();
                   }
                   else if(firstVisibleItem > lastVisibleItemPosition){
                       // 下滑
                      // toTopBtn.setVisibility(View.GONE);
                       bt.hide();
                   }else {
                       return;
                   }
                   lastVisibleItemPosition = firstVisibleItem;

               }

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
        query.order("-updatedAt");
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("updatedAt", date);
        query.include("author");
        query.setLimit(10);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(getActivity(), new FindListener<QiangYu>() {


            @Override
            public void onSuccess(List<QiangYu> list) {


                mData = list;
                mAdapter.refresh(list);

                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
Toast.makeText(getActivity(),"检查网络后下拉刷新",Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
            }
        });


    }
}
