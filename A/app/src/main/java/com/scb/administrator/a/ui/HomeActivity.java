package com.scb.administrator.a.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.MyApplication;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.User;
import com.scb.administrator.a.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.UpdateListener;


public class HomeActivity extends Activity {

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;

    private static final int CODE_RESULT_REQUEST = 0xa2;
    private String path= Environment.getExternalStorageDirectory()+"/11.jpeg";
   private TextView   signature;
    private   ImageView headImage;
 private EditText passwordInput;
    private View positiveAction;

    private   String URL;
    private    SharedPreferences header;
    private ProgressDialog pd;
    private User currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView nick,out;
        nick = (TextView) findViewById(R.id.nick);
        signature = (TextView) findViewById(R.id.sig);
        out = (TextView) findViewById(R.id.check_new);
        headImage = (ImageView) findViewById(R.id.header);
        header = getApplication().getSharedPreferences("header", Context.MODE_PRIVATE);
        URL = header.getString("url", " ");

        if(!URL.equals(" ")){

            imageInit(URL);

        }
        //添加你的bmob的Key
        Bmob.initialize(HomeActivity.this, YOUR_Bmob_KEY);
       currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {
            // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段

            imageInit(currentUser.getAvatar());
            nick.setText(currentUser.getUsername());
            signature.setText(currentUser.getSignature());
        }
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut(HomeActivity.this);   //清除缓存用户对象
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                finish();
            }
        });


signature.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        MaterialDialog dialog = new MaterialDialog.Builder(HomeActivity.this)
                .customView(R.layout.dialog_customview2, true)
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {


                        User user = MyApplication.getInstance().getCurrentUser();
                        user.setSignature(passwordInput.getText().toString());

                        signature.setText(passwordInput.getText().toString());

                        user.update(HomeActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(HomeActivity.this,"更改成功",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(HomeActivity.this,"更改失败",Toast.LENGTH_SHORT).show();
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
                positiveAction.setEnabled(s.toString().length()<=30);

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


    public void fabOnclick(View view) {


            Intent intentFromGallery = new Intent();

            intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "已取消操作", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;

           default:break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        Bitmap photo;
        if (extras != null) {
            photo = extras.getParcelable("data");

            saveBitmap(photo);

            headImage.setImageBitmap(photo);


            Toast.makeText(getApplication(), "头像上传中...", Toast.LENGTH_SHORT).show();
            BmobProFile.getInstance(getApplication()).upload(path, new UploadListener() {

                @Override
                public void onError(int i, String s) {



                }

                @Override
                public void onSuccess(String s, String s1) {




                    URL = BmobProFile.getInstance(getApplication()).signURL(s, s1, "70fa0a62c4cc932af6060a12f2242284", 0, null);

                    if (currentUser != null) {
                        // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
                        currentUser.setAvatar(URL);
                        currentUser.update(getApplication(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplication(), "头像上传成功.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplication(), "头像上传失败.", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }

                @Override
                public void onProgress(int i) {


                }
            });


        }

    }

    private void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");


        // 设置裁剪
        intent.putExtra("crop", true);

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 225);
        intent.putExtra("outputY", 225);
        intent.putExtra("scale", true);  //可保持图片的大小
        intent.putExtra("scaleUpIfNeeded", true);//可保持图片的大小
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection",true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    public void saveBitmap(Bitmap mBitmap)  {

        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void imageInit(String Imageuri) {


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
               // .showImageOnLoading(R.drawable.ic_user)
                //.showImageOnFail(R.drawable.ic_user)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(Imageuri, headImage, options);


    }

    @Override
    protected void onPause() {

       // imageInit(URL);
        SharedPreferences.Editor editor = header.edit();
        editor.putString("url", URL);
        editor.commit();
        super.onPause();
    }

    public void back(View view) {
        finish();
    }
}
