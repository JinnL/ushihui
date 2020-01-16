package com.mcz.xhj.yz.dr_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.mcz.xhj.yz.gesture.GestureContentView;
import com.mcz.xhj.yz.gesture.GestureDrawline.GestureCallBack;
import com.mcz.xhj.yz.gesture.SPUtils;

/**
 * 
 * 手势密码设置界面
 * 
 */
public class GestureEditActivity extends Activity implements OnClickListener {
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
	private TextView mTextCancel;
	private TextView title_centertextview ;
	// private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private String mParamSetUpcode = null;
	private String mParamPhoneNumber;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private String mConfirmPassword = null;
	private int mParamIntentCode;
	private String login;
	private String getGpsd;// 手势密码
	private int psd_count = 3; // 可设置次数
	private TextView tv_other_user , tv_forget_psw ;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit) ;
		initParams() ;
	}
	
	private Intent in;
	protected void initParams() {
		 mySharedPreferences = getSharedPreferences(
					"userinfo", Activity.MODE_PRIVATE);
		 editor = mySharedPreferences.edit();
		getGpsd = LocalApplication.getInstance().sharereferences.getString("gesturePsd", "");
		mTextReset = (TextView) findViewById(R.id.tv_phone_login);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		title_centertextview = (TextView) findViewById(R.id.title_centertextview) ;
		tv_other_user = (TextView) findViewById(R.id.tv_other_user) ;
		tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw) ;
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextReset.setClickable(false);
		
		tv_forget_psw.setOnClickListener(this) ;
		tv_other_user.setOnClickListener(this) ;
		in = getIntent();
		// 是否有设置过手势密码
		if (!"".equals(getGpsd)) {
			mTextTip.setText("请输入手势密码");
			title_centertextview.setText("验证手势密码") ;
			tv_other_user.setText("使用账号登录") ;
			testUpViews();
		} else {
			title_centertextview.setText("设置手势密码") ;
			setUpViews();
		}
		setUpListeners();
	
	}
	

	// 验证手势密码
	private void testUpViews() {
		mGestureContentView = new GestureContentView(this, true, getGpsd,
				new GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {
					}

					@Override
					public void checkedSuccess() {
						// TODO Auto-generated method stub
						mGestureContentView.clearDrawlineState(0L);
						Toast.makeText(GestureEditActivity.this, "验证成功", Toast.LENGTH_LONG).show() ;
//						if(in!=null){
//							if(in.getStringExtra("flag")!=null){
//								if(in.getStringExtra("flag").equalsIgnoreCase("wellcome")){
//									startActivity(new Intent(GestureEditActivity.this, MainActivity.class));
//								}
//							}
//						}
						finish() ;

					}

					@Override
					public void checkedFail() {
						// TODO Auto-generated method stub
						psd_count--;
						if (psd_count <= 0) {
							String phone = LocalApplication.getInstance().sharereferences.getString("phone", "") ;
							editor.clear() ;
							editor.putBoolean("login",false) ;
							editor.putBoolean("FirstLog",false) ;
							editor.putBoolean("loginshoushi", false);
							editor.putString("phone", phone) ;
							editor.commit();
//							if(in!=null){
//								if(in.getStringExtra("flag")!=null){
//									if(in.getStringExtra("flag").equalsIgnoreCase("wellcome")){
//										startActivity(new Intent(GestureEditActivity.this, MainActivity.class));
//									}
//								}
//							}else{
////								这里可以用LocalApplication.getInstance().getMainActivity().onResume();代替
//								LocalApplication.getInstance().getMainActivity().finish();
//								startActivity(new Intent(GestureEditActivity.this, MainActivity.class));
//								LocalApplication.getInstance().getMainActivity().isLog = true;
//							}
							ToastMaker.showShortToast("您密码错误3次，请使用账号登录") ;
							finish();
						}

						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText(Html
								.fromHtml("<font color='#c70c1e'>密码错误,还可以尝试</font>"
										+ "<font color='#c70c1e'>"
										+ psd_count
										+ "</font>"
										+ "<font color='#c70c1e'>次</font>"));
						// 左右移动动画
						// Animation shakeAnimation = AnimationUtils
						// .loadAnimation(GestureEditActivity.this,
						// R.anim.shake);
						// mTextTip.startAnimation(shakeAnimation);

					}
				});
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}

	// 设置手势密码
	private void setUpViews() {

		// mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);

		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(getApplicationContext(),
				false, "", new GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
						if (!isInputPassValidate(inputCode)) {
							mTextTip.setText(Html
									.fromHtml("<font color='#c70c1e'>最少连接4个点, 请重新输入</font>"));
							mGestureContentView.clearDrawlineState(0L);
							if (mIsFirstInput) {
							    tv_forget_psw.setVisibility(View.GONE) ;
							}else{
								tv_forget_psw.setVisibility(View.VISIBLE) ;
							}
							return;
						}
						if (mIsFirstInput) {
							tv_forget_psw.setVisibility(View.VISIBLE) ;
							mFirstPassword = inputCode;
							updateCodeList(inputCode);
							mGestureContentView.clearDrawlineState(0L);
							mTextReset.setClickable(true);
							mTextReset.setText("");
							mTextTip.setText(Html
									.fromHtml("<font color='#c70c1e'>再次绘制解锁图案</font>"));
						} else {
							if (inputCode.equals(mFirstPassword)) {
								mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>设置成功</font>"));
								SPUtils.put(GestureEditActivity.this, "gesturePsd", inputCode);
								mGestureContentView.clearDrawlineState(0L);
								editor.putBoolean("loginshoushi", true);
								editor.putString("gesturePsd", inputCode);
								editor.commit();
								GestureEditActivity.this.finish();
							} else {
								mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上次绘制不一致，请重新绘制</font>"));
								mGestureContentView.clearDrawlineState(1300L);
							}
						}
						mIsFirstInput = false;
					}

					@Override
					public void checkedSuccess() {

					}

					@Override
					public void checkedFail() {
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}

	private void setUpListeners() {
		mTextReset.setOnClickListener(this);
	}

	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		// mLockIndicator.setPath(inputCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_forget_psw:
			mIsFirstInput = true ;
			mFirstPassword = "" ;
			tv_forget_psw.setVisibility(View.GONE) ;
			mTextTip.setText(Html
					.fromHtml("<font color='#c70c1e'>绘制解锁图案</font>"));
			break;
			
		case R.id.tv_other_user:
			if(LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
				String phone = LocalApplication.getInstance().sharereferences.getString("phone", "") ;
				editor.clear() ;
				editor.putBoolean("login",false) ;
				editor.putBoolean("FirstLog",false) ;
				editor.putBoolean("loginshoushi", false);
				editor.putString("phone", phone) ;
				editor.commit();
				if(!"使用账号登录".equals(tv_other_user.getText().toString())){
					ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改") ;
				}else{
					startActivity(new Intent(GestureEditActivity.this, NewLoginActivity.class));
				}
//				if(in!=null){
//					if(in.getStringExtra("flag")!=null){
//						if(in.getStringExtra("flag").equalsIgnoreCase("wellcome")){
//							startActivity(new Intent(GestureEditActivity.this, NewLoginActivity.class).putExtra("flag","wellcome"));
//						}
//					}
//				}else{
//					LocalApplication.getInstance().getMainActivity().finish();
//					startActivity(new Intent(GestureEditActivity.this, MainActivity.class));
//					LocalApplication.getInstance().getMainActivity().isLog = true;
//				}
			}
			finish() ;
			break;
		default:
			break;
		}
	}

	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)) {
//				LocalApplication.getInstance().getMainActivity().finish() ;
				GestureEditActivity.this.finish();
				return true;
			} else {
				GestureEditActivity.this.finish();
				return true;

			}

		}
		return false;
	}

	

}
