package com.scb.administrator.a.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.NoticeAdapter;
import com.scb.administrator.a.entity.Notice;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment3 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onResume() {
        super.onResume();
    }


    private NoticeAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    private List<Notice> mData;




    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       ListView list;
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly_3);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(this);
        list = (ListView) getActivity().findViewById(R.id.MyListView);

        mData = new ArrayList<>();

        mAdapter = new NoticeAdapter(mData,getActivity());
        list.setAdapter(mAdapter);

       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String url = null;
               url = mData.get(position).getUri();
               Intent intent = new Intent();
               intent.putExtra("url", url);
               intent.setClass(getActivity(), TongzhiActivity.class);
               startActivity(intent);
           }
       });



        onRefresh();


    }





    /**
     * 覆盖此函数，先通过inflater inflate函数得到view最后返回
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_demo3, container, false);

        return v;
    }

    @Override
    public void onRefresh() {

        new MyAT().execute("http://jwc.hust.edu.cn/show/article/listMoreArticle?type=xxtz_0");

    }




    class MyAT extends AsyncTask<String,Void,List<Notice>>{

        @Override
        protected void onPostExecute(List<Notice> hashMaps) {
            super.onPostExecute(hashMaps);

            if(!hashMaps.isEmpty()) {


          mData = hashMaps;
                mAdapter.refresh(hashMaps);
                DataSupport.deleteAll(Notice.class);
                DataSupport.saveAll(hashMaps);

                mSwipeLayout.setRefreshing(false);
            }
            else {

                List<Notice> allNews = DataSupport.findAll(Notice.class);
                mAdapter.refresh(allNews);
   mData = allNews;

                Toast.makeText(getActivity(),"检查网络后下拉刷新",Toast.LENGTH_SHORT).show();

                mSwipeLayout.setRefreshing(false);
            }
        }

        @Override
        protected List<Notice> doInBackground(String... params) {

            List<Notice> mlist = new ArrayList<>();
            String url = params[0];
            Connection conn = Jsoup.connect(url);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (doc != null) {
                boolean i =true;
                int j=0;
                Elements elements = doc.getElementsByClass("box");

                for(Element element : elements) {
                    ++j;

                    if(i)
                    {
                        String companyName = element.getElementsByTag("a").text();
                        // String time = element.select("td.box").first().text();
                        String address = "http://jwc.hust.edu.cn"+ element.getElementsByTag("a").attr("href");
                        Notice item = new Notice(companyName,address);

                        mlist.add(item);
                        i=false;
                    }
                    else i =true;

                    if(j>20) break;
                }
                Notice it = new Notice("更多通知\n\nMore Notice",url);
                mlist.add(it);
            }

            return mlist;
        }
    }
}