package com.scb.administrator.a.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;

import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.scb.administrator.a.CircleImageView;

import com.scb.administrator.a.R;

import com.scb.administrator.a.adapter.CommentAdapter;
import com.scb.administrator.a.adapter.FaceGVAdapter;
import com.scb.administrator.a.adapter.FaceVPAdapter;
import com.scb.administrator.a.entity.Comment;
import com.scb.administrator.a.entity.QiangYu;
import com.scb.administrator.a.entity.User;

import com.scb.administrator.a.util.MyImageGetter;
import com.scb.administrator.a.util.MyTagHandler;
import com.scb.administrator.a.util.ScreenUtil;
import com.scb.administrator.a.util.TextViewHtmlParser;
import com.scb.administrator.a.util.URLDrawable;


import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;



public class CommentActivity extends Activity  implements View.OnClickListener {

    private TextView reply,title,pics;
    private TextView commentItemContent;
    private CircleImageView userlogo;
    private QiangYu qiangYu;
    private ViewPager mViewPager;
    private CommentAdapter mAdapter;
    private LinearLayout chat_face_container;
    private int pageNum;

    private EditText  input;
    private int columns =3;
    private int rows = 4;

    private List<View> views = new ArrayList<View>();
    private List<String> staticFacesList;
    private LinearLayout mDotsLayout;
    private ListView commentList;

    private Button send_sms;

    private   DisplayImageOptions options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commet);

       commentList = (ListView)findViewById(R.id.comment_2);


        input = (EditText) findViewById(R.id.input_sms);

        ImageView image_face = (ImageView) findViewById(R.id.image_face);
        chat_face_container=(LinearLayout) findViewById(R.id.chat_face_container);
         mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mViewPager.setOnPageChangeListener(new PageChange());
        //表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        LayoutInflater
                lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.comment_main, commentList, false);

        commentList.addHeaderView(headerView, null, false);

        reply = (TextView)headerView.findViewById(R.id.m_time);

       title = (TextView)headerView.findViewById(R.id.m_title);

        commentItemContent = (TextView)headerView.findViewById(R.id.m_content);
        pics = (TextView)headerView.findViewById(R.id.m_pic);

        userlogo = (CircleImageView)headerView.findViewById(R.id.m_logo);


        qiangYu = (QiangYu)getIntent().getSerializableExtra("data");
        initMoodView(qiangYu);

      List<Comment> comments;
       comments = new ArrayList<Comment>();
        input.setOnClickListener(this);


        image_face.setOnClickListener(this);
     mAdapter = new CommentAdapter(CommentActivity.this, comments);
     commentList.setAdapter(mAdapter);

        initStaticFaces();

        send_sms = (Button) findViewById(R.id.send_sms);
      commentList.setCacheColorHint(0);
        commentList.setScrollingCacheEnabled(false);
        commentList.setScrollContainer(false);
        commentList.setFastScrollEnabled(true);
        commentList.setSmoothScrollbarEnabled(true);
        commentList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (chat_face_container.getVisibility() == View.VISIBLE) {
                        chat_face_container.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });


fetchComment();
        InitViewPager();

        commentList.setOnScrollListener(new AbsListView.OnScrollListener() {


                                            @Override
                                            public void onScrollStateChanged(AbsListView view, int scrollState) {


                                                switch (scrollState) {
                                                    // 当不滚动时
                                                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                                                        // 判断滚动到底部
                                                        if (commentList.getLastVisiblePosition() == (commentList
                                                                .getCount() - 1)) {

                                                            fetchComment();


                                                        }

                                                        break;
                                                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                                                        break;
                                                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                                                        break;
                                                }

                                            }

                                            @Override
                                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                            }
                                        }

        );

        //显示图片的配置
       options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    private void InitViewPager() {
        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    private View viewPagerItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);

        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows),
                        (columns * rows) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows)
                                * (position + 1)));

        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString();

                    input.setText(input.getText() + " " + png);
                    input.setSelection(input.getText().toString().length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    private void fetchComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("relation", new BmobPointer(qiangYu));
        query.include("user");
        query.order("createdAt");
        query.setLimit(10);
        query.setSkip(10 * (pageNum++));
        query.findObjects(this, new FindListener<Comment>() {


            @Override
            public void onSuccess(List<Comment> data) {
                // TODO Auto-generated method stub

                if (data.size() != 0 && data.get(data.size() - 1) != null) {

                    mAdapter.getDataList().addAll(data);
                    mAdapter.notifyDataSetChanged();
                } else {


                    pageNum--;
                }


            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

                Toast.makeText(CommentActivity.this, "检查一下网络", Toast.LENGTH_SHORT).show();
                pageNum--;
            }
        });
    }

    private void initMoodView(QiangYu mood2) {
        if(mood2 == null){
            return;
        }

        title.setText(qiangYu.getTitle());
        commentItemContent.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效
        commentItemContent.setClickable(true);
        commentItemContent.setLinkTextColor(Color.BLUE);
        commentItemContent.setText(qiangYu.getContent());

        pics.setMovementMethod(LinkMovementMethod.getInstance());

        MyImageGetter imageGetter = new MyImageGetter(this,pics);
        MyTagHandler tagHandler = new MyTagHandler(this);
        pics.setText(Html.fromHtml(qiangYu.getPic(), imageGetter, tagHandler));

                imageInit(qiangYu.getAuthor().getAvatar(), userlogo);
        reply.setText("By " + qiangYu.getAuthor().getUsername() + "  at  " + qiangYu.getCreatedAt());

    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     * **/

    /**
     * 向输入框里添加表情
     * */


    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     * */


    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows ) == 0 ? count / (columns * rows )
                : count / (columns * rows) + 1;
    }

    /**
     * 初始化表情列表staticFacesList
     */
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = {"ミ ﾟДﾟ彡","::>_<:: ","٩(×̯×)۶",
                    "(～ o ～)","٩͡[๏̯͡๏]۶",
                    "*^_^* ","(｀･ω･´)","π_π","-_-b","~zZ",
"◑ˍ◐","o(*≧▽≦)ツ","✪ω✪","(°□°；)","╮(╯_╰)╭","┌( ಠ_ಠ)┘"
    ,"◑ε ◐","◐ 3◑","(oﾟωﾟo)","(╬▔皿▔)","(●'◡'●)ﾉ♥","<(▰˘◡˘▰)>","(●´ω｀●)φ","(-__-)b","<(‵^′)>"
            };
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void imageInit(String Imageuri, CircleImageView img) {




        ImageLoader.getInstance().displayImage(Imageuri, img, options);

    }



    public void bt_Commit(View view) {
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if(currentUser != null){//已登录

            String commentEdit;
            commentEdit = input.getText().toString().trim();
            if(TextUtils.isEmpty(commentEdit)){

                return;
            }
            //comment now
            publishComment(currentUser,commentEdit);
            if (chat_face_container.getVisibility() == View.VISIBLE) {
                chat_face_container.setVisibility(View.GONE);
            }
        }else{//未登录
            Toast.makeText(CommentActivity.this, "请先登录帐号", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void publishComment(User user, String content) {

        send_sms.setClickable(false);
        hideSoftInput();
        final Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentContent(content);
        comment.save(this, new SaveListener() {



            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

              if(mAdapter.getDataList().size()>=1) {
                  mAdapter.getDataList().add(comment);
                  mAdapter.notifyDataSetChanged();
              }




                input.setText("");


                send_sms.setClickable(true);

                //将该评论与强语绑定到一起
                BmobRelation relation = new BmobRelation();
                relation.add(comment);
                qiangYu.setRelation(relation);

                qiangYu.setComment(qiangYu.getComment()+1);

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
                send_sms.setClickable(true);

            }
        });

    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.input_sms://输入框
                if(chat_face_container.getVisibility()==View.VISIBLE){
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_face://表情
                hideSoftInput();//隐藏软键盘
                if(chat_face_container.getVisibility()==View.GONE){
                    chat_face_container.setVisibility(View.VISIBLE);
                }else{
                    chat_face_container.setVisibility(View.GONE);
                }
                break;


        }
    }

    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }

    }


}
