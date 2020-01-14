package com.mcz.xhj.yz.dr_app;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.TimeButton;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class LoginPswAct extends BaseActivity implements OnClickListener {
	@BindView(R.id.et_phonenumber)
	EditText et_phonenumber;
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.bt_login)
	Button bt_login;// 确定
	@BindView(R.id.tv_phone)
	TextView tv_phone; // 手机号码
	@BindView(R.id.code_et)
	EditText code_et; // 验证码
	@BindView(R.id.image_clean)
	ImageView image_clean; // 清除验证码
	@BindView(R.id.tv_getcode)
	TimeButton tv_getcode; // 重发验证码
	@BindView(R.id.code_voice)
	TextView code_voice; // 语音验证码
	@BindView(R.id.et_login_psw)
	EditText et_login_psw; // 登录密码
	@BindView(R.id.image_clean_psw)
	ImageView image_clean_psw; // 清除登录密码
	@BindView(R.id.image_clean_phone)
	ImageView image_clean_phone; // 清除登录密码
	@BindView(R.id.tv_yuying)
	TextView tv_yuying;
	
	@BindView(R.id.image_sure)
	CheckBox image_sure;
	@BindView(R.id.tv_agreement_user)
	TextView tv_agreement_user;
	
	@BindView(R.id.image_eye)
	CheckBox image_eye; // 显示登录密码
	@Nullable
	@BindView(R.id.check_tuijian)
	CheckBox check_tuijian; // 显示登录密码
	@BindView(R.id.et_login_referrer)
	EditText et_login_referrer; // 推荐人ID
	@BindView(R.id.lv_banner_login)
	LinearLayout lv_banner_login;
	@BindView(R.id.lv_code_pass)
	LinearLayout lv_code_pass;
	@BindView(R.id.ll_phone)
	LinearLayout ll_phone;
	@BindView(R.id.lv_login_referrer)
	LinearLayout lv_login_referrer;
	@BindView(R.id.ll_xieyi)
	LinearLayout ll_xieyi;
	@BindView(R.id.ll_login)
	LinearLayout ll_login;
	private SharedPreferences preferences;

	private Boolean isRun;
	private String mobilephone, uidOrMobile_Key, uidOrMobile_Value, uid;
	Pattern p = Pattern.compile("^[0-9]{4}$");
	Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
	Pattern pPhone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_loginpsw;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		tv_getcode.onCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onResume(this);
		MobclickAgent.onEvent(LoginPswAct.this, "100011");
	}
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		tv_getcode.onDestroy();
		super.onDestroy();
	}
	@Override
	protected void initParams() {
		tv_getcode.setTextAfter("发送").setTextBefore("发送验证码").setLenght(60 * 1000);
		preferences = LocalApplication.getInstance().sharereferences;
		uid = preferences.getString("uid", "");
		if (getIntent().getBooleanExtra("isForget", false)) {
			if ("".equals(uid)) {
				title_centertextview.setText("找回登录密码");
				bt_login.setText("确定");
			} else {
				title_centertextview.setText("重置登录密码");
				bt_login.setText("完成");
			}
			ll_xieyi.setVisibility(View.GONE);
			lv_banner_login.setVisibility(View.GONE);
			lv_code_pass.setVisibility(View.GONE);
			lv_login_referrer.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);
		} else {
			title_centertextview.setText("新手注册");
		}
		title_rightimageview.setVisibility(View.GONE);

		et_login_psw.setHintTextColor(0xffA0A0A0);
		code_et.setHintTextColor(0xffA0A0A0);
		mobilephone = getIntent().getStringExtra("phone_num");
		tv_phone.setText(stringCut.phoneCut(mobilephone));
		if ("".equals(uid)) {
			uidOrMobile_Key = "mobilephone";
			uidOrMobile_Value = mobilephone;
		} else {
			uidOrMobile_Key = "uid";
			uidOrMobile_Value = uid;
		}

		bt_login.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		image_clean.setOnClickListener(this);
		image_clean_psw.setOnClickListener(this);
		image_eye.setOnClickListener(this);
		image_clean_phone.setOnClickListener(this);
		tv_yuying.setOnClickListener(this);
		image_sure.setOnClickListener(this);
		tv_agreement_user.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		tv_getcode.setOnClickListener(this);
		
		
		SpannableString ss = new SpannableString("请输入手机号     (建议使用银行预留手机号)");
		ss.setSpan(new RelativeSizeSpan(1f), 0, 6, TypedValue.COMPLEX_UNIT_PX);
		ss.setSpan(new RelativeSizeSpan(0.9f), 7, 24,
				TypedValue.COMPLEX_UNIT_PX);
		et_phonenumber.setHint(ss);
		
		// 手机号间隔
		Watcher1 watcher1 = new Watcher1();
		et_phonenumber.addTextChangedListener(watcher1);

		// 密码监听
		Watcher watcher = new Watcher();
		et_login_psw.addTextChangedListener(watcher);
		Watcher_code watcher_code = new Watcher_code();
		code_et.addTextChangedListener(watcher_code);
		Watcher_phone watcher_phone = new Watcher_phone();
		et_login_referrer.addTextChangedListener(watcher_phone);
		// time();
		
