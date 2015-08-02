package com.scb.administrator.a.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.Movie;
import com.scb.administrator.a.entity.Wenzhang;
import com.scb.administrator.a.entity.Wenzi;
import com.scb.administrator.a.util.HttpDownloader;
import com.scb.administrator.a.util.Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;


public class ViewPagerFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    private  TextView tv , wenzi,oMovie,weather1,icome;
    private SwipeRefreshLayout mSwipeLayout;
   private Button gushi;
    private ImageView moviep;
    private  SharedPreferences weather;

    private Button icome2;


    @Override
    public void onDestroy() {

        super.onDestroy();
    }






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bmob.initialize(getActivity(), "4ef015e97fb35a0f58b00043679e2b9a");
        initViews();

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(this);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/zz.ttf");
        wenzi.setTypeface(typeFace);
        gushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = gushi.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("tittle", title);
                intent.setClass(getActivity(), WenzhangActivity.class);
                startActivity(intent);
            }
        });
        gushi.setTypeface(typeFace);
        String sb ="\"你那么擅长安慰他人，一定度过了很多自己安慰自己的日子吧。\"\nBy 陈亚豪";
        weather = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        tv.setText(weather.getString("temp","下拉刷新"));
        weather1.setText(weather.getString("weather1","下拉刷新"));
        wenzi.setText(weather.getString("wenzi",sb));
        gushi.setText(weather.getString("tittle", "晓雨的故事 / RainDroid"));

        oMovie.setTypeface(typeFace);
        oMovie.setText(weather.getString("movieT", "我敢在你怀里孤独"));
        imageInit(weather.getString("movieU", "http://img4.douban.com/view/note/large/public/p27728049.jpg"));
        oMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = oMovie.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("uri", uri);
                intent.setClass(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });
        onRefresh();
  icome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          new MaterialDialog.Builder(getActivity())
                  .content(R.string.shareLocationPrompt)
                  .positiveText("确定")
                  .show();
      }
  });

        icome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .content(R.string.shareLocationPrompt)
                        .positiveText("确定")
                        .show();
            }
        });
       


    }

    private void imageInit(String Imageuri) {


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(2000))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(Imageuri, moviep, options);

    }

    private void initViews() {
        tv = (TextView)getActivity().findViewById(R.id.viewPagerText1);
        wenzi = (TextView)getActivity().findViewById(R.id.wenzi);
        gushi= (Button) getActivity().findViewById(R.id.gushi);
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly_1);
        oMovie= (TextView) getActivity().findViewById(R.id.oneMovie);
        moviep= (ImageView) getActivity().findViewById(R.id.movie);
        weather1=(TextView) getActivity().findViewById(R.id.weather1);
        icome = (TextView) getActivity().findViewById(R.id.collect);
        icome2 = (Button) getActivity().findViewById(R.id.wolai2);
    }

    /**
     * 覆盖此函数，先通过inflater inflate函数得到view最后返回
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_demo, container, false);
        return v;
    }

    @Override
    public void onRefresh() {

         new My().execute("http://apistore.baidu.com/microservice/weather?citypinyin=wuhan");
        BmobQuery<Wenzi> query = new BmobQuery<Wenzi>();
        query.setLimit(1);
        query.findObjects(getActivity(), new FindListener<Wenzi>() {
            @Override
            public void onSuccess(List<Wenzi> list) {

                SharedPreferences.Editor editor = weather.edit();
                wenzi.setText(list.get(0).getName());
                editor.putString("wenzi", list.get(0).getName());
                editor.commit();

            }

            @Override
            public void onError(int i, String s) {

            }
        });

        BmobQuery<Wenzhang> query2 = new BmobQuery<Wenzhang>();
        query2.setLimit(1);
        query2.getObject(getActivity(), "qhfeDDDF", new GetListener<Wenzhang>() {
            @Override
            public void onFailure(int i, String s) {


            }

            @Override
            public void onSuccess(Wenzhang wenzhang) {

                SharedPreferences.Editor editor = weather.edit();
                gushi.setText(wenzhang.getTittle());
                editor.putString("tittle", wenzhang.getTittle());
                editor.commit();
            }


        });

        BmobQuery<Movie> query3 = new BmobQuery<Movie>();

        query3.getObject(getActivity(), "mO9rIIId", new GetListener<Movie>() {
            @Override
            public void onFailure(int i, String s) {


            }

            @Override
            public void onSuccess(Movie movie) {
                String uri  = movie.getMovieUri();
                oMovie.setText(movie.getMovieName());

                imageInit(uri);
                SharedPreferences.Editor editor = weather.edit();
                editor.putString("movieT", movie.getMovieName());
                editor.putString("movieUri", movie.getMovieUri());
                editor.commit();

            }


        });

        mSwipeLayout.setRefreshing(false);

    }

    class My extends AsyncTask<String ,Void,String[]>{


        @Override
        protected String[] doInBackground(String... params) {

            String url = params[0];
            String text[] = {"", ""};
            HttpDownloader httpDownloader = new HttpDownloader();
            String jonString = httpDownloader.download(url);
            Util util = new Util();
            try {
                List<Map<String, Object>> all = util
                        .getInformation(jonString);
                if(all!=null) {
                    Iterator<Map<String, Object>> iterator = all.iterator();
                    while (iterator.hasNext()) {
                        Map<String, Object> map = iterator.next();
                        text[0] = map.get("l_temp").toString()+" - "+map.get("h_temp").toString()+" ℃";
                        text[1]=map.get("weather").toString();
                    }


                }
                else Toast.makeText(getActivity(), "检查网络后下拉刷新", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();

            }

            return text;

        }

        @Override
        protected void onPostExecute(final String s[]) {

            super.onPostExecute(s);
            if(s!=null) {
                tv.setText(s[0]);
                weather1.setText(s[1]);

                SharedPreferences.Editor editor = weather.edit();
                editor.putString("temp", s[0]);
                editor.putString("weather1", s[1]);
                editor.commit();






               CardView cv ;

                cv= (CardView) getActivity().findViewById(R.id.Cv2);

                YoYo.with(Techniques.Shake).duration(1000).playOn(cv);

            }




        }
    }





}
