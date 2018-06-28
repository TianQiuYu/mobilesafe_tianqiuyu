package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		initUI();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		final CheckBox cb_box = (CheckBox) findViewById(R.id.cb_box);
		// 读取本地是否已经开启了安全设置--进行回显
		Boolean readBoolean = SpUtils.readBoolean(getApplicationContext(),
				ConstantValue.OPEN_SAFE, false);

		cb_box.setChecked(readBoolean);

		if (readBoolean) {
			cb_box.setText("安全设置已开启");
		} else {
			cb_box.setText("安全设置已关闭");
		}

		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				cb_box.setChecked(isChecked);
				if (isChecked) {
					cb_box.setText("安全设置已开启");
				} else {
					cb_box.setText("安全设置已关闭");
				}

				SpUtils.writeBoolean(getApplicationContext(),
						ConstantValue.OPEN_SAFE, isChecked);
			}
		});
	}

	// 给按钮设置点击事件
	public void nextPage(View v) {
		// 判断一下是否开启了安全设置
		Boolean readBoolean = SpUtils.readBoolean(getApplicationContext(), ConstantValue.OPEN_SAFE, false);
		if (readBoolean) {
			Intent intent = new Intent(getApplicationContext(),
					SetOverActivity.class);
			startActivity(intent);
			finish();
			
			//存储设置完成的结果
			SpUtils.writeBoolean(getApplicationContext(), ConstantValue.SETUP_OVER, true);
			
			overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
		} else {
			ToastUtil.show(getApplicationContext(), "请开启防盗保护");
		}

	}

	public void ProPage(View v) {
		Intent intent = new Intent(getApplicationContext(),
				Setup3Activity.class);
		startActivity(intent);
		finish();
		
		overridePendingTransition(R.anim.pro_in_anim, R.anim.pro_out_anim);
	}
}
