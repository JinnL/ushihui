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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
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
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils;
import com.mcz.xhj.yz.dr_adapter.ConponsUnuseAdapter;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_introduce;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_picture;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_record;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.HorizontalProgressBarWithNumber;
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
* 描述：普通标（优选金服）的详情页
* */

public class Act_Detail_Pro extends BaseActivity implements ISlideCallback, View.OnClickListener {
	@BindView(R.id.slidedetails_pro)
	SlideDetailsLayout mSlideDetailsLayout;
	@BindView(R.id.vp_pro)
	ViewPager vp_pro;
	@BindView(R.id.pro_indicator)
	TabPageIndicator pro_indicator;
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
	HorizontalProgressBarWithNumber progressbar_pert;

	@BindView(R.id.tv_prodetail_changeone)
	TextView tv_prodetail_changeone;
	@BindView(R.id.tv_tip_money)
	TextView tv_tip_money;
	@BindView(R.id.tv_prodetail_changetwo)
	TextView tv_prodetail_changetwo;
	@BindView(R.id.tv_prodetail_changetwo_gift)
	TextView tv_prodetail_changetwo_gift;
	@BindView(R.id.tv_prodetail_num)
	TextView tv_prodetail_num;
	@BindView(R.id.tv_invest_type)
	TextView tv_invest_type;
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
	@BindView(R.id.ll_money)
	LinearLayout ll_money;
	@BindView(R.id.tv_prodetail_num_two)
	@Nullable
	ImageView tv_prodetail_num_two;
	@BindView(R.id.rl_pro)
    RelativeLayout rl_pro;
	@BindView(R.id.ll_limit)
    LinearLayout ll_limit;
	@BindView(R.id.tv_wan_shouyi)
	TextView tv_wan_shouyi;
	@BindView(R.id.check_tiyan)
	CheckBox check_tiyan;
	@BindView(R.id.ll_tiyanjin)
	LinearLayout ll_tiyanjin;

	private String[] tab;//标题
	private TabFragPA tabPA;
	private Fragment frag;
	private Fragment frag1;
	private Fragment frag2;
	private String ptype = "2";
	private String pid,uid,atid,investId;
	private SharedPreferences preferences;
	private String intent_startTime;
	private String intent_endTime;
	Long lastClick = 0l;//防重复点击计时
    public static boolean hasNavigationBar = false;

	private TradePwdPopUtils pop;
	private JSONArray couponList;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_detail_pro;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDate();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

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

