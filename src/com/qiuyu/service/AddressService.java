package com.qiuyu.service;

import com.qiuyu.engine.AddressDao;
import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	private TelephonyManager mTm;
	private MyPhoneStateListener mMyPhoneStateListener;
	private WindowManager.LayoutParams mParams=new WindowManager.LayoutParams();
	private static TextView tv_toast;
	private static String mQueryAddress;
	private LinearLayout my_toast_view;
	private WindowManager mWm;
	private int width;
	private int height;
	private static Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			tv_toast.setText(mQueryAddress);
		};
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//监听电话
		mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mMyPhoneStateListener = new MyPhoneStateListener();
		mTm.listen(mMyPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		//获得屏幕的宽度和高度
		mWm = (WindowManager) getSystemService(WINDOW_SERVICE);
		width = mWm.getDefaultDisplay().getWidth();
		height = mWm.getDefaultDisplay().getHeight();
		//监听拨打的广播--动态注册
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//创建广播接受者对象
		OutCallReceiver outCallReceiver = new OutCallReceiver();
		registerReceiver(outCallReceiver, intentFilter);
	}
	
	//定义广播接受者
	class OutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String phone = getResultData();
			showMyToast(phone);
		}
		
	}

	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//移除吐司
				if(mWm!=null&&my_toast_view!=null){
					mWm.removeView(my_toast_view);
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//展示吐司
				showMyToast(incomingNumber);
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			}
		}
	}
	
	/**
	 * 展示自己定义的吐司
	 * @param incomingNumber来电的号码
	 */
	public void showMyToast(String incomingNumber) {
		
		 final WindowManager.LayoutParams params = mParams;
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         params.format = PixelFormat.TRANSLUCENT;
         params.type = WindowManager.LayoutParams.TYPE_PHONE;
         params.setTitle("Toast");
         //指定吐司展示位置
         params.gravity=Gravity.LEFT+Gravity.TOP;
         //读取本地保存吐司的位置
         int left = SpUtils.readInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
         int top = SpUtils.readInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
         params.x=left;
         params.y=top;
         
         //为吐司加载一个布局
         my_toast_view = (LinearLayout) View.inflate(getApplicationContext(), R.layout.my_toast_view, null);
         tv_toast = (TextView) my_toast_view.findViewById(R.id.tv_toast);
         
         //添加一个拖拽事件
         my_toast_view.setOnTouchListener(new OnTouchListener() {
			
			private float startX;
			private float startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				//判断是那种触摸方式
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
					
					params.x=(int) (params.x+disX);
					params.y=(int) (params.y+disY);
					
					//容错处理
					if(params.x<0){
						params.x=0;
					}
					if(params.x>width-my_toast_view.getWidth()){
						params.x=width-my_toast_view.getWidth();
					}
					if(params.y<0){
						params.y=0;
					}
					if(params.y>height-my_toast_view.getHeight()-22){
						params.y=height-my_toast_view.getHeight()-22;
					}
					
					//告诉窗体安装移动之后的坐标去更新
					mWm.updateViewLayout(my_toast_view, params);
					
					//重置一次起始坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					
					//存储移动之后的位置
					SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_X, params.x);
					SpUtils.writeInt(getApplicationContext(), ConstantValue.LOCATION_Y, params.y);
					break;

				}
				return true;
			}
		});
         
         //选择吐司的显示风格
         int[] aa=new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,
        		 			R.drawable.call_locate_gray,R.drawable.call_locate_gray};
         
         //读取sp中保存的颜色索引值
         int style_index = SpUtils.readInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
         tv_toast.setBackgroundResource(aa[style_index]);
         
         //根据打进来的电话号码查询归属地
         queryAddress(incomingNumber);
         
         //获得窗体管理者
         mWm=(WindowManager) getSystemService(WINDOW_SERVICE);
         //添加一个吐司
         mWm.addView(my_toast_view, params);
         
	}

	/**
	 * 
	 * @param incomingNumber打进来的号码
	 */
	private void queryAddress(final String incomingNumber) {
		new Thread(){
			public void run() {
				mQueryAddress = AddressDao.queryAddress(incomingNumber);
				mHandler.sendEmptyMessage(1);			};
		}.start();
	}
	@Override
	public void onDestroy() {
		//取消电话状态的监听
		mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}


}
