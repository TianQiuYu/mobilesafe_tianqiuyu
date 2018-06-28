package com.qiuyu.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtil {
	public static boolean isRunning(Context context,String ServiceName){
		//1.获取activitymanager管理者对象
		ActivityManager AM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//2.获取正在运行的所有服务集合
		List<RunningServiceInfo> runningServices = AM.getRunningServices(200);
		//3.遍历所有服务,判断是否有何传进来的服务名一致的服务
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			if(ServiceName.equals(className)){
				//说明服务正在运行
				return true;
			}
		}
		return false;
	}
}
