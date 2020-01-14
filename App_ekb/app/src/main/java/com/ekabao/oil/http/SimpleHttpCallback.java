package com.ekabao.oil.http;

import android.text.TextUtils;

import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * 对OkhttpCallback进行结果处理
 * Created by lj on 2016/11/23.
 */
public abstract class SimpleHttpCallback implements OkHttpEngine.OkHttpCallback {

    private static final String TAG = "SimpleHttpCallback";
    /*0为操作成功；
    101位验签失败
    500为服务器异常
    * */
    public int SUCCESS = 0;
    public int ERROR = 101;

    int code = 0;

    @Override
    public void onSuccess(String result) {

        LogUtils.e("SimpleHttpCallback"+result);

        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObject object = new JSONObject(result);

                String msg = object.getString("success");

                if (msg.equals("true")) {
                    String data = null;
                    try {
                        data = object.getString("map");
                        //传递data数据
                        onLogicSuccess(data);
                    } catch (JSONException e) {
                        //当返回的没有data时,直接传message
                        onLogicSuccess(result);
                    }

                   /* int page = 1;
                    try {
                        page = object.getInt("pages");
                        //传递data数据
                        onGetPages(page, result);
                    } catch (JSONException e) {
                        //当返回的没有data时,直接传message
                        onGetPages(1, result);
                    }
                    */

                } else {


                    try {
                        code = object.getInt("code");
                    } catch (JSONException e) {

                        onLogicError(code, result);
                    }
                    //系统错误 9999
                    //您的手机号已被锁定，请联系客服 1111
                    //"9998","999999": 您的登录已过期或已在其他设备登录，请重新登录
                    if (code == 9999) { //  系统错误
                        //ToastMaker.showShortToast("系统错误");
                        onLogicError(code, "系统错误");
                    } else if (code == 1111) { //  您的手机号已被锁定，请联系客服
                        //ToastMaker.showShortToast("您的手机号已被锁定，请联系客服");
                        onLogicError(code, "您的手机号已被锁定，请联系客服");
                    } else if (code == 9998||code == 999999) {  //您的登录已过期或已在其他设备登录，请重新登录
                        //ToastMaker.showShortToast("您的登录已过期或已在其他设备登录，请重新登录");
                        onLogicError(code, "您的登录已过期或已在其他设备登录，请重新登录");
                    }else {
                        onLogicError(code, result);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            onLogicError(code, result);
        }
    }

    @Override
    public void onFail(IOException e) {
        ToastMaker.showShortToast("网络异常");
        LogUtils.e("网络异常"+e.toString());
        onError(e);
    }

    /**
     * 请求成功并且操作成功调用的方法
     *
     * @param data
     */
    public abstract void onLogicSuccess(String data);

    /**
     * 请求成功并且操作失败调用的方法
     *
     * @param code
     * @param msg
     */
    public abstract void onLogicError(int code, String msg);

    /**
     * 请求失败
     * */
    public abstract void onError(IOException e);
   // public abstract void onGetPages(int Pages, String data);

/**
 *
   Type type = new TypeToken<List<Address>>() {
 }.getType();
 ArrayList<Address> List = (ArrayList<Address>) GsonUtil.parseJsonToList(data, type);


 * */
}