	public int getNavigationBarHeight() {

		boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if (!hasMenuKey && !hasBackKey) {
			Resources resources = getResources();
			int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
			//获取NavigationBar的高度
			int height = resources.getDimensionPixelSize(resourceId);
			return height;
		}
		else{
			return 0;
		}
	}
	@Override
	protected void initParams() {
		if(checkDeviceHasNavigationBar(Act_Detail_Pro.this)){
			rl_pro.setPadding(0,0,0,140);
		}
		et_prodetail_money.setHintTextColor(Color.parseColor("#aaaaaa"));
//		getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, 25);
//        if (checkDeviceHasNavigationBar(Act_Detail_Pro.this)) {
//            rl_pro.setFitsSystemWindows(true);
////			bt_prodetail_ok.setFitsSystemWindows(true);
////			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
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
		tab = new String[] { "项目信息", "资金保障", "投资记录"};
		// 给viewpager设置适配器
		tabPA = new TabFragPA(getSupportFragmentManager());//继承fragmentactivity
		vp_pro.setAdapter(tabPA);
		//viewpagerindictor和viewpager关联
		pro_indicator.setViewPager(vp_pro);
		vp_pro.setOffscreenPageLimit(2);
//		FragmentManager fm = getSupportFragmentManager();
//		fm.beginTransaction().replace(R.id.slidedetails_front, new Frag_Detail_Pro_Top()).commit();
		preferences = LocalApplication.getInstance().sharereferences;
		Intent intent = getIntent();
		Uri uri = intent.getData();
		LogUtils.i("--->Act_Detail_Pro：Uri："+uri);
		if (uri != null) {
			pid = uri.getQueryParameter("pid");
			ptype = uri.getQueryParameter("ptype");
			atid = uri.getQueryParameter("atid");
			intent_fid = uri.getQueryParameter("fid");
			intent_amount = uri.getQueryParameter("amount");
			intent_enableAmount = uri.getQueryParameter("enableAmount");
			LogUtils.i("--->Act_Detail_Pro：pid="+pid+" ,ptype="+ptype+" ,atid="+atid+" ,intent_fid="+intent_fid+" ,intent_amount="+intent_amount+" intent_enableAmount="+intent_enableAmount);
		}else{
			pid = getIntent().getStringExtra("pid");
			ptype = getIntent().getStringExtra("ptype");
			atid = getIntent().getStringExtra("atid");
			intent_fid = getIntent().getStringExtra("fid");
			intent_amount = getIntent().getStringExtra("amount");
			intent_enableAmount = getIntent().getStringExtra("enableAmount");
			intent_startTime = getIntent().getStringExtra("startTime");
			intent_endTime = getIntent().getStringExtra("endTime");
			LogUtils.i("--->Act_Detail_Pro：pid="+pid+" ,ptype="+ptype+" ,atid="+atid+" ,intent_fid="+intent_fid+" ,intent_amount="+intent_amount+" intent_enableAmount="+intent_enableAmount);
		}
		Watcher watcher = new Watcher();
//		et_prodetail_money.addTextChangedListener(watcher);
		ll_prodetail_conpons.setOnClickListener(this);
		title_righttextview.setOnClickListener(this);
		title_righttextview_add.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
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
				startActivity(new Intent(Act_Detail_Pro.this, Winner_Act.class).putExtra("pid", pid));
				break;
			case R.id.ll_prodetail_changeone://去iPhone7标详情
				if(linkUrl!=null&&!linkUrl.equalsIgnoreCase("")){
					startActivity(new Intent(Act_Detail_Pro.this,WebViewActivity.class)
							.putExtra("URL", linkUrl)
							.putExtra("TITLE", "活动详情")
							.putExtra("BANNER", "banner")
					);
				}
				break;
			case R.id.ll_prodetail_conpons:
				MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+23+"");
				if (System.currentTimeMillis() - lastClick <= 100) {
					//ToastMaker.showShortToast("点那么快干什么");
					return;
				}
				lastClick = System.currentTimeMillis();
				if(preferences.getString("uid", "").equalsIgnoreCase("")&&pid!=null){
					startActivity(new Intent(Act_Detail_Pro.this, NewLoginActivity.class));
				}else{
					if(!et_prodetail_money.getText().toString().equalsIgnoreCase("")){
						getConponsList();
					}else{
						ToastMaker.showLongToast("请输入金额");
					}
				}
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
				startActivity(new Intent(Act_Detail_Pro.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
				break;
			case R.id.title_righttextview_add:
				startActivity(new Intent(Act_Detail_Pro.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
				break;
			case R.id.bt_prodetail_ok:
				MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+22+"");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_prodetail_money.getWindowToken(),0);
//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				if (System.currentTimeMillis() - lastClick <= 100) {
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
					MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+37+"");
					startActivity(new Intent(Act_Detail_Pro.this, NewLoginActivity.class));
				}
				else if (et_prodetail_money.getText().length() <= 0) {
					ToastMaker.showShortToast("请输入投资金额");
				}
				else if (!"1".equals(preferences.getString("realVerify", ""))) {
					ToastMaker.showShortToast("还未认证 ，请认证");
					MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+38+"");
					startActivityForResult(new Intent(Act_Detail_Pro.this, FourPartAct.class)
							.putExtra("proName",fullname)//产品名称
							.putExtra("proDeadline",deadline)//投资期限
							.putExtra("proRate",rate_h)//预期年化收益
							.putExtra("specialRate", exRate+"")//活动利率
							.putExtra("proAmount",et_prodetail_money.getText().toString()),4//投资金额
					);
				}
				/*else if (enableAmount > Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString()))) {
					ToastMaker.showShortToast("不满足红包最低使用金额");
				}*/
				else if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) < Double.parseDouble(increasAmount)) {
					ToastMaker.showShortToast("投资金额最少为"+ stringCut.getNumKbs(Double.parseDouble(increasAmount)));
				}
				else if ((Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString()))) % Double.parseDouble(increasAmount) != 0) {
					ToastMaker.showShortToast("投资金额需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
				}
				else  if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(surplusAmount)) {
					ToastMaker.showShortToast("投资金额不能大于产品可投金额");
				}
