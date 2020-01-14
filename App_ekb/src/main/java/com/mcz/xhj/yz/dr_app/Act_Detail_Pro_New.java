package com.mcz.xhj.yz.dr_app;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils;
import com.mcz.xhj.yz.dr_adapter.ConponsUnuseAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.HorizontalProgressBarWithNumberNewHand;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/*
* 描述：新手标的详情页
* */

public class Act_Detail_Pro_New extends BaseActivity implements ISlideCallback,View.OnClickListener {
	@BindView(R.id.slidedetails)
    SlideDetailsLayout mSlideDetailsLayout;
	@BindView(R.id.ptr_pro_detail)
    PtrClassicFrameLayout ptr_pro_detail;
	@BindView(R.id.slidedetails_front)
	LinearLayout slidedetails_front;
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.title_centertextview_add)
	TextView title_centertextview_add;
	@BindView(R.id.title_righttextview_add)
	TextView title_righttextview_add;
	@BindView(R.id.title_leftimageview_add)
	ImageView title_leftimageview_add;

	@BindView(R.id.tv_prodetail_rate)
	TextView tv_prodetail_rate;
	@BindView(R.id.tv_gift)
	TextView tv_gift;
	@BindView(R.id.tv_prodetail_rate_jiaxi)
	TextView tv_prodetail_rate_jiaxi;
	@BindView(R.id.tv_prodetail_red)
	TextView tv_prodetail_red;
	@BindView(R.id.tv_prodetail_interest)
	TextView tv_prodetail_interest;
	@BindView(R.id.tv_prodetail_double)
	TextView tv_prodetail_double;
	@BindView(R.id.tv_prodetail_deadline)
	TextView tv_prodetail_deadline;
	@BindView(R.id.tv_prodetail_sum)
	TextView tv_prodetail_sum;
	@BindView(R.id.tv__prodetail_balance)
	TextView tv__prodetail_balance;
	@BindView(R.id.tv_prodetail_income)
	TextView tv_prodetail_income;
	@BindView(R.id.tv_prodetail_income_add)
	TextView tv_prodetail_income_add;
	@BindView(R.id.tv_prodetail_income_name)
	TextView tv_prodetail_income_name;
	@BindView(R.id.et_prodetail_money)
	EditText et_prodetail_money;
	@BindView(R.id.bt_prodetail_ok)
	Button bt_prodetail_ok;
	@BindView(R.id.progressbar)
    HorizontalProgressBarWithNumberNewHand progressbar;

	@BindView(R.id.tv_prodetail_changeone)
	TextView tv_prodetail_changeone;
	@BindView(R.id.tv_prodetail_changetwo)
	TextView tv_prodetail_changetwo;
	@BindView(R.id.tv_prodetail_changetwo_gift)
	TextView tv_prodetail_changetwo_gift;
	@BindView(R.id.tv_prodetail_num)
	TextView tv_prodetail_num;
	@BindView(R.id.tv_prodetail_conpons)
	TextView tv_prodetail_conpons;
	@BindView(R.id.iv_prodetail_changetwo)
	ImageView iv_prodetail_changetwo;
	@BindView(R.id.ll_prodetail_changeone)
	LinearLayout ll_prodetail_changeone;
	@BindView(R.id.ll_prodetail_changetwo)
	LinearLayout ll_prodetail_changetwo;
	@BindView(R.id.ll_prodetail_conpons)
	LinearLayout ll_prodetail_conpons;
	@BindView(R.id.ll_bottom)
	LinearLayout ll_bottom;
	@BindView(R.id.ll_shouyi)
	LinearLayout ll_shouyi;
	@BindView(R.id.iv_gift_to)
	@Nullable
	ImageView iv_gift_to;
	@BindView(R.id.sv_bottem)
	ScrollView sv_bottem;
	@BindView(R.id.rl_pro_new)
    RelativeLayout rl_pro_new;
	@BindView(R.id.check_tiyan)
	CheckBox check_tiyan;
	@BindView(R.id.ll_limit)
	LinearLayout ll_limit;
	@BindView(R.id.tv_wan_shouyi_new)
	TextView tv_wan_shouyi_new;
	@BindView(R.id.ll_tiyanjin_new)
	LinearLayout ll_tiyanjin_new;

	private String pid, ptype, uid,atid;
	private SharedPreferences preferences;
	Long lastClick = 0l;//防重复点击计时
	private boolean isShowLabel = false;//是否提示激活体验金
	private TradePwdPopUtils pop;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_detail_pro_new;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getDate();
	}

    public static boolean hasNavigationBar = false;
    // 获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass,"qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

	@Override
	protected void initParams() {
        if(checkDeviceHasNavigationBar(Act_Detail_Pro_New.this)){
            rl_pro_new.setPadding(0,0,0,140);
        }
		ptr_pro_detail.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				getDate();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				// TODO Auto-generated method stub
					return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			}
		});
//		mSlideDetailsLayout = (SlideDetailsLayout)findViewById(R.id.slidedetails);
		sv_bottem.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				int height = sv_pic.getChildAt(0).getMeasuredHeight();
//				sv_pic.getHeight();
				if(sv_bottem.getScrollY()==0){
					mSlideDetailsLayout.smoothOpen(false);
				}else{
					mSlideDetailsLayout.smoothOpen(true);
				}
