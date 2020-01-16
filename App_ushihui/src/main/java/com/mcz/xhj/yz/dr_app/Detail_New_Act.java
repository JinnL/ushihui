package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils.CallBackTradePwd;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class Detail_New_Act extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.touzi_now)
	Button touzi_now;
	@BindView(R.id.tv_balance)
	TextView tv_balance; // 余额
	@BindView(R.id.hongbao_user)
	LinearLayout hongbao_user;
	@BindView(R.id.tv_name_detail)
	TextView tv_name_detail; // 产品名称
	@BindView(R.id.tv_rate)
	TextView tv_rate; // 进度数字
//	@BindView(R.id.guize)
//	private TextView guize; // 起息规则
	@BindView(R.id.tv_leastaAmount)
	TextView tv_leastaAmount; // 起投金额(元)
	@BindView(R.id.tv_maxAmount)
	TextView tv_maxAmount; // 起投金额(元)
	@BindView(R.id.et_invest_money)
	EditText et_invest_money; // 募集金额
	@BindView(R.id.check_all)
	CheckBox check_all; // 募集金额quanbu
	@BindView(R.id.tv_shouyi)
	TextView tv_shouyi; // 收益
	@BindView(R.id.tv_deadline)
	TextView tv_deadline; // 产品期限
	@BindView(R.id.tv_cashin)
	TextView tv_cashin; // 产品期限
	@BindView(R.id.tv_rate_jiaxi)
	TextView tv_rate_jiaxi; // 加息
	@BindView(R.id.iv_rate_jiaxi_h)
	ImageView iv_rate_jiaxi_h;
	@BindView(R.id.tv_hongbao_fanxian)
	TextView tv_hongbao_fanxian; // 红包
	@BindView(R.id.tv_chose_conpons)
	TextView tv_chose_conpons; // hongbao