//				else if (Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString())) > Double.parseDouble(balance)) {
//					ToastMaker.showShortToast("账户可用余额不足，请先充值！");
//					startActivity(new Intent(Act_Detail_Pro.this,CashInAct.class));
//				}
				else{
					Invest_Begin_bl = true ;
//					Invest_Begin() ;
					//是否弹出有优惠券未使用的弹窗
					LogUtils.i("否弹出有优惠券未使用的弹窗");

					if(!isChooseCoupon && mlslb2.size()>0){
						Dialog dialog = showAlertDialog("温馨提示", "你有"+mlslb2.size()+"张优惠券未使用，是否现在使用？", new String[]{"暂不使用", "立即使用"}, true, true, "coupon");
					}else{

						showPopupWindowGoPay();
					}
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
				startActivityForResult(new Intent(Act_Detail_Pro.this, TransactionPswAct.class),1);
			}
		}
		if(((String) tag).equalsIgnoreCase("three_time")){
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Act_Detail_Pro.this, TransactionPswAct.class),1);
			}
		}
		if(((String) tag).equalsIgnoreCase("coupon")){
			if (position == 0) {
				showPopupWindowGoPay();
			}
			if (position == 1) {
				getConponsList();
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
							MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+24+"");
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
							new show_Dialog_IsLogin(Act_Detail_Pro.this).show_Is_Login() ;
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
								pop.showPopWindow(Act_Detail_Pro.this, Act_Detail_Pro.this, slidedetails_front);
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
										startActivityForResult(new Intent(Act_Detail_Pro.this, TransactionPswAct.class),1);
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
								MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+24+"");
								hongbao_check = false ;
								MobclickAgent.onEvent(Act_Detail_Pro.this, "100023");
								pop = new TradePwdPopUtils();
								pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

									@Override
									public void callBaceTradePwd(String pwd) {
										firstPwd = pwd;
										MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+24+"");
										product_Invest();
									}
								});
								pop.showPopWindow(Act_Detail_Pro.this, Act_Detail_Pro.this, slidedetails_front);
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
										startActivityForResult(new Intent(Act_Detail_Pro.this, TransactionPswAct.class),1);
									}
								});
							}

						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_Detail_Pro.this)
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
				.addParams("amount",stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
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
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
							JSONObject map = obj.getJSONObject("map");
							if (map!=null) {
								luckcode = map.getString("luckCodes");
								luckcodesize = map.getString("luckCodeCount");
								investId = map.getString("investId");
								jumpURLActivity = map.getString("activityUrl");
								if (!map.getBoolean("isRepeats")) {
									is_Activity(map.getString("expireDate")) ;
								} else {
									Intent in = new Intent();
									in.putExtra("pid", pid);
									in.putExtra("tv_name", fullname);
									in.putExtra("tv_money", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()));
									in.putExtra("tv_day", deadline);
									in.putExtra("tv_rate", rate);
									if("amount".equalsIgnoreCase(nothing)){
										in.putExtra("tv_earn", shouyi+"");
									}else {
										in.putExtra("tv_earn", shouyi+shouyi_add+"");
									}
									in.putExtra("tv_red", resultNum+"");
									in.putExtra("nothing", nothing);
									in.putExtra("investId", investId);
									in.putExtra("tv_start", tv_establish);
									in.putExtra("isPicture", map.getString("activityURL"));
									in.putExtra("jumpURLActivity", map.getString("activityUrl"));
									in.putExtra("specialRate", exRate+"");
									in.putExtra("jumpURL", map.getString("jumpURL"));
									in.putExtra("tv_end", tv_expireDate);
									in.putExtra("luckcode", luckcode);
									in.putExtra("luckcodesize", luckcodesize);
									in.putExtra("endTime", map.getString("expireDate"));
									in.setClass(Act_Detail_Pro.this,Act_Pro_End.class);
									startActivityForResult(in,4);
//									startActivityForResult(new Intent(Act_Detail_Pro.this,Act_Pro_End.class)
//											.putExtra("pid", pid)
//											.putExtra("tv_name", fullname)
//											.putExtra("tv_money", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
//											.putExtra("tv_day", deadline)
//											.putExtra("tv_rate", rate)
////											.putExtra("tv_earn", tv_prodetail_income.getText().toString().trim())
//											.putExtra("tv_earn", shouyi+shouyi_add+"")
//											.putExtra("tv_red", resultNum+"")
//											.putExtra("nothing", nothing)
//											.putExtra("investId", investId)
//											.putExtra("tv_start", tv_establish)
//											.putExtra("isPicture", map.getString("activityURL"))
//											.putExtra("jumpURLActivity", map.getString("activityUrl"))
//											.putExtra("specialRate", exRate+"")//活动利率
//											.putExtra("jumpURL", map.getString("jumpURL"))
//											.putExtra("tv_end", tv_expireDate)
//											.putExtra("luckcode", luckcode)
//											.putExtra("luckcodesize", luckcodesize)
//											.putExtra("endTime", map.getString("expireDate")),4
//									);
//									finish();
								}
							}
							else {
								is_Activity(map.getString("expireDate")) ;
							}
						}
						else if ("2001".equals(obj.getString("errorCode"))) {
							MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+20+"");
							pop.tv_tips.setText("连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码");
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
							MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+20+"");
							pop.tv_tips.setText("密码输入错误，请重新输入");
