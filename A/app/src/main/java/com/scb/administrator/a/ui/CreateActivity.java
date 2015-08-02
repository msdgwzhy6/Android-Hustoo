package com.scb.administrator.a.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class CreateActivity extends Activity {


   private EditText et1,et2;
    private ImageView ok;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et1 = (EditText) findViewById(R.id.edit_title);
        et2 = (EditText) findViewById(R.id.edit_content);
        ok = (ImageView) findViewById(R.id.cre_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ok.setClickable(false);
                if(TextUtils.isEmpty(et1.getText())){
                    YoYo.with(Techniques.Shake).duration(500).playOn(et1);
                    Toast.makeText(CreateActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(TextUtils.isEmpty(et2.getText())){
                    YoYo.with(Techniques.Shake).duration(500).playOn(et2);
                    Toast.makeText(CreateActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return ;
                }

                QiangYu qy = new QiangYu();
                qy.setAuthor(  BmobUser.getCurrentUser(CreateActivity.this, User.class));
                qy.setContent(et2.getText().toString());
                qy.setTitle(et1.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
               String   date   =   sdf.format(new java.util.Date());

                qy.setReply("Last Reply·"+date);

                pd = ProgressDialog.show(CreateActivity.this, "Loading...", "Please  wait...", true, false);


              qy.save(CreateActivity.this, new SaveListener() {
                  @Override
                  public void onSuccess() {
                      pd.dismiss();
          ok.setClickable(true);
                      Toast toast = Toast.makeText(CreateActivity.this, "发布成功", Toast.LENGTH_LONG);
                      toast.setGravity(Gravity.CENTER,0,0);
                      toast.show();
                      finish();
                  }

                  @Override
                  public void onFailure(int i, String s) {
                      pd.dismiss();
                      ok.setClickable(true);
                      Toast to  =   Toast.makeText(CreateActivity.this,"发布失败",Toast.LENGTH_SHORT);
                      to.setGravity(Gravity.CENTER, 0, 0);
                      to.show();
                  }
              });

            }
        });
    }


    public void back(View view) {
        finish();
    }
}
