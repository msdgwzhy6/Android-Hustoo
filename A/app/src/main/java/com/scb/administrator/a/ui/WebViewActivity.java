package com.scb.administrator.a.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.Movie;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class WebViewActivity extends Activity {

    @SuppressLint("SetJavaScriptEnabled")

  private  String num1;
  private  WebView webView;
  private  ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        num1 = intent.getStringExtra("tittle");

        webView = (WebView) findViewById(R.id.webview);
        pb=(ProgressBar)findViewById(R.id.pb);


       webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setSupportZoom(false); // 不支持页面放大功能

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                if(newProgress==100){
                    pb.setVisibility(View.GONE);
                }else{

                    if (pb.getVisibility() == View.GONE)
                        pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);

                }

            }




        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO 自动生成的方法存

                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            //加速
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }


        });

        BmobQuery<Movie> query = new BmobQuery<Movie>();
        query.setLimit(1);
        query.addWhereEqualTo("movieName", num1);
        query.findObjects(WebViewActivity.this, new FindListener<Movie>() {


            @Override
            public void onSuccess(List<Movie> list) {

                webView.loadUrl(list.get(0).getToUri());


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(WebViewActivity.this, "请检查一下网络连接", Toast.LENGTH_SHORT).show();
                finish();

            }
        });





    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
