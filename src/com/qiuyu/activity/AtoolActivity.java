package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AtoolActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		
		initUI();
		
	}

	private void initUI() {
		// TODO Auto-generated method stub
		TextView tv_phone_address = (TextView) findViewById(R.id.tv_phone_address);
		
		tv_phone_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), QureyAddressActivity.class));
			}
		});
	}
}
