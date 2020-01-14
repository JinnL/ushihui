package com.ekabao.oil.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class LogUtils {
	/** 日志输出级别NONE */
	private static final int LEVEL_NONE = 0;
	/** 日志输出级别V */
	private static final int LEVEL_VERBOSE = 1;
	/** 日志输出级别D */
	private static final int LEVEL_DEBUG = 2;
	/** 日志输出级别I */
	private static final int LEVEL_INFO = 3;
	/** 日志输出级别W */
	private static final int LEVEL_WARN = 4;
	/** 日志输出级别E */
	private static final int LEVEL_ERROR = 5;

	/** 日志输出时的TAG */
	private static String mTag = "LF";
	/** 是否允许输出log */
	// TODO: 2018/4/19  是否允许输出log
	private static int mDebuggable = 6;

	/** 用于记时的变量 */
	private static long mTimestamp = 0;
	/** 写文件的锁对象 */
	private static final Object mLogLock = new Object();


	public static boolean isTest(){

		if(mDebuggable==LEVEL_NONE){
			return true;
		}
		return false;

	}

	/** 以级别为 d 的形式输出LOG */
	public static void v(String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(generateTag(), msg);
		}
	}

	/** 以级别为 d 的形式输出LOG */
	public static void d(String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(generateTag(), msg);
		}
	}

	/** 以级别为 i 的形式输出LOG */
	public static void i(String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			//Log.i(generateTag(), msg);

			String tag = generateTag();
			int strLength = msg.length();
			int start = 0;
			int end = 2000;
			for (int i = 0; i < 100; i++) {
				if (strLength > end) {
					Log.i(tag + i, msg.substring(start, end));
					start = end;
					end = end + 2000;
				} else {
					Log.i(tag + i, msg.substring(start, strLength));
					break;
				}
			}
		}
	}

	/** 以级别为 w 的形式输出LOG */
	public static void w(String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(generateTag(), msg);
		}
	}

	/** 以级别为 w 的形式输出Throwable */
	public static void w(Throwable tr) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(generateTag(), "", tr);
		}
	}

	/** 以级别为 w 的形式输出LOG信息和Throwable */
	public static void w(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_WARN && null != msg) {
			Log.w(generateTag(), msg, tr);
		}
	}

	/** 以级别为 e 的形式输出LOG */
	public static void e(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			String tag = generateTag();
			//Log.v(tag, msg);
			//Log.e(tag, msg);

			int strLength = msg.length();
			int start = 0;
			int end = 2000;
			for (int i = 0; i < 100; i++) {
				if (strLength > end) {
					Log.e(tag + i, msg.substring(start, end));
					start = end;
					end = end + 2000;
				} else {
					Log.e(tag + i, msg.substring(start, strLength));
					break;
				}
			}

		}
	}

	/** 以级别为 e 的形式输出Throwable */
	public static void e(Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(generateTag(), "", tr);
		}
	}

	/** 以级别为 e 的形式输出LOG信息和Throwable */
	public static void e(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR && null != msg) {
			Log.e(generateTag(), msg, tr);
		}
	}

	/**
	 * 把Log存储到文件中
	 * 
	 * @param log
	 *            需要存储的日志
	 * @param path
	 *            存储路径
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	public static void log2File(String log, String path, boolean append) {
		synchronized (mLogLock) {
			//FileUtils.writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段起始点
	 * 
	 * @param msg
	 *            需要输出的msg
	 */
	public static void msgStartTime(String msg) {
		mTimestamp = System.currentTimeMillis();
		if (!TextUtils.isEmpty(msg)) {
			e("[Started：" + mTimestamp + "]" + msg);
		}
	}

	/** 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg */
	public static void elapsed(String msg) {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - mTimestamp;
		mTimestamp = currentTime;
		e("[Elapsed：" + elapsedTime + "]" + msg);
	}

	public static <T> void printList(List<T> list) {
		if (list == null || list.size() < 1) {
			return;
		}
		int size = list.size();
		i("---begin---");
		for (int i = 0; i < size; i++) {
			i(i + ":" + list.get(i).toString());
		}
		i("---end---");
	}

	public static <T> void printArray(T[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		int length = array.length;
		i("---begin---");
		for (int i = 0; i < length; i++) {
			i(i + ":" + array[i].toString());
		}
		i("---end---");
	}

	/**
	 * 得到tag（所在类.方法（L:行））
	 * @return
	 */
	private static String generateTag() {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
		String callerClazzName = stackTraceElement.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		String tag = "%s.%s(L:%d)";
		tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
		//给tag设置前缀
		tag = TextUtils.isEmpty(mTag) ? tag : mTag + ":" + tag;
		return tag;
	}
}