//							ToastMaker.showShortToast("密码输入错误，请重新输入。");
						}
						else{
							MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+20+"");
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
							if ("XTWH".equals(obj.getString("errorCode"))) {
								startActivity(new Intent(Act_Detail_Pro.this,WebViewActivity.class)
										.putExtra("URL", UrlConfig.WEIHU)
										.putExtra("TITLE", "系统维护"));
								return;
							}
							else if ("1002".equals(obj.getString("errorCode"))) {
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
								new show_Dialog_IsLogin(Act_Detail_Pro.this).show_Is_Login();
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
	 * 是否有活动
	 */
	private void is_Activity(String endDate) {
		Intent in = new Intent();
		in.putExtra("pid", pid);
		in.putExtra("tv_name", fullname);
		in.putExtra("tv_money", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()));
		in.putExtra("tv_day", deadline);
		in.putExtra("tv_rate", rate);
		if("amount".equalsIgnoreCase(nothing)){
			in.putExtra("tv_earn", shouyi+"");
		}else {
			in.putExtra("tv_earn", shouyi+shouyi_add+"");
		}
		in.putExtra("tv_red", resultNum+"");
		in.putExtra("nothing", nothing);
		in.putExtra("investId", investId);
		in.putExtra("tv_start", tv_establish);
		in.putExtra("jumpURLActivity", jumpURLActivity);
		in.putExtra("specialRate", exRate+"");
		in.putExtra("tv_end", tv_expireDate);
		in.putExtra("luckcode", luckcode);
		in.putExtra("luckcodesize", luckcodesize);
		in.putExtra("endTime", endDate);
		in.setClass(Act_Detail_Pro.this,Act_Pro_End.class);
		startActivityForResult(in,4);

//		startActivityForResult(new Intent(Act_Detail_Pro.this,Act_Pro_End.class)
//				.putExtra("pid", pid)
//				.putExtra("tv_name", fullname)
//				.putExtra("tv_money", stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
//				.putExtra("tv_day", deadline)
//				.putExtra("tv_rate", rate)
//				.putExtra("tv_earn", shouyi+shouyi_add+"")
////				.putExtra("tv_earn", tv_prodetail_income.getText().toString().trim())
//				.putExtra("tv_red", resultNum+"")
//				.putExtra("nothing", nothing)
//				.putExtra("tv_start", tv_establish)
//				.putExtra("investId", investId)
//				.putExtra("jumpURLActivity", jumpURLActivity)
//				.putExtra("specialRate", exRate+"")//活动利率
//				.putExtra("tv_end", tv_expireDate)
//				.putExtra("luckcode", luckcode)
//				.putExtra("luckcodesize", luckcodesize)
//				.putExtra("endTime", endDate),4
//		);
//		ToastMaker.showShortToast("投资成功");
//		finish();

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
			// TODO Auto-generated method stub
			if(s.length()>0){
				if(Double.parseDouble(s.toString())>=5000){
					check_tiyan.setChecked(true);
					check_tiyan.setClickable(false);
				}else{
					check_tiyan.setChecked(false);
					check_tiyan.setClickable(true);
				}
				//如果投资金额不满足红包最低，需要去掉“使用XX红包”字样
				if(Double.parseDouble(s.toString())<enableAmount){
					tv_prodetail_income_add.setText("");
					tv_prodetail_income_name.setText("");
					tv_prodetail_conpons.setText("优惠券");
					tv_prodetail_num.setText(couponList.size()+"个可用");
					isChooseCoupon = false;
					fid = "";
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
	private String repayType = "";//还款方式
	private String endTime;//还款方式
	private String principleH5;//产品原理图
	public Bundle bundle;

	private String surplusAmount; // 剩余金额
	private String deadline;
	private String endDate;
	private String startTime;
	private String rate_h, rate = null;
	private String fid = ""; // 可用红包id
	private SpannableString ss;
	private ArrayList<ConponsBean> mlslb2 = new ArrayList<>(); // 红包列表
	String fullname  ; //产品名称
	private boolean isOldUser = false;//是否是老用户
	private double specialRate1 = 0;//双旦活动利率
	private boolean isShowLabel = false;//是否提示激活体验金
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
						LogUtils.i("--->产品详情product_detail："+response);
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
							if(map.getString("linkURL")!=null){
								if(!map.getString("linkURL").equalsIgnoreCase("")){
									linkUrl = map.getString("linkURL");
								}
							}

							if(preferences.getString("uid", "").equalsIgnoreCase("")){
								ll_tiyanjin.setVisibility(View.VISIBLE);
							} else {
								if(isShowLabel){
									ll_tiyanjin.setVisibility(View.VISIBLE);
								}else{
									ll_tiyanjin.setVisibility(View.GONE);
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
							if ("1".equals(info.getString("isInterest"))) {
								tv_prodetail_interest.setVisibility(View.VISIBLE);
							}
							if ("1".equals(info.getString("isCash"))) {
								tv_prodetail_red.setVisibility(View.VISIBLE);
							}
							if ("1".equals(info.getString("isDouble"))) {
								tv_prodetail_double.setVisibility(View.VISIBLE);
							}
							if("1".equals(info.getString("isPrize"))){
								tv_prodetail_changetwo_gift.setText("已开奖");
								tv_prodetail_num_two.setVisibility(View.VISIBLE);
							}else if("2".equals(info.getString("isPrize"))){
								tv_prodetail_changetwo_gift.setText("未开奖");
								tv_prodetail_changetwo_gift.setClickable(false);
							}

							fullname  = info.getString("fullName");
							title_centertextview.setText(fullname);
							title_centertextview_add.setText(fullname);
							proAnimator(Integer.valueOf(stringCut.pertCut(info.getString("pert"))).intValue(),progressbar_pert) ;
							rate = info.getString("rate");
							deadline = info.getString("deadline");
							maxAmount = info.getString("maxAmount");
							increasAmount = info.getString("increasAmount");
							surplusAmount = info.getString("surplusAmount");
							leastaAmount = info.getString("leastaAmount");//起投
							principleH5 = info.getString("principleH5");//产品原理图
							rate_h = stringCut.getNumKbs(Double.parseDouble(rate));
							tv__prodetail_balance.setText(stringCut.getNumKbs(Double.parseDouble(surplusAmount)));
							tv_prodetail_rate.setText(rate_h);
							tv_prodetail_deadline.setText(deadline + "天 ");
							if (et_prodetail_money.getText().length() <= 0) {
								et_prodetail_money.setHint(stringCut.getNumKbs(Double.parseDouble(leastaAmount))+"元起购买，" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "元递增");
							}
							//万元预计收益
							Double rate_expect = Double.valueOf(Act_Detail_Pro.this.rate)/100;
							Double exRate_expect = Double.valueOf(Act_Detail_Pro.this.exRate)/100;
							Double day = Double.valueOf(Act_Detail_Pro.this.deadline);
							double shouyi = 10000 * (rate_expect + exRate_expect) * day / 360;
							BigDecimal bg = new BigDecimal(shouyi);
							shouyi = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							tv_wan_shouyi.setText(shouyi+"");
							/**
							 * 送豪礼专用
							 *
							 */
							if(prize!=null){
								ll_prodetail_conpons.setVisibility(View.GONE);
								ll_prodetail_changeone.setVisibility(View.VISIBLE);
								ll_prodetail_changetwo.setVisibility(View.VISIBLE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								tv_prodetail_changetwo.setText("礼品详情");
								iv_prodetail_changetwo.setImageResource(R.mipmap.pro_detail_gift_icon);
								tv_prodetail_changetwo_gift.setText("");
								isNot = prize.getInteger("isNot");  //是否抢完 0 = 未强完,1抢完
								ppid = prize.getString("id");//奖品id预约的时候需要
								prizeAmount = prize.getString("amount");
								tv_gift.setVisibility(View.VISIBLE);
								tv_gift.setText("投资即送"+prize.getString("name"));
								et_prodetail_money.setText(stringCut.getNumKbs(Double.parseDouble(prizeAmount)));
								et_prodetail_money.setFocusable(false);
								et_prodetail_money.setFocusableInTouchMode(false);
								if(isNot == 0){
									bt_prodetail_ok.setText("投资免费领");
								}else{
									bt_prodetail_ok.setText("标的余额不足，请预约");
								}
								tv_prodetail_income.setText(stringCut.getDoubleKb(Double.parseDouble(stringCut.douHao_Cut(et_prodetail_money.getText().toString().trim()))
										* ((Double.parseDouble(Act_Detail_Pro.this.rate) + exRate) / 360 / 100)
										* Double.parseDouble(deadline))+"");
							}else{
								ll_prodetail_conpons.setVisibility(View.VISIBLE);
								/**
								 *  送iPhone7专用
								 */
								if(atid!=null){
									ll_prodetail_conpons.setVisibility(View.GONE);
									ll_prodetail_changeone.setVisibility(View.VISIBLE);
									ll_prodetail_changetwo.setVisibility(View.VISIBLE);
									tv_prodetail_changetwo.setText("中奖者信息");
									tv_gift.setVisibility(View.VISIBLE);
									tv_gift.setText("投资白送iPhone7");
								}else {
									tv_gift.setVisibility(View.GONE);
									ll_prodetail_conpons.setVisibility(View.VISIBLE);
								}
								Watcher watcher = new Watcher();
								et_prodetail_money.addTextChangedListener(watcher);
							}


							couponList = map.getJSONArray("couponList");
							if (null != couponList) {
								if (couponList.size() <= 0) {
									ll_prodetail_conpons.setFocusable(false);
									ll_prodetail_conpons.setEnabled(false);
									tv_prodetail_num.setText("0个可用");
								} else {
									tv_prodetail_num.setText(couponList.size()+"个可用");
									mlslb2 = (ArrayList<ConponsBean>) JSON.parseArray(couponList.toJSONString(),ConponsBean.class);
									if(intent_fid!=null&&!intent_fid.equalsIgnoreCase("")){//你用返现券
										if(isFirstGet){
											isFirstGet = false;
											resultNum = 1 ;
											raisedRate = 0;
											multiple = 1;
											fid = intent_fid;
											enableAmount = Double.parseDouble(intent_enableAmount);
											amount = Double.parseDouble(intent_amount);
											tv_prodetail_conpons.setText("使用"+stringCut.getNumKbs(amount)+"元返现红包");
											nothing = stringCut.getNumKbs(amount) ;
											if (et_prodetail_money.getText().toString().trim().length() > 0) {
												Money_Get("amount");
												tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
											}
										}
									}else{//使用加息券
										for (int i = 0; i < mlslb2.size(); i++) {
											if(mlslb2.get(i).getPid()!=null){
												if(isFirstGet){
													if(mlslb2.get(i).getPid().equalsIgnoreCase(pid)){
														isFirstGet = false;
														resultNum = 2 ;
														raisedRate = 0;
														multiple = 1;
														fid = mlslb2.get(i).getId();
														enableAmount = mlslb2.get(i).getEnableAmount();
														raisedRate = mlslb2.get(i).getRaisedRates();
//														double exRates = raisedRate + exRate;
														tv_prodetail_conpons.setText("使用" + raisedRate + "%加息券");
														nothing = raisedRate+"" ;
														if (et_prodetail_money.getText().toString().trim().length() > 0) {
															Money_Get("raisedRate");
															tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
														}
													}
												}
											}
										}
									}
								}
							}
							if(info.getInteger("repayType")==1){
								repayType = "到期还本付息";
							}else if(info.getInteger("repayType")==2){
								repayType = "按月付息到期还本";
							}
							tv_invest_type.setText(repayType);

							if ("6".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								ll_prodetail_conpons.setVisibility(View.GONE);
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setText("抢光了 ");
								bt_prodetail_ok.setBackgroundResource(R.drawable.bg_corner_gray);
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								tv_tip_money.setTextColor(Color.parseColor("#a0a0a0"));
								ll_money.setBackgroundResource(R.drawable.bg_corner_blackline_gray);
								et_prodetail_money.setFocusableInTouchMode(false);
								et_prodetail_money.setFocusable(false);
							} else if ("8".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								bt_prodetail_ok.setText("待还款");
								bt_prodetail_ok.setBackgroundResource(R.drawable.bg_corner_gray);
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								tv_tip_money.setTextColor(Color.parseColor("#a0a0a0"));
								ll_money.setBackgroundResource(R.drawable.bg_corner_blackline_gray);
								et_prodetail_money.setFocusableInTouchMode(false);
								et_prodetail_money.setFocusable(false);
							} else if ("9".equals(info.getString("status"))) {
//								startTime = info.getString("endDate");//计息日期
								bt_prodetail_ok.setText("已还款");
								bt_prodetail_ok.setBackgroundResource(R.drawable.bg_corner_gray);
								ll_shouyi.setVisibility(View.GONE);
								ll_prodetail_conpons.setVisibility(View.GONE);
								bt_prodetail_ok.setClickable(false);
								et_prodetail_money.setHint("项目额度已募集结束");
								ll_limit.setVisibility(View.GONE);
								tv_tip_money.setTextColor(Color.parseColor("#a0a0a0"));
								ll_money.setBackgroundResource(R.drawable.bg_corner_blackline_gray);
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
							}

							// 详情信息
							startTime = map.getString("nowTime");//计息日期
							windMeasure = info.getString("windMeasure");
							introduce = info.getString("introduce");
							repaySource = info.getString("repaySource");
							borrower = info.getString("borrower");
							endTime = info.getString("expireDate");//回款日
							//成立日期,到期日期
//							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;
							tv_expireDate= info.getString("expireDate");//还款日期
//							tv_establish = info.getString("establish") ;//成立日期
							endDate = info.getString("endDate");//募集结束日期
							bundle = new Bundle();
							bundle.putString("pid",pid);
							if(TextUtils.isEmpty(ptype)){
								ptype = "2";
							}
							bundle.putString("ptype",ptype);
							bundle.putString("repayType",repayType);
							bundle.putString("windMeasure",windMeasure);
							bundle.putString("introduce",introduce);
							bundle.putString("repaySource",repaySource);
							bundle.putString("borrower",borrower);
							bundle.putString("ingTime",endDate);
							bundle.putString("principleH5",principleH5);
							if(intent_startTime!=null&&intent_endTime!=null){
								bundle.putString("startTime",intent_startTime);
								bundle.putString("endTime",intent_endTime);
							}else{
								bundle.putString("startTime",startTime);
								bundle.putString("endTime",endTime);
							}
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("产品已下架");
						}
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_Detail_Pro.this)
									.show_Is_Login();
						}
						else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					/*进度条动画*/
					private void proAnimator(final int pert,final HorizontalProgressBarWithNumber progressbar) {
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
					/*private void proAnimator(final int pert) {
						ValueAnimator animator = ValueAnimator.ofInt(0, pert);
						animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator animation) {
								int progress = (int) animation.getAnimatedValue();
								progressbar_pert.setSecondaryProgress(progress);
							}
						});
						animator.setDuration(2000);
						animator.start();
					}*/
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
			tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
			return;
		}
		if (shouyi < 0.01) {
			tv_prodetail_income.setText("0.00");
		} else {
			tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
		}
		if (shouyi_add < 0.01) {
			tv_prodetail_income_add.setText("+0");
		} else {
			tv_prodetail_income_add.setText("+"+stringCut.getDoubleKb(shouyi_add));
		}

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
				LogUtils.i("--->投资详情页，选择优惠券：result="+result);
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONArray objrows = objmap.getJSONArray("list");
					lscb = JSON.parseArray(objrows.toJSONString(),ConponsBean.class);
					if(lscb == null){
						lscb = new ArrayList<>();
					}
					showPopupWindowConpons(lscb);
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
					finish();
					new show_Dialog_IsLogin(Act_Detail_Pro.this).show_Is_Login() ;
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
	private boolean isChooseCoupon = false;
	public void showPopupWindowConpons(final List<ConponsBean> lsrb) {
		layout = LayoutInflater.from(this).inflate(R.layout.pop_choseconpons, null);
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		ImageView iv_close = (ImageView) (layout).findViewById(R.id.iv_close);
		RelativeLayout rl_close = (RelativeLayout) layout.findViewById(R.id.rl_notUse_conponslist);
		TextView tv_title = (TextView) (layout).findViewById(R.id.tv_title);
		ListView lv_act_coupons = (ListView) (layout).findViewById(R.id.lv_act_coupons);
		ImageView iv_wenhao = (ImageView) (layout).findViewById(R.id.iv_wenhao);
		iv_wenhao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Act_Detail_Pro.this,WebViewActivity.class)
                            .putExtra("URL", UrlConfig.REMINDER)
                            .putExtra("TITLE", "优惠券温馨提示")
                            .putExtra("BANNER", "banner"));
			}
		});
		lv_act_coupons.setAdapter(new ConponsUnuseAdapter(Act_Detail_Pro.this,lsrb,fid,null));

		if(lscb.size() !=0 && lsrb.size() !=0){
			if(lsrb.get(0).getStatus()==3){
				tv_title.setTextColor(Color.parseColor("#ec5c59"));
				tv_title.setText("购买金额/项目期限未满足红包卡券使用条件");
			}else{
				tv_title.setText("按照当前投资收益由高到低排列");
			}
			lv_act_coupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if(lscb.get(position).getStatus()==3){

					}else{
						tv_prodetail_num.setText("");
						if (lscb.get(position).getType()==1) {//使用返现券
							isChooseCoupon = true;
							raisedRate = 0;
							multiple = 1;
							fid = lscb.get(position).getId();
							enableAmount = lscb.get(position).getEnableAmount();
							amount = lscb.get(position).getAmount();
							nothing = "amount";
							tv_prodetail_conpons.setText("使用"+stringCut.getNumKbs(amount)+"元返现红包");
							if (et_prodetail_money.getText().toString().trim().length() > 0) {
								Money_Get("amount");
								tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
							}
						} else if (lscb.get(position).getType()==2) { // 2=加息券
							isChooseCoupon = true;
							amount = 0;
							multiple = 1;
							fid = lscb.get(position).getId();
							enableAmount = lscb.get(position).getEnableAmount();
							raisedRate = lscb.get(position).getRaisedRates();
							tv_prodetail_conpons.setText("使用"+raisedRate+"%加息券");
							nothing = "raisedRate" ;
							if (et_prodetail_money.getText().toString().trim().length() > 0) {
								Money_Get("raisedRate");
								tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
							}
						} else if (lscb.get(position).getType()==3) {

						}
						else if (lscb.get(position).getType()==4) { // 翻倍
							isChooseCoupon = true;
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
		}

		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				backgroundAlpha(1f);
//			}
//		});
		rl_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				backgroundAlpha(1f);
				popupWindow.dismiss();
			}
		});
