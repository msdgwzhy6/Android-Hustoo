package com.scb.administrator.a.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scb.administrator.a.CircleImageView;
import com.scb.administrator.a.R;
import com.scb.administrator.a.entity.Comment;
import com.scb.administrator.a.entity.QiangYu;

import java.util.List;

public class MyAdapterC extends BaseContentAdapter<QiangYu> {

	public MyAdapterC(Context context, List<QiangYu> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ai_item, null);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.user_name);
			viewHolder.tv_title = (TextView)convertView.findViewById(R.id.title_text);
			viewHolder.tv_reply = (TextView)convertView.findViewById(R.id.reply_text);
			viewHolder.iv = (CircleImageView) convertView.findViewById(R.id.user_logo);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		

		viewHolder.tv_name.setText(dataList.get(position).getAuthor().getUsername());
		viewHolder.tv_title.setText(dataList.get(position).getTitle());
		viewHolder.tv_reply.setText(dataList.get(position).getReply());
		imageInit(dataList.get(position).getAuthor().getAvatar(),viewHolder.iv);
		return convertView;
	}

	public static class ViewHolder{
		private TextView tv_name ,tv_title,tv_reply;
		private CircleImageView iv;
	}

	private void imageInit(String Imageuri, CircleImageView img) {


		//显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.a)
				.showImageOnFail(R.drawable.a)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(500))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		ImageLoader.getInstance().displayImage(Imageuri, img, options);

	}
}
