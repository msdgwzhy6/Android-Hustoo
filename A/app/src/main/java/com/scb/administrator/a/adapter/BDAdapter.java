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
import com.scb.administrator.a.entity.BookAdd;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28 0028.
 */
public class BDAdapter extends BaseAdapter {

    private  Context mContext;
    private  List<BookAdd> mdata;


    public BDAdapter(Context mContext, List<BookAdd> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;

    }

    public void refresh(List<BookAdd> list) {
        mdata = list;
        notifyDataSetChanged();
    }

    public List<BookAdd> getDataList() {
        return mdata;
    }

    public void setDataList(List<BookAdd> dataList) {
        this.mdata = dataList;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public BookAdd getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_add, null);
            holder = new Holder();
            holder.o = (TextView)convertView.findViewById(R.id.b_one);
            holder.t = (TextView)convertView.findViewById(R.id.b_two);
            holder.th = (TextView)convertView.findViewById(R.id.b_thr);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }
        holder.o.setText(mdata.get(position).getO());
        holder.t.setText(mdata.get(position).getT());
        holder.th.setText(mdata.get(position).getThr());
        return convertView;
    }

    class Holder {
        private TextView o ,t,th;

    }

}