package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class Act_Fourpart_Cashin extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	
	@BindView(R.id.tv_name)
	TextView tv_name;
	@BindView(R.id.tv_money)
	@Nullable
	TextView tv_money;
	@BindView(R.id.tv_day)
	@Nullable
	TextView tv_day;
	@BindView(R.id.tv_rate)
	@Nullable
	TextView tv_rate;
	@BindView(R.id.tv_cashin)
	@Nullable
	TextView tv_cashin;
	@BindView(R.id.tv_invest_money)
	@Nullable
	TextView tv_invest_money;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_fourpart_cashin;
	}

	@Override
	protected void initParams() {
		title_centertextview.setText("绑卡成功") ;
		title_leftimageview.setOnClickListener(this);
		tv_cashin.setOnClickListener(this);

		tv_name.setText(getIntent().getStringExtra("proName")) ;
		tv_money.setText("实名绑卡成功") ;
		tv_day.setText(getIntent().getStringExtra("proDeadline")+"天") ;
		if(Double.parseDouble(getIntent().getStringExtra("specialRate"))!=0){
			double rate = Double.parseDouble(getIntent().getStringExtra("proRate")) +Double.parseDouble(getIntent().getStringExtra("specialRate"));
			tv_rate.setText(rate+"%");
		}else{
			tv_rate.setText(getIntent().getStringExtra("proRate")+"%");
		}
		tv_invest_money.setText(getIntent().getStringExtra("proAmount")+"元");
		LogUtils.i("--->实名绑卡成功:预期年化收益proRate="+getIntent().getStringExtra("proRate")+" ,活动利率specialRate="+getIntent().getStringExtra("specialRate"));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onEvent(Act_Fourpart_Cashin.this, UrlConfig.point+11+"");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private String payNum = "";
	private String bankMobilePhone = "";
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private void doCashInNext() {
		OkHttpUtils
				.post()
				.url(UrlConfig.CASHINNEXT)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("amount", stringCut.space_Cut(getIntent().getStringExtra("proAmount")))
//				.addParams("smsCode", stringCut.space_Cut(code_et.getText().toString()))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onResponse(String response) {
//						isResponse = true;
//						dismissDialog();
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
//					`	ToastMaker.showShortToast("订单创建成功");
							JSONObject objmap = obj.getJSONObject("map");
							payNum = objmap.getString("payNum");
							bankMobilePhone = objmap.getString("bankMobilePhone");
//							codeSend = false;//每次创建订单重置
//							showPopupWindow();
//							sendRegMsg();
							startActivityForResult(new Intent(Act_Fourpart_Cashin.this,Act_SendMsg.class)
									.putExtra("payNum",payNum)
									.putExtra("bankMobilePhone",bankMobilePhone)
									.putExtra("amount",stringCut.space_Cut(getIntent().getStringExtra("proAmount"))),4
							);
//							finish() ;

						} else {
							switch (Integer.parseInt(obj.getString(("errorCode")))) {
								case 9999:
									ToastMaker.showShortToast("系统错误");
									break;
								case 1001:
									ToastMaker.showShortToast("充值金额有误");
									break;
								case 1002:
									ToastMaker.showShortToast("系统错误，请稍后重试");
									break;
								case 1003:
									ToastMaker.showShortToast("超过限额，请修改金额后重试");
									break;
								case 9998:
									finish();
									new show_Dialog_IsLogin(Act_Fourpart_Cashin.this).show_Is_Login();
									break;
								default:
									break;
							}
						}
					}

					@Override
					public void onError(Call call, Exception e) {
//						isResponse = true;
//						dismissDialog();
						ToastMaker.showShortToast("请检查网络!");

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 4 && resultCode == 4){
			setResult(4);
			finish();
		}else if(requestCode == 4 && resultCode == 3){
			setResult(3);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_leftimageview:
			setResult(3);
			finish() ;
			break;
		case R.id.tv_cashin:
			MobclickAgent.onEvent(Act_Fourpart_Cashin.this, UrlConfig.point+12+"");
			startActivityForResult(new Intent(Act_Fourpart_Cashin.this,CashInAct.class).putExtra("money",stringCut.space_Cut(getIntent().getStringExtra("proAmount"))),4);
//			finish();
//			doCashInNext();
			break;
		default:
			break;
		}
	}
}
