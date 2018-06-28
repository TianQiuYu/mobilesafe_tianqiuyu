package com.qiuyu.utils;

import java.net.ContentHandler;

import android.content.Context;
import android.widget.Toast;

/**
 * @author liqiu
 *弹出吐司
 */
public class ToastUtil {

	public static void show(Context context,String content) {
		// TODO Auto-generated method stub
		Toast.makeText(context, content, 0).show();
	}

}
