package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import android.app.AlertDialog.Builder;
import com.qiuyu.service.AddressService;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.ServiceUtil;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.view.SettingAddressView;
import com.qiuyu.view.SettingItemView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private String[] mToastStyles;
	private int mReadInt;
	private SettingAddressView sav;
	private SettingAddressView itl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		//1.初始化自动更新条目
		initUpdata();
		//2.初始化归属地显示条目
		initAddress();
		//3.初始化归属地吐司风格
		initAddressStyle();
		//4.初始化归属地吐司的显示位置
		initToastLocation();
	
	}

	private void initToastLocation() {
		itl = (SettingAddressView)findViewById(R.id.item_toast_location);
		
		itl.setTitle("归属地提示框位置设置");
		itl.setDes("设置归属地提示框位置");
		itl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//弹出一个半透明的对话框
				Intent intent = new Intent(getApplicationContext(), ToastLocationActivity.class);
				startActivity(intent);
				
			}
		});
	}

	private void initAddressStyle() {
		//1.显示风格集合
		mToastStyles = new String[]{"透明","橙色","蓝色","灰色","绿色"};
		sav = (SettingAddressView)findViewById(R.id.item_address_style);
		sav.setTitle("设置归属地显示风格");
		mReadInt = SpUtils.readInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
		
		sav.setDes(mToastStyles[mReadInt]);
		sav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showToastStyleDialog();
			}
		});
	}

	protected void showToastStyleDialog() {
		// TODO Auto-generated method stub
		Builder builder =new AlertDialog.Builder(this);
		
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle("请选择显示风格");
		int readInt = SpUtils.readInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
		builder.setSingleChoiceItems(mToastStyles, readInt, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//保存显示风格到sp中
				SpUtils.writeInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
				sav.setDes(mToastStyles[which]);
				dialog.dismiss();
			}
			
		});
		
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		builder.show();
	}

	/**
	 * 初始化归属地显示条目
	 */
	private void initAddress() {
		// TODO Auto-generated method stub
		final SettingItemView item_siv_address = (SettingItemView) findViewById(R.id.item_siv_address);
		
		/*//读取本地保存的归属地是否显示的状态
		Boolean readBoolean = SpUtils.readBoolean(getApplicationContext(), ConstantValue.ADDRESS__DISPLAY, false);*/
		 boolean readBoolean = ServiceUtil.isRunning(getApplicationContext(), "com.qiuyu.service.AddressService");
		
		item_siv_address.setCheck(readBoolean);
		
		item_siv_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//获取当前的的状态
				boolean check = item_siv_address.isCheck();
				item_siv_address.setCheck(!check);
				if(!check){ 
					//开启归属地显示
					startService(new Intent(getApplicationContext(), AddressService.class));
				}else{
					//关闭归属地显示
					stopService(new Intent(getApplicationContext(), AddressService.class));
				}
			}
		});
	}

	/**
	 * //1.初始化自动更新条目
	 */
	private void initUpdata() {
	final SettingItemView siv_update=(SettingItemView)findViewById(R.id.item_update);
		//1.读取之前的sharedpreference存储的更新开关状态
		Boolean updata_state = SpUtils.readBoolean(getApplicationContext(), ConstantValue.UPDATA_STATE, false);
		//2.设置到条目中
		siv_update.setCheck(updata_state);
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1.判断当前item中的CheckBox的状态
				boolean check_state = siv_update.isCheck();
				
				siv_update.setCheck(!check_state);
				//2.将更改后的更新开关状态存储到sp中
				SpUtils.writeBoolean(getApplicationContext(), ConstantValue.UPDATA_STATE, !check_state);
			}
		});
	}
}
