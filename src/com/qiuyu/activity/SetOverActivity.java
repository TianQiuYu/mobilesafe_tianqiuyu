package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetOverActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//1.得到是否完成设置的判断符
		Boolean setup_over = SpUtils.readBoolean(getApplicationContext(), ConstantValue.SETUP_OVER, false);
		if(setup_over){
			//设置完成,进入设置完成界面
			setContentView(R.layout.activity_setover);
			
			initUI();
		}else {
			//设置未完成,进入设置导航界面
			Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		
	}

	private void initUI() {
		//设置安全号码
		TextView tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
		
		String phone = SpUtils.readString(getApplicationContext(), ConstantValue.PHONE_NUMBER, "");
		
		tv_safe_number.setText(phone);
		
		TextView tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
		
		tv_reset_setup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
