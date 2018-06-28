package com.qiuyu.activity;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qiuyu.mobilesafe.R;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;
import com.qiuyu.utils.StreamUtil;
import com.qiuyu.utils.ToastUtil;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {

	/**
	 * 应用需要更新
	 */
	protected static final int UPDATE_MSG = 1;
	/**
	 * 应用不需要更新 直接进入主界面
	 */
	protected static final int NOTUPDATE_MSG = 2;
	protected static final int JSON_ERROR = 3;
	protected static final int URL_ERROR = 4;
	protected static final int IO_ERROR = 5;
	private static final int INSTALL_ACTIVITY = 0;
	private String mVersionName;
	private int mVersionCode;
	private String mVersionDes;
	private String mDownloadUrl;
	private final String tag = "SplashActivity";
	private RelativeLayout rl_root;
	/**
	 * 消息机制:处理发过来的消息
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_MSG:
				// 应用需要更新--弹出对话框
				showDialog();

				break;
			case NOTUPDATE_MSG:
				// 应用不需更新--直接进入主界面
				enterHome();
				break;
			case JSON_ERROR:
				// 弹出吐司--抽成一个工具类
				ToastUtil.show(SplashActivity.this, "json解析异常");
				enterHome();
				break;
			case URL_ERROR:
				ToastUtil.show(SplashActivity.this, "url异常");
				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(SplashActivity.this, "io异常");
				enterHome();
				break;

			}
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 1.准备数据
		initData();

		// 2.初始化UI
		initUI();
		
		//3.设置一个透明动画
		setAphlaAnimation();
		//4.初始化数据库
		initDatabase();
	}

	private void initDatabase() {
		//1.初始化address数据库
		initAddressDB();
	}

	private void initAddressDB() {
		// TODO Auto-generated method stub
		InputStream in = null;
		FileOutputStream fos=null;
		// TODO Auto-generated method stub
		//1.初始化address.db数据库
		File filesDir = getFilesDir();
		File file = new File(filesDir, "address.db");
		//判断数据库是否已经初始化
		if(!file.exists()){
			//2.读取assets中的数据库
			AssetManager assets = getAssets();
			try {
				 in= assets.open("address.db");
				//3.读取流操作
				fos=new FileOutputStream(file);
				byte[] bb=new byte[1024];
				int temp=-1;
				while((temp=in.read(bb))!=-1){
					fos.write(bb, 0, temp);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
				if(in!=null&&fos!=null){
					try {
						in.close();
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}

	/**
	 * 设置透明动画
	 */
	private void setAphlaAnimation() {
		// TODO Auto-generated method stub
		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(3000);
		
		rl_root.setAnimation(alphaAnimation);
		
		
	}
	/**
	 * 应用需要更新,弹出更新对话框
	 */
	protected void showDialog() {
		// 参数不能是getApplicationContext--因为对话框是依附在activity上的,需要指定一个可以依附的activity对象
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新");
		builder.setMessage(mVersionDes);

		// 两个按钮

		builder.setPositiveButton("立即更新",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 立即更新--下载服务器中的apk
						downLoadUpdateApk();

					}
				});

		builder.setNegativeButton("暂不更新",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//
						enterHome();

					}
				});

		//对话框取消时调用
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			//Parameters:dialog The dialog that was canceled will be passed into the method.
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
				dialog.dismiss();
			}
		});
		// 最后一定要记得show出来!!!!!!!!!!!!!!!!!
				builder.show();
	}

	/**
	 * 下载需要更新的apk文件,并且安装
	 */
	protected void downLoadUpdateApk() {
		//1.下载文件放置的路径--这是sd存在
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mobileSafe.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//咱们下载的文件
					File file = arg0.result;
					//提示用户安装
					installApk(file);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}
				@Override
				public void setUserTag(Object userTag) {
					super.setUserTag(userTag);
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
				}
			});
		}
	}

	/**
	 * 安装更新的应用的方法
	 * @param file 要安装的文件
	 */
	protected void installApk(File file) {
		//1.开启系统的安装页面--隐式意图
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		
		startActivityForResult(intent, INSTALL_ACTIVITY);
		
	}
	
	//当用startActivityForResult(intent, 2)方法开启的activity关闭时就会调用这个方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case INSTALL_ACTIVITY:
			enterHome();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入应用主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		// 开启activity之后关闭splashactivity
		finish();
	}

	/**
	 * 准备数据
	 */
	private void initData() {
		// 1.得到版本名称数据
		mVersionName = getVersionName();
		// 2.得到本地的版本号数据
		mVersionCode = getVersionCode();
		
		//判断应用是否开启了自动更新开关
		Boolean updata_state = SpUtils.readBoolean(getApplicationContext(), ConstantValue.UPDATA_STATE, false);
		if(updata_state){
			// 3.检测版本号
			checkVersion();
		}else {
			//TODO
			handler.sendEmptyMessageDelayed(NOTUPDATE_MSG, 3000);
		}
	}

	/**
	 * 与服务器发过来的版本号相比较
	 */
	private void checkVersion() {
		new Thread() {

			public void run() {
				// 0.创建message对象
				Message msg = Message.obtain();
				// 0.1记录开始长时间操作(联网操作)的时间
				long beforeTime = System.currentTimeMillis();
				// 1.创建url对象
				try {
					URL url = new URL("http://10.216.36.138:8080/update.json");
					// 2.打开一个连接

					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					// 3.设置请求的参数
					connection.setConnectTimeout(2000);
					connection.setReadTimeout(5000);
					connection.setRequestMethod("GET");

					// 4.获得状态码 200==访问成功

					if (connection.getResponseCode() == 200) {
						InputStream in = connection.getInputStream();
						// 5.把流对象转换成字符串
						String content = StreamUtil.streamToString(in);
						// System.out.println(content);
						Log.i(tag, content);
						// 6.将字符串转换成json对象
						JSONObject jsonObject = new JSONObject(content);

						// 7.获取json对象中的字段
						String versionCode = jsonObject
								.getString("versionCode");
						String versionName = jsonObject
								.getString("versionName");
						mVersionDes = jsonObject.getString("versionDes");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						// 8.与应用当前的版本号比较
						if (Integer.parseInt(versionCode) > mVersionCode) {
							// 需要更新--弹出对话框--更新ui--在主线程中--使用handler
							msg.what = UPDATE_MSG;
						} else {
							// 不需要更新--直接进入应用主界面
							msg.what = NOTUPDATE_MSG;
						}

					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = JSON_ERROR;
				}finally {
					// 长时间操作(联网操作)之后的时间
					long afterTime = System.currentTimeMillis();
					// 判断长时间操作的时间是否超过了3秒钟,如果没超过保证3秒钟之后在进入主界面
					long timeDif = afterTime - beforeTime;
					if (timeDif < 3000) {
						try {
							Thread.sleep((3000 - timeDif));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 得到应用的版本号数据
	 * 
	 * @return 返回0代表出现异常,否则正常
	 */
	private int getVersionCode() {
		// 1.得到包管理者
		PackageManager pm = getPackageManager();
		// 2.得到该应用包的基本信息
		try {
			PackageInfo PI = pm.getPackageInfo(this.getPackageName(), 0);
			return PI.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到版本号名称
	 * 
	 * @return 返回版本号名称 如果返回null 说明发生了异常
	 */
	private String getVersionName() {
		// 1.得到包管理者
		PackageManager pm = getPackageManager();
		// 2.得到该应用包的基本信息
		try {
			PackageInfo PI = pm.getPackageInfo(this.getPackageName(), 0);
			return PI.versionName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		TextView tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		tv_version_name.setText("版本:" + mVersionName);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
	}
}
