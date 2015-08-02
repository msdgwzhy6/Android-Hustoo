package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.scb.administrator.a.R;


public class TongzhiActivity extends Activity {

   private  String num1;
   private WebView webView;
   private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        num1 = intent.getStringExtra("url");

        webView = (WebView) findViewById(R.id.webview);
        pb=(ProgressBar)findViewById(R.id.pb);

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
       webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true); // 支持页面放

       webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(num1);
        webView.getSettings().setJavaScriptEnabled(true);




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
        super.onCreateOptionsMenu(menu);
        menu.add(1, 1, 1, "用浏览器打开");

        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case 1: // do something here

                Uri uri = Uri.parse(num1);
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);

                break;


            default:

                return super.onOptionsItemSelected(item);

        }

        return true;

    }
}