//				if(sv_pic.fullScroll(ScrollView.FOCUS_UP)){
//					mSlideDetailsLayout.isMove = true;
//				}else{
//					mSlideDetailsLayout.isMove = false;
//				}
				return false;
			}
		});
//		FragmentManager fm = getSupportFragmentManager();
//		fm.beginTransaction().replace(R.id.slidedetails_front, new Frag_Detail_Pro_Top()).commit();
		preferences = LocalApplication.getInstance().sharereferences;
		Intent intent = getIntent();
		Uri uri = intent.getData();
		if (uri != null) {
			pid = uri.getQueryParameter("product");
			ptype = uri.getQueryParameter("ptype");
			atid = uri.getQueryParameter("atid");
			intent_fid = uri.getQueryParameter("fid");
			intent_amount = uri.getQueryParameter("amount");
			intent_enableAmount = uri.getQueryParameter("enableAmount");
		}else{
			pid = getIntent().getStringExtra("pid");
			ptype = getIntent().getStringExtra("ptype");
			atid = getIntent().getStringExtra("atid");
			intent_fid = getIntent().getStringExtra("fid");
			intent_amount = getIntent().getStringExtra("amount");
			intent_enableAmount = getIntent().getStringExtra("enableAmount");
		}
		Watcher watcher = new Watcher();
		et_prodetail_money.addTextChangedListener(watcher);
		ll_prodetail_conpons.setOnClickListener(this);
		title_righttextview.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		title_righttextview_add.setOnClickListener(this);
		title_leftimageview_add.setOnClickListener(this);
		bt_prodetail_ok.setOnClickListener(this);
		ll_prodetail_changetwo.setOnClickListener(this);
		tv_prodetail_changetwo_gift.setOnClickListener(this);
		ll_prodetail_changeone.setOnClickListener(this);
		check_tiyan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_leftimageview:
				finish();
				break;
			case R.id.title_leftimageview_add:
				finish();
				break;
			case R.id.tv_prodetail_changetwo_gift://去iPhone7标的中奖者
				startActivity(new Intent(Act_Detail_Pro_New.this, Winner_Act.class).putExtra("pid", pid));
				break;
			case R.id.ll_prodetail_changeone://去iPhone7标详情
				if(linkUrl!=null&&!linkUrl.equalsIgnoreCase("")){
					startActivity(new Intent(Act_Detail_Pro_New.this,WebViewActivity.class)
							.putExtra("URL", linkUrl)
							.putExtra("TITLE", "活动详情")
							.putExtra("BANNER", "banner")
					);
				}
				break;
			case R.id.ll_prodetail_conpons:
//				if(preferences.getString("uid", "").equalsIgnoreCase("")&&pid!=null){
//					startActivity(new Intent(Act_Detail_Pro_New.this, isLoginAct.class));
//				}else{
//					if(!et_prodetail_money.getText().toString().equalsIgnoreCase("")){
//						getConponsList();
//					}else{
//						ToastMaker.showLongToast("请输入金额");
//					}
//				}
//				showPopupWindowConpons(null);
//				if (mlslb2 != null && !mlslb2.equals("")) {
//					startActivityForResult(
//							new Intent(Act_Detail_Pro.this, choose_Coupons.class)
//									.putExtra("list", (Serializable) mlslb2)
//									.putExtra("insestAmount", et_prodetail_money.getText().toString())
//									.putExtra("fid", fid), 1);
//				} else {
//					ToastMaker.showShortToast("无可用优惠券");
//				}
				// usable() ;
				break;
			case R.id.title_righttextview:
				startActivity(new Intent(Act_Detail_Pro_New.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
				break;
			case R.id.title_righttextview_add:
				startActivity(new Intent(Act_Detail_Pro_New.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
				break;
			case R.id.bt_prodetail_ok:
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_prodetail_money.getWindowToken(),0);
//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				if (System.currentTimeMillis() - lastClick <= 1000) {
					// ToastMaker.showShortToast("点那么快干什么");
					return;
				}
				lastClick = System.currentTimeMillis();
//				startActivity(new Intent(Act_Detail_Pro.this, FourPartAct.class)
//						.putExtra("proName",fullname)//产品名称
//						.putExtra("proDeadline",deadline)//投资期限
//						.putExtra("proRate",rate_h)//预期年化收益
//						.putExtra("proAmount",et_prodetail_money.getText().toString())//投资金额
//				);
				if(preferences.getString("uid", "").equalsIgnoreCase("")){
					startActivity(new Intent(Act_Detail_Pro_New.this, isLoginAct.class));
				}
				else if (et_prodetail_money.getText().length() <= 0) {
					ToastMaker.showShortToast("请输入投资金额");
				}
				else if (!"1".equals(preferences.getString("realVerify", ""))) {
					ToastMaker.showShortToast("还未认证 ，请认证");
					startActivity(new Intent(Act_Detail_Pro_New.this, FourPartAct.class)
							.putExtra("proName",fullname)//产品名称
							.putExtra("proDeadline",deadline)//投资期限
							.putExtra("proRate",rate_h)//预期年化收益
							.putExtra("specialRate", exRate+"")//活动利率
							.putExtra("proAmount",et_prodetail_money.getText().toString())//投资金额
					);
				}
				else if (enableAmount > Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString()))) {
					ToastMaker.showShortToast("不满足红包最低使用金额");
				}
				else if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) < Double.parseDouble(increasAmount)) {
					ToastMaker.showShortToast("投资金额最少为"+ stringCut.getNumKbs(Double.parseDouble(increasAmount)));
				}
				else if ((Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString()))) % Double.parseDouble(increasAmount) != 0) {
					ToastMaker.showShortToast("投资金额需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
				}
//				else  if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(surplusAmount)) {
//					ToastMaker.showShortToast("投资金额不能大于产品可投金额");
//				}
//				else if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(balance)) {
//					ToastMaker.showShortToast("账户可用余额不足，请先充值！");
//					startActivity(new Intent(Act_Detail_Pro.this,CashInAct.class));
//				}
				else{
					Invest_Begin_bl = true ;
//					Invest_Begin() ;
					showPopupWindowGoPay();
					MobclickAgent.onEvent(Act_Detail_Pro_New.this, "100022");
				}
				break;
			case R.id.check_tiyan:
				if(!et_prodetail_money.getText().toString().equalsIgnoreCase("")){
					if(Double.parseDouble(et_prodetail_money.getText().toString())<5000){
						check_tiyan.setClickable(true);
						et_prodetail_money.setText("5000");
					}else{
						check_tiyan.setClickable(false);
					}
				}else{
					check_tiyan.setClickable(true);
					et_prodetail_money.setText("5000");
				}
			default:
				break;
		}
	}

	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		if(((String) tag).equalsIgnoreCase("hongbao_user")){
			if (position == 0) {
				hongbao_check = true ;
				Invest_Begin() ;
			}
			if (position == 1) {
				Invest_Begin_bl = false;
				getConponsList();
//				startActivityForResult(
//						new Intent(Act_Detail_Pro.this, choose_Coupons.class)
//								.putExtra("list", (Serializable) mlslb2)
//								.putExtra("insestAmount",et_prodetail_money.getText().toString())
//								.putExtra("fid", fid), 1);

			}
		}
		if(((String) tag).equalsIgnoreCase("shezhi")){
			shezhi_check = true ;
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Act_Detail_Pro_New.this, TransactionPswAct.class),1);
			}
		}
		if(((String) tag).equalsIgnoreCase("three_time")){
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Act_Detail_Pro_New.this, TransactionPswAct.class),1);
			}
		}
	}

	/**
	 * 投资前判断以及投资
	 */
	private boolean isfirstPwd = false;//是否是第一次设置密码
	private void Invest_Begin(){
		firstPwd = "" ;
//		if(!hongbao_check && mlslb2 != null && !mlslb2.equals("") && fid == ""){
//			showAlertDialog("提示", "您还有可用优惠券是否使用？", new String[] { "忽略","使用" }, true, true, "hongbao_user");
//			return;
//		}
		memberSetting();
	}

	//设置交易密码
	private void sendFirstTpwdCode() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
