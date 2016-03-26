package com.scb.administrator.a.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scb.administrator.a.R;


public class TongzhiActivity extends AppCompatActivity {

   private  String num1;
   private WebView webView;
   private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        num1 = intent.getStringExtra("url");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        mToolbar.setTitle("教务通知");

        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_about:

                        Uri uri = Uri.parse(num1);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);

                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        webView = (WebView) findViewById(R.id.webview);
        pb=(ProgressBar)findViewById(R.id.pb);
        Toast.makeText(TongzhiActivity.this, "点击菜单可用浏览器打开", Toast.LENGTH_SHORT).show();
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
       webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true); // 支持页面放

       webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(num1);
        webView.getSettings().setJavaScriptEnabled(true);

           webView.setDownloadListener(new DownloadListener() {
               @Override
               public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                   Uri uri = Uri.parse(url);
                   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                   startActivity(intent);
               }
           });


       // webView.getSettings().setSupportZoom(false); // 不支持页面放大功能

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



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


}