//		if(LocalApplication.getInstance().getCount() != 60 ){
//			time() ;
//		}

	}

	int i = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			Matcher m1;
			Matcher mPsw;
			Matcher mPhone;
			Matcher mmPhone;
			if (getIntent().getBooleanExtra("isForget", false)) {

				m1 = p.matcher(code_et.getText().toString().trim());
				mPsw = psw.matcher(et_login_psw.getText().toString().trim());
				mPhone = pPhone.matcher(et_login_referrer.getText().toString()
						.trim());
				if (!m1.matches()) {
					ToastMaker.showShortToast("请输入正确的验证码");
				} else if (!stringCut.isPsw(et_login_psw.getText().toString()
						.trim())) {
					ToastMaker.showShortToast("请输入规则的密码");
				} else {
					if ("".equals(uid)) {
						updateLoginPassWord_Mobile();
					} else {
						updateLoginPassWord_Uid();
					}
				}

			} else {

				m1 = p.matcher(code_et.getText().toString().trim());
				mPsw = psw.matcher(et_login_psw.getText().toString().trim());
				mPhone = pPhone.matcher(et_login_referrer.getText().toString().trim());
				mmPhone = pPhone.matcher(stringCut.space_Cut(et_phonenumber.getText().toString()));
				if (stringCut.space_Cut(et_phonenumber.getText().toString().trim())
						.length() < 11) {
					ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
					return;
				}
				if (!mmPhone.matches()) {
					ToastMaker.showShortToast("请输入正确的手机号码");
				}
				else if (!m1.matches()) {
					ToastMaker.showShortToast("请输入正确的验证码");
				} 
				else if (!stringCut.isPsw(et_login_psw.getText().toString()
						.trim())) {
					ToastMaker.showShortToast("请输入规则的密码");
				} 
				else if ((et_login_referrer.getText().length() > 0)&& !mPhone.matches()&&check_tuijian.isChecked()) {
					ToastMaker.showShortToast(getResources().getString(R.string.phone_TuiJian_Wrong));
				} 
				else if (!image_sure.isChecked()) {
					ToastMaker.showShortToast(getResources().getString(
							R.string.xieyi_pro_zhuce));
					return;
				}
				else {
					MobclickAgent.onEvent(LoginPswAct.this, "100013");
					register_Reg();// 注册
				}
			}
			// startActivity(new Intent(LoginPswAct.this,
			// TransactionPswAct.class));
			break;
		case R.id.title_leftimageview:
			this.finish();
			break;
		case R.id.ll_login:
			startActivity(new Intent(LoginPswAct.this, NewLoginActivity.class));
			finish();
			break;
		case R.id.image_clean:
			code_et.setText("");
			break;
		case R.id.tv_agreement_user:
			startActivity(new Intent(LoginPswAct.this, WebViewActivity.class)
			.putExtra("URL", UrlConfig.ZHUCE)
			.putExtra("TITLE", "注册协议"));
			break;
		case R.id.image_clean_psw:
			et_login_psw.setText("");
			break;
		case R.id.image_clean_phone:
			et_login_referrer.setText("");
			break;
		case R.id.tv_getcode:
//			time();
			if (getIntent().getBooleanExtra("isForget", false)) {

				if ("".equals(uid)) {
					forgetPwdSmsCode_Mobile();
				} else {
					forgetPwdSmsCode_Uid();
				}
			} else {
				Matcher mmmPhone = pPhone.matcher(stringCut.space_Cut(et_phonenumber.getText().toString()));
				if(et_phonenumber.getText().toString().equalsIgnoreCase("")||!mmmPhone.matches()){
					tv_getcode.stopTime();
					ToastMaker.showLongToast("请填写正确的手机号码");
				}else{
					tv_getcode.startTime();
					MobclickAgent.onEvent(LoginPswAct.this, "100012");
					sendRegMsg();
				}
			}
			break;
		case R.id.tv_yuying:
