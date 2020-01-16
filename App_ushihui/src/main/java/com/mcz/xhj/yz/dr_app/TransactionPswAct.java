package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.TimeButton;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

public class TransactionPswAct extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.et_transactionpsw)
	EditText et_transactionpsw; // 交易密码
	@BindView(R.id.image_close)
	ImageView image_close; // 清除密码
	@BindView(R.id.image_eye)
	CheckBox image_eye; // 查看密码
	@BindView(R.id.bt_login)
	Button bt_login; // 确定
	@BindView(R.id.tv_phone)
			@Nullable
	TextView tv_phone; // 确定
	@BindView(R.id.code_et)
	EditText code_et; // 验证吗
	@BindView(R.id.image_clean)
	ImageView image_clean; // 验证吗清除tv_getcode
	@BindView(R.id.tv_getcode)
	TimeButton tv_getcode; // 验证吗重发
	private Boolean isRun;
	private String mobilephone;
	Pattern p = Pattern.compile("^[0-9]{4}$");
	Pattern psw = Pattern.compile("^[0-9]{6}$");
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_transactionpsw;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		tv_getcode.onCreate(savedInstanceState);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		tv_getcode.onDestroy();
		super.onDestroy();
	}
	@Override
	protected void initParams() {
		tv_getcode.setTextAfter("发送").setTextBefore("发送验证码").setLenght(60 * 1000);
		if ("1".equals(preferences.getString("tpwdFlag", ""))) {
			title_centertextview.setText("重置交易密码");
			bt_login.setText("重置") ;
		} else {
			title_centertextview.setText("设置交易密码");
			bt_login.setText("完成") ;
		}
		
		title_rightimageview.setVisibility(View.GONE);
		title_leftimageview.setOnClickListener(this);
		tv_getcode.setOnClickListener(this) ;
		image_close.setOnClickListener(this) ;
		image_clean.setOnClickListener(this) ;
		image_eye.setOnClickListener(this) ;
		bt_login.setOnClickListener(this) ;
		mobilephone = getIntent().getStringExtra("phone_num");
//		tv_phone.setText(stringCut.phoneCut(mobilephone));

		// 密码监听
		Watcher watcher = new Watcher();
		et_transactionpsw.addTextChangedListener(watcher);
		Watcher_code watcher_code = new Watcher_code();
		code_et.addTextChangedListener(watcher_code);
//		if(LocalApplication.getInstance().getCount_Transaction() != 60 ){
//			time() ;
//		}
	}

	int i = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_getcode:
//			time() ;
			sendForgetTpwdCode();
			break ;
		case R.id.bt_login:
			Matcher m1;
			Matcher mPsw;
			m1 = p.matcher(code_et.getText().toString().trim());
			mPsw = psw.matcher(et_transactionpsw.getText().toString().trim());
			if (!m1.matches()) {
				ToastMaker.showShortToast("请输入正确的验证码");
			} else if (!mPsw.matches()) {
				ToastMaker.showShortToast("请输入规则的密码");
			} else {
				updateLoginPassWord();// 下一步
			}

		
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.image_clean:
			code_et.setText("");
			break;
		case R.id.image_close:
			et_transactionpsw.setText("");
			break;
		case R.id.image_eye:
			if (image_eye.isChecked()) {
				et_transactionpsw
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				et_transactionpsw.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		default:
			break;
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
			if (onTextLength > 0) {
				image_close.setVisibility(View.VISIBLE);
			} else {
				image_close.setVisibility(View.GONE);
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
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;;
	//设置交易密码
	private void updateLoginPassWord() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("tpwd", SecurityUtils.MD5AndSHA256(et_transactionpsw.getText().toString().trim()))
				.addParams("smsCode", code_et.getText().toString().trim())
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						setResult(5, new Intent());
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("交易密码设置成功");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("tpwdFlag","1") ;
							editor.commit() ;
							finish() ;
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("验证码错误");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("密码为空");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("交易密码不合法");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							finish();
							new show_Dialog_IsLogin(TransactionPswAct.this).show_Is_Login() ;
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
	
	//sendForgetTpwdCode
			private void sendForgetTpwdCode() {
				showWaitDialog("加载中...", false, "");
				OkHttpUtils
						.post()
						.url(UrlConfig.SENDFORGETPWSCODE)
						.addParams("uid", preferences.getString("uid", ""))
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
									new show_Dialog_IsLogin(TransactionPswAct.this).show_Is_Login() ;
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
			private void sendYuyinMsg() {
				OkHttpUtils
				.post()
				.url(UrlConfig.SENDFORGETPWSCODE)
				.addParams("uid", preferences.getString("uid", ""))
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
								new show_Dialog_IsLogin(TransactionPswAct.this).show_Is_Login() ;
								
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
//			// 计时器
//			public void time() {
//				isRun = true;
//
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						while (isRun) {
//							count_time--;
//							LocalApplication.getInstance().setCount_Transaction(count_time) ;
//							handler.sendEmptyMessage(10);
//							try {
//								Thread.sleep(1000);
//							} catch (Exception e) {
//							}
//						}
//					}
//				}).start();
//			}
//
//			public void stopTimer() {
//				tv_getcode.setEnabled(true);
//				tv_getcode.setText("重发");
//				tv_getcode.setTextColor(0xffff6e00);
//				// tv_getcode.setBackgroundColor(Color.parseColor("#ff6e00"));
//				isRun = false;
//			}
//
//			private Handler handler = new Handler() {
//				public void handleMessage(Message msg) {
//					switch (msg.what) {
//					case 10:
//						if (count_time <= 0) {
//							stopTimer();
//							LocalApplication.getInstance().setCount_Transaction(60) ;
//						} else {
//							tv_getcode.setEnabled(false);
//							tv_getcode.setTextColor(0xffcccccc);
//							// tv_getcode.setBackgroundColor(Color.parseColor("#b5b5b5"));
//							tv_getcode.setText("重发(" + count_time + ")秒");
//						}
//
//						break;
//					default:
//						break;
//					}
//				}
//			};

}
