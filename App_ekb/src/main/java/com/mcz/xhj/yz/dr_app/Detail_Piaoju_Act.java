package com.mcz.xhj.yz.dr_app;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
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
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class Detail_Piaoju_Act extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;

	@BindView(R.id.tv_name_detail)
	TextView tv_name_detail; // 产品名称
	@BindView(R.id.tv_rate)
	TextView tv_rate; // 产品名称
	@BindView(R.id.progressbar_pert)
	ProgressBar progressbar_pert; // 进度

	@BindView(R.id.tv_deadline)
	TextView tv_deadline; // 产品期限
	@BindView(R.id.tv_leastaAmount)
	TextView tv_leastaAmount; // 起投金额
	@BindView(R.id.tv_maxAmount)
	TextView tv_maxAmount; // 最大投资
	@BindView(R.id.image_isDouble)
	ImageView image_isDouble;

	@BindView(R.id.tv_balance)
	TextView tv_balance; // 余额
	@BindView(R.id.et_invest_money)
	EditText et_invest_money; // 募集金额
	@BindView(R.id.check_all)
	CheckBox check_all; // 募集金额quanbu
	@BindView(R.id.image_sure)
	CheckBox image_sure;
	@BindView(R.id.touzi_now)
	Button touzi_now;
	@Nullable
	@BindView(R.id.image_eye)
	CheckBox image_eye;
	@BindView(R.id.tv_shouyi)
	TextView tv_shouyi; // 收益
	@BindView(R.id.tv_cashin)
	TextView tv_cashin; // 充值
	@BindView(R.id.image_isInterest)
	ImageView image_isInterest;
	@BindView(R.id.image_isCash)
	ImageView image_isCash;
	@BindView(R.id.hongbao_user)
	LinearLayout hongbao_user;
	@BindView(R.id.tv_chose_conpons)
	TextView tv_chose_conpons; // hongbao
	@BindView(R.id.tv_hongbao_fanxian)
	TextView tv_hongbao_fanxian; // hongbao
	@BindView(R.id.tv_rate_jiaxi)
	TextView tv_rate_jiaxi; // jiaxi
	@BindView(R.id.image_hongbao_close)
	ImageView image_hongbao_close;
	@BindView(R.id.lv_fanxian)
	LinearLayout lv_fanxian;
	@BindView(R.id.tv_shouyi_fanxian)
	TextView tv_shouyi_fanxian; // jiaxi
	@BindView(R.id.tv_xieyi_pro_zhuanrang)
	TextView tv_xieyi_pro_zhuanrang; // jiaxi
	@BindView(R.id.ptr_invest)
	PtrClassicFrameLayout ptr_invest; // jiaxi
	@BindView(R.id.lin_shouyi)
	LinearLayout lin_shouyi;
	@BindView(R.id.greater)
	ImageView greater;
	private String pid, ptype, uid;          //ptype:1：返现券  2：加息券 3：体验金 4：翻倍券
	private SharedPreferences preferences;
	@BindView(R.id.image_billtype)
	ImageView image_billtype;
	@BindView(R.id.tv_piaofrom)
	TextView tv_piaofrom;
	@BindView(R.id.tv_xieyi_weituo)
    TextView tv_xieyi_weituo;
	@BindView(R.id.lv_xieyi_weituo)
	LinearLayout lv_xieyi_weituo;
	@BindView(R.id.pro_start)
	TextView pro_start ;
	@BindView(R.id.pro_end)
	TextView pro_end ;
	@BindView(R.id.detail_piao)
	LinearLayout detail_piao ;


	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_detail_piaoju;
	}
	private String atid;
	private String intent_fid;
	private String intent_amount;
	private String intent_enableAmount;
	private boolean isFirstGet = true;
	private boolean isFirstGetRate = true;
	@Override
	protected void initParams() {
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
        if(atid!=null){
        	hongbao_user.setFocusable(false) ;
        	hongbao_user.setEnabled(false) ;
        	hongbao_user.setVisibility(View.GONE);
        }
		title_centertextview.setText("优选金服");
		title_rightimageview.setVisibility(View.GONE);
		title_righttextview.setOnClickListener(this);
		check_all.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		image_sure.setOnClickListener(this);
		touzi_now.setOnClickListener(this);
		tv_cashin.setOnClickListener(this);
		hongbao_user.setOnClickListener(this);
		image_hongbao_close.setOnClickListener(this);
		tv_xieyi_pro_zhuanrang.setOnClickListener(this);
		tv_xieyi_weituo.setOnClickListener(this);
		// 手机号间隔
		Watcher watcher = new Watcher();
		et_invest_money.addTextChangedListener(watcher);
		// 交易密码
		ptr_invest.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				getDate();

			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});
		getDate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onEvent(Detail_Piaoju_Act.this, "100021");
