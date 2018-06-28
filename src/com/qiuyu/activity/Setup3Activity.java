package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends Activity {
	private EditText contact_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		
		initUI();
	}
	
	private void initUI() {
		contact_phone = (EditText) findViewById(R.id.et_contact_phone);
		//读取sp,进行号码回显
		String phone = SpUtils.readString(getApplicationContext(), ConstantValue.PHONE_NUMBER, "");
		if(phone!=null){
			contact_phone.setText(phone);
		}
		Button bt_select_contact = (Button) findViewById(R.id.bt_select_contact);
		//给按钮设置点击事件
		bt_select_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击按钮跳转到一个新的activity,用于展示联系人数据
				Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
				startActivityForResult(intent, 0);
				
			}
		});
	}
	//当使用startActivityForResult(intent, 0);调用的activity关闭时调用这个方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String phone = data.getStringExtra("phone");
			//对特殊字符进行处理一下--trim是去除两端的空格
			phone=phone.replace("-", "").replace(" ", "").trim();
			//设置给edittext
			contact_phone.setText(phone);
			//保存一下
			SpUtils.writeString(getApplicationContext(), ConstantValue.PHONE_NUMBER,phone);
		}
	}

	// 给按钮设置点击事件
		public void nextPage(View v) {
			//判断一下号码编写框中是否有数据
			String phone = contact_phone.getText().toString();
			if(phone!=null){
				Intent intent = new Intent(getApplicationContext(),
						Setup4Activity.class);
				startActivity(intent);
				finish();
				//把2用户直接输入的号码进行保存操作
				SpUtils.writeString(getApplicationContext(), ConstantValue.PHONE_NUMBER, phone);
				
				overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
			}else {
				ToastUtil.show(getApplicationContext(), "请输入安全号码");
			}
			
		}

		public void ProPage(View v) {
			Intent intent = new Intent(getApplicationContext(),
					Setup2Activity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.pro_in_anim, R.anim.pro_out_anim);
		}
}
