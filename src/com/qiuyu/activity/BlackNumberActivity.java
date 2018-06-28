package com.qiuyu.activity;

import java.util.ArrayList;

import com.qiuyu.activity.ContactListActivity.MyAdapter;
import com.qiuyu.db.dao.BlackNumberDao;
import com.qiuyu.db.domain.BlackNumber;
import com.qiuyu.mobilesafe.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BlackNumberActivity extends Activity {
	private Button bt_add;
	private ListView lv_black;
	private ArrayList<BlackNumber> findAll;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			//展示数据
		
			lv_black.setAdapter(new MyAdapter());
			
		};
	};
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return findAll.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return findAll.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);      
			ImageView iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
			String phone = findAll.get(position).getPhone();
			tv_phone.setText(phone);
			String mode = findAll.get(position).getMode();
			int intMode = Integer.parseInt(mode);
			switch (intMode) {
			case 1:
				tv_mode.setText("拦截所有");
				break;
			case 2:
				tv_mode.setText("拦截电话");
				break;
			case 3:
				tv_mode.setText("拦截短信");
				break;
			}
			return view;
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		
		initUI();
		initData();
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		//从数据库中读取数据-耗时操作
		new Thread(){
			

			public void run() {
				BlackNumberDao bnd = BlackNumberDao.getinstance(getApplicationContext());
				findAll = bnd.findAll();
				//发送消息
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	private void initUI() {
		// 1.找到我们关心的控件
		bt_add = (Button) findViewById(R.id.bt_add);
		lv_black = (ListView) findViewById(R.id.lv_blcak_list);
		//2.给按钮设置事件监听器
		bt_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//3.弹出添加联系人到黑名单对话框
				showAddDialog();
			}

		});
	}

	protected void showAddDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder addDialog = new AlertDialog.Builder(this);
		AlertDialog create = addDialog.create();
		View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);
		create.setView(view);
		create.show();
	}
}
