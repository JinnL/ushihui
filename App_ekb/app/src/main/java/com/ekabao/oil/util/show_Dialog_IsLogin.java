package com.ekabao.oil.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.view.DialogMaker;


public class show_Dialog_IsLogin extends Dialog implements DialogMaker.DialogCallBack {
	private Context context ;
	protected Dialog dialog;

	public show_Dialog_IsLogin(Context context) {
		super(context);
		this.context = context ;
		show_Is_Login() ;
	}
	
	public show_Dialog_IsLogin(Context context , String isFinish) {
		super(context);
		this.context = context ;
		show_Is_Login() ;
	}

	public void show_Is_Login() {
		showAlertDialog("提示", "您的登录已过期或已在其他设备登录，请重新登录!",
				new String[]{"重新登录"},false, true, "");
	}



	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		if(position==0){
			context.startActivity(new Intent(context ,LoginActivity.class)) ;
			exit_dr() ;
		}
	}
	
	private void exit_dr() {
		SharedPreferences.Editor editor = LocalApplication.getInstance().sharereferences.edit();
		boolean isOpenUpush =  LocalApplication.getInstance().sharereferences.getBoolean("isOpenUpush",false);
		String deviceToken = LocalApplication.getInstance().sharereferences.getString("deviceToken","");
		editor.clear() ;
		editor.putBoolean("login",false) ;
		editor.putBoolean("FirstLog",false) ;
		editor.putBoolean("isOpenUpush",isOpenUpush) ;
		editor.putString("deviceToken",deviceToken) ;
//		editor.putBoolean("autoInvest", false) ;//是否登录后弹红包
		editor.commit() ;
//		LocalApplication.getInstance().getMainActivity().isNewFinish = false;
		// TODO: 2018/7/24 这个地方不修改会导致闪退  "com.akzj.oil.ui.activity.MainActivity"
		if(!context.getResources().getString(R.string.MainActivityname).equals(context.getClass().getName())){
			LogUtils.e("exit_dr 闪退");
			((Activity) context).finish() ;
		}
	}

	@Override
	public void onCancelDialog(Dialog dialog, Object tag) {

	}
	/**
	 * 弹出对话框
	 * 
	 * @author blue
	 */
	public Dialog showAlertDialog(String title, String msg, String[] btns, boolean isCanCancelabel, final boolean isDismissAfterClickBtn, Object tag)
	{
		if (null == dialog || !dialog.isShowing())
		{
			dialog = DialogMaker.showCommonAlertDialog(context, title, msg, btns, this, isCanCancelabel, isDismissAfterClickBtn, tag);
		}
		return dialog;
	}
	
	
}
