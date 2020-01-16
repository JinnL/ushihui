package com.mcz.xhj.yz.dr_exception;

import java.io.IOException;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * 本地异常处理类
 * 
 * @author blue
 *
 */
public class LocalFileHandler extends BaseExceptionHandler
{
	private Context context;

	public LocalFileHandler(Context context)
	{
		this.context = context;
	}

	/**
	 * 
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 * @throws IOException
	 * 
	 */
	@Override
	public boolean handleException(Throwable ex)
	{
		if (ex == null)
			return false;

		new Thread()
		{
			public void run()
			{
				Looper.prepare();
				Toast.makeText(context, "很抱歉,程序出现异常,正在从错误中恢复", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		return true;
	}

}
