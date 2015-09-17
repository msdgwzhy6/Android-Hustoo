package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.MyAdapter;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;

import com.scb.administrator.a.util.ScreenUtil;
import com.software.shell.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;


public class SchoolActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {



    private SwipeRefreshLayout mSwipeLayout;
    private MyAdapter mAdapter;
    private ListView actualListView;
    private FloatingActionButton bt;
    private List<QiangYu> mData;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private User currentUser;

    private int pageNum;


    private RelativeLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        actualListView = (ListView) findViewById(R.id.s_list);
  //添加你的bmob的Key
        Bmob.initialize(SchoolActivity.this, KEY);


        header = (RelativeLayout) findViewById(R.id.header_view);



        LayoutInflater
                lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.header, actualListView, false);

        //设置不可点击
        actualListView.addHeaderView(headerView ,null,false);

        mSwipeLayout = (SwipeRefreshLayout) SchoolActivity.this.findViewById(R.id.id_swipe_ly_s);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(this);



        mData = new ArrayList<>();
        mAdapter = new MyAdapter(SchoolActivity.this,mData);
        actualListView.setAdapter(mAdapter);






        bt = (FloatingActionButton) findViewById(R.id.s_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUser = BmobUser.getCurrentUser(SchoolActivity.this, User.class);
                if (currentUser != null) {
                    Intent intent = new Intent();
                    intent.setClass(SchoolActivity.this, CreateActivity.class);
                    startActivity(intent);
                } else Toast.makeText(SchoolActivity.this, "请先登录帐号", Toast.LENGTH_SHORT).show();

            }
        });

        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(SchoolActivity.this, CommentActivity.class);
                intent.putExtra("data", mData.get(position - 1));
                startActivity(intent);
            }
        });




        fetchComment();


        actualListView.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (actualListView.getLastVisiblePosition() == (actualListView
                                .getCount() - 1)) {

                       fetchComment();
                            bt.hide();

                        }
                        // 判断滚动到顶部
                        if (actualListView.getFirstVisiblePosition() == 0) {

                            bt.show();
                        }

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = true;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(actualListView) <= ScreenUtil
                        .getScreenHeight(SchoolActivity.this)) {
                    if (firstVisibleItem < lastVisibleItemPosition) {
                        // 上滑
                        //toTopBtn.setVisibility(View.VISIBLE);
                        header.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        bt.show();
                    } else if (firstVisibleItem > lastVisibleItemPosition) {
                        // 下滑
                        // toTopBtn.setVisibility(View.GONE);
                        header.animate().translationY(-header.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                        bt.hide();
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;

                }

            }
        });



    }

    private void fetchComment() {
        BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();

        query.include("user");
        query.order("-updatedAt");
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("updatedAt", date);
        query.include("author");
        query.setLimit(8);
        query.setSkip(8 * (pageNum++));
        query.findObjects(this, new FindListener<QiangYu>() {


            @Override
            public void onSuccess(List<QiangYu> data) {
                // TODO Auto-generated method stub

                if (data.size() != 0 && data.get(data.size() - 1) != null) {


                    mAdapter.getDataList().addAll(data);
                    mAdapter.notifyDataSetChanged();


                } else {


                    pageNum--;
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

                Toast.makeText(SchoolActivity.this, "检查一下网络", Toast.LENGTH_SHORT).show();
                pageNum--;
            }
        });

        mSwipeLayout.setRefreshing(false);

    }







    public void back(View view) {
        finish();
    }


    @Override
    public void onRefresh() {
        pageNum = 0;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        fetchComment();
    }
}
