package com.scb.administrator.a.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
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

public class CommentAdapter extends BaseContentAdapter<Comment> {

	public CommentAdapter(Context context, List<Comment> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.comment_item, null);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (TextView)convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView)convertView.findViewById(R.id.index_comment);
			viewHolder.iv= (CircleImageView) convertView.findViewById(R.id.i_logo);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();

		}
		
		final Comment comment = dataList.get(position);
		if(comment.getUser()!=null){
			viewHolder.userName.setText(comment.getUser().getUsername());
            imageInit(comment.getUser().getAvatar(),viewHolder.iv);
		}else{
			viewHolder.userName.setText("墙友");
		}
		viewHolder.index.setText(" "+(position+1)+" F ");
		viewHolder.commentContent.setText(comment.getCommentContent());
		
		return convertView;
	}

	public static class ViewHolder{
		public TextView userName;
		public TextView commentContent;
		public TextView index;
		public CircleImageView iv;
	}

	private void imageInit(String Imageuri, CircleImageView img) {


		//显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_default_avatar_lite)
				.showImageOnFail(R.drawable.ic_default_avatar_lite)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		ImageLoader.getInstance().displayImage(Imageuri, img, options);

	}


}