//				.addParams("smsCode", code_et.getText().toString().trim())
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						setResult(5, new Intent());
						// TODO Auto-generated method stub
						dismissDialog();
						pop.popWindow.dismiss();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("交易密码设置成功");
//							SharedPreferences.Editor editor = preferences.edit();
//							editor.putString("tpwdFlag","1") ;
//							editor.commit() ;
							product_Invest();
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
							new show_Dialog_IsLogin(Act_Detail_Pro_New.this).show_Is_Login() ;
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

	/**
	 * 账户信息
	 *
	 */
	private void memberSetting() {
		OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
//							String realVerify = map.getString("realVerify");
//							String tpwdFlag = map.getString("tpwdFlag");
							if(map.getInteger("tpwdFlag")==0){
								pop = new TradePwdPopUtils();
								pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

									@Override
									public void callBaceTradePwd(String pwd) {
										pop.tv_tips.setText("");
										if(!isfirstPwd){
											firstPwd = pwd;
											isfirstPwd = true;
											pop.tv_pwd1.setText("✔");
											pop.tv_pwd_line.setBackgroundColor(Color.parseColor("#fb5b6d"));
											pop.tv_pwd2.setBackgroundResource(R.drawable.bg_corner_red_yuan) ;
										}else{
											secondPwd = pwd;
											if(firstPwd.equalsIgnoreCase(secondPwd)){
												pop.tv_tips.setText("");
												//去设置支付密码
												sendFirstTpwdCode();
											}else {
												pop.tv_tips.setText("您输入的确认密码和之前不一致");
											}
										}
									}
								});
								pop.showPopWindow(Act_Detail_Pro_New.this, Act_Detail_Pro_New.this, slidedetails_front);
								pop.ll_invest_money.setVisibility(View.GONE);
								pop.ll_pwd_title.setVisibility(View.VISIBLE);
								pop.ll_pwd.setVisibility(View.VISIBLE);
								pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

									@Override
									public void onDismiss() {
										Invest_Begin_bl = false;
									}
								}) ;
								pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss();
									}
								});
								pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss() ;
										startActivityForResult(new Intent(Act_Detail_Pro_New.this, TransactionPswAct.class),1);
									}
								});
								pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
									@Override
									public void onDismiss() {
										pop.tv_tips.setText("");
										isfirstPwd = false;
									}
								});
							}else{
								hongbao_check = false ;
								MobclickAgent.onEvent(Act_Detail_Pro_New.this, "100023");
								pop = new TradePwdPopUtils();
								pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

									@Override
									public void callBaceTradePwd(String pwd) {
										firstPwd = pwd;
										product_Invest();
									}
								});
								pop.showPopWindow(Act_Detail_Pro_New.this, Act_Detail_Pro_New.this, slidedetails_front);
								pop.ll_invest_money.setVisibility(View.VISIBLE);
								pop.tv_key_money.setText(et_prodetail_money.getText().toString());
								pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss();
									}
								});
								pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

									@Override
									public void onDismiss() {
										Invest_Begin_bl = false;
									}
								}) ;
								pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss() ;
										startActivityForResult(new Intent(Act_Detail_Pro_New.this, TransactionPswAct.class),1);
									}
								});
							}

						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_Detail_Pro_New.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	// 投资
	private String jumpURLActivity = "";
	private void product_Invest() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.INVEST)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
				.addParams("amount", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
				.addParams("fid", fid)
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						ptr_pro_detail.refreshComplete();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject jsonMap = obj.getJSONObject("map");
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
							startActivity(new Intent(Act_Detail_Pro_New.this,Act_Pro_End.class)
											.putExtra("pid", pid)
											.putExtra("tv_name", fullname)
											.putExtra("tv_money", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
											.putExtra("tv_day", deadline)
											.putExtra("tv_rate", rate)
											.putExtra("tv_earn", shouyi+shouyi_add+"")
											.putExtra("tv_red", resultNum+"")
											.putExtra("nothing", nothing)
											.putExtra("pid", pid)
											.putExtra("tv_start", tv_establish)
											.putExtra("specialRate", exRate+"")//活动利率
											.putExtra("tv_end", tv_expireDate)
											.putExtra("tv_red", "xinshou")
											.putExtra("endTime", jsonMap.getString("expireDate"))
							) ;
							finish();
						} else if ("1001".equals(obj.getString("errorCode"))) {
							pop.tv_tips.setText("密码输入错误，请重新输入");
//							ToastMaker.showShortToast("密码输入错误，请重新输入。");
						} else{
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
							if ("XTWH".equals(obj.getString("errorCode"))) {
								startActivity(new Intent(Act_Detail_Pro_New.this,WebViewActivity.class)
										.putExtra("URL", UrlConfig.WEIHU)
										.putExtra("TITLE", "系统维护"));
								return;
							}
							if ("2001".equals(obj.getString("errorCode"))) {
								showAlertDialog("提示", "连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码。", new String[] { "稍后再试",
										"忘记密码" }, true, true, "three_time");
							} else if ("1002".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("产品已募集完");
							} else if ("1003".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("项目可投资金额不足");
							} else if ("1004".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("小于起投金额");
							} else if ("1005".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("非递增金额整数倍");
							} else if ("1006".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("投资金额大于项目单笔投资限额");
							} else if ("1007".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("账户可用余额不足");
							} else if ("1008".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("已投资过产品，不能投资新手产品");
							} else if ("1009".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("用户不存在");
							} else if ("9998".equals(obj.getString("errorCode"))) {
								new show_Dialog_IsLogin(Act_Detail_Pro_New.this).show_Is_Login();
							} else if ("1010".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("不符合优惠券使用条件");
							} else if ("1011".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("投资失败,请稍后再试");
							} else {
								ToastMaker.showShortToast("系统异常");
							}
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ptr_pro_detail.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络异常，请检查");
					}
				});
	}

	/**
	 * 监听输入金额
	 */
	class Watcher implements TextWatcher {
		int onTextLength = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onTextLength = s.length();
			if (onTextLength > 0 && rate != null) {
				et_prodetail_money.setHint("");
				Money_Get(nothing);
			} else {
				tv_prodetail_income.setText("0.00");
				tv_prodetail_income_add.setText("");
				tv_prodetail_income_name.setText("");
				et_prodetail_money.setHint(stringCut.getNumKbs(Double.parseDouble(leastaAmount))+"元起购买，" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "元递增");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>0){
				if(Double.parseDouble(s.toString())>=5000){
					check_tiyan.setChecked(true);
					check_tiyan.setClickable(false);
				}else{
					check_tiyan.setChecked(false);
					check_tiyan.setClickable(true);
				}
			}else{
				check_tiyan.setClickable(true);
				check_tiyan.setChecked(false);
			}
		}
	}

	private String maxAmount; // 产品投资限额
	private String balance, leastaAmount; // 余额，起投
	private int int_last; // 100整数
	private String increasAmount; // 递增
	private String windMeasure; // 风控措施
	private String introduce;// 产品介绍
	private String repaySource;// 还款来源
	private String borrower;// 债务人概况
	private String repayType;//还款方式
	public Bundle bundle;

	private String surplusAmount; // 剩余金额
	private String deadline;
	private String endDate;
	private String startTime;
	private String rate_h, rate = null;
	private String fid = ""; // 可用红包id
	private SpannableString ss;
	private ArrayList<ConponsBean> mlslb2; // 红包列表
	String fullname  ; //产品名称
	private boolean isOldUser = false;//是否是老用户
	private double specialRate1 = 0;//双旦活动利率
	private double exRate = 0;//总共增加的利率
	private boolean isFirstGet = true;
	private boolean isFirstGetRate = true;
	double enableAmount = 0, amount = 0, raisedRate = 0, multiple = 1;
	Double shouyi;
	Double shouyi_add;
	int resultNum = 0 ;
	/**
	 * nothing用于判断使用了那种优惠券
	 * amount--返现券
	 * multiple--翻倍券
	 * raisedRate--加息券
	 */
	private String nothing = "";
	private String intent_fid;
	private String intent_amount;
	private String intent_enableAmount;
	private Boolean hongbao_check = false , shezhi_check = false ,Invest_Begin_bl = false;
	String firstPwd;
	String secondPwd;

	private int isNot;  //是否抢完 0 = 未强完,1抢完
	private String ppid; //奖品ID
	private String prizeAmount;//送豪礼需要的投资金额
	private String linkUrl = null;//iPhone7详情链接
	private void getDate() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post()
				.url(UrlConfig.PRODUCT_DETAIL)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtils.i("--->product_detail(new)：");
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						ptr_pro_detail.refreshComplete();
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							balance = map.getString("balance");
							isOldUser = map.getBoolean("isOldUser");//是否是老用户
							specialRate1 = map.getDouble("specialRate");
							isShowLabel = map.getBoolean("isShowLabel");
							JSONObject info = map.getJSONObject("info");
							JSONObject prize = map.getJSONObject("prize");
							exRate = specialRate1 + info.getDouble("activityRate");//额外增加的利息
							tv_prodetail_sum.setText(stringCut.getNumKbs(Double.parseDouble(info.getString("amount"))));
