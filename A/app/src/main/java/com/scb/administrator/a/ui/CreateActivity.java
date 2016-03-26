package com.scb.administrator.a.ui;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;

import com.bmob.btp.callback.UploadListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.MyApplication;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;


import net.sf.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dmax.dialog.SpotsDialog;


public class CreateActivity extends Activity {


    int num = 100;
    private String secPath= Environment.getExternalStorageDirectory()+"/"+num+".jpg";

   private EditText et1,et2;
    private ImageView ok;
    private ProgressDialog pd;
    private String picPath = null,URL;
private  SpotsDialog dialog ;
    private LinearLayout layPicContainer;
   private List<String> paths;
    private   DisplayImageOptions options;

    public final Handler.Callback mHandlerCallback = new Handler.Callback(){


        @Override
        public boolean handleMessage(Message msg) {


            switch (msg.what){

                case 0:
                    boolean flag = false ;
                    Bitmap  bitmap = null;
                    try {
                        bitmap = getimage(picPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    File f = new File(secPath);
                    if (f.exists()) {
                        f.delete();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                            out.flush();
                            out.close();
                            flag = true;
                        }
                    } catch (Exception e1) {
                        // TODO: handle exception
                    }

                if(flag)
                    updateFile();
                    return true;

                default:
                    return false;

            }


        }
    };

    private   Handler mHandler = new Handler(mHandlerCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et1 = (EditText) findViewById(R.id.edit_title);
        et2 = (EditText) findViewById(R.id.edit_content);
        ok = (ImageView) findViewById(R.id.cre_ok);
        TextView tv = (TextView) findViewById(R.id.edit_pic);
        layPicContainer = (LinearLayout) findViewById(R.id.get_pic);
        paths  = new ArrayList<>();
        if (layPicContainer != null) {
            layPicContainer.removeAllViews();
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        options   = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_avatar_lite)
                .showImageOnFail(R.drawable.ic_default_avatar_lite)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ok.setClickable(false);
                if (TextUtils.isEmpty(et1.getText().toString().trim())) {
                    YoYo.with(Techniques.Shake).duration(500).playOn(et1);
                    Toast.makeText(CreateActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    ok.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(et2.getText().toString().trim())&&paths.size()==0) {
                    YoYo.with(Techniques.Shake).duration(500).playOn(et2);
                    Toast.makeText(CreateActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    ok.setClickable(true);
                    return;
                }

                QiangYu qy = new QiangYu();
                qy.setAuthor(BmobUser.getCurrentUser(CreateActivity.this, User.class));
                qy.setContent(et2.getText().toString());
                qy.setTitle(et1.getText().toString());

              StringBuffer  pics = new StringBuffer();
                for(String url:paths)
                {
                   pics.append("<br><img src=\""+url+"\"/><br/>");
                }

                qy.setPic(pics.toString());


                pd = ProgressDialog.show(CreateActivity.this, "Loading...", "Please  wait...", true, false);


                qy.save(CreateActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        ok.setClickable(true);
                        Toast toast = Toast.makeText(CreateActivity.this, "发布成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                       finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        pd.dismiss();
                        ok.setClickable(true);
                        Toast to = Toast.makeText(CreateActivity.this, "发布失败", Toast.LENGTH_SHORT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "已取消操作", Toast.LENGTH_LONG).show();
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();

            picPath = getPath(CreateActivity.this, uri);

            mHandler.sendEmptyMessage(0);




            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void updateFile() {
        if (secPath == null) {

            Toast.makeText(CreateActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
        } else {


            dialog = new SpotsDialog(CreateActivity.this,R.style.Custom);
            dialog.show();

            BmobProFile.getInstance(CreateActivity.this).upload(secPath, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1) {

                    URL = BmobProFile.getInstance(CreateActivity.this).signURL(s, s1, "70fa0a62c4cc932af6060a12f2242284", 0, null);
                    Toast.makeText(CreateActivity.this, "图片添加成功...", Toast.LENGTH_SHORT).show();



                    paths.add(URL);


                    View itemView = View.inflate(CreateActivity.this, R.layout.as_item_publish_pic, null);
                    ImageView img = (ImageView) itemView.findViewById(R.id.img);
                    itemView.setTag(URL);
                    // ImageLoader.getInstance().displayImage(p, img);

                    ImageLoader.getInstance().displayImage(URL, img, options);

                    if (layPicContainer != null) {
                        layPicContainer.addView(itemView,
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        itemView.setOnClickListener(onPictureClickListener);

                    }


                    dialog.dismiss();
                }

                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int i, String s) {

                    dialog.dismiss();
                    Toast.makeText(CreateActivity.this, "图片添加失败...", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }






    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private Bitmap getimage(String srcPath) throws Exception {
        File f = new File(srcPath);

      //  double size = getFileSize(f);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
// 开始读入图片, 此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为

        float ww = 140f;// 这里设置宽度为480f
// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / 250.0);
        }
        if (be <= 0||w<=400)
            be = 1;




        newOpts.inSampleSize = be;// 设置缩放比例
      //  System.out.println(newOpts.inSampleSize);
// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }





    View.OnClickListener onPictureClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String path = v.getTag().toString();

            MaterialDialog dialog = new MaterialDialog.Builder(CreateActivity.this)
                    .customView(R.layout.pic_dialog, true)
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {


                            for (int i = 0; i < layPicContainer.getChildCount(); i++) {
                                View view = layPicContainer.getChildAt(i);

                                if (view.getTag().toString().equals(path)) {
                                    layPicContainer.removeView(view);
                                    paths.remove(view.getTag().toString());
                                    break;
                                }
                            }


                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                        }
                    }).build();

            dialog.show();
        }

    };
}
