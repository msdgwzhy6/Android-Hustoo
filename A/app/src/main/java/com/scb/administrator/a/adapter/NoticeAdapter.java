package com.scb.administrator.a.adapter;

import java.util.List;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.Notice;


public class NoticeAdapter extends BaseAdapter {

	private List<Notice> mList;
	private Context mContext;

	public NoticeAdapter(List<Notice> mList, Context mContext) {
		this.mList = mList;
		this.mContext = mContext;
	}

	public void refresh(List<Notice> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
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
			convertView = inflater.inflate(R.layout.my_listitem, null);
			holder = new Holder();
			holder.mNameText = (TextView)convertView.findViewById(R.id.ItemTitle);
			holder.mIDText = (TextView)convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.mNameText.setText(mList.get(position).getNotice());
		holder.mIDText.setText(mList.get(position) .getUri());
		return convertView;
	}

	class Holder {
		private TextView mNameText, mIDText;
	}
}