//							if(map.getString("linkURL")!=null){
//								if(!map.getString("linkURL").equalsIgnoreCase("")){
//									linkUrl = map.getString("linkURL");
//								}
//							}
							if(preferences.getString("uid", "").equalsIgnoreCase("")){
								ll_tiyanjin_new.setVisibility(View.VISIBLE);
							} else {
								if(isShowLabel){
									ll_tiyanjin_new.setVisibility(View.VISIBLE);
								}else{
									ll_tiyanjin_new.setVisibility(View.GONE);
								}
							}

							//老用户专享加息
							if(exRate!=0){
								tv_prodetail_rate_jiaxi.setVisibility(View.VISIBLE);
								if(specialRate1!=0&&isOldUser){
									if(isFirstGetRate){
										tv_prodetail_rate_jiaxi.setText("+" + exRate + "%");
										isFirstGetRate = false;
									}
								}else{
									if(info.getDouble("activityRate")!=0){
										tv_prodetail_rate_jiaxi.setText("+"+info.getDouble("activityRate")+"%");
									}else {
										tv_prodetail_rate_jiaxi.setVisibility(View.GONE);
									}
									exRate = info.getDouble("activityRate");
								}
							}else {
								tv_prodetail_rate_jiaxi.setVisibility(View.GONE);
							}

							//判断可用哪种优惠券
