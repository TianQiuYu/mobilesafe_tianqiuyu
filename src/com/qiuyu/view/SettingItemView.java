package com.qiuyu.view;

import com.qiuyu.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.qiuyu.mobilesafe";
	private CheckBox cb;
	private TextView tv_des;
	private TextView tv_title;
	private String desTitle;
	private String desOff;
	private String desOn;

	public SettingItemView(Context context) {
		this(context, null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initUI(context);

		initData(attrs);

		// 3.将获得的属性值设置到子控件中去
		tv_title.setText(desTitle);

	}

	/**
	 * 获取组合控件自定义的属性
	 * 
	 * @param attrs
	 *            传过来的属性集合
	 */
	private void initData(AttributeSet attrs) {
		desTitle = attrs.getAttributeValue(NAMESPACE, "destitle");
		desOff = attrs.getAttributeValue(NAMESPACE, "desoff");
		desOn = attrs.getAttributeValue(NAMESPACE, "deson");
	}

	/**
	 * 找到组合控件里面的子控件
	 */
	private void initUI(Context context) {
		// 1.找到组合控件里面的子控件
		View view = View.inflate(context, R.layout.item_update_setting, this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
		cb = (CheckBox) view.findViewById(R.id.cb);
	}

	/**
	 * 获取当前条目的CheckBox的开启状态
	 */
	public boolean isCheck() {
		// TODO Auto-generated method stub
		return cb.isChecked();
	}

	/**
	 * 设置CheckBox的状态
	 * 
	 * @param isChecked
	 *            要设置给CheckBox的状态
	 */
	public void setCheck(boolean isChecked) {
		cb.setChecked(isChecked);
		if (isChecked) {
			// 开启
			tv_des.setText(desOn);

		} else {
			// 关闭
			tv_des.setText(desOff);
		}
	}

}
