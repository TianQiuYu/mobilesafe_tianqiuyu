package com.qiuyu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * @param in 流对象
	 * @return 把对象转换成字符串
	 */
	public static String streamToString(InputStream in) {
		//使用ByteArrayOutputStream,因为数据较少,将数据写到缓存中,然后一次性转换成字符串
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		byte[] bt=new byte[1024];
		int temp=-1;
		try {
			while((temp=in.read(bt))!=-1){
				bos.write(bt,0,temp);
			}
			return bos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				//关闭流
				in.close();
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