//							if ("1".equals(info.getString("isInterest"))) {
//								tv_prodetail_interest.setVisibility(View.VISIBLE);
//							}
//							if ("1".equals(info.getString("isCash"))) {
//								tv_prodetail_red.setVisibility(View.VISIBLE);
//							}
//							if ("1".equals(info.getString("isDouble"))) {
//								tv_prodetail_double.setVisibility(View.VISIBLE);
//							}
//							if("1".equals(info.getString("isPrize"))){
//								tv_prodetail_changetwo_gift.setText("已开奖");
//								iv_gift_to.setVisibility(View.VISIBLE);
//							}else if("2".equals(info.getString("isPrize"))){
//								tv_prodetail_changetwo_gift.setText("未开奖");
//								tv_prodetail_changetwo_gift.setClickable(false);
//							}

							fullname  = info.getString("fullName") ;
							title_centertextview.setText(fullname);
							title_centertextview_add.setText(fullname);
							proAnimator(Integer.valueOf(stringCut.pertCut(info.getString("pert"))).intValue(),progressbar) ;
							rate = info.getString("rate");
							deadline = info.getString("deadline");
							maxAmount = info.getString("maxAmount");
							increasAmount = info.getString("increasAmount");
							surplusAmount = info.getString("surplusAmount");
							leastaAmount = info.getString("leastaAmount");//起投
							rate_h = stringCut.getNumKbs(Double.parseDouble(rate)) + "%";
							tv__prodetail_balance.setText(stringCut.getNumKbs(Double.parseDouble(surplusAmount)));
							tv_prodetail_rate.setText(rate_h);
							tv_prodetail_deadline.setText("期限" + deadline + "天 ");
							if (et_prodetail_money.getText().length() <= 0) {
								et_prodetail_money.setHint(stringCut.getNumKbs(Double.parseDouble(leastaAmount))+"元起购买，" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "元递增");
							}
							//万元预计收益
							Double rate_expert = Double.valueOf(Act_Detail_Pro_New.this.rate)/100;
							Double exRate_expert = Double.valueOf(Act_Detail_Pro_New.this.exRate)/100;
							Double day = Double.valueOf(deadline);
							double shouyi = 10000 * (rate_expert + exRate_expert) * day / 360;
							BigDecimal bg = new BigDecimal(shouyi);
							shouyi = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							tv_wan_shouyi_new.setText(shouyi+"");
							/**
							 * 送豪礼专用
							 *
							 */