//		register_Reg();
	}

	double enableAmount = 0, amount = 0, raisedRate = 0, multiple = 1;
	Double shouyi;
	int resultNum = 0 ;
	private String nothing ;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		if (requestCode == 1 && resultCode == 1) {
			resultNum = resultCode ;
			raisedRate = 0;
			multiple = 1;
			fid = data.getStringExtra("id");
			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
			amount = data.getDoubleExtra("amount", 0.00);
			lv_fanxian.setVisibility(View.VISIBLE);
			tv_shouyi_fanxian.setText(stringCut.getNumKbs(amount));
			nothing = stringCut.getNumKbs(amount) ;
			tv_chose_conpons.setText("已使用返现券");
			tv_chose_conpons.setGravity(Gravity.CENTER);
			if (et_invest_money.getText().toString().trim().length() > 0) {
				Money_Get();
				tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			} else {
			}
			if(exRate!=0){
				tv_rate_jiaxi.setText("+" + exRate + "%");
			}
			else{
				tv_rate_jiaxi.setText("");
			}
			tv_hongbao_fanxian.setText("");
		} else if (requestCode == 1 && resultCode == 2) { // 2=加息券
			resultNum = resultCode ;
			amount = 0;
			multiple = 1;
			tv_chose_conpons.setText("已使用加息券");
			tv_chose_conpons.setGravity(Gravity.CENTER);
			fid = data.getStringExtra("id");
			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
			raisedRate = data.getDoubleExtra("raisedRate", 0.00);
			double jiaxijia = raisedRate+exRate;
			tv_rate_jiaxi.setText("+" + jiaxijia + "%");
			nothing = raisedRate+"" ;
			if (et_invest_money.getText().toString().trim().length() > 0) {
				Money_Get();
				tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			} else {
			}
			lv_fanxian.setVisibility(View.GONE);
			tv_hongbao_fanxian.setText("");
		} else if (requestCode == 1 && resultCode == 3) {

		} else if (requestCode == 1 && resultCode == 4) { // 翻倍
			resultNum = resultCode ;
			raisedRate = 0;
			amount = 0;
			fid = data.getStringExtra("id");
			enableAmount = data.getDoubleExtra("enableAmount", 0.00);
			multiple = data.getDoubleExtra("multiple", 0.00);
			nothing = multiple+"" ;
			if(exRate!=0){
				tv_rate_jiaxi.setText("+" + exRate + "%");
			}
			else{
				tv_rate_jiaxi.setText("");
			}
			Double_conpons();
		}
		if(Invest_Begin_bl){
			if(null == data){
				return ;
			}else{
				Invest_Begin() ;
			}			}
	}

	private void Double_conpons() {
		// TODO Auto-generated method stub
		amount = 0;
		tv_chose_conpons.setText("已使用翻倍券");
		tv_chose_conpons.setGravity(Gravity.CENTER);

		if (et_invest_money.getText().toString().trim().length() > 0) {
			Money_Get();
			tv_shouyi.setText(stringCut.getNumKbs(shouyi));
		} else {
		}
		lv_fanxian.setVisibility(View.GONE);
	}

	Long lastClick = 0l;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_xieyi_pro_zhuanrang:
			if(isFid){
				startActivity(new Intent(Detail_Piaoju_Act.this,
						WebViewActivity.class)
				.putExtra("URL", UrlConfig.ZHUANRANG).putExtra("TITLE",
						tv_xieyi_pro_zhuanrang.getText()));
			}else{
				startActivity(new Intent(Detail_Piaoju_Act.this,
						WebViewActivity.class)
				.putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE",
						tv_xieyi_pro_zhuanrang.getText()));
			}
			break;
		case R.id.tv_xieyi_weituo:
			startActivity(new Intent(Detail_Piaoju_Act.this,
					WebViewActivity.class)
			.putExtra("URL", UrlConfig.WEITUO).putExtra("TITLE",
					tv_xieyi_weituo.getText()));
			break ;
		case R.id.image_hongbao_close:
			tv_chose_conpons.setText("使用优惠券");
			tv_chose_conpons.setGravity(Gravity.RIGHT);
			image_hongbao_close.setVisibility(View.GONE);
			lv_fanxian.setVisibility(View.GONE);
			tv_rate_jiaxi.setText("");
			tv_hongbao_fanxian.setText("");
			nothing = "" ;
			resultNum = 0 ;
			enableAmount = 0;
			raisedRate = 0;
			amount = 0;
			multiple = 1;
			fid = "";
			if (et_invest_money.getText().toString().trim().length() > 0) {
				Money_Get();
				tv_shouyi.setText(stringCut.getNumKbs(shouyi));
			}
			break;
		case R.id.hongbao_user:
			if (mlslb2 != null && !mlslb2.equals("")) {
				startActivityForResult(
						new Intent(Detail_Piaoju_Act.this, choose_Coupons.class)
								.putExtra("list", (Serializable) mlslb2)
								.putExtra("insestAmount",et_invest_money.getText().toString())
								.putExtra("fid", fid), 1);
			} else {
				greater.setVisibility(View.GONE) ;
				ToastMaker.showShortToast("无可用优惠券");
			}
			// usable() ;
			break;
		case R.id.tv_cashin:
			if ("1".equals(preferences.getString("realVerify", ""))) {
				MobclickAgent.onEvent(Detail_Piaoju_Act.this, "100015");
				startActivity(new Intent(Detail_Piaoju_Act.this,
						CashInAct.class));
			} else {
				MobclickAgent.onEvent(Detail_Piaoju_Act.this, "100002");
				startActivity(new Intent(Detail_Piaoju_Act.this,
						FourPartAct.class));
			}
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.title_righttextview:
			startActivity(new Intent(Detail_Piaoju_Act.this, ProDetails.class)
					.putExtra("pid", pid).putExtra("ptype", ptype)
					.putExtra("windMeasure", windMeasure)
					.putExtra("introduce", introduce)
					.putExtra("repaySource", repaySource)
					.putExtra("borrower", borrower));
			break;
		case R.id.check_all:
			if (check_all.isChecked()) {

				if (Double.parseDouble((balance)) < Double
						.parseDouble(increasAmount)) {
					check_all.setChecked(false);
					ToastMaker.showShortToast("账户可用余额不足，请先充值！"); // 小于起投
				} else if (Double.parseDouble(balance) < Double
						.parseDouble(surplusAmount)) { //小于产品剩余
					if (Double.parseDouble(balance) < Double
							.parseDouble(maxAmount)) {// 小于限额
						int_last = (int) (Double.parseDouble(balance) / Double
								.parseDouble(increasAmount));
						et_invest_money.setText(stringCut.douHao_Cut(stringCut
								.getNumKbs(Double.parseDouble(increasAmount)
										* int_last)));
					} else {// 大于限额
						int_last = (int) (Double.parseDouble(maxAmount) / Double
								.parseDouble(increasAmount));
						et_invest_money.setText(stringCut.douHao_Cut(stringCut
								.getNumKbs(Double.parseDouble(increasAmount)
										* int_last)));
					}
				} else {// 大于产品剩余
					if (Double.parseDouble(surplusAmount) < Double
							.parseDouble(maxAmount)) {// 小于限额
						int_last = (int) (Double.parseDouble(surplusAmount) / Double
								.parseDouble(increasAmount));
						et_invest_money.setText(stringCut.douHao_Cut(stringCut
								.getNumKbs(Double.parseDouble(increasAmount)
										* int_last)));
					} else {// 大于限额
						int_last = (int) (Double.parseDouble(maxAmount) / Double
								.parseDouble(increasAmount));
						et_invest_money.setText(stringCut.douHao_Cut(stringCut
								.getNumKbs(Double.parseDouble(increasAmount)
										* int_last)));// 小于限额
					}
				}
			} else {
				et_invest_money.setText("");
			}
			Editable etable = et_invest_money.getText();
			Selection.setSelection(etable, et_invest_money.getText().length());
			break;
		case R.id.touzi_now:
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	         imm.hideSoftInputFromWindow(et_invest_money.getWindowToken(),0);
	      if (System.currentTimeMillis() - lastClick <= 1000) {
	           // ToastMaker.showShortToast("点那么快干什么");
	           return;
              }
            lastClick = System.currentTimeMillis();
            Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
            Matcher mPsw;
			if (!image_sure.isChecked()) {
				ToastMaker.showShortToast(getResources().getString(
						R.string.xieyi_pro_goumai));
			}
			else if (!"1".equals(preferences.getString("realVerify", ""))) {
				ToastMaker.showShortToast("还未认证 ，请认证");
				startActivity(new Intent(Detail_Piaoju_Act.this,
						FourPartAct.class));
			}
			else if (et_invest_money.getText().length() <= 0) {
				ToastMaker.showShortToast("请输入投资金额");
			}
			else if (enableAmount > Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString()))) {
				ToastMaker.showShortToast("不满足红包最低使用金额");
			}
			else if (Double.parseDouble(stringCut.douHao_Cut(et_invest_money
					.getText().toString())) < Double
					.parseDouble(increasAmount)) {
				ToastMaker.showShortToast("投资金额最少为"+ stringCut.getNumKbs(Double
						.parseDouble(increasAmount)));
			} else if ((Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString()))) % Double.parseDouble(increasAmount) != 0) {
				ToastMaker
						.showShortToast("投资金额需为"
								+ stringCut.getNumKbs(Double
										.parseDouble(increasAmount)) + "的倍数");
			} else  if (Double.parseDouble(stringCut.douHao_Cut(et_invest_money
					.getText().toString())) > Double.parseDouble(surplusAmount)) {
				ToastMaker.showShortToast("投资金额不能大于产品可投金额");
			} else if (Double.parseDouble(stringCut.douHao_Cut(et_invest_money
					.getText().toString())) > Double.parseDouble(balance)) {
				ToastMaker.showShortToast("账户可用余额不足，请先充值！");
			} else{
				Invest_Begin_bl = true ;
				Invest_Begin() ;
				MobclickAgent.onEvent(Detail_Piaoju_Act.this, "100022");
			}
			break;

		default:
			break;
		}
	}
	private Boolean hongbao_check = false , shezhi_check = false ,Invest_Begin_bl = false;
	private TradePwdPopUtils pop;
	String firstPwd;
	void Invest_Begin(){
		firstPwd = "" ;
		if(atid!=null){

		}else{
			if(!hongbao_check && mlslb2 != null && !mlslb2.equals("") && fid == ""){
				showAlertDialog("提示", "您还有可用优惠券是否使用？", new String[] { "忽略","使用" }, true, true, "hongbao_user");
				return;
			}
		}
		if(!"1".equals(preferences.getString("tpwdFlag", ""))){
				showAlertDialog("提示", "为了保障资金安全，请在投资之前先设置交易密码！", new String[] { "取消","设置" }, true, true, "shezhi");
		}else{
				hongbao_check = false  ;
				MobclickAgent.onEvent(Detail_Piaoju_Act.this, "100023");
				pop = new TradePwdPopUtils();
				pop.setCallBackTradePwd(new CallBackTradePwd() {

					@Override
					public void callBaceTradePwd(String pwd) {
						firstPwd = pwd  ;
						product_Invest() ;
					}
				});
				pop.showPopWindow(Detail_Piaoju_Act.this, Detail_Piaoju_Act.this, detail_piao);
				pop.forget_pwd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pop.popWindow.dismiss() ;
						startActivityForResult(new Intent(Detail_Piaoju_Act.this, TransactionPswAct.class),1);
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
		if(((String) tag).equalsIgnoreCase("hongbao_user")){
		if (position == 0) {
			hongbao_check = true ;
			Invest_Begin() ;
		}
		if (position == 1) {
			Invest_Begin_bl = false;
			startActivityForResult(
					new Intent(Detail_Piaoju_Act.this, choose_Coupons.class)
							.putExtra("list", (Serializable) mlslb2)
							.putExtra("insestAmount",et_invest_money.getText().toString())
							.putExtra("fid", fid), 1);

		}
		}
		if(((String) tag).equalsIgnoreCase("shezhi")){
			shezhi_check = true ;
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Detail_Piaoju_Act.this, TransactionPswAct.class),1);
			}
			}
		if(((String) tag).equalsIgnoreCase("three_time")){
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				startActivityForResult(new Intent(Detail_Piaoju_Act.this, TransactionPswAct.class),1);
			}
			}
	}

	private void no_checked() {
		tv_rate.setTextColor(0xffA0A0A0);
		progressbar_pert.setSecondaryProgress(0);
		tv_xieyi_pro_zhuanrang.setTextColor(0xffA0A0A0);
		tv_shouyi.setTextColor(0xffA0A0A0);
		tv_cashin.setFocusable(false);
		tv_cashin.setEnabled(false);
		et_invest_money.setFocusable(false);
		et_invest_money.setEnabled(false);
		check_all.setFocusable(false);
		check_all.setEnabled(false);
		tv_chose_conpons.setFocusable(false);
		tv_chose_conpons.setEnabled(false);
		image_sure.setFocusable(false);
		image_sure.setEnabled(false);
		touzi_now.setFocusable(false);
		touzi_now.setEnabled(false);
		hongbao_user.setFocusable(false);
		hongbao_user.setEnabled(false);
		image_sure.setBackgroundResource(R.mipmap.icon_check_g);
		image_isInterest.setImageResource(R.mipmap.icon_raise_g);
		image_isCash.setImageResource(R.mipmap.icon_cash_g);
		image_isDouble.setImageResource(R.mipmap.double_g);
		touzi_now.setBackgroundColor(0xffA0A0A0);
		lin_shouyi.setVisibility(View.GONE);

	}

	private String maxAmount; // 产品投资限额
	private String balance, leastaAmount; // 余额，起投
	private int int_last; // 100整数
	private String increasAmount; // 递增
	private String windMeasure; // 风控措施
	private String introduce;// 产品介绍
	private String repaySource;// 还款来源
	private String borrower;// 债务人概况

	private String surplusAmount; // 剩余金额
	private String deadline;
	private String endDate;
	private String rate_h, rate = null;
	private String fid = ""; // 可用红包id
	private SpannableString ss;
	private ArrayList<ConponsBean> mlslb2; // 红包列表
	String fullname  ; //产品名称
	private boolean isOldUser = false;//是否是老用户
	private double specialRate1 = 0;//双旦活动利率
	private double exRate = 0;//总共增加的利率

	private void getDate() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL).addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						ptr_invest.refreshComplete();
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							balance = map.getString("balance");
							isOldUser = map.getBoolean("isOldUser");
							specialRate1 = map.getDouble("specialRate");
							JSONObject info = map.getJSONObject("info");
							exRate = specialRate1 + info.getDouble("activityRate");
							if(exRate!=0){
								tv_rate_jiaxi.setVisibility(View.VISIBLE);
								if(specialRate1!=0&&isOldUser){
									if(isFirstGetRate){
										tv_rate_jiaxi.setText("+" + exRate + "%");
										isFirstGetRate = false;
									}
								}else{
									if(info.getDouble("activityRate")!=0){
										tv_rate_jiaxi.setText("+"+info.getDouble("activityRate")+"%");
									}else {
										tv_rate_jiaxi.setVisibility(View.GONE);
									}
									exRate = info.getDouble("activityRate");
								}
							}else {
								tv_rate_jiaxi.setVisibility(View.GONE);
							}
							tv_balance.setText(stringCut.getNumKbs(Double.parseDouble(balance)));

							is_fid(info.getString("fid"));
							// 票据类型 1=商票，2=银票
                            status_type(info.getString("billType") , false) ;
                          	//成立日期,到期日期
							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;

							if ("1".equals(info.getString("isInterest"))) {
								image_isInterest.setVisibility(View.VISIBLE);
							}
							if ("1".equals(info.getString("isCash"))) {
								image_isCash.setVisibility(View.VISIBLE);
							}
							if ("1".equals(info.getString("isDouble"))) {
								image_isDouble.setVisibility(View.VISIBLE);
							}

							fullname  = info.getString("fullName") ;
							tv_name_detail.setText(fullname);

							proAnimator(Integer.valueOf(stringCut.pertCut(info
									.getString("pert")))
					                .intValue()) ;

							rate = info.getString("rate");
							deadline = info.getString("deadline");
							maxAmount = info.getString("maxAmount");
							increasAmount = info.getString("increasAmount");
							surplusAmount = info.getString("surplusAmount");
							leastaAmount = info.getString("leastaAmount");
							rate_h = stringCut.getNumKbs(Double.parseDouble(rate)) + "%";
							tv_piaofrom.setText(stringCut.getNumKbs(Double.parseDouble(surplusAmount)));
							tv_rate.setText(rate_h);
							tv_deadline.setText("期限" + deadline + "天 ");
							tv_leastaAmount.setText("起投" + stringCut.getNumKbs(Double.parseDouble(leastaAmount)) + "元");
							tv_maxAmount.setText("次日计息");
							if (et_invest_money.getText().length() <= 0) {
								et_invest_money.setHint("需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
							}
							// 详情信息
							windMeasure = info.getString("windMeasure");
							introduce = info.getString("introduce");
							repaySource = info.getString("repaySource");
							borrower = info.getString("borrower");
							JSONArray list = map.getJSONArray("couponList");
							if (null == list) {
							} else {
								if (list.size() <= 0) {
									hongbao_user.setFocusable(false);
									hongbao_user.setEnabled(false);
									greater.setVisibility(View.GONE);
									tv_chose_conpons.setText("无可用优惠券");
								} else {
									mlslb2 = (ArrayList<ConponsBean>) JSON.parseArray(list.toJSONString(),ConponsBean.class);

									if(intent_fid!=null&&!intent_fid.equalsIgnoreCase("")){
										if(isFirstGet){
											isFirstGet = false;
											resultNum = 1 ;
											raisedRate = 0;
											multiple = 1;
											fid = intent_fid;
											enableAmount = Double.parseDouble(intent_enableAmount);
											amount = Double.parseDouble(intent_amount);
											lv_fanxian.setVisibility(View.VISIBLE);
											tv_shouyi_fanxian.setText(stringCut.getNumKbs(amount));
											nothing = stringCut.getNumKbs(amount) ;
											tv_chose_conpons.setText("已使用返现券");
											tv_chose_conpons.setGravity(Gravity.CENTER);
											if (et_invest_money.getText().toString().trim().length() > 0) {
												Money_Get();
												tv_shouyi.setText(stringCut.getNumKbs(shouyi));
											}
										}
									}else{
										for (int i = 0; i < mlslb2.size(); i++) {
											if(mlslb2.get(i).getPid()!=null){
												if(isFirstGet){
													if(mlslb2.get(i).getPid().equalsIgnoreCase(pid)){
														isFirstGet = false;
														resultNum = 2 ;
														raisedRate = 0;
														multiple = 1;
														tv_rate_jiaxi.setText("");
														tv_hongbao_fanxian.setText("");
														tv_chose_conpons.setText("已使用加息券");
														tv_chose_conpons.setGravity(Gravity.CENTER);
														fid = mlslb2.get(i).getId();
														enableAmount = mlslb2.get(i).getEnableAmount();
														raisedRate = mlslb2.get(i).getRaisedRates();
														double exRates = raisedRate + exRate;
														tv_rate_jiaxi.setVisibility(View.VISIBLE);
														tv_rate_jiaxi.setText("+" + exRates + "%");
														nothing = raisedRate+"" ;
														if (et_invest_money.getText().toString().trim().length() > 0) {
															Money_Get();
															tv_shouyi.setText(stringCut.getNumKbs(shouyi));
														}
													}
												}
												if (!mlslb2.get(i).getPid().equalsIgnoreCase(pid)&&mlslb2.get(i).getType()==2) {
													mlslb2.remove(i);
													i--;
												}
											}
										}
									}
								}
							}
							if ("6".equals(info.getString("status"))) {
								no_checked();
								touzi_now.setText("募集结束 ");
								status_type(info.getString("billType") , true) ;
							} else if ("8".equals(info.getString("status"))) {
								no_checked();
								touzi_now.setText("已计息");
								status_type(info.getString("billType") , true) ;
							} else if ("9".equals(info.getString("status"))) {
								no_checked();
								touzi_now.setText("已回款");
								status_type(info.getString("billType") , true) ;
							} else {
								endDate = info.getString("endDate");
							}

							memberSetting();
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Piaoju_Act.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					private void proAnimator(int pert) {
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
					}
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ptr_invest.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}
	private Boolean isFid = false  , is_Big = false ;  //ture 为蓄发
	// 续发
		private void is_fid(String fid) {
			if (!("".equals(fid) || null == fid)) {
				isFid =true ;
				tv_xieyi_pro_zhuanrang.setText("债权转让协议") ;
			} else {
				isFid =false ;
				tv_xieyi_pro_zhuanrang.setText("借款协议") ;
			}
		}
	private void status_type(String billType , Boolean nochecked) {
		// 票据类型 1=商票，2=银票
		if (!("".equals(billType) || null == billType)) {
            if(nochecked){
            	if ("1".equals(billType)) {
    				image_billtype
    						.setBackgroundResource(R.mipmap.icon_trade_p_gray);
    			   } else if ("2".equals(billType)) {
    				image_billtype
    						.setBackgroundResource(R.mipmap.icon_bank_p_gray);
    			   }
            }else{
			   if ("1".equals(billType)) {
				image_billtype
						.setBackgroundResource(R.mipmap.icon_trade_p);
			   } else if ("2".equals(billType)) {
				image_billtype
						.setBackgroundResource(R.mipmap.icon_bank_p);
			   }
            }
		}
	}
	String tv_establish,  tv_expireDate ,luckcode,luckcodesize;
	private void pro_Str_Or(String establish, String expireDate) {
		// 成立日期,到期日期
		if (!("".equals(establish) || null == establish)) {
			tv_establish = stringCut.getDateToString(Long.parseLong(establish)) ;
			pro_start.setText("产品成立 : "+stringCut.getDateToString(Long.parseLong(establish))) ;
		}
		if (!("".equals(expireDate) || null == expireDate)) {
			tv_expireDate= stringCut.getDateToString(Long.parseLong(expireDate)) ;
			pro_end.setText("产品到期 : "+stringCut.getDateToString(Long.parseLong(expireDate))) ;
		}
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
				.addParams("tpwd",SecurityUtils.MD5AndSHA256(firstPwd))
				.addParams("amount",stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
				.addParams("fid", fid)
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
							Invest_Begin_bl = false ;
							pop.popWindow.dismiss() ;
							JSONObject map = obj.getJSONObject("map");
							if (map!=null) {
									luckcode = map.getString("luckCodes");
									luckcodesize = map.getString("luckCodeCount");
									jumpURLActivity = map.getString("activityUrl");
									if (!map.getBoolean("isRepeats")) {
										is_Activity() ;
									} else {
										startActivity(new Intent(Detail_Piaoju_Act.this,Act_End.class)
										.putExtra("pid", pid)
										.putExtra("tv_name", fullname)
										.putExtra("tv_money", stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
										.putExtra("tv_day", deadline)
										.putExtra("tv_rate", rate)
										.putExtra("tv_earn", stringCut.getNumKbs(shouyi))
										.putExtra("tv_red", resultNum+"")
										.putExtra("nothing", nothing)
										.putExtra("pid", pid)
										.putExtra("tv_start", tv_establish)
										.putExtra("isPicture", map.getString("activityURL"))
										.putExtra("jumpURLActivity", map.getString("activityUrl"))
										.putExtra("specialRate", exRate+"")//活动利率
										.putExtra("jumpURL", map.getString("jumpURL"))
										.putExtra("tv_end", tv_expireDate)
										.putExtra("luckcode", luckcode)
										.putExtra("luckcodesize", luckcodesize)
										) ;
										finish();
									}
								}
								else {
									is_Activity() ;
									}
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
								new show_Dialog_IsLogin(Detail_Piaoju_Act.this)
										.show_Is_Login();
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
						ptr_invest.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络异常，请检查");
					}
				});
	}
	private void is_Activity() {

		startActivity(new Intent(Detail_Piaoju_Act.this,Act_End.class)
		.putExtra("pid", pid)
		.putExtra("tv_name", fullname)
		.putExtra("tv_money", stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
		.putExtra("tv_day", deadline)
		.putExtra("tv_rate", rate)
		.putExtra("tv_earn", stringCut.getNumKbs(shouyi))
		.putExtra("tv_red", resultNum+"")
		.putExtra("nothing", nothing)
		.putExtra("tv_start", tv_establish)
		.putExtra("pid", pid)
		.putExtra("jumpURLActivity", jumpURLActivity)
		.putExtra("specialRate", exRate+"")//活动利率
		.putExtra("tv_end", tv_expireDate)
		.putExtra("luckcode", luckcode)
		.putExtra("luckcodesize", luckcodesize)
				) ;
		ToastMaker.showShortToast("投资成功");
		finish();

	}
	// 获取产品可用优惠券
	private void usable() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.USABLE).addParams("pid", pid)
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
							JSONArray list = map.getJSONArray("list");
							List<InvestListBean> mlslb2 = JSON.parseArray(list.toJSONString(), InvestListBean.class);
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Piaoju_Act.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("系统异常，请检查");
					}
				});
	}

	private void Money_Get() {
		shouyi =
//				(Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
				Double.parseDouble(stringCut.douHao_Cut(et_invest_money.getText().toString().trim()))
//				* (multiple - 1))
				* ((Double.parseDouble(rate) * multiple + raisedRate + exRate) / 360 / 100)
				* Double.parseDouble(deadline);
	}

	class Watcher implements TextWatcher {
		int onTextLength = 0;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			onTextLength = s.length();
			if (onTextLength > 0 && rate != null) {
				et_invest_money.setHint("");
				Money_Get();
				if (shouyi < 0.01) {
					tv_shouyi.setText("0.00");
				} else {
					tv_shouyi.setText(stringCut.getNumKbs(shouyi));
				}
			} else {
				tv_shouyi.setText("0.00");
				tv_hongbao_fanxian.setText("");
				et_invest_money
						.setHint("需为"
								+ stringCut.getNumKbs(Double
										.parseDouble(increasAmount)) + "的倍数");
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
		showWaitDialog("加载中...", false, "");
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
							new show_Dialog_IsLogin(Detail_Piaoju_Act.this)
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

}
