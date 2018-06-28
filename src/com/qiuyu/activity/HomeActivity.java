package com.qiuyu.activity;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.Md5Util;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private GridView gv_function;
	private String[] mFunction_names;
	private int[] mFunction_imgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initUI();

		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mFunction_names = new String[] { "手机防盗", "通信卫士", "软件管理", "进程管理",
				"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

		mFunction_imgs = new int[] { R.drawable.home_safe,
				R.drawable.home_callmsgsafe, R.drawable.home_apps,
				R.drawable.home_taskmanager, R.drawable.home_netmanager,
				R.drawable.home_trojan, R.drawable.home_sysoptimize,
				R.drawable.home_tools, R.drawable.home_settings };

		// 设置数据适配器
		gv_function.setAdapter(new Myadapter());
		// 设置单个条目的点击事件
		gv_function.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					showDialog();
					break;
				case 1:
					startActivity(new Intent(getApplicationContext(), BlackNumberActivity.class));
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(), AtoolActivity.class));
					break;
				case 8:
					enterSettingActivity();
					break;

				default:
					break;
				}
			}
		});

	}

	/**
	 * 进入手机防盗模块,弹出对话框
	 */
	protected void showDialog() {
		// 1.判断本地是否已经保存了密码
		String mobile_safe_psd = SpUtils.readString(getApplicationContext(),
				ConstantValue.MOBILE_SAFE_PSD, "");
		if (TextUtils.isEmpty(mobile_safe_psd)) {
			// 2.弹出设置密码对话框
			showSetPsdDialog();
		} else {
			// 3.弹出确认密码对话框
			showConfirmPsdDialog();
		}
	}

	/**
	 * 弹出确认密码对象框
	 */
	private void showConfirmPsdDialog() {
		// 自己定义对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog create = builder.create();
		// 将自己定义的xml文件转换为一个view对象
		View view = View.inflate(getApplicationContext(),R.layout.dialog_confirm_psd, null);
		create.setView(view);
		create.show();
		
		//得到弹出对话框的控件
		final EditText et_input = (EditText) view.findViewById(R.id.et_input);
		final EditText et_confirm = (EditText) view.findViewById(R.id.et_confirm);
		Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		//给确认按钮设置点击事件
		bt_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//和本地存储的密码比对
				String input_psd = et_input.getText().toString();
				input_psd=Md5Util.encoder(input_psd);
				String local_psd = SpUtils.readString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
				if(input_psd.equals(local_psd)){
					Intent intent = new Intent(getApplicationContext(), SetOverActivity.class);
					startActivity(intent);
					create.dismiss();
				}else {
					ToastUtil.show(getApplicationContext(), "输入的密码不正确");
				}
			}
		});
		//给取消按钮设置点击事件
		bt_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				create.dismiss();
			}
			
		});
		

	}

	/**
	 * 弹出设置密码对象框
	 */
	private void showSetPsdDialog() {
		// 自己定义对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog create = builder.create();
		// 将自己定义的xml文件转换为一个view对象
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_setting_psd, null);
		create.setView(view);
		create.show();
		
		//找到对话框中的控件
				final EditText et_input = (EditText) view.findViewById(R.id.et_input);
				final EditText et_confirm = (EditText) view.findViewById(R.id.et_confirm);
				Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
				Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
				
				//给确认按钮设置点击事件
				bt_confirm.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//1.获得用户输入的密码
						String input_psd = et_input.getText().toString();
						String confirm_psd = et_confirm.getText().toString();
//						System.out.println(input_psd+"==="+confirm_psd);
						if(!TextUtils.isEmpty(input_psd)&&!TextUtils.isEmpty(confirm_psd)){
							//判断密码是否相同
							if(input_psd.equals(confirm_psd)){
								//存储密码
								SpUtils.writeString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(input_psd));
								//进入手机防盗界面
								Intent intent = new Intent(getApplicationContext(), SetOverActivity.class);
								startActivity(intent);
								create.dismiss();
							}else {
								ToastUtil.show(getApplicationContext(), "密码不一致");
							}
						}else{
							ToastUtil.show(getApplicationContext(), "请输入密码");
						}
					}
				});
				//给取消按钮设置点击事件
				bt_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						create.dismiss();
					}
				});
		
		
		
	}

	/**
	 * 进入设置界面
	 */
	protected void enterSettingActivity() {
		Intent intent = new Intent(getApplicationContext(),
				SettingActivity.class);
		startActivity(intent);
	}

	/**
	 * 更新ui
	 */
	private void initUI() {
		gv_function = (GridView) findViewById(R.id.gv_function);
	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mFunction_imgs.length;
		}

		@Override
		public Object getItem(int position) {
			return mFunction_imgs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.grid_item, null);

			ImageView function_img = (ImageView) view
					.findViewById(R.id.iv_function_img);
			TextView function_name = (TextView) view
					.findViewById(R.id.tv_function_name);

			function_img.setImageResource(mFunction_imgs[position]);
			function_name.setText(mFunction_names[position]);
			return view;
		}

	}
}