//							if(prize!=null){
//								ll_prodetail_conpons.setVisibility(View.GONE);
//								ll_prodetail_changeone.setVisibility(View.VISIBLE);
//								ll_prodetail_changetwo.setVisibility(View.VISIBLE);
//								ll_prodetail_conpons.setVisibility(View.GONE);
//								tv_prodetail_changetwo.setText("礼品详情");
//								iv_prodetail_changetwo.setImageResource(R.mipmap.pro_detail_gift_icon);
//								tv_prodetail_changetwo_gift.setText("");
//								isNot = prize.getInteger("isNot");  //是否抢完 0 = 未强完,1抢完
//								ppid = prize.getString("id");//奖品id预约的时候需要
//								prizeAmount = prize.getString("amount");
//								tv_gift.setVisibility(View.VISIBLE);
//								tv_gift.setText("投资即送"+prize.getString("name"));
//								et_prodetail_money.setText(stringCut.getNumKbs(Double.parseDouble(prizeAmount)));
//								et_prodetail_money.setFocusable(false);
//								et_prodetail_money.setFocusableInTouchMode(false);
//								if(isNot == 0){
//									bt_prodetail_ok.setText("投资免费领");
//								}else{
//									bt_prodetail_ok.setText("标的余额不足，请预约");
//								}
//								tv_prodetail_income.setText(stringCut.getNumKbs(Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
//										* ((Double.parseDouble(rate) + exRate) / 360 / 100)
//										* Double.parseDouble(deadline))+"");
//							}else{
//								ll_prodetail_conpons.setVisibility(View.VISIBLE);
//								/**
//								 *  送iPhone7专用
//								 */
//								if(atid!=null){
//									ll_prodetail_conpons.setVisibility(View.GONE);
//									ll_prodetail_changeone.setVisibility(View.VISIBLE);
//									ll_prodetail_changetwo.setVisibility(View.VISIBLE);
//									tv_prodetail_changetwo.setText("中奖者信息");
//									tv_gift.setVisibility(View.VISIBLE);
//									tv_gift.setText("投资白送iPhone7");
//								}else {
//									tv_gift.setVisibility(View.GONE);
//									ll_prodetail_conpons.setVisibility(View.VISIBLE);
//								}
//								Watcher watcher = new Watcher();
//								et_prodetail_money.addTextChangedListener(watcher);
//							}


//							JSONArray list = map.getJSONArray("couponList");
//							if (null != list) {
//								if (list.size() <= 0) {
//									ll_prodetail_conpons.setFocusable(false);
//									ll_prodetail_conpons.setEnabled(false);
//									tv_prodetail_num.setText("0个可用");
//								} else {
//									tv_prodetail_num.setText(list.size()+"个可用");
//									mlslb2 = (ArrayList<ConponsBean>) JSON.parseArray(list.toJSONString(),ConponsBean.class);
//									if(intent_fid!=null&&!intent_fid.equalsIgnoreCase("")){//你用返现券
//										if(isFirstGet){
//											isFirstGet = false;
//											resultNum = 1 ;
//											raisedRate = 0;
//											multiple = 1;
//											fid = intent_fid;
//											enableAmount = Double.parseDouble(intent_enableAmount);
//											amount = Double.parseDouble(intent_amount);
//											tv_prodetail_conpons.setText("使用"+stringCut.getNumKbs(amount)+"元返现红包");
//											nothing = stringCut.getNumKbs(amount) ;
//											if (et_prodetail_money.getText().toString().trim().length() > 0) {
//												Money_Get("amount");
//												tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
//											}
//										}
//									}else{//使用加息券
//										for (int i = 0; i < mlslb2.size(); i++) {
//											if(mlslb2.get(i).getPid()!=null){
//												if(isFirstGet){
//													if(mlslb2.get(i).getPid().equalsIgnoreCase(pid)){
//														isFirstGet = false;
//														resultNum = 2 ;
//														raisedRate = 0;
//														multiple = 1;
//														fid = mlslb2.get(i).getId();
//														enableAmount = mlslb2.get(i).getEnableAmount();
//														raisedRate = mlslb2.get(i).getRaisedRates();
////														double exRates = raisedRate + exRate;
//														tv_prodetail_conpons.setText("使用" + raisedRate + "%加息券");
//														nothing = raisedRate+"" ;
//														if (et_prodetail_money.getText().toString().trim().length() > 0) {
//															Money_Get("raisedRate");
//															tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
//														}
//													}
//												}
//											}
//										}
//									}
//								}
//							}
							if(info.getInteger("repayType")==1){
								repayType = "到期还本付息";
							}else if(info.getInteger("repayType")==2){
								repayType = "按月付息到期还本";
							}

							if ("6".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								ll_prodetail_conpons.setVisibility(View.GONE);
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setText("抢光了 ");
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								et_prodetail_money.setFocusableInTouchMode(false);
								et_prodetail_money.setFocusable(false);
							} else if ("8".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								bt_prodetail_ok.setText("待还款");
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								et_prodetail_money.setFocusableInTouchMode(false);
								et_prodetail_money.setFocusable(false);
							} else if ("9".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								bt_prodetail_ok.setText("已还款");
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								et_prodetail_money.setFocusableInTouchMode(false);
								et_prodetail_money.setFocusable(false);
							} else {
								bt_prodetail_ok.setClickable(true);
//								startTime = map.getString("nowTime");//计息日期
								if (preferences.getString("uid", "").equalsIgnoreCase("")) {
									bt_prodetail_ok.setText("请登录后抢购");
								} else {
									bt_prodetail_ok.setText("立即抢购");
								}
//								bt_prodetail_ok.setText("立即抢购");
							}
							if(map.getBoolean("newHandInvested")!=null){
								if(map.getBoolean("newHandInvested")){
									bt_prodetail_ok.setText("已完成体验 ");
									bt_prodetail_ok.setClickable(false);
									et_prodetail_money.setHint("此项目为新用户专享标，仅限新用户投资");
									et_prodetail_money.setFocusableInTouchMode(false);
									et_prodetail_money.setFocusable(false);
								}
							}

							// 详情信息
