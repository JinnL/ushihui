package com.mcz.xhj.yz.dr_app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CircleTextProgressbar;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class LoginQQPswAct extends BaseActivity implements OnClickListener {
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
	@BindView(R.id.et_login_psw)
	EditText et_login_psw; // 登录密码
	@BindView(R.id.image_eye)
	CheckBox image_eye; // 显示登录密码
	@BindView(R.id.tv_red_progress_text)
	CircleTextProgressbar mTvProgressBar;
	@BindView(R.id.tv_getsmg)
	TextView tv_getsmg;
	@BindView(R.id.check_tuijian)
	CheckBox check_tuijian;
	@BindView(R.id.et_login_referrer)
	EditText et_login_referrer; // 推荐人ID


	private SharedPreferences preferences;

	private String mobilephone,uid;
	private String time = "";
	Pattern p = Pattern.compile("^[0-9]{4}$");
	Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
	Pattern pPhone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
	private boolean isSend = false;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_qq_loginpsw;
	}

	private CircleTextProgressbar.OnCountdownProgressListener progressListener = new CircleTextProgressbar.OnCountdownProgressListener() {
		@Override
		public void onProgress(int what, int progress) {
			if (what == 1) {
				mTvProgressBar.setText(progress + "");
				if(progress == 0){
					tv_getsmg.setVisibility(View.VISIBLE);
					mTvProgressBar.setVisibility(View.GONE);
					isSend = true;
					mTvProgressBar.setText("重新获取");
					mTvProgressBar.setTextColor(Color.parseColor("#ec5c59"));
				}
			} else if (what == 2) {
				mTvProgressBar.setText(progress + "");
			}
			// 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		 if (requestCode == 3 && resultCode == 3) {
			 setResult(3);
			 finish();
		}
		else if (requestCode == 3 && resultCode == 2) {
			 setResult(2);
			 finish();
		}

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onEvent(LoginQQPswAct.this, UrlConfig.point+3+"");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void initParams() {
		preferences = LocalApplication.getInstance().sharereferences;
		uid = preferences.getString("uid", "");
		title_centertextview.setText("注册");
//		title_rightimageview.setVisibility(View.GONE);

		Intent intent = getIntent();
		if(intent.getStringExtra("phone")!=null&&!intent.getStringExtra("phone").equalsIgnoreCase("")){
			mobilephone = intent.getStringExtra("phone");
		}
		if(intent.getStringExtra("time")!=null&&!intent.getStringExtra("time").equalsIgnoreCase("")){
			time = intent.getStringExtra("time");
		}
		Uri uri = intent.getData();
		if (uri != null) {
			if(uri.getQueryParameter("phone")!=null&&!uri.getQueryParameter("phone").equalsIgnoreCase("")){
					mobilephone = uri.getQueryParameter("phone");
			}
		}
		et_login_psw.setHintTextColor(0xffA0A0A0);
		code_et.setHintTextColor(0xffA0A0A0);
//		mobilephone = getIntent().getStringExtra("phone_num");
		tv_phone.setText(stringCut.phoneCut(mobilephone.replaceAll(" ","")));
//		tv_getcode.startTime();
		sendRegMsgS();
//		tv_getcode.setTextAfter("发送").setTextBefore("发送验证码").setLenght(60 * 1000);

		bt_login.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		image_eye.setOnClickListener(this);
		tv_getsmg.setOnClickListener(this);


		// 密码监听
		Watcher watcher = new Watcher();
		et_login_psw.addTextChangedListener(watcher);
		Watcher_code watcher_code = new Watcher_code();
		code_et.addTextChangedListener(watcher_code);

		//短信验证码倒计时
		mTvProgressBar.setCountdownProgressListener(1, progressListener);
		mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
		mTvProgressBar.setProgressColor(Color.parseColor("#ec5c59"));
		mTvProgressBar.setTimeMillis(60000);
		mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
		mTvProgressBar.reStart();
		tv_getsmg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isSend){
					tv_getsmg.setVisibility(View.GONE);
					mTvProgressBar.setVisibility(View.VISIBLE);
					mTvProgressBar.reStart();
					sendRegMsgS();
					isSend = false;
				}
			}
		});

		check_tuijian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					et_login_referrer.setFocusable(true);
					et_login_referrer.setFocusableInTouchMode(true);
				}else{
					et_login_referrer.setFocusable(false);
					et_login_referrer.setFocusableInTouchMode(false);
				}
			}
		});
	}

	int i = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			MobclickAgent.onEvent(LoginQQPswAct.this, UrlConfig.point+5+"");
			Matcher m1;
			Matcher mPsw;
			Matcher mPhone;
			Matcher mmPhone;
			mPhone = pPhone.matcher(et_login_referrer.getText().toString());
			if (getIntent().getBooleanExtra("isForget", false)) {

				m1 = p.matcher(code_et.getText().toString().trim());
				mPsw = psw.matcher(et_login_psw.getText().toString().trim());
				if (!m1.matches()) {
					ToastMaker.showShortToast("请输入正确的验证码");
				} else if (!stringCut.isPsw(et_login_psw.getText().toString()
						.trim())) {
					ToastMaker.showShortToast("请输入规则的密码");
				}

			} else {
				m1 = p.matcher(code_et.getText().toString().trim());
				mPsw = psw.matcher(et_login_psw.getText().toString().trim());
				if (!m1.matches()) {
					ToastMaker.showShortToast("请输入正确的验证码");
				} 
				else if (!stringCut.isPsw(et_login_psw.getText().toString()
						.trim())) {
					ToastMaker.showShortToast("请输入规则的密码");
				}
				else if (!mPhone.matches()&&check_tuijian.isChecked()) {
					ToastMaker.showShortToast(getResources().getString(R.string.phone_TuiJian_Wrong));
				}
				else {
					register_Reg();// 注册
				}
			}
			break;
		case R.id.title_leftimageview:
			this.finish();
			break;
