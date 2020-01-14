package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.DialogMaker.DialogCallBack;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 预约页面
 * @author DELL
 *
 */
public class ReservationActivity extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.tv_name_detail)
	TextView tv_name_detail;  //产品名称
	@BindView(R.id.amount_et)
	EditText amount_et;  //预约金额
	@BindView(R.id.touzi_now)
	Button touzi_now;  //按钮
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	
	private int prid ; // 预约规则id
	private String name ;// 预约规则名称
	private String editStr;  //输入金额
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_reservation;
	}

	@Override
	protected void initParams() {
		if(getIntent()!=null){
			prid = getIntent().getIntExtra("prid", -1);
			name = getIntent().getStringExtra("name");
		}
		title_centertextview.setText("预约下期");
		tv_name_detail.setText(name);
		title_leftimageview.setOnClickListener(this);
		touzi_now.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.touzi_now:
			editStr = amount_et.getText().toString().trim();
			if(!"".equals(editStr)){
				if((Double.parseDouble(stringCut.douHao_Cut(editStr))) % Double.parseDouble("1000") != 0){
					ToastMaker.showLongToast("投资金额为1000的倍数");
				}else if(Double.parseDouble(stringCut.douHao_Cut(editStr))>1000000){
					ToastMaker.showLongToast("投资金额需低于 1,000,000");
				}else{
					getReservation();
				}
			}
			break;
		default:
			break;
		}
	}

	/*
	 * 预约
	 * 规则id	prid	是	int	规则id
	*	用户id	uid	是	int	用户id
		*金额	amount	是	double	预约金额
	 */
	private void getReservation() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.GET_RESERVATON)
				.addParams("uid", preferences.getString("uid", ""))
				.addParam("prid", ""+prid)
				.addParam("amount", editStr)  //金額
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2")
				.build().execute(new StringCallback() {

					@SuppressLint("ResourceAsColor") @Override
					public void onResponse(String response) {
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean("success")) {
							DialogMaker.showYuyueDialog(ReservationActivity.this, R.mipmap.ok_iv, getString(R.string.yuyue_success),
									new DialogCallBack() {
								
								@Override
								public void onCancelDialog(Dialog dialog, Object tag) {
									// TODO Auto-generated method stub
								}
								
								@Override
								public void onButtonClicked(Dialog dialog, int position, Object tag) {
									// TODO Auto-generated method stub
									if(position == 1){
										dialog.cancel();
										finish();
									}
								}
							}, false,false, "");
						}else{
							if("1003".equals(obj.getString("errorCode"))){  
								DialogMaker.showYuyueDialog(ReservationActivity.this, R.mipmap.end_iv, getString(R.string.yuyue_end),
										new DialogCallBack() {
									
									@Override
									public void onCancelDialog(Dialog dialog, Object tag) {
										// TODO Auto-generated method stub
									}
									
									@Override
									public void onButtonClicked(Dialog dialog, int position, Object tag) {
										// TODO Auto-generated method stub
										if(position == 1){
											dialog.cancel();
											finish();
										}
									}
								}, false,false, "");
							}
							
							ToastMaker.showShortToast(obj.getString("errorMsg"));
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						dismissDialog();
						ToastMaker.showShortToast("请检查网络");
						// mLoadingAndRetryManager.showRetry();
						// dismissDialog();
					}
				});
				
	}
}