//							windMeasure = info.getString("windMeasure");
//							introduce = info.getString("introduce");
//							repaySource = info.getString("repaySource");
//							borrower = info.getString("borrower");
//							//成立日期,到期日期
////							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;
//							tv_expireDate= info.getString("expireDate");//还款日期
////							tv_establish = info.getString("establish") ;//成立日期
//							endDate = info.getString("endDate");//募集结束日期
//							bundle = new Bundle();
//							bundle.putString("repayType",repayType);
//							bundle.putString("windMeasure",windMeasure);
//							bundle.putString("introduce",introduce);
//							bundle.putString("repaySource",repaySource);
//							bundle.putString("borrower",borrower);
//							bundle.putString("ingTime",endDate);
//							bundle.putString("startTime",startTime);
//							bundle.putString("endTime",info.getString("expireDate"));
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("该产品已下架");
						}
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_Detail_Pro_New.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					/*进度条动画*/
					private void proAnimator(final int pert,final HorizontalProgressBarWithNumberNewHand progressbar) {
						ValueAnimator animator = ValueAnimator.ofInt(0, pert);
						animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator animation) {
								int progress = (int) animation.getAnimatedValue();
								progressbar.setProgress(progress);
							}
						});
						animator.setDuration(2000);
						animator.start();
					}
//					private void proAnimator(int pert) {
//						ValueAnimator animator = ValueAnimator.ofInt(0, pert);
//						animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//							@Override
//							public void onAnimationUpdate(ValueAnimator animation) {
//								int progress = (int) animation.getAnimatedValue();
//								progressbar_pert.setSecondaryProgress(progress);
//							}
//						});
//						animator.setDuration(2000);
//						animator.start();
//					}
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ptr_pro_detail.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}

	//显示成立日期和到期日期
	String tv_establish,  tv_expireDate ,luckcode,luckcodesize;
	private void pro_Str_Or(String establish, String expireDate) {
		// 成立日期,到期日期
		if (!("".equals(establish) || null == establish)) {
			tv_establish = stringCut.getDateToString(Long.parseLong(establish)) ;
//			pro_start.setText("产品成立 : "+stringCut.getDateToString(Long.parseLong(establish))) ;
		}
		if (!("".equals(expireDate) || null == expireDate)) {
			tv_expireDate= stringCut.getDateToString(Long.parseLong(expireDate)) ;
//			pro_end.setText("产品到期 : "+stringCut.getDateToString(Long.parseLong(expireDate))) ;
		}
	}

	/*
		计算收益
	 */
	private void Money_Get(String flag) {
		if(flag.equalsIgnoreCase("amount")){
			shouyi = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
							* ((Double.parseDouble(rate) + exRate) / 360 / 100)
							* Double.parseDouble(deadline);
			shouyi_add = amount;
			tv_prodetail_income_name.setText("(返现红包)");
		}else if(flag.equalsIgnoreCase("raisedRate")){
			shouyi = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
					* ((Double.parseDouble(rate) + exRate) / 360 / 100)
					* Double.parseDouble(deadline);
			shouyi_add = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
					* (raisedRate / 360 / 100)
					* Double.parseDouble(deadline);
			tv_prodetail_income_name.setText("(加息收益)");
		}else if(flag.equalsIgnoreCase("multiple")){
			shouyi = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
					* ((Double.parseDouble(rate) + exRate) / 360 / 100)
					* Double.parseDouble(deadline);
			shouyi_add = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
					* ((Double.parseDouble(rate) * (multiple-1)) / 360 / 100)
					* Double.parseDouble(deadline);
			tv_prodetail_income_name.setText("(翻倍收益)");
		}else{
			shouyi = Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
					* ((Double.parseDouble(rate) + exRate) / 360 / 100)
					* Double.parseDouble(deadline);
			shouyi_add = 0.00;
			tv_prodetail_income_name.setText("");
			tv_prodetail_income_add.setText("");
			tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
			return;
		}
		if (shouyi < 0.01) {
			tv_prodetail_income.setText("0.00");
		} else {
			tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
		}
		if (shouyi_add < 0.01) {
			tv_prodetail_income_add.setText("+0.00");
		} else {
			tv_prodetail_income_add.setText("+"+ stringCut.getNumKbs(shouyi_add));
		}
//		shouyi =
////				(Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
//				Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
////				* (multiple - 1))
//				* ((Double.parseDouble(rate) * multiple + raisedRate + exRate) / 360 / 100)
//				* Double.parseDouble(deadline);
	}

	/**
	 * 优惠券筛选
	 *
	 */
	private List<ConponsBean> lscb;
	private void getConponsList() {
		OkHttpUtils.post()
				.url(UrlConfig.CONPONS_CHOSE)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("pid",pid)
				.addParams("amount", et_prodetail_money.getText().toString())
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build().execute(new StringCallback() {

			@Override
			public void onResponse(String result) {
				LogUtils.i("--->选择优惠券：result=" + result);
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONArray objrows = objmap.getJSONArray("list");
					lscb = JSON.parseArray(objrows.toJSONString(),ConponsBean.class);
					showPopupWindowConpons(lscb);
//					lv_act_coupons.setAdapter(new ConponsUnuseAdapter(Act_Detail_Pro.this,lscb,1,null));
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
					finish();
					new show_Dialog_IsLogin(Act_Detail_Pro_New.this).show_Is_Login();
				}
				else{
					ToastMaker.showShortToast("服务器异常");
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				ToastMaker.showShortToast("请检查网络");
			}
		});

	}
	private View layout;
	private PopupWindow popupWindow;
	public void showPopupWindowConpons(final List<ConponsBean> lsrb) {
		layout = LayoutInflater.from(this).inflate(R.layout.pop_choseconpons, null);
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		ImageView iv_close = (ImageView) (layout).findViewById(R.id.iv_close);
		TextView tv_title = (TextView) (layout).findViewById(R.id.tv_title);
		ListView lv_act_coupons = (ListView) (layout).findViewById(R.id.lv_act_coupons);
		if(lsrb.get(0).getStatus()==3){
			tv_title.setTextColor(Color.parseColor("#ec5c59"));
			tv_title.setText("购买金额/项目期限未满足红包卡券使用条件");
		}else{
			tv_title.setText("按照当前投资收益由高到低排列");
		}
		lv_act_coupons.setAdapter(new ConponsUnuseAdapter(Act_Detail_Pro_New.this,lsrb,fid,null));
		lv_act_coupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(lscb.get(position).getStatus()==3){

				}else{
					if (lscb.get(position).getType()==1) {//使用返现券
						raisedRate = 0;
						multiple = 1;
						fid = lscb.get(position).getId();
						enableAmount = lscb.get(position).getEnableAmount();
						amount = lscb.get(position).getAmount();
						nothing = "amount";
						tv_prodetail_conpons.setText("使用"+ stringCut.getNumKbs(amount)+"元返现红包");
						if (et_prodetail_money.getText().toString().trim().length() > 0) {
							Money_Get("amount");
							tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
						}
					} else if (lscb.get(position).getType()==2) { // 2=加息券
						amount = 0;
						multiple = 1;
						fid = lscb.get(position).getId();
						enableAmount = lscb.get(position).getEnableAmount();
						raisedRate = lscb.get(position).getRaisedRates();
						tv_prodetail_conpons.setText("使用"+raisedRate+"%加息券");
						nothing = "raisedRate" ;
						if (et_prodetail_money.getText().toString().trim().length() > 0) {
							Money_Get("raisedRate");
							tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
						}
					} else if (lscb.get(position).getType()==3) {

					}
					else if (lscb.get(position).getType()==4) { // 翻倍
						raisedRate = 0;
						amount = 0;
						fid = lscb.get(position).getId();
						enableAmount = lscb.get(position).getEnableAmount();
						multiple = lscb.get(position).getMultiple();
						tv_prodetail_conpons.setText("使用"+multiple+"倍翻倍券");
						nothing = "multiple" ;
						Double_conpons();
					}
					popupWindow.dismiss();
				}
			}
		});
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
		iv_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backgroundAlpha(1f);
				popupWindow.dismiss();
			}
		});
