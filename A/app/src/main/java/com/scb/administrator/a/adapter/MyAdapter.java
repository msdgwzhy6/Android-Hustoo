package com.scb.administrator.a.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.CircleImageView;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.QiangYu;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28 0028.
 */
public class MyAdapter extends BaseAdapter {

    private  Context mContext;
    private  List<QiangYu> mdata;
    private   DisplayImageOptions options;

    public MyAdapter(Context mContext, List<QiangYu> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    options   = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_avatar_lite)
                .showImageOnFail(R.drawable.ic_default_avatar_lite)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void refresh(List<QiangYu> list) {
        mdata = list;
        notifyDataSetChanged();
    }

    public List<QiangYu> getDataList() {
        return mdata;
    }

    public void setDataList(List<QiangYu> dataList) {
        this.mdata = dataList;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public QiangYu getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.ai_item, null);
            holder = new Holder();
            holder.tv_name = (TextView)convertView.findViewById(R.id.user_name);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.item_action_comment);
            holder.tv_title = (TextView)convertView.findViewById(R.id.title_text);
            holder.tv_reply = (TextView)convertView.findViewById(R.id.reply_text);
            holder.iv = (CircleImageView) convertView.findViewById(R.id.user_logo);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        holder.tv_name.setText(mdata.get(position).getAuthor().getUsername());
        holder.tv_title.setText(mdata.get(position).getTitle());
        holder.tv_reply.setText("Last Reply  at  "+mdata.get(position).getUpdatedAt());
        holder.tv_comment.setText(" "+mdata.get(position).getComment()+" ");
        imageInit(mdata.get(position).getAuthor().getAvatar(),holder.iv);
        return convertView;
    }

    class Holder {
        private TextView tv_name ,tv_title,tv_reply,tv_comment;
        private CircleImageView iv;
    }
    private void imageInit(String Imageuri, CircleImageView img) {


        //显示图片的配置
        ImageLoader.getInstance().displayImage(Imageuri, img, options);

    }
}