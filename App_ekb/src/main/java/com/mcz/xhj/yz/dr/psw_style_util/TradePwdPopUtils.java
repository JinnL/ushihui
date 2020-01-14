package com.mcz.xhj.yz.dr.psw_style_util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr.psw_style_util.SecurityPasswordEditText.SecurityEditCompleListener;

/**
 * 封装pop类，创建回调
 * 
 * @author AHF
 * 
 */
public class TradePwdPopUtils {
	public PopupWindow popWindow = null;
	private CallBackTradePwd callBackTradePwd;

	public TradePwdPopUtils() {
		super();
	}

	public CallBackTradePwd getCallBackTradePwd() {
		return callBackTradePwd;
	}

	public void setCallBackTradePwd(CallBackTradePwd callBackTradePwd) {
		this.callBackTradePwd = callBackTradePwd;
	}

	public interface CallBackTradePwd {
		public void callBaceTradePwd(String pwd);
	}

	private Context context;
	SecurityPasswordEditText myEdit;
	public TextView forget_pwd;
	public ImageView iv_key_close;
	public LinearLayout ll_invest_money;
	public TextView tv_key_money;
	public LinearLayout ll_pwd_title;
	public LinearLayout ll_pwd;
	public TextView tv_pwd1;
	public TextView tv_pwd2;
	public TextView tv_tips;
	public TextView tv_title;
	public TextView tv_cashtip;
	public TextView tv_key_title;
	public View tv_pwd_line;

	public void showPopWindow(Context context, Activity ac, LinearLayout lin) {
		popWindow = null;
		this.context = context;
		if (popWindow == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.trade_key_layout, null);
			iv_key_close = (ImageView) view.findViewById(R.id.iv_key_close) ;
			myEdit = (SecurityPasswordEditText) view.findViewById(R.id.my_edit);
			forget_pwd = (TextView) view.findViewById(R.id.forget_pwd);
			ll_invest_money = (LinearLayout) view.findViewById(R.id.ll_invest_money);
			tv_key_money = (TextView) view.findViewById(R.id.tv_key_money);
			ll_pwd_title = (LinearLayout) view.findViewById(R.id.ll_pwd_title);
			ll_pwd = (LinearLayout) view.findViewById(R.id.ll_pwd);
			tv_pwd1 = (TextView) view.findViewById(R.id.tv_pwd1);
			tv_pwd2 = (TextView) view.findViewById(R.id.tv_pwd2);
			tv_tips = (TextView) view.findViewById(R.id.tv_tips);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_cashtip = (TextView) view.findViewById(R.id.tv_cashtip);
			tv_key_title = (TextView) view.findViewById(R.id.tv_key_title);
			tv_pwd_line = view.findViewById(R.id.tv_pwd_line);

//			forget_pwd.setTextColor(Color.parseColor("#ECB04C")) ;
			myEdit.setSecurityEditCompleListener(new SecurityEditCompleListener() {

				@Override
				public void onNumCompleted(String num) {
					if (callBackTradePwd != null) {
						myEdit.clearSecurityEdit() ;
						callBackTradePwd.callBaceTradePwd(num);
					}
				}
			});
			
			ac.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			Method setShowSoftInputOnFocus = null;
			try {
				setShowSoftInputOnFocus = myEdit.getClass().getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(myEdit, false);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			new KeyboardUtil(view, context, myEdit).showKeyboard();
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			popWindow.setFocusable(true);
			popWindow.setOutsideTouchable(true);
			popWindow.setBackgroundDrawable(new BitmapDrawable());
			popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			popWindow.showAtLocation(lin, Gravity.BOTTOM, 0, 0);
		}
	}

	protected void startActivity(Intent intent) {
		// TODO Auto-generated method stub

	}
}