//			time();
			if (stringCut.space_Cut(et_phonenumber.getText().toString().trim())
					.length() < 11) {
				ToastMaker.showShortToast(getResources().getString(
						R.string.phone_Wrong));
				return;
			}
			Matcher m11 = p.matcher(stringCut.space_Cut(et_phonenumber.getText()
					.toString().trim()));
			if (!m11.matches()) {
				ToastMaker.showShortToast(getResources().getString(
						R.string.phone_Wrong));
			} 
			if (getIntent().getBooleanExtra("isForget", false)) {

				if ("".equals(uid)) {
					if(LocalApplication.time==1){
						tv_getcode.startTime();
						sendYuyinMsgSmsCode_Mobile() ;
						LocalApplication.time=2;
					}else{
						ToastMaker.showShortToast("操作频繁请您稍后再试");
					}
				} else {
					if(LocalApplication.time==1){
						tv_getcode.startTime();
						sendYuyinMsgSmsCode_Uid() ;
						LocalApplication.time=2;
					}else{
						ToastMaker.showShortToast("操作频繁请您稍后再试");
					}
				}
			} else {
				if(LocalApplication.time==1){
					tv_getcode.startTime();
					sendYuyinRegMsg();
					LocalApplication.time=2;
				}else{
					ToastMaker.showShortToast("操作频繁请您稍后再试");
				}
				
			}
			break;
		case R.id.image_eye:
			if (image_eye.isChecked()) {
				et_login_psw
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				et_login_psw.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		default:
			break;
		}
	}

