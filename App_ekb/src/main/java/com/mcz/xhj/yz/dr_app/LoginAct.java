package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.EventMessage.EvmInvest;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

public class LoginAct extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.tv_phone)
	TextView tv_phone; // 手机号
	@BindView(R.id.bt_forgetPsw)
	TextView bt_forgetPsw; // 忘记密码
	@BindView(R.id.et_login_psw)
	EditText et_login_psw; // 登录密码
	@BindView(R.id.image_clean)
	ImageView image_clean; // 清除密码
	@BindView(R.id.image_eye)
	CheckBox image_eye; // 查看密码
	@BindView(R.id.bt_login)
	Button bt_login ;
	private SharedPreferences preferences;
	
	private String phone_num , uid ;
	Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_login;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onEvent(LoginAct.this, "100009");
//		MobclickAgent.onResume(this);
	}
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
	
	@Override
	protected void initParams() {
		title_centertextview.setText("登录");
		title_leftimageview.setOnClickListener(this);
		title_rightimageview.setVisibility(View.GONE);
		bt_forgetPsw.setOnClickListener(this);
		image_clean.setOnClickListener(this) ;
		image_eye.setOnClickListener(this) ;
		bt_login.setOnClickListener(this) ;
		uid = getIntent().getStringExtra("uid") ;
		phone_num = getIntent().getStringExtra("phone_num") ;
		tv_phone.setText(stringCut.phoneCut(phone_num)) ;
		// 密码监听
		Watcher watcher = new Watcher();
		et_login_psw.addTextChangedListener(watcher);
	}

	int i = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login :
			Matcher mPsw = psw.matcher(et_login_psw.getText().toString().trim());
			if(et_login_psw.getText().toString().trim().length()<=0){
				ToastMaker.showShortToast("登录密码不能为空");
			}else if(!mPsw.matches()){
				ToastMaker.showShortToast("请输入规则的密码");
			}else {
				MobclickAgent.onEvent(LoginAct.this, "100010");
				doLogin();// 下一步
			}
			break ;
		case R.id.bt_forgetPsw:
			startActivity(new Intent(LoginAct.this, LoginPswAct.class)
			        .putExtra("isForget", true)
					.putExtra("phone_num", phone_num)
					.putExtra("uid", uid));
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.image_clean:
			et_login_psw.setText("");
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
	
	
	private void doLogin() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.DOLOGIN)
				.addParam("mobilephone", phone_num)
				.addParam("passWord", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
				.addParam("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						EventBus.getDefault().post(new EvmInvest(true));
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("登录成功");
							JSONObject jsonObj = obj.getJSONObject("map") ;
							String token = jsonObj.getString("token") ;
							JSONObject objmem = jsonObj.getJSONObject("member");
//							 String emailVerify = objmem.getString("emailVerify") ;
//							 String lastLoginIp = objmem.getString("lastLoginIp") ;
//							 String lastLoginTime = objmem.getString("lastLoginTime") ;
//							 String loginVerify = objmem.getString("loginVerify") ;
//							 String mobileVerify = objmem.getString("mobileVerify") ;
							 String mobilephone = objmem.getString("mobilephone") ;
							 String realVerify = objmem.getString("realVerify") ;
							 String uid = objmem.getString("uid") ;
							 String recommCodes = objmem.getString("recommCodes") ;
//							 String regDate = objmem.getString("regDate") ;
//							 String regFrom = objmem.getString("regFrom") ;
//							 String status = objmem.getString("status") ;
//							 String tpwdSetting = objmem.getString("tpwdSetting") ;
//							 String name = objmem.getString("realName") ;
//							 String sex = objmem.getString("sex") ;

					           
							preferences = LocalApplication.getInstance().sharereferences;
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("login",true ) ;
							editor.putString("uid",uid ) ;
							editor.putString("recommCodes",recommCodes ) ;
							editor.putString("realVerify", realVerify);
							editor.putString("phone",mobilephone ) ;
//							editor.putString("name",name ) ;
							editor.putString("token",token ) ;
							editor.commit() ;
							pushRegisterId();
//				            setResult(RESULT_OK, new Intent());
							showAlertDialog("登录成功", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, false, true, "");
//							if("1".equalsIgnoreCase(realVerify)){
//								if(sex!=null){
//									if("1".equalsIgnoreCase(sex)){
//										editor.putString("name",","+name.substring(0, 1)+"先生");
//									}else{
//										editor.putString("name",","+name.substring(0, 1)+"女士");
//									}
//								}
//							}

//							finish() ;
//							startActivity(new Intent(LoginPswAct.this,
//									LoginPswAct.class).putExtra(
//									"phone_num",
//									stringCut.space_Cut(et_phonenumber
//											.getText().toString().trim())));
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("账号或密码为空 ");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("账号或密码错误   ");
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
	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		if (position == 0) {
			finish() ;
			ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改") ;
		}
		if (position == 1) {
			startActivity(new Intent(LoginAct.this, GestureEditActivity.class)) ;
			finish() ;
		}
	}
}
