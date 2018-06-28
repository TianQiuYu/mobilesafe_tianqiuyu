package com.qiuyu.view;

import com.qiuyu.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingAddressView extends RelativeLayout {

	private TextView tv_des;
	private TextView tv_title;

	public SettingAddressView(Context context) {
		this(context, null);
	}

	public SettingAddressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingAddressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initUI(context);

	}


	/**
	 * 找到组合控件里面的子控件
	 */
	private void initUI(Context context) {
		// 1.找到组合控件里面的子控件
		View view = View.inflate(context, R.layout.item_address_setting, this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
	}
	
	public void setTitle(String title){
		tv_title.setText(title);
	}

	public void setDes(String Des){
		tv_des.setText(Des);
	}


}