//		popupWindow.showAsDropDown(layout);
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//		backgroundAlpha(0.5f);
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
					MobclickAgent.onEvent(Act_Detail_Pro.this, UrlConfig.point+41+"");
					startActivityForResult(new Intent(Act_Detail_Pro.this,CashInAct.class).putExtra("money",stringCut.douHao_Cut(et_prodetail_money.getText().toString())),4);
				}else{
					Invest_Begin();
				}
			}
		});
//		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				backgroundAlpha(1f);
//			}
//		});
		iv_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				backgroundAlpha(1f);
				popupWindow.dismiss();
			}
		});
//		popupWindow.showAsDropDown(layout);
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//		backgroundAlpha(0.5f);
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
		// requestCode标示请求的标示 resultCode表示有数据
//		if (requestCode == 1 && resultCode == 1) {//使用返现券
//			resultNum = resultCode ;
//			raisedRate = 0;
//			multiple = 1;
//			fid = data.getStringExtra("id");
//			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
//			amount = data.getDoubleExtra("amount", 0.00);
//			nothing = "amount";
//			tv_prodetail_conpons.setText("使用"+stringCut.getNumKbs(amount)+"元返现红包");
//			if (et_prodetail_money.getText().toString().trim().length() > 0) {
//				Money_Get("amount");
//				tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
//			}
//		} else if (requestCode == 1 && resultCode == 2) { // 2=加息券
//			resultNum = resultCode ;
//			amount = 0;
//			multiple = 1;
//			fid = data.getStringExtra("id");
//			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
//			raisedRate = data.getDoubleExtra("raisedRate", 0.00);
//			tv_prodetail_conpons.setText("使用"+raisedRate+"%加息券");
//			nothing = "raisedRate" ;
//			if (et_prodetail_money.getText().toString().trim().length() > 0) {
//				Money_Get("raisedRate");
//				tv_prodetail_income.setText(stringCut.getNumKbs(shouyi));
//			}
//		} else if (requestCode == 1 && resultCode == 3) {
//
//		} else if (requestCode == 1 && resultCode == 4) { // 翻倍
//			resultNum = resultCode ;
//			raisedRate = 0;
//			amount = 0;
//			fid = data.getStringExtra("id");
//			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
//			multiple = data.getDoubleExtra("multiple", 0.00);
//			tv_prodetail_conpons.setText("使用"+multiple+"倍翻倍券");
//			nothing = "multiple" ;
//			Double_conpons();
//		}
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
			tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
		}
	}

	@Override
	public void openDetails(boolean smooth) {
		mSlideDetailsLayout.smoothOpen(smooth);
	}

	@Override
	public void closeDetails(boolean smooth) {
		mSlideDetailsLayout.smoothClose(smooth);
	}

	class TabFragPA extends FragmentPagerAdapter {

		public TabFragPA(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
				case 0://项目信息
					frag1 = frag1==null?new Frag_ProDetails_picture():frag1;
					frag1.setArguments(bundle);
					return frag1;
				case 1://资金保障
					frag = frag==null?new Frag_ProDetails_introduce():frag;
					frag.setArguments(bundle);
					return frag;
				case 2://投资记录
					frag2 = frag2==null?new Frag_ProDetails_record():frag2;
					frag2.setArguments(bundle);
					return frag2;
				default:
					return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tab[position % tab.length];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tab.length;
		}

	}
}
