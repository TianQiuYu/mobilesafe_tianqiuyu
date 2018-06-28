package com.qiuyu.activity;

import org.w3c.dom.Text;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.utils.ToastUtil;
import com.qiuyu.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Setup2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		final SettingItemView siv_number_bound = (SettingItemView) findViewById(R.id.siv_number_bound);

		// 判断是否已经保存了SIM卡序列号
		String number_bound = SpUtils.readString(getApplicationContext(),
				ConstantValue.NUMBER_BOUND, "");
		if (TextUtils.isEmpty(number_bound)) {
			siv_number_bound.setCheck(false);
		} else {
			siv_number_bound.setCheck(true);
		}

		siv_number_bound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取当前的CheckBox的状态
				boolean check = siv_number_bound.isCheck();
				siv_number_bound.setCheck(!check);
				if (!check) {
					// 存储SIM卡的序列号
					TelephonyManager telephony_service=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = telephony_service.getSimSerialNumber();
					SpUtils.writeString(getApplicationContext(),
							ConstantValue.NUMBER_BOUND,simSerialNumber);
				} else {
					SpUtils.remove(getApplicationContext(),
							ConstantValue.NUMBER_BOUND);
				}
			}
		});
	}

	// 给按钮设置点击事件
	public void nextPage(View v) {
		//判断是否已经绑定了SIM卡的序列号
		String number_bound = SpUtils.readString(getApplicationContext(), ConstantValue.NUMBER_BOUND, "");
		if(!TextUtils.isEmpty(number_bound)){
			Intent intent = new Intent(getApplicationContext(),
					Setup3Activity.class);
			startActivity(intent);
			finish();
			
			overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
		}else {
			ToastUtil.show(getApplicationContext(), "请先绑定SIM卡");
		}
		
	}

	public void ProPage(View v) {
		Intent intent = new Intent(getApplicationContext(),
				Setup1Activity.class);
		startActivity(intent);
		finish();
		//加载平移动画
		overridePendingTransition(R.anim.pro_in_anim, R.anim.pro_out_anim);
	}
}
