package com.ekabao.oil.util;

import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.ekabao.oil.global.LocalApplication;



/**
 * Created by lj on 2016/11/22.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(String msg){
        if(toast==null){
            toast = Toast.makeText(LocalApplication.context,msg,Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0); //在屏幕中间
        toast.setText(msg);
        toast.show();
    }


    /**
     * 居中显示的
     * */
    public static void showToastCenter(String msg){
        if(toast==null){
            toast = Toast.makeText(LocalApplication.context,msg,Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 在主线程显示吐司
     * @param msg
     */
    public static void showToastOnUIThread(final String msg){
        Handler handler = new Handler(LocalApplication.context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }
}