//	// 计时器
//	public void time() {
//				isRun = true;
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while (isRun) {
//					count_time--;
//					LocalApplication.getInstance().setCount(count_time) ;
//					handler.sendEmptyMessage(10);
//					try {
//						Thread.sleep(1000);
//					} catch (Exception e) {
//					}
//				}
//			}
//		}).start();
//	}
//
//	public void stopTimer() {
//		tv_getcode.setEnabled(true);
//		tv_getcode.setText("重发验证码");
//		tv_getcode.setTextColor(0xffff6e00);
//		// tv_getcode.setBackgroundColor(Color.parseColor("#ff6e00"));
//		isRun = false;
//	}
//
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 10:
//				if (count_time <= 0) {
//					stopTimer();
//					LocalApplication.getInstance().setCount(60) ;
//				} else {
//					tv_getcode.setEnabled(false);
//					tv_getcode.setTextColor(0xffcccccc);
//					// tv_getcode.setBackgroundColor(Color.parseColor("#b5b5b5"));
//					tv_getcode.setText("重发(" + count_time + ")秒");
//				}
//
//				break;
//			default:
//				break;
//			}
//		}
//	};
	class Watcher_phone implements TextWatcher {
		int beforeTextLength = 0;
		int onTextLength = 0;
		boolean isChanged = false;

		int location = 0;// 记录光标的位置
		private char[] tempChar;
		private StringBuffer buffer = new StringBuffer();
		int konggeNumberB = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			View v = new View(LoginPswAct.this) ;
			if (onTextLength > 0) {
				image_clean_phone.setVisibility(View.VISIBLE);
			} else {
				image_clean_phone.setVisibility(View.GONE);

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	}
	class Watcher_code implements TextWatcher {
		int beforeTextLength = 0;
		int onTextLength = 0;
		boolean isChanged = false;

		int location = 0;// 记录光标的位置
		private char[] tempChar;
		private StringBuffer buffer = new StringBuffer();
		int konggeNumberB = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			View v = new View(LoginPswAct.this) ;
			if (onTextLength > 0) {
					image_clean.setVisibility(View.VISIBLE);
			} else {
					image_clean.setVisibility(View.GONE);

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	}
	class Watcher1 implements TextWatcher {
		int beforeTextLength = 0;
		int onTextLength = 0;
		boolean isChanged = false;

		int location = 0;// 记录光标的位置
		private char[] tempChar;
		private StringBuffer buffer = new StringBuffer();
		int konggeNumberB = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			buffer.append(s.toString());
			if (onTextLength > 0) {
//				lin_phonenumber.setVisibility(View.VISIBLE);
				image_clean.setVisibility(View.VISIBLE);
			} else {
//				lin_phonenumber.setVisibility(View.GONE);
				image_clean.setVisibility(View.GONE);
			}
			if (onTextLength == beforeTextLength || isChanged) {
				isChanged = false;
				return;
			}
			isChanged = true;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			beforeTextLength = s.length();
			if (buffer.length() > 0) {
				buffer.delete(0, buffer.length());
			}
			konggeNumberB = 0;
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == ' ') {
					konggeNumberB++;
				}
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (isChanged) {
				location = et_phonenumber.getSelectionEnd();
				int index = 0;
				while (index < buffer.length()) {
					if (buffer.charAt(index) == ' ') {
						buffer.deleteCharAt(index);
					} else {
						index++;
					}
				}

				index = 0;
				int konggeNumberC = 0;
				while (index < buffer.length()) {
					if ((index == 3 || index == 8)) {
						buffer.insert(index, ' ');
						konggeNumberC++;
					}
					index++;
				}
				if (konggeNumberC > konggeNumberB) {
					location += (konggeNumberC - konggeNumberB);
				}

				tempChar = new char[buffer.length()];
				buffer.getChars(0, buffer.length(), tempChar, 0);
				String str = buffer.toString();
				if (location > str.length()) {
					location = str.length();
				} else if (location < 0) {
					location = 0;
				}

				et_phonenumber.setText(str);
//				tv_phonenumber.setText(str);
				Editable etable = et_phonenumber.getText();
				Selection.setSelection(etable, location);
				isChanged = false;
			}
		}
	}
	class Watcher implements TextWatcher {
		int beforeTextLength = 0;
		int onTextLength = 0;
		boolean isChanged = false;

		int location = 0;// 记录光标的位置
		private char[] tempChar;
		private StringBuffer buffer = new StringBuffer();
		int konggeNumberB = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			View v = new View(LoginPswAct.this) ;
			if (onTextLength > 0) {
					image_clean_psw.setVisibility(View.VISIBLE);
			} else {
					image_clean_psw.setVisibility(View.GONE);

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	}

	private void updateLoginPassWord_Uid() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.UPDATELOGINPASSWORD)
				.addParams(uidOrMobile_Key, uidOrMobile_Value)
				.addParams("pwd", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
				.addParams("smsCode", code_et.getText().toString().trim())
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("登录密码设置成功");
							finish();
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("验证码错误");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("密码为空");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("登录密码不合法");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							finish();
							new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	private void updateLoginPassWord_Mobile() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.UPDATELOGINPASSWORD)
				.addParams(uidOrMobile_Key, uidOrMobile_Value)
				.addParams("pwd",SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
				.addParams("smsCode", code_et.getText().toString().trim())
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("登录密码设置成功");
							finish();
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("验证码错误");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("密码为空");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("登录密码不合法");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	private void forgetPwdSmsCode_Uid() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.FORGETPWDSMSCODE)
				.addParams(uidOrMobile_Key, uidOrMobile_Value)
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("验证码已发送");
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信发送失败");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast(" 请检查网络");
					}
				});
	}
	//获取语音验证码
		private void sendYuyinMsgSmsCode_Uid() {
			OkHttpUtils
			.post()
			.url(UrlConfig.FORGETPWDSMSCODE)
			.addParams(uidOrMobile_Key, uidOrMobile_Value)
			.addParams("type", "2")
			.addParams("version", UrlConfig.version)
			.addParams("channel", "2").build()
			.execute(new StringCallback() {
				
				@Override
				public void onResponse(String response) {
					// TODO Auto-generated method stub
					JSONObject obj = JSON.parseObject(response);
					if (obj.getBoolean(("success"))) {
						ToastMaker.showShortToast("获取成功请留意您的电话");
					} else {
						if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信发送失败");
						}else if ("8888".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("操作频繁请您稍后再试");
						} 
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							finish();
							new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
							
						}
						else{
							ToastMaker.showShortToast("系统异常");	
						}
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					ToastMaker.showShortToast("请检查网络!");
					
				}
			});
		}
	private void forgetPwdSmsCode_Mobile() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.FORGETPWDSMSCODE)
				.addParam(uidOrMobile_Key, uidOrMobile_Value)
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2").build().execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("验证码已发送");
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信发送失败");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast(" 请检查网络");
					}
				});
	}
	//获取语音验证码
	private void sendYuyinMsgSmsCode_Mobile() {
		OkHttpUtils
		.post()
		.url(UrlConfig.FORGETPWDSMSCODE)
		.addParam(uidOrMobile_Key, uidOrMobile_Value)
		.addParams("type", "2")
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2").build()
		.execute(new StringCallback() {
			
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				JSONObject obj = JSON.parseObject(response);
				if (obj.getBoolean(("success"))) {
					ToastMaker.showShortToast("获取成功请留意您的电话");
				} else {
					if ("1001".equals(obj.getString("errorCode"))) {
						ToastMaker.showShortToast("短信发送失败");
					}else if ("8888".equals(obj.getString("errorCode"))) {
						ToastMaker.showShortToast("操作频繁请您稍后再试");
					} 
					else if ("9999".equals(obj.getString("errorCode"))) {
						ToastMaker.showShortToast("系统异常");
					}
					else if ("9998".equals(obj.getString("errorCode"))) {
						finish();
						new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
						
					}
					else{
						ToastMaker.showShortToast("系统异常");	
					}
				}
			}
			
			@Override
			public void onError(Call call, Exception e) {
				ToastMaker.showShortToast("请检查网络!");
				
			}
		});
	}
	private void register_Reg() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.REGISTER_REG)
				.addParam("mobilephone", stringCut.space_Cut(et_phonenumber.getText().toString()))
				.addParam("passWord", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
				.addParam("smsCode", code_et.getText().toString().trim())
				.addParam("recommPhone",et_login_referrer.getText().toString().trim())
				.addParam("toFrom", LocalApplication.getInstance().channelName)
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2").build().execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							LocalApplication.getInstance().getMainActivity().onResume();
//							ToastMaker.showShortToast("注册成功");
							JSONObject jsonObj = obj.getJSONObject("map");
							String token = jsonObj.getString("token");
							JSONObject objmem = jsonObj.getJSONObject("member");
//							String emailVerify = objmem.getString("emailVerify");
//							String lastLoginIp = objmem.getString("lastLoginIp");
//							String lastLoginTime = objmem.getString("lastLoginTime");
//							String loginVerify = objmem.getString("loginVerify");
//							String mobileVerify = objmem.getString("mobileVerify");
							String mobilephone = objmem.getString("mobilephone");
							String realVerify = objmem.getString("realVerify");
							String uid = objmem.getString("uid");
							String recommCodes = objmem.getString("recommCodes");
//							String name = objmem.getString("realName") ;
//							String regDate = objmem.getString("regDate");
//							String regFrom = objmem.getString("regFrom");
//							String status = objmem.getString("status");
//							String tpwdSetting = objmem.getString("tpwdSetting");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("login", true);
							editor.putString("uid", uid);
							editor.putString("recommCodes", recommCodes);
							editor.putString("realVerify", realVerify);
							editor.putString("phone", mobilephone);
//							editor.putString("name",name ) ;
							editor.putString("token", token);
							editor.commit();
							pushRegisterId();
							setResult(2, new Intent());
							LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
							LocalApplication.getInstance().getMainActivity().isHome = true;
							LocalApplication.getInstance().getMainActivity().isLoginPsw = true;
							ToastMaker.showShortToast("您已注册成功");
							MobclickAgent.onEvent(LoginPswAct.this, "100025");
							finish();
							startActivity(new Intent(LoginPswAct.this, FourPartAct.class).putExtra("flag", "zhucequdao"));
//							showAlertDialog("注册成功", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, false, true, "");
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信验证码为空");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信验证码错误");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("手机号为空");
						} else if ("1005".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("密码格式错误");
						} else if ("1006".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("未勾选注册协议");
						} else if ("1007".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("手机号已注册");
						} else if ("1008".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("推荐人不存在");
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	private void sendRegMsg() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.SENDREGMSG)
				.addParams("mobilephone", stringCut.space_Cut(et_phonenumber.getText().toString()))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("验证码已发送");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("每个手机号当天只能发送5条");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信发送失败");
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}
	//获取语音验证码
		private void sendYuyinRegMsg() {
			OkHttpUtils
			.post()
			.url(UrlConfig.SENDREGMSG)
			.addParams("mobilephone", stringCut.space_Cut(mobilephone))
			.addParams("type", "2")
			.addParams("version", UrlConfig.version)
			.addParams("channel", "2").build()
			.execute(new StringCallback() {
				
				@Override
				public void onResponse(String response) {
					// TODO Auto-generated method stub
					JSONObject obj = JSON.parseObject(response);
					if (obj.getBoolean(("success"))) {
						ToastMaker.showShortToast("获取成功请留意您的电话");
					} else {
						if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("每个手机号当天只能发送5条");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("短信发送失败");
						}else if ("8888".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("操作频繁请您稍后再试");
						} 
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							finish();
							new show_Dialog_IsLogin(LoginPswAct.this).show_Is_Login() ;
							
						}
						else{
							ToastMaker.showShortToast("系统异常");	
						}
					}
				}
				
				@Override
				public void onError(Call call, Exception e) {
					ToastMaker.showShortToast("请检查网络!");
					
				}
			});
		}
		@Override
		public void onButtonClicked(Dialog dialog, int position, Object tag) {
			// TODO Auto-generated method stub
			if (position == 0) {
				finish() ;
				ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改") ;
			}
			if (position == 1) {
				startActivity(new Intent(LoginPswAct.this, GestureEditActivity.class)) ;
				finish() ;
			}
		}
}
