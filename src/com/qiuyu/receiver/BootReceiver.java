package com.qiuyu.receiver;

import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;

public class BootReceiver extends BroadcastReceiver {

	//接受到监听的广播事件时调用这个方法
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		本地保存的手机序列号和当前的SIM卡序列号比较
		String simNumber = SpUtils.readString(context, ConstantValue.NUMBER_BOUND, "");
		TelephonyManager systemService = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String simSerialNumber = systemService.getSimSerialNumber()+"11";
		
		if(simNumber!=simSerialNumber){
			android.telephony.SmsManager default1 = android.telephony.SmsManager.getDefault();
			default1.sendTextMessage(SpUtils.readString(context, ConstantValue.PHONE_NUMBER, ""), 
					null, "sim changed", null, null);
		}
		
	}

}
