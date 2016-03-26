package com.scb.administrator.a.ui;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.scb.administrator.a.R;

public class AboutActivity extends Activity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tv = (TextView) findViewById(R.id.about_tv);
                tv.setText(getString(R.string.about));
    }


    public void back(View view) {
        finish();
        overridePendingTransition(0, R.anim.slide_out_to_left);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_to_left);
    }
}
