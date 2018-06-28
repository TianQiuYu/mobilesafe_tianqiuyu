package com.qiuyu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView view = new TextView(getApplicationContext());
		view.setText("woshiceshijiemain");
		setContentView(view);
	}
}