//	@BindView(R.id.image_tiyanjin)
//	private ImageView image_tiyanjin;
	@BindView(R.id.image_sure)
	CheckBox image_sure;
	@BindView(R.id.tv_xieyi_pro_zhuanrang)
	TextView tv_xieyi_pro_zhuanrang; // jiaxi
	@BindView(R.id.lv_check)
	LinearLayout lv_check; // jiaxi
	@BindView(R.id.ptr_invest)
	PtrClassicFrameLayout ptr_invest; // jiaxi
	@BindView(R.id.lin_shouyi)
	LinearLayout lin_shouyi;
	@BindView(R.id.detail_new)
	LinearLayout detail_new;
	

	@BindView(R.id.image_hongbao_close)
	ImageView image_hongbao_close;

	private String pid, ptype;
	private SharedPreferences preferences;
	private SpannableString ss;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_detail_new;
		// return R.layout.act_detail_new_noname;
	}

	@Override
	protected void initParams() {
		title_righttextview.setVisibility(View.GONE);
		title_centertextview.setText("新手体验");
		title_rightimageview.setVisibility(View.GONE);
		title_leftimageview.setOnClickListener(this);
		check_all.setOnClickListener(this);
		touzi_now.setOnClickListener(this);
		tv_cashin.setOnClickListener(this);
		image_sure.setOnClickListener(this);
		tv_xieyi_pro_zhuanrang.setOnClickListener(this);
		hongbao_user.setOnClickListener(this);
//		image_hongbao_close.setOnClickListener(this);
		image_hongbao_close.setVisibility(View.GONE) ;
		preferences = LocalApplication.getInstance().sharereferences;
		Intent intent = getIntent();  
        Uri uri = intent.getData();  
        if (uri != null) {  
            pid = uri.getQueryParameter("product");
        }else{
        	pid = getIntent().getStringExtra("pid");
    		ptype = getIntent().getStringExtra("ptype");
        }
		// 手机号间隔
		Watcher watcher = new Watcher();
		et_invest_money.addTextChangedListener(watcher);
		// 交易密码

		ptr_invest.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				product_Details();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onEvent(Detail_New_Act.this, "100021");
		product_Details();
		memberSetting();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据

		if (requestCode == 1 && resultCode == 3) {
			is_back = true ;
			fid = data.getStringExtra("id");
			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
			hongbao = data.getDoubleExtra("amount", 0.00);
//			image_hongbao_close.setVisibility(View.VISIBLE);
			tv_hongbao_fanxian.setText("+" + stringCut.getNumKbs(hongbao));
//			SpannableString ss_borrower = new SpannableString("体验金: "+stringCut.getNumKbs(hongbao));;
//			ss_borrower.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bt_bg_orange)), 4, ss_borrower.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
//			tv_chose_conpons.setText(ss_borrower);
			tv_chose_conpons.setText("已使用体验金");
			if (et_invest_money.getText().toString().trim().length() > 0) {
				if (enableAmount > Double.parseDouble(stringCut
						.douHao_Cut(et_invest_money.getText().toString()))) {
					ToastMaker.showShortToast("投资金额已调整");
					et_invest_money.setText(stringCut.douHao_Cut(stringCut
							.getNumKbs(enableAmount)));
				}
				 Money_Get();
				 tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			} else {
				et_invest_money.setText(stringCut.douHao_Cut(stringCut
						.getNumKbs(enableAmount)));
				ToastMaker.showShortToast("投资金额已调整");
				Money_Get();
				 tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			}
		}
		else if(resultCode == 4){
			finish();
		}
		if(Invest_Begin_bl){
			if(null == data){
				return ;
			}else{
				Invest_Begin() ;
			}			}
	}

	private Boolean is_back =false ;
	private String maxAmount; // 产品投资限额
	private String leastaAmount; // 产品起投限额
	private String balance;
	private int int_last; // 100整数
	private String increasAmount; // 递增
	private String winMeasure; // 风控措施
	private String introduce;// 产品介绍
	private String deadline = null;
	private String endDate;
	private String rate = null;
	private Double hongbao, enableAmount;
	private ArrayList<ConponsBean> mlslb2; // 红包列表
	private String fid = ""; // 可用红包id
	private String fullname = null;
	private String rate_jiaxi = "0"; // 可用红包id
	private void product_Details() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL).addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						ptr_invest.refreshComplete();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							if (map.getBoolean("newHandInvested")) {
								no_checked();
							}
							balance = map.getString("balance");
							tv_balance.setText(stringCut.getNumKbs(Double.parseDouble(balance)));
							JSONObject info = map.getJSONObject("info");
							fullname = info.getString("fullName");
							rate = info.getString("rate");
							deadline = info.getString("deadline");
							leastaAmount = info.getString("leastaAmount");
							maxAmount = info.getString("maxAmount");
							increasAmount = info.getString("increasAmount");
							rate_jiaxi = info.getString("activityRate");
							tv_name_detail.setText(fullname);
							if(rate_jiaxi!=null){
								if(Double.parseDouble(rate_jiaxi)!=0){
									tv_rate_jiaxi.setVisibility(View.VISIBLE);
									iv_rate_jiaxi_h.setVisibility(View.VISIBLE);
									tv_rate_jiaxi.setText("+"+stringCut.getNumKbs(Double.parseDouble(rate_jiaxi))+"%");
								}else{
									rate_jiaxi="0";
								}
							}
							String rate_h = stringCut.getNumKbs(Double.parseDouble(rate)) + "%";
//							SpannableString ss = new SpannableString(rate_h);
//							ss.setSpan(new RelativeSizeSpan(0.6f), rate_h.length() - 1, rate_h.length(), TypedValue.COMPLEX_UNIT_PX);
							tv_rate.setText(rate_h);
							tv_deadline.setText("期限"+deadline + "天 ");
							tv_leastaAmount.setText("起投"+stringCut.getNumKbs(Double
									.parseDouble(info.getString("leastaAmount")))+"元");
