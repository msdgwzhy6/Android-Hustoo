package com.scb.administrator.a.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.User;
import com.scb.administrator.a.util.StringUtils;

import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends Activity {

    private  EditText nick , email , password ;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
            nick= (EditText) findViewById(R.id.re_name);
           email =(EditText) findViewById(R.id.re_email);
            password= (EditText) findViewById(R.id.re_mima);


    }





    public void onRegister(View view) {

        if(TextUtils.isEmpty(nick.getText())){

            YoYo.with(Techniques.Shake).duration(1000).playOn(nick);
            Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email.getText())){
            YoYo.with(Techniques.Shake).duration(1000).playOn(email);
            Toast.makeText(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password.getText())){
            YoYo.with(Techniques.Shake).duration(1000).playOn(password);
            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!StringUtils.isValidEmail(email.getText())){
            YoYo.with(Techniques.Shake).duration(1000).playOn(email);
            Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        User bu = new User();
        bu.setUsername(nick.getText().toString());
        bu.setPassword(password.getText().toString());
        bu.setEmail(email.getText().toString());
        bu.setSignature("什么也不说");
        bu.setAvatar("http://newfile.codenow.cn:8080/2b92c2bddf804af0bcbadc0927b32f5b.jpeg?t=2&a=70fa0a62c4cc932af6060a12f2242284");

        pd = ProgressDialog.show(RegisterActivity.this, "Loading...", "Please  wait...", true, false);

        bu.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("name",nick.getText().toString());
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void back(View view) {
        finish();
    }
}
