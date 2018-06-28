package com.qiuyu.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	private static String location = "未知号码";

	public static String queryAddress(String phone) {
		location = "未知号码";
		// 正则表达式^1[3-8]\\d{9}
		String regularExpression = "^1[3-8]\\d{9}";
		// 从数据库中查询--从资产文件到拷贝到本地中去
		String pathString = "data/data/com.qiuyu.mobilesafe/files/address.db";
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(pathString,
				null, SQLiteDatabase.OPEN_READONLY);
		if (phone.matches(regularExpression)) {
			phone=phone.substring(0, 7);
			// 执行查询逻辑
			Cursor cursor = openDatabase.query("data1",
					new String[] { "outkey" }, "id=?", new String[] { phone },
					null, null, null);
			if (cursor.moveToNext()) {
				String outkey = cursor.getString(0);
				// 再此查询
				Cursor cursor_location = openDatabase.query("data2",
						new String[] { "location" }, "id=?",
						new String[] { outkey }, null, null, null);
				if (cursor_location.moveToNext()) {
					location = cursor_location.getString(0);
					// 数据库不关
					// openDatabase.close();
				}
			} else {
				location = "未知号码";
			}

		}else {
			int length = phone.length();
			switch (length) {
			case 3:// 119 110 120 114
				location = "报警电话";
				break;
			case 4:// 119 110 120 114
				location = "模拟器";
				break;
			case 5:// 10086 99555
				location = "服务电话";
				break;
			case 7:
				location = "本地电话";
				break;
			case 8:
				location = "本地电话";
				break;
			case 11:
				// (3+8) 区号+座机号码(外地),查询data2
				String area = phone.substring(1, 3);
				Cursor cursor = openDatabase.query("data2",
						new String[] { "location" }, "area = ?",
						new String[] { area }, null, null, null);
				if (cursor.moveToNext()) {
					location = cursor.getString(0);
				} else {
					location = "未知号码";
				}
				break;
			case 12:
				// (4+8) 区号(0791(江西南昌))+座机号码(外地),查询data2
				String area1 = phone.substring(1, 4);
				Cursor cursor1 = openDatabase.query("data2",
						new String[] { "location" }, "area = ?",
						new String[] { area1 }, null, null, null);
				if (cursor1.moveToNext()) {
					location = cursor1.getString(0);
				} else {
					location = "未知号码";
				}
				break;
			}

		}
		return location;
	}

}
