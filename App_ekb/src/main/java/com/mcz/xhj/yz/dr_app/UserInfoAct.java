package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BankNameAdd_Pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class UserInfoAct extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview ;
	@BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.title_rightimageview)
   	ImageView title_rightimageview ;
	@BindView(R.id.rl_gesture)
	RelativeLayout rl_gesture ;
	@BindView(R.id.tv_phonenum)
    TextView tv_phonenum;
	@BindView(R.id.tv_name)
    TextView tv_name;
	@BindView(R.id.tv_num)
    TextView tv_num;
	@BindView(R.id.tv_card)
    TextView tv_card;
	@BindView(R.id.tv_card_img)
	ImageView tv_card_img ;
	@BindView(R.id.tv_num_img)
	ImageView tv_num_img ;
	@BindView(R.id.tv_name_img)
	ImageView tv_name_img ;
	@BindView(R.id.rl_login_next)
	RelativeLayout rl_login_next ;
	@BindView(R.id.rl_forget_psw)
	RelativeLayout rl_forget_psw ;
	@BindView(R.id.bt_ok)
	Button bt_ok ;
	@BindView(R.id.tv_getcode)
	@Nullable
	TextView tv_getcode ;
	@BindView(R.id.ll_bank)
	LinearLayout ll_bank ;
	@BindView(R.id.iv_bank)
	ImageView iv_bank ;
	@BindView(R.id.tv_Setting)
	TextView tv_Setting ;
	@BindView(R.id.ll_name)
	LinearLayout ll_name ;
	@BindView(R.id.ll_num)
	LinearLayout ll_num ;
	@BindView(R.id.ll_card)
	LinearLayout ll_card ;
	@BindView(R.id.rl_Gesture_psw)
	RelativeLayout rl_Gesture_psw ;
	@BindView(R.id.check_Gesture)
	CheckBox check_Gesture ;
	 
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;;
	private SharedPreferences.Editor editor  ;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		
		return R.layout.act_userinfo;
	}

	@Override
	protected void initParams() {
		 editor = preferences.edit();
		// TODO Auto-generated method stub
		title_centertextview.setText("安全中心") ;
		title_leftimageview.setOnClickListener(this) ;
		rl_gesture.setOnClickListener(this) ;
		rl_login_next.setOnClickListener(this) ;
		rl_forget_psw.setOnClickListener(this) ;
		tv_name.setOnClickListener(this) ;
		tv_num.setOnClickListener(this) ;
		tv_card.setOnClickListener(this) ;
		bt_ok.setOnClickListener(this) ;
		iv_bank.setOnClickListener(this) ;
		ll_name.setOnClickListener(this) ;
		ll_num.setOnClickListener(this) ;
		ll_card.setOnClickListener(this) ;
		rl_Gesture_psw.setOnClickListener(this) ;
		check_Gesture.setOnClickListener(this) ;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		memberSetting();
		if(LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
			check_Gesture.setChecked(true) ;
		}else{
			check_Gesture.setChecked(false) ;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.check_Gesture:
			if (check_Gesture.isChecked()) {
				if(!LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
					startActivity(new Intent(UserInfoAct.this, GestureEditActivity.class)) ;
				}
			}else{
//				ToastMaker.showShortToast("可以在我的信息-手势密码 中进行修改") ;
				editor.putBoolean("loginshoushi", false);
				editor.putString("gesturePsd", "");
				editor.commit();
			}
			break ;
//		case R.id.rl_Gesture_psw:
//			startActivity(new Intent(UserInfoAct.this , GestureEditActivity.class)) ;
//			break;
		case R.id.rl_login_next:
			startActivity(new Intent(UserInfoAct.this,
					LoginPswAct.class).putExtra("uid", preferences.getString("uid", "")).putExtra("phone_num", mobilephone).putExtra("isForget", true));
			break;
		case R.id.rl_forget_psw:
			startActivity(new Intent(UserInfoAct.this,
					TransactionPswAct.class).putExtra("uid", preferences.getString("uid", "")).putExtra("phone_num", mobilephone).putExtra("isForget", true));
			break;
		case R.id.bt_ok:
			showAlertDialog("退出", "是否确认退出？", new String[]{"取消","确定"}, true, true, "");
			break;
		case R.id.tv_name:
		case R.id.ll_name:
			if(realVerify.equalsIgnoreCase("0")){
				startActivity(new Intent(UserInfoAct.this , FourPartAct.class)) ;
				MobclickAgent.onEvent(UserInfoAct.this, "100001");
			}
			break;
		case R.id.tv_num:
		case R.id.ll_num:
			if(realVerify.equalsIgnoreCase("0")){
				startActivity(new Intent(UserInfoAct.this , FourPartAct.class)) ;
				MobclickAgent.onEvent(UserInfoAct.this, "100001");
			}
			break;
		case R.id.tv_card:
		case R.id.ll_card:
			if(realVerify.equalsIgnoreCase("0")){
				startActivity(new Intent(UserInfoAct.this , FourPartAct.class)) ;
				MobclickAgent.onEvent(UserInfoAct.this, "100001");
			}
			break;
		case R.id.iv_bank:
			startActivity(new Intent(UserInfoAct.this , BankDetailAct.class)) ;
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		super.onButtonClicked(dialog, position, tag);
		if(position==1){
			exit_dr() ;
		}
		
	}
	
	private void exit_dr() {
		boolean isOpenUpush = preferences.getBoolean("isOpenUpush",false);
		String deviceToken = preferences.getString("deviceToken","");
		editor.clear() ;
		editor.putBoolean("login",false) ;
		editor.putBoolean("FirstLog",false) ;
		editor.putBoolean("isOpenUpush",isOpenUpush) ;
		editor.putString("deviceToken",deviceToken) ;
		editor.commit() ;
		finish() ;
		LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
		LocalApplication.getInstance().getMainActivity().isHome = true;
		LocalApplication.getInstance().getMainActivity().isExit = true;
//		LocalApplication.getInstance().getMainActivity().isNewFinish = false;
	}
	String mobilephone ;
	String realVerify="1";
	private void memberSetting() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.MEMBERSETTING)
				.addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						dismissDialog();
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map") ;
							 mobilephone = map.getString("mobilephone") ;
							realVerify = map.getString("realVerify") ;
							String tpwdFlag = map.getString("tpwdFlag") ;
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("realVerify",realVerify ) ;
							editor.putString("tpwdFlag",tpwdFlag ) ;
							editor.commit() ;
							if("1".equals(tpwdFlag)){
								tv_Setting.setText("重置交易密码") ;
							}else{
								tv_Setting.setText("设置交易密码") ;
							}
							if("1".equals(realVerify)){
								String realName = map.getString("realName");
								String idCards = map.getString("idCards");
								String bankid = map.getString("bankId");
								tv_name.setText(realName) ;
								tv_num.setText(idCards) ;
								Integer pic = new BankNameAdd_Pic(bankid).bank_Pic();
								iv_bank.setImageResource(pic);
								iv_bank.setVisibility(View.VISIBLE);
								tv_name_img.setVisibility(View.GONE) ;
								tv_num_img.setVisibility(View.GONE) ;
								tv_card.setVisibility(View.GONE) ;
							}
							
							tv_phonenum.setText(mobilephone) ;
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							finish() ;
//							new show_Dialog_IsLogin(UserInfoAct.this).show_Is_Login() ;
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						dismissDialog();
						// TODO Auto-generated method stub
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}
	
}
