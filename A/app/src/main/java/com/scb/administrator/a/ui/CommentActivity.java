package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.CircleImageView;
import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.CommentAdapter;
import com.scb.administrator.a.entity.Comment;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;



public class CommentActivity extends Activity {

    private TextView reply,title;
    private TextView commentItemContent;
    private CircleImageView userlogo;
    private QiangYu qiangYu;
    private CommentAdapter mAdapter;


    private EditText et;

    private int pageNum;

    private ListView commentList;
    private TextView footer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commet);

        commentList = (ListView)findViewById(R.id.comment_list);
        footer = (TextView)findViewById(R.id.loadmore);
        et = (EditText) findViewById(R.id.comment_content);



        reply = (TextView) findViewById(R.id.m_time);
      title = (TextView)findViewById(R.id.m_title);
        commentItemContent = (TextView)findViewById(R.id.m_content);
        userlogo = (CircleImageView)findViewById(R.id.m_logo);

        qiangYu = (QiangYu)getIntent().getSerializableExtra("data");
        initMoodView(qiangYu);
        List<Comment> comments;
        comments = new ArrayList<Comment>();

        mAdapter = new CommentAdapter(CommentActivity.this, comments);
        commentList.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(commentList);

        commentList.setCacheColorHint(0);
        commentList.setScrollingCacheEnabled(false);
        commentList.setScrollContainer(false);
        commentList.setFastScrollEnabled(true);
        commentList.setSmoothScrollbarEnabled(true);

        fetchComment();
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchComment();
            }
        });



    }

    private void fetchComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("relation", new BmobPointer(qiangYu));
        query.include("user");
        query.order("createdAt");
        query.setLimit(8);
        query.setSkip(8*(pageNum++));
        query.findObjects(this, new FindListener<Comment>() {


            @Override
            public void onSuccess(List<Comment> data) {
                // TODO Auto-generated method stub

                if(data.size()!=0 && data.get(data.size()-1)!=null){

                    if(data.size()<8){

                        footer.setText("暂无更多评论~");
                    }

                    mAdapter.getDataList().addAll(data);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);

                }else{

                    footer.setText("暂无更多评论~");
                    pageNum--;
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

                Toast.makeText(CommentActivity.this,"检查一下网络",Toast.LENGTH_SHORT).show();
                pageNum--;
            }
        });
    }

    private void initMoodView(QiangYu mood2) {
        if(mood2 == null){
            return;
        }
        title.setText(qiangYu.getTitle());
        commentItemContent.setText(qiangYu.getContent());
        imageInit(qiangYu.getAuthor().getAvatar(), userlogo);
        reply.setText("By " + qiangYu.getAuthor().getUsername() + "  at  " + qiangYu.getCreatedAt());

    }





    private void imageInit(String Imageuri, CircleImageView img) {


        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(Imageuri, img, options);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount()-1))
                +15;
        listView.setLayoutParams(params);
    }

    public void bt_Commit(View view) {
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if(currentUser != null){//已登录

            String commentEdit;
            commentEdit = et.getText().toString().trim();
            if(TextUtils.isEmpty(commentEdit)){

                return;
            }
            //comment now
            publishComment(currentUser,commentEdit);
        }else{//未登录
            Toast.makeText(CommentActivity.this, "请先登录帐号", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void publishComment(User user, String content) {

        hideSoftInput();
        final Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentContent(content);
        comment.save(this, new SaveListener() {



            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

                if(mAdapter.getDataList().size()<8){
                    mAdapter.getDataList().add(comment);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);
                }
                et.setText("");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
                String   date   =   sdf.format(new java.util.Date());


                //将该评论与强语绑定到一起
                BmobRelation relation = new BmobRelation();
                relation.add(comment);
                qiangYu.setRelation(relation);
                qiangYu.setReply("Last Reply·"+date);


                qiangYu.update(CommentActivity.this, new UpdateListener() {


                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub

                        Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(CommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(CommentActivity.this,"评论失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public void back(View view) {
        finish();
    }
}
