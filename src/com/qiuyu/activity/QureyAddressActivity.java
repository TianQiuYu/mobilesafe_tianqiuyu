package com.qiuyu.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.animation.Animation;
import com.qiuyu.engine.AddressDao;
import com.qiuyu.mobilesafe.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QureyAddressActivity extends Activity {
	private String location;
	private TextView tv_display_result;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			tv_display_result.setText(location);
		};
	};
	private EditText et_phone_number;
	private Button bt_qurey;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qurey_address);

		initUI();

	}

	private void initUI() {
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		bt_qurey = (Button) findViewById(R.id.bt_qurey);
		tv_display_result = (TextView) findViewById(R.id.tv_display_result);

		bt_qurey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = et_phone_number.getText().toString();
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(phone)) {
					// 根据号码查询归属地--引入引擎概念
					query(phone);
				}else {
					//抖动效果
					Animation shake=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
			        et_phone_number.startAnimation(shake);
			        //振动效果
			        Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
			        vibrator.vibrate(new long[]{2000,5000,2000,5000}, -1);
				}
				
			}
		});
		
		//实时查询的操作--给EditText设置监听器
		et_phone_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String phone = et_phone_number.getText().toString();
				query(phone);
			}
		});
	
	}

	/**
	 * 
	 * @param phone
	 *            要查询的号码
	 */
	protected void query(final String phone) {
		//查询数据库是耗时操作放在子线程中
		new Thread() {

			public void run() {
				location = AddressDao.queryAddress(phone);
				handler.sendEmptyMessage(1);
			}
		}.start();

	}
}
