package com.qiuyu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//1.获取位置管理者对象
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//2.以最优的方式获取经纬度坐标
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//3.获得位置提供器--有网络\基站\GPS
		String bestProvider = lm.getBestProvider(criteria, true);
		
		
		lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			//当位置改变时调用这个方法
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				//发送位置短信给安全号码
				sendAalarmMessage(location);
			}
		});
		
		
	}
	/**
	 * 发送短信给安全号码
	 * @param location 位置对象
	 */
	protected void sendAalarmMessage(Location location) {
		// TODO Auto-generated method stub
		SmsManager sm = SmsManager.getDefault();
		
//		String phoneNumber = SpUtils.readString(getApplicationContext(), ConstantValue.PHONE_NUMBER, "");
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		String text="latitude:"+latitude+",longitude:"+longitude;
		sm.sendTextMessage("5556", null, text, null,null);
	}

}
