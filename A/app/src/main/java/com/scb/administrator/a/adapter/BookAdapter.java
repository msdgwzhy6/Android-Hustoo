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
import com.scb.administrator.a.entity.Book;
import com.scb.administrator.a.entity.QiangYu;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28 0028.
 */
public class BookAdapter extends BaseAdapter {

    private  Context mContext;
    private  List<Book> mdata;
    private   DisplayImageOptions options;

    public BookAdapter(Context mContext, List<Book> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    options   = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.a)
                .showImageOnFail(R.drawable.a)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void refresh(List<Book> list) {
        mdata = list;
        notifyDataSetChanged();
    }

    public List<Book> getDataList() {
        return mdata;
    }

    public void setDataList(List<Book> dataList) {
        this.mdata = dataList;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Book getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_book, null);
            holder = new Holder();
            holder.tv_book = (TextView)convertView.findViewById(R.id.book_text);
            holder.tv_uri = (TextView) convertView.findViewById(R.id.book_uri);
            holder.iv = (CircleImageView) convertView.findViewById(R.id.book_logo);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        holder.tv_book.setText(mdata.get(position).getName());
        holder.tv_uri.setText(mdata.get(position).getLink());
        imageInit(mdata.get(position).getPic(),holder.iv);
        return convertView;
    }

    class Holder {
        private TextView tv_book ,tv_uri;
        private CircleImageView iv;
    }
    private void imageInit(String Imageuri, CircleImageView img) {


        //显示图片的配置
        ImageLoader.getInstance().displayImage(Imageuri, img, options);

    }
}