//							tv_maxAmount.setText("限投"+stringCut.getNumKbs(Double
//									.parseDouble(info.getString("maxAmount")))+"元");
							et_invest_money.setHint("需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
							et_invest_money.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxAmount.length()) });
							JSONArray list = map.getJSONArray("couponList");
							if (list.size() <= 0) {
								hongbao_user.setFocusable(false);
								hongbao_user.setEnabled(false);
								// tv_chose_conpons.setText("无可用红包");
							} else {
								mlslb2 = (ArrayList<ConponsBean>) JSON.parseArray(list.toJSONString(), ConponsBean.class);
								if(!is_back){
//									getMaxRed() ;
								}
								
							}
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_New_Act.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					private void getMaxRed() {
							List<Double> maxBed = new ArrayList<>() ;
							for(int i = 0; i<mlslb2.size();i++){
								maxBed.add(mlslb2.get(i).getEnableAmount()) ;
							}
							for (int j = 0; j < maxBed.size(); j++) {
								if(maxBed.get(j).equals(Collections.max(maxBed))){
									fid = mlslb2.get(j).getId();
									enableAmount = mlslb2.get(j).getEnableAmount();
									hongbao = mlslb2.get(j).getAmount();
									tv_hongbao_fanxian.setText("+" + stringCut.getNumKbs(hongbao));
//									SpannableString ss_borrower = new SpannableString("体验金: "+stringCut.getNumKbs(hongbao));;
//									ss_borrower.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bt_bg_orange)), 4, ss_borrower.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
//									 tv_chose_conpons.setText(ss_borrower);
									tv_chose_conpons.setText("已使用体验金");
//									 image_hongbao_close.setVisibility(View.VISIBLE);
									
										
								if (et_invest_money.getText().toString().trim().length() > 0) {
									if (enableAmount > Double.parseDouble(stringCut
										.douHao_Cut(et_invest_money.getText().toString()))) {
										ToastMaker.showShortToast("投资金额已调整");
									      et_invest_money.setText(stringCut.douHao_Cut(stringCut
														.getNumKbs(enableAmount)));
									}
									Money_Get();
									 tv_shouyi.setText(stringCut.getNumKbs(shouyi));
								}else{
									et_invest_money.setText(stringCut.douHao_Cut(stringCut
											.getNumKbs(enableAmount)));
									ToastMaker.showShortToast("投资金额已调整"); 	
									Money_Get();
									 tv_shouyi.setText(stringCut.getNumKbs(shouyi));
									}
								 
								}
							}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ToastMaker.showShortToast("请检查网络");
						dismissDialog();
						ptr_invest.refreshComplete();
					}
				});
	}

	private void no_checked() {
		et_invest_money.setFocusable(false);
		et_invest_money.setEnabled(false);
		tv_cashin.setFocusable(false);
		tv_cashin.setEnabled(false);
		check_all.setFocusable(false);
		check_all.setEnabled(false);
		touzi_now.setFocusable(false);
		touzi_now.setEnabled(false);
		image_sure.setFocusable(false);
		image_sure.setEnabled(false);
		hongbao_user.setFocusable(false);
		hongbao_user.setEnabled(false);
		tv_chose_conpons.setText("无可用红包");
		tv_hongbao_fanxian.setText("");
		tv_hongbao_fanxian.setVisibility(View.GONE);
		tv_rate.setTextColor(0xffA0A0A0);
		tv_shouyi.setTextColor(0xffA0A0A0);
		touzi_now.setBackgroundColor(0xffA0A0A0);
		tv_xieyi_pro_zhuanrang.setTextColor(0xffA0A0A0);
		image_sure.setBackgroundResource(R.mipmap.icon_check_g);
//		image_tiyanjin.setImageResource(R.drawable.label_tiyan);
		lv_check.setVisibility(View.GONE);
		lin_shouyi.setVisibility(View.GONE);
		touzi_now.setText("已完成体验");
	}

	Long lastClick = 0l;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_xieyi_pro_zhuanrang:
			startActivity(new Intent(Detail_New_Act.this, WebViewActivity.class)
					.putExtra("URL", UrlConfig.TOUZI).putExtra("TITLE",
							"权益转让及受让协议"));
			break;
		// case R.id.tv_xieyi_pro_zhuanrang:
		// startActivity(new Intent(Detail_New_Act.this,WebViewActivity.class)
		// .putExtra("URL", UrlConfig.XIEYITITLE)
		// .putExtra("TITLE", "权益转让及受让协议"));
		// break;
		case R.id.image_hongbao_close:
			tv_chose_conpons.setText("选择红包");
			image_hongbao_close.setVisibility(View.GONE);
			tv_hongbao_fanxian.setText("");
			hongbao = (double) 0;
			fid = "";
			if (et_invest_money.getText().toString().trim().length() > 0) {
				 Money_Get();
				 tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			}
			break;
		case R.id.hongbao_user:
			if (mlslb2 != null && !mlslb2.equals("")) {
				startActivityForResult(
						new Intent(Detail_New_Act.this, choose_Coupons.class)
								.putExtra("list", (Serializable) mlslb2)
								.putExtra("fid", fid), 1);
			} else {
				ToastMaker.showShortToast("无可用红包");
			}
			// usable() ;
			break;
		case R.id.tv_cashin:
			if ("1".equals(preferences.getString("realVerify", ""))) {
				MobclickAgent.onEvent(Detail_New_Act.this, "100015");
				startActivity(new Intent(Detail_New_Act.this, CashInAct.class));
			} else {
				MobclickAgent.onEvent(Detail_New_Act.this, "100002");
				startActivity(new Intent(Detail_New_Act.this, FourPartAct.class));
			}
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.touzi_now:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	         imm.hideSoftInputFromWindow(et_invest_money.getWindowToken(),0);
			
			if (System.currentTimeMillis() - lastClick <= 1000) {
				// ToastMaker.showShortToast("点那么快干什么");
				return;
			}
			lastClick = System.currentTimeMillis();
			if (!image_sure.isChecked()) {
				ToastMaker.showShortToast(R.string.xieyi_pro_goumai);
			} else if (!"1".equals(preferences.getString("realVerify", ""))) {
				ToastMaker.showShortToast("还未认证 ，请认证");
				startActivity(new Intent(Detail_New_Act.this, FourPartAct.class));
			} else if (et_invest_money.getText().length() <= 0) {
				ToastMaker.showShortToast("请输入投资金额");
			} 
