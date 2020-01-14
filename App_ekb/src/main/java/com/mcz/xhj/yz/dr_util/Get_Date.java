package com.mcz.xhj.yz.dr_util;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;

public class Get_Date {

	public static String get_Internet_Time(String endDateStr) {
		// TODO Auto-generated method stub
		Date beginDate;
		Date endDate;
		URL url;
		String beginDateStr = null, endDate_Str = null;
		String end_time = "";
		long day = 0;
		Boolean is_date_type = true ;
		Date date ;
		try {
			url = new URL(UrlConfig.RENEWAL);

			HttpURLConnection  uc = (HttpURLConnection) url.openConnection();// 生成连接对象
			uc.setRequestMethod("POST") ;
			uc.setDoInput(true); // 向连接中写入数据
			uc.setDoOutput(true); // 从连接中读取数据
			uc.setUseCaches(false); // 禁止缓存
			uc.setInstanceFollowRedirects(true);   //自动执行HTTP重定向
			uc.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded"); // 设置内容类型
            DataOutputStream out = new DataOutputStream(
            		uc.getOutputStream()); // 获取输出流
            String param = "version="
                    + URLEncoder.encode(UrlConfig.version, "utf-8")
                    + "&channel="
                    + URLEncoder.encode("2", "utf-8"); //连接要提交的数据
            out.writeBytes(param);//将要传递的数据写入数据输出流
            out.flush();    //输出缓存
            out.close();    //关闭数据输出流
			uc.connect(); // 发出连接

			long ld = uc.getDate(); // 取得网站日期时间

			if(ld > 0){
				 date = new Date(ld);
			}else{
				 date = new Date(System.currentTimeMillis()) ;
			}
			//
//			Date date = new Date(ld); // 转换为标准时间对象
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			try {
				format.setLenient(false);
				format.parse(endDateStr);
			} catch (Exception e) {
				// e.printStackTrace();
				// 如果throw
				// java.text.ParseException或者NullPointerException，就说明格式不对
				is_date_type = false ;
			}
			if(is_date_type){
				endDate_Str = endDateStr;
				
			}else{
				Date is_date = new Date(Long.valueOf(endDateStr)) ;
				endDate_Str = format.format(is_date);
			}

			beginDateStr = format.format(date);

			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDate_Str);

			day = (endDate.getTime() - beginDate.getTime()) / (60 * 1000);
			if(day < 0){
				end_time = "已结束";
			}else if (day <= 60) {
				end_time = "1小时";
			} else if (day < 60 * 24) {
				end_time = (day / 60) + "小时";
			} else {
				end_time = (day / 60 / 24) + "天";
			}

			return end_time;

			// 分别取得时间中的小时，分钟和秒，并输出
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 取得资源对象
		return endDateStr;
	}

}
