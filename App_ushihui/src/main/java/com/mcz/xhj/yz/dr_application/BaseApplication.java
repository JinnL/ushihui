package com.mcz.xhj.yz.dr_application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.awen.photo.FrescoImageLoader;
import com.mcz.xhj.R;

public abstract class BaseApplication extends Application
{
	public static final String TAG = "Application";

	public static Context applicationContext;

	// 以键值对的形式存储用户名和密码
	public SharedPreferences sharereferences;

	@Override
	public void onCreate()
	{
		super.onCreate();
		applicationContext = getApplicationContext();

//		if (getDefaultUncaughtExceptionHandler() == null)
//		{
//			Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(applicationContext));
//		} else
//		{
//			Thread.setDefaultUncaughtExceptionHandler(getDefaultUncaughtExceptionHandler());
//		}

		// 初始化键值对存储
		sharereferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		//FrescoImageLoader.init(this);
		//下面是配置toolbar颜色和存储图片地址的
        FrescoImageLoader.init(this, R.color.toolbar_blue_color);
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");
	}
//	public abstract BaseExceptionHandler getDefaultUncaughtExceptionHandler();
}