//			else if (enableAmount > Double.parseDouble(stringCut
//					.douHao_Cut(et_invest_money.getText().toString()))) {
//				ToastMaker.showShortToast("不满足红包使用金额");
//				return;
//			} 
			else if (Double.parseDouble((balance)) < Double
					.parseDouble(leastaAmount)) {
				ToastMaker.showShortToast("账户可用余额不足，请先充值！");
			} else {
				Invest_Begin_bl = true ;
				Invest_Begin() ;
				MobclickAgent.onEvent(Detail_New_Act.this, "100022");
//				product_Invest();// 下一步
			}
			break;
		case R.id.check_all:
			if (check_all.isChecked()) {

				if (Double.parseDouble((balance)) < Double
						.parseDouble(leastaAmount)) {
					check_all.setChecked(false);
					ToastMaker.showShortToast("余额不足请先充值"); // 小于起投
				} else if (Double.parseDouble(balance) < Double
						.parseDouble(maxAmount)) { // 小于产品剩余
					int_last = (int) (Double.parseDouble(balance) / Double
							.parseDouble(increasAmount));
					et_invest_money.setText(stringCut.getNumKbs(Double
							.parseDouble(increasAmount) * int_last));
				} else {// 大于产品剩余
					int_last = (int) (Double.parseDouble(maxAmount) / Double.parseDouble(increasAmount));
					et_invest_money.setText(stringCut.getNumKbs(Double.parseDouble(increasAmount) * int_last));
				}
			} else {
				et_invest_money.setText("");
			}
			Editable etable = et_invest_money.getText();
			Selection.setSelection(etable, et_invest_money.getText().length());
			break;
			

		default:
			break;
		}
	}
	private TradePwdPopUtils pop;
	void Invest_Begin(){
		firstPwd = "" ;
		 if(!"1".equals(preferences.getString("tpwdFlag", ""))){
				showAlertDialog("提示", "为了保障资金安全，请在投资之前先设置交易密码！", new String[] { "取消",
				"设置" }, true, true, "shezhi");
			}else{
				MobclickAgent.onEvent(Detail_New_Act.this, "100023");
				pop = new TradePwdPopUtils();
				pop.setCallBackTradePwd(new CallBackTradePwd() {
					
					@Override
					public void callBaceTradePwd(String pwd) {
						firstPwd = pwd ;
						 product_Invest() ;
					}
				});
				pop.showPopWindow(Detail_New_Act.this, Detail_New_Act.this, detail_new);
				pop.forget_pwd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pop.popWindow.dismiss() ;
						startActivityForResult(new Intent(Detail_New_Act.this, TransactionPswAct.class),1);
					}
				});
                pop.iv_key_close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						pop.popWindow.dismiss() ;
					}
				}) ;
                pop.popWindow.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						Invest_Begin_bl = false;
					}
				}) ;
			}
	}
	
	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		
		if(((String) tag).equalsIgnoreCase("shezhi")){
			shezhi_check = true ;
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivity(new Intent(Detail_New_Act.this,
						TransactionPswAct.class));
			}
			}
		if(((String) tag).equalsIgnoreCase("three_time")){
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Detail_New_Act.this, TransactionPswAct.class),1);
			}
			}
	}
	// 投资
	private void product_Invest() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.INVEST)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("tpwd",SecurityUtils.MD5AndSHA256(firstPwd))
				.addParams("amount",stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
//				.addParams("fid", fid)
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
//							setResult(2, new Intent());
							ToastMaker.showShortToast("投资成功");
							startActivity(new Intent(Detail_New_Act.this,Act_End.class)
									.putExtra("pid", pid)
									.putExtra("tv_name", fullname)
									.putExtra("tv_money", stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
									.putExtra("tv_day", deadline)
									.putExtra("tv_rate", rate +"%+"+ rate_jiaxi)
									.putExtra("tv_earn", stringCut.getNumKbs(shouyi))
									.putExtra("tv_red", "5")
							) ;
							Detail_New_Act.this.finish();
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("密码输入错误，请重新输入。");
						} else{
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
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
								new show_Dialog_IsLogin(Detail_New_Act.this)
										.show_Is_Login();
							} else if ("1010".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("不符合优惠券使用条件");
							} else if ("1011".equals(obj.getString("errorCode"))) {
								ToastMaker.showShortToast("投资失败,请稍后再试");
							}  else {
								ToastMaker.showShortToast("系统错误");
						    }
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("服务器异常");
					}
				});
	}
	private Double shouyi ;
	private void Money_Get() {
		 shouyi = (Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString().trim())))
					* ((Double.parseDouble(rate)+Double.parseDouble(rate_jiaxi)) / 360 / 100)
					* Double.parseDouble(deadline);
//		 shouyi = (Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString().trim())) + hongbao)
//				 * (Double.parseDouble(rate) / 360 / 100)
//				 * Double.parseDouble(deadline);
	}

	
	class Watcher implements TextWatcher {
		int onTextLength = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			if (onTextLength > 0 && rate != null) {
				Money_Get() ;
				if (shouyi < 0.01) {
					tv_shouyi.setText("0.00");
				} else {
					tv_shouyi.setText(stringCut.getNumKbs(shouyi));
				}
			} else {
				tv_shouyi.setText("0.00");
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


	private void memberSetting() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
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
							JSONObject map = obj.getJSONObject("map");
							String realVerify = map.getString("realVerify");
							String tpwdFlag = map.getString("tpwdFlag");
							SharedPreferences.Editor editor = preferences
									.edit();
							editor.putString("realVerify", realVerify);
							editor.putString("tpwdFlag", tpwdFlag);
							editor.commit();
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_New_Act.this)
									.show_Is_Login();
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

	private LinearLayout layout;
	private ListView lv_dialog_message;
	boolean isFirst = true;
	String firstPwd;
	ArrayList<String> as = new ArrayList<>() ;
	private Boolean hongbao_check = false , shezhi_check = false ,Invest_Begin_bl = false;
//
         }