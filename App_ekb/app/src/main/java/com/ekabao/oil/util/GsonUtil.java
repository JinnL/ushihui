package com.ekabao.oil.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

/**
 * 解析Json的封装
 * @author Administrator
 *
 */
public class GsonUtil {


	/**
	 * 把一个json字符串变成对象
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("解析异常"+e);
		}
		return t;
	}


	/**
	 * 把json字符串变成集合
	 * params: new TypeToken<List<yourbean>>(){}.getType(),
	 * 
	 * @param json
	 * @param type  new TypeToken<List<yourbean>>(){}.getType()
	 * @return
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		Gson gson = new Gson();
		List<?> list = null;
		try {
			 list = gson.fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("parseJsonToList"+e.toString());
		}


		return list;

		/*List<?> list = gson.fromJson(json, type);
		return list;*/
	}

	public static List<?> parseJsonToList(InputStream is, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(new InputStreamReader(is), type);
		return list;
	}

	public static String toJson(Object obj){
		if(obj==null) {
            return "";
        }
		return new Gson().toJson(obj);
	}

}
