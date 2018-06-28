package com.qiuyu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusTextView extends TextView {
	//使用Java代码创建控件时调用
	public FocusTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//系统调用
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	//让咱们自定义的控件,始终聚焦
	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
