package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ToastLocationActivity extends Activity {
	private ImageView iv_center;
	private WindowManager mWmManager;
	private int mHeight;
	private int mWidth;
	private Button bt_top;
	private Button bt_bottom;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);

		initUI();
	}

	/**
	 * 初始化控件
	 */
	private void initUI() {
		//找到我们关心的控件
		iv_center = (ImageView) findViewById(R.id.iv_center);
		bt_top = (Button)findViewById(R.id.bt_top);
		bt_bottom = (Button)findViewById(R.id.bt_bottom);
		//获得屏幕的高度和宽度
		mWmManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mHeight = mWmManager.getDefaultDisplay().getHeight();
		mWidth = mWmManager.getDefaultDisplay().getWidth();
		//获取之前存储的位置
		int readInt = SpUtils.readInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		int readInt2 = SpUtils.readInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
		
		//图片在相对布局中,所以其所在位置的规则由相对布局提供
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin=readInt;
		layoutParams.topMargin=readInt2;
		//将此规则应用到图片控件上
		iv_center.setLayoutParams(layoutParams);
		
		if(readInt2>mHeight/2){
			bt_bottom.setVisibility(View.INVISIBLE);
			bt_top.setVisibility(View.VISIBLE);
		}else{
			bt_bottom.setVisibility(View.VISIBLE);
			bt_top.setVisibility(View.INVISIBLE);
		}
		
		
		//图片拖拽
		iv_center.setOnTouchListener(new OnTouchListener() {
			private float startX;
			private float startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				// 1.判断是那种触摸方式
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					float moveX = event.getRawX();
					float moveY = event.getRawY();
					
					float disX = moveX-startX;
					float disY = moveY-startY;
					//2.获得未移动之前控件距屏幕左上角的位置
					int left = iv_center.getLeft();
					int right = iv_center.getRight();
					int top = iv_center.getTop();
					int bottom = iv_center.getBottom();
					//3.经过移动点击事件之后的位置
					int aleft = (int) (left+disX);
					int aright = (int) (right+disX);
					int atop = (int) (top+disY);
					int abottom = (int) (bottom+disY);
					//容错处理
					if(aleft<0){
						return true;
					}
					if(aright>mWidth){
						return true;
					}
					if(atop<0){
						return true;
					}
					if(abottom>mHeight-22){
						return true;
					}
					
					
					if(atop>mHeight/2){
						bt_bottom.setVisibility(View.INVISIBLE);
						bt_top.setVisibility(View.VISIBLE);
					}else{
						bt_bottom.setVisibility(View.VISIBLE);
						bt_top.setVisibility(View.INVISIBLE);
					}
					
					//4.告诉控件移动到这个位置
					iv_center.layout(aleft, atop, aright, abottom);
					
					//5.重置一次起始坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//6.存储移动之后的位置
					int left2 = iv_center.getLeft();
					int top2 = iv_center.getTop();
					
					SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_X, left2);
					SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_Y, top2);
					break;

				}
				//既要响应拖拽,又要响应点击事件此处要返回false,如果只响应拖拽则返回true
				return false;
			}
		});
		//双击居中
		iv_center.setOnClickListener(new OnClickListener() {
			private long startTime=0;
			@Override
			public void onClick(View v) {
				if(startTime!=0){
					long endTime = System.currentTimeMillis();
					long disTime = endTime-startTime;
					if(disTime<500){
						//图片的宽度,长度
						int width = iv_center.getWidth();
						int height = iv_center.getHeight();
						
						//图片在相对布局中,所以其所在位置的规则由相对布局提供
						RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
						layoutParams.leftMargin=mWidth/2-width/2;
						layoutParams.topMargin=mHeight/2-height/2;
						//将此规则应用到图片控件上
						iv_center.setLayoutParams(layoutParams);
						//将位置保存到sp中
						SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_X, layoutParams.leftMargin);
						SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_Y, layoutParams.topMargin);
					}
				}
				startTime = System.currentTimeMillis();
			}
		});
	}
}
