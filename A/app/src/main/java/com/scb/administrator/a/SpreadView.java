package com.scb.administrator.a;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Administrator on 2015/9/8 0008.
 */
public class SpreadView extends View {


   /**
	 * ���ε�List
	 */
	private List<Wave> waveList;

	private int x = 0 ;
	private int y = 0 ;

	public void setStart(boolean start) {
		this.start = start;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}



	private  boolean start ,first ;

	private int delay ;

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}



	/**
	 * ���Ĳ�͸���ȣ���ȫ��͸��
	 */
	private static final int MAX_ALPHA = 255;


	private boolean isStart = true;




	// /**
	// * ���µ�ʱ��x���
	// */
	// private int xDown;
	// /**
	// * ���µ�ʱ��y�����
	// */
	// private int yDown;
	// /**
	// * ������ʾԲ���İ뾶
	// */
	// private float radius;
	// private int alpha;

	/*
	 * 1�����ι��캯��
	 */
	public SpreadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SpreadView);
		x = a.getInt(R.styleable.SpreadView_xDown,0);
		y = a.getInt(R.styleable.SpreadView_yDown,0);
		start = a.getBoolean(R.styleable.SpreadView_start,false);
		delay = a.getInt(R.styleable.SpreadView_delay,0);
		first = a.getBoolean(R.styleable.SpreadView_first,true);


		waveList = Collections.synchronizedList(new ArrayList<Wave>());
		//init();
		a.recycle();
	}



	/**
	 * onMeasure������ȷ���ؼ���С������ʹ��Ĭ�ϵ�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// �ػ�����Բ��
  if(waveList.size()==0&&(!first)) {
	init();
   }else if(waveList.size()==0&&first){
	  initFirst();
  }

		for (int i = 0; i<waveList.size(); i++) {
			Wave wave = waveList.get(i);
			canvas.drawCircle(wave.xDown, wave.yDown, wave.radius, wave.paint);

		}

	}

	private void initFirst() {
		if(this.start) {

			Wave wave = new Wave();
			wave.radius = 0;
			wave.alpha = MAX_ALPHA;

			wave.xDown = x;
			wave.yDown = y;
			wave.paint = initPaint(wave.alpha);

				isStart = true;


			waveList.add(wave);


			if (isStart) {
				first = false;
				mHandler.sendEmptyMessageDelayed(0, delay);

			}
		}
	}


	private Paint initPaint(int alpha) {
		/*
		 * �½�һ������
		 */
		Paint paint = new Paint();

		paint.setAntiAlias(true);
	//	paint.setStrokeWidth(width);

		// �����ǻ��η�ʽ����
		//paint.setStyle(Paint.Style.STROKE);

		// System.out.println(alpha= + alpha);
		paint.setAlpha(alpha);
		// System.out.println(�õ���͸���ȣ� + paint.getAlpha());

		paint.setColor(Color.WHITE);
		return paint;
	}
/*
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				flushState();

				invalidate();

				if (waveList != null &&waveList.size()>0) {
					handler.sendEmptyMessage(0);
				}


				break;

			default:
				break;
			}
		}

	};
*/

	public final Handler.Callback mHandlerCallback = new Handler.Callback(){


		@Override
		public boolean handleMessage(Message msg) {


			switch (msg.what){

				case 0:
					flushState();

					invalidate();

					if (waveList != null &&waveList.size()>0) {
						mHandler.sendEmptyMessage(0);
					}

					return true;

				default:
					return false;

			}


		}
	};

	private   Handler mHandler = new Handler(mHandlerCallback);


	/**
	 * ˢ��״̬
	 */
	private void flushState() {


			Wave w = waveList.get(0);
			if (w == null) {
				waveList.clear();
				this.postInvalidate();
			} else {

				w.radius +=dip2px(getContext(),0.65f);

				w.alpha -= 1;


				w.paint.setAlpha(w.alpha);
				if (w.alpha < 0) {
					waveList.clear();
					this.postInvalidate();
				}

			}


		}







	public void init() {
		if(this.start) {

			Wave wave = new Wave();
			wave.radius = 0;
			wave.alpha = MAX_ALPHA;

			wave.xDown = x;
			wave.yDown = y;
			wave.paint = initPaint(wave.alpha);

				isStart = true;


			waveList.add(wave);


			if (isStart) {
				mHandler.sendEmptyMessageDelayed(0, 1000);

			}
		}
	}

	private class Wave {

		/**
		 * ������ʾԲ���İ뾶
		 */
		float radius;
		Paint paint;
		/**
		 * ���µ�ʱ��x���
		 */
		int xDown;
		/**
		 * ���µ�ʱ��y�����
		 */
		int yDown;

		int alpha;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