//		popupWindow.showAsDropDown(layout);
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		backgroundAlpha(0.5f);
	}
	public void showPopupWindowGoPay() {
		layout = LayoutInflater.from(this).inflate(R.layout.pop_lessbalance, null);
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		ImageView iv_close = (ImageView) (layout).findViewById(R.id.iv_close);
		TextView tv_lessbalance_money = (TextView) (layout).findViewById(R.id.tv_lessbalance_money);
		Button bt_lessbalance_ok = (Button) (layout).findViewById(R.id.bt_lessbalance_ok);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		tv_lessbalance_money.setText("账户余额支付（账户余额："+balance+")");
		if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(balance)) {
			bt_lessbalance_ok.setText("余额不足,立即充值");
		}else{
			bt_lessbalance_ok.setText("去支付");
		}
		bt_lessbalance_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(balance)) {
					startActivityForResult(new Intent(Act_Detail_Pro_New.this,CashInAct.class),4);
				}else{
					Invest_Begin() ;
				}
			}
		});
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
		iv_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				backgroundAlpha(1f);
				popupWindow.dismiss();
			}
		});
//		popupWindow.showAsDropDown(layout);
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		backgroundAlpha(0.5f);
	}
	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = LocalApplication.getInstance().getMainActivity().getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		LocalApplication.getInstance().getMainActivity().getWindow().setAttributes(lp);
	}
	/*
	选择优惠券
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 4 && resultCode == 4){
			setResult(4);
			finish();
		}
		if(Invest_Begin_bl){
			if(null == data){
				return ;
			}else{
//				Invest_Begin() ;
			}
		}
	}
	private void Double_conpons() {
		// TODO Auto-generated method stub
		if (et_prodetail_money.getText().toString().trim().length() > 0) {
			Money_Get("multiple");
			tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
		}
	}

	@Override
	public void openDetails(boolean smooth) {
//		mSlideDetailsLayout.smoothOpen(smooth);
		mSlideDetailsLayout.isMove = smooth;
	}

	@Override
	public void closeDetails(boolean smooth) {
		mSlideDetailsLayout.smoothClose(smooth);
	}

}