//		case R.id.tv_getcode:
////			time();
//			sendRegMsgS();
//			break;
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

	class Watcher_code implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>0&&!et_login_psw.getText().toString().equalsIgnoreCase("")){
				bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
			}else{
				bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
			}
		}
	}

	class Watcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(s.length()>0&&!code_et.getText().toString().equalsIgnoreCase("")){
				bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
			}else{
				bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
			}
		}
	}

	private void register_Reg() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.REGISTER_REG)
				.addParam("mobilephone", mobilephone.replaceAll(" ",""))
				.addParam("passWord", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
				.addParam("smsCode", code_et.getText().toString().trim())
				.addParam("recommPhone",et_login_referrer.getText().toString().trim())
				.addParam("toFrom", LocalApplication.getInstance().channelName)
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2")
				.build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
//							LocalApplication.getInstance().getMainActivity().onResume();
//							ToastMaker.showShortToast("注册成功");
							JSONObject jsonObj = obj.getJSONObject("map");
							String token = jsonObj.getString("token");
							JSONObject objmem = jsonObj.getJSONObject("member");
							String mobilephone = objmem.getString("mobilephone");
							String realVerify = objmem.getString("realVerify");
							String uid = objmem.getString("uid");
//							String name = objmem.getString("realName") ;
							String recommCodes = objmem.getString("recommCodes");
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
//							setResult(3, new Intent());
//							LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
//							LocalApplication.getInstance().getMainActivity().isHome = true;
							LocalApplication.getInstance().getMainActivity().setCheckedFram(1);
							LocalApplication.getInstance().getMainActivity().isLoginPsw = true;
//							ToastMaker.showShortToast("您已注册成功");
							startActivityForResult(new Intent(LoginQQPswAct.this, Login_End.class)
									.putExtra("newId",jsonObj.getString("pid"))
									.putExtra("regSendLabel",jsonObj.getString("regSendLabel")),3
							);
//							startActivity(new Intent(LoginQQPswAct.this, Login_End.class)
//								.putExtra("isCps",jsonObj.getString("isCps")));
//							finish();
//							showAlertDialog("注册成功", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, false, true, "");
						}
						else if ("XTWH".equals(obj.getString("errorCode"))) {
							startActivity(new Intent(LoginQQPswAct.this,WebViewActivity.class)
									.putExtra("URL", UrlConfig.WEIHU)
									.putExtra("TITLE", "系统维护"));
							return;
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
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

//	private void sendRegMsg() {
//		MobclickAgent.onEvent(LoginQQPswAct.this, UrlConfig.point+4+"");
//		showWaitDialog("加载中...", false, "");
//		OkHttpUtils.post().url(UrlConfig.SENDREGMSG)
//				.addParams("mobilephone", mobilephone.replaceAll(" ",""))
//				.addParams("version", UrlConfig.version)
//				.addParams("channel", "2").build()
//				.execute(new StringCallback() {
//
//					@Override
//					public void onResponse(String response) {
//						// TODO Auto-generated method stub
//						  dismissDialog();
//						JSONObject obj = JSON.parseObject(response);
//						if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("验证码已发送");
//						} else if ("1002".equals(obj.getString("errorCode"))) {
//							ToastMaker.showShortToast("每个手机号当天只能发送5条");
//						} else if ("1003".equals(obj.getString("errorCode"))) {
//							ToastMaker.showShortToast("短信发送失败");
//						} else {
//							ToastMaker.showShortToast("系统错误");
//						}
//					}
//
//					@Override
//					public void onError(Call call, Exception e) {
//						// TODO Auto-generated method stub
//						  dismissDialog();
//						ToastMaker.showShortToast("请检查网络");
//					}
//				});
//	}
	private void sendRegMsgS() {
		MobclickAgent.onEvent(LoginQQPswAct.this, UrlConfig.point+4+"");
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.SENDREGMSGAPP)
				.addParams("mobilephone", mobilephone.replaceAll(" ",""))
				.addParams("time", SecurityUtils.MD5AndSHA256(time))
				.addParams("version", UrlConfig.version)
//				.addParams("type", "1")
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
}
