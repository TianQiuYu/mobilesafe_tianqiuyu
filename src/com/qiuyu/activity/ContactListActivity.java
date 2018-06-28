package com.qiuyu.activity;

import java.util.ArrayList;
import java.util.List;

import com.qiuyu.bean.Contact;
import com.qiuyu.mobilesafe.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends Activity {
	private ListView lv_contact_list;
	private List<Contact> list;
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_contact_list);
		
		initUI();
		
		initData();
	}

	/**
	 * 为listview准备数据集合
	 */
	private void initData() {
		//###使用内容解析者从安卓联系人的内容提供者获取数据
		//1.获取内容解析者   content://com.android.contacts/raw_contacts
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
						new String[]{"contact_id"}, 
						null, null, null);
		list=new ArrayList<Contact>();
		while(cursor.moveToNext()){
			String contact_id = cursor.getString(0);
			//根据用户唯一性id值,查询data表和mimetype表生成的视图,获取data1以及mimetype字段
			Cursor cursor_data = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
					new String[]{"mimetype","data1"}, 
					"raw_contact_id=?", new String[]{contact_id}, null);
			
			Contact contact =new Contact();
			
			while(cursor_data.moveToNext()){
				String mimetype_id = cursor_data.getString(0);
				String data1 = cursor_data.getString(1);
				if(mimetype_id.equals("vnd.android.cursor.item/phone_v2")){
					contact.setPhoneString(data1);
				}else{
					contact.setNameString(data1);
				}
			}
			list.add(contact);
		}
		myAdapter = new MyAdapter();
		lv_contact_list.setAdapter(myAdapter);
	}

	private void initUI() {
		lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
		//给listview条目设置点击事件
		lv_contact_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(myAdapter!=null){
					Contact item = (Contact) myAdapter.getItem(position);
					String phone = item.getPhoneString();
					//返回给开启自己的activity
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(0, intent);
					finish();
				}
			}
		});
		
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView != null){
				view = convertView;
			}else {
				view =  View.inflate(getApplicationContext(), R.layout.contact_list_item, null);
			}
		
			TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_phone_number = (TextView) view.findViewById(R.id.tv_phone_number);
			
			Contact contact = list.get(position);
			
			tv_contact_name.setText(contact.getNameString());
			tv_phone_number.setText(contact.getPhoneString());
			return view;
		}
		
	}
}
