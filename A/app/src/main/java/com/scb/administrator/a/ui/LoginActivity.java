package com.scb.administrator.a.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.User;
import com.scb.administrator.a.util.StringUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;


public class LoginActivity extends Activity  {


   private EditText zhangHao , password;
    private ProgressDialog pd;
    private TextView forget;
    private EditText passwordInput;
    private View positiveAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         initView();
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDialog dialog = new MaterialDialog.Builder(LoginActivity.this)
                        .customView(R.layout.dialog_customview, true)
                        .positiveText("确定")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {


                                final String email = passwordInput.getText().toString();
                                BmobUser.resetPasswordByEmail(LoginActivity.this, email, new ResetPasswordByEmailListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(LoginActivity.this,"请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(LoginActivity.this,"请求失败:" + s,Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        }).build();

                positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                //noinspection ConstantConditions
                passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
                passwordInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(StringUtils.isValidEmail(s.toString().trim()));

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                dialog.show();
                positiveAction.setEnabled(false);

            }
        });
    }

    private void initView() {
        zhangHao= (EditText) findViewById(R.id.yonghuming);
        password= (EditText) findViewById(R.id.password);
        forget  = (TextView) findViewById(R.id.forget);
    }


    public void onLogin(View view) {

        if(TextUtils.isEmpty(zhangHao.getText())){
            YoYo.with(Techniques.Shake).duration(500).playOn(zhangHao);
            Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password.getText())){
            YoYo.with(Techniques.Shake).duration(1000).playOn(password);
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        pd = ProgressDialog.show(LoginActivity.this, "Loading...", "Please  wait...", true, false);

        User.loginByAccount(this, zhangHao.getText().toString(), password.getText().toString(), new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                pd.dismiss();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void register(View view) {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }


    public void back(View view) {
        finish();
    }
}
