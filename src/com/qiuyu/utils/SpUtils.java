package com.qiuyu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

	private static SharedPreferences sp;

	/**
	 * 把当前的Boolean值写入到sharedpreference中
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            键的名称
	 * @param value
	 *            键的值
	 */
	public static void writeBoolean(Context context, String key, Boolean value) {

		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	public static Boolean readBoolean(Context context, String key,
			Boolean defaultVaule) {
		// (存储节点文件名称,读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defaultVaule);
	}
	
	/**
	 * 把当前的String值写入到sharedpreference中
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            键的名称
	 * @param value
	 *            键的值
	 */
	public static void writeString(Context context, String key, String value) {

		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}

	public static String readString(Context context, String key,
			String defaultVaule) {
		// (存储节点文件名称,读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defaultVaule);
	}
	
	public static void remove(Context context, String key){
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().remove(key).commit();
	}
	
	
	public static void writeInt(Context context, String key, int value) {

		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	public static int readInt(Context context, String key,
			int defaultVaule) {
		// (存储节点文件名称,读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defaultVaule);
	}

}
