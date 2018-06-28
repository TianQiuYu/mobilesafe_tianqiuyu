package com.qiuyu.receiver;

import java.util.List;

import com.qiuyu.mobilesafe.R;
import com.qiuyu.service.LocationService;
import com.qiuyu.utils.ConstantValue;
import com.qiuyu.utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
//广播接受者里面不能够处理耗时业务逻辑
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		//1.判断是否开启了防盗保护
		Boolean open_safe = SpUtils.readBoolean(context, ConstantValue.OPEN_SAFE, false);
		if(open_safe){
			//2.获取短信内容
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			//3.遍历短信内容,匹配标示
			for (Object object2 : object) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object2);
				String messageBody = smsMessage.getMessageBody();
				//4.判断是否包含#*alarm*#
				
				if(messageBody.contains("#*alarm*#")){
					//5.播放报警音乐
					MediaPlayer mediaplayer = MediaPlayer.create(context, R.raw.ylzs);
					//5.1循环
					mediaplayer.setLooping(true);
					mediaplayer.start();
				}
				if(messageBody.contains("#*location*#")){
					//1.开启服务
					Intent intent2 = new Intent(context, LocationService.class);
					context.startService(intent2);
			}
		}
	}

}
}
