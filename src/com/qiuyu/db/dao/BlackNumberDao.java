package com.qiuyu.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.qiuyu.db.BlackNumberOpenHelper;
import com.qiuyu.db.domain.BlackNumber;

public class BlackNumberDao {
	private BlackNumberOpenHelper blackNumberOpenHelper;
	private static BlackNumberDao blackNumberDao=null;
	//单例模式
	//1.私有化构造参数
	private BlackNumberDao(Context context){
		blackNumberOpenHelper = new BlackNumberOpenHelper(context);
	}
	//2.提供一个方法-返回一个对象
	public static BlackNumberDao getinstance(Context context){
		if(blackNumberDao==null){
		blackNumberDao = new BlackNumberDao(context);
		}
		return blackNumberDao;
	}
	//3.实现增删改查的方法
	public void insert(String phone,String mode){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
	}
	public void delete(String phone){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		db.delete("blacknumber", "phone=?", new String[]{phone});
		db.close();
	}
	public void update(String phone,String mode){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		db.update("blacknumber", values, "phone=?", new String[]{phone});
		db.close();
	}
	public ArrayList<BlackNumber> findAll(){
		ArrayList<BlackNumber> list = new ArrayList<BlackNumber>();
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"Phone", "mode"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			BlackNumber blackNumber = new BlackNumber();
			String phone = cursor.getString(0);
			String mode = cursor.getString(1);
			blackNumber.setPhone(phone);
			blackNumber.setMode(mode);
			list.add(blackNumber);
		}
		db.close();
		return list;
	}
}
