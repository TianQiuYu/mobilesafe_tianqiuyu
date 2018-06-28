package com.qiuyu.test;

import com.qiuyu.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {
	public void insert(){
		BlackNumberDao bnd = BlackNumberDao.getinstance(getContext());
		bnd.insert("120", "1");
	}
	public void delete(){
		BlackNumberDao bnd = BlackNumberDao.getinstance(getContext());
		bnd.delete("120");
	}
}
