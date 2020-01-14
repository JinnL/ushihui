package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_app.me.NewMyInvestmentActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class Act_Pro_End extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	
	@BindView(R.id.image)
	ImageView image;
	@BindView(R.id.tv_name)
	TextView tv_name;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.tv_day)
	TextView tv_day;
	@BindView(R.id.tv_earnname)
	TextView tv_earnname;
	@BindView(R.id.tv_earn)
	TextView tv_earn;
	@BindView(R.id.tv_end)
	TextView tv_end;
	@BindView(R.id.tv_goinvest)
	TextView tv_goinvest;
	@BindView(R.id.tv_gouser)
	TextView tv_gouser;
	@BindView(R.id.ll_activity)
	LinearLayout ll_activity;

	@BindView(R.id.tv_huikuai)
	TextView tv_huikuai;
	@BindView(R.id.tv_giftLeft)
	TextView tv_giftLeft;
	@BindView(R.id.tv_gift)
	TextView tv_gift;
	@BindView(R.id.tv_xutou)
	TextView tv_xutou;
	@BindView(R.id.tv_giveup)
	TextView tv_giveup;
	@BindView(R.id.ll_bt)
	LinearLayout ll_bt;
	@BindView(R.id.ll_shenmi)
	LinearLayout ll_shenmi;

	private String pid;

	private String isFrom ;  //0代表体验标
	private String prizeName;
	private String investId;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_pro_end;
	}

	double count;
	@Override
	protected void initParams() {
		title_centertextview.setText("") ;
		title_righttextview.setVisibility(View.VISIBLE) ;
		title_righttextview.setText("关闭") ;
		title_leftimageview.setVisibility(View.GONE) ;
		pid = getIntent().getStringExtra("pid");
		investId = getIntent().getStringExtra("investId");
		tv_goinvest.setOnClickListener(this);
		tv_gouser.setOnClickListener(this);
		tv_xutou.setOnClickListener(this);
		tv_giveup.setOnClickListener(this);
//		if(!"5".equals(getIntent().getStringExtra("tv_red"))){
//			register_Reg();
//		}
		count = Double.parseDouble(getIntent().getStringExtra("tv_money"))+Double.parseDouble(getIntent().getStringExtra("tv_earn"));
//		if(getIntent().getStringExtra("nothing")!=null){
//			if("amount".equalsIgnoreCase(getIntent().getStringExtra("nothing"))){//是否是红包
//				count = Double.parseDouble(getIntent().getStringExtra("tv_money"));
//			}
//		}
		title_righttextview.setOnClickListener(this) ;
		tv_name.setText(getIntent().getStringExtra("tv_name")) ;
		tv_money.setText("恭喜您成功投资"+stringCut.getNumKbs(Double.parseDouble(getIntent().getStringExtra("tv_money")))+"元") ;
		tv_day.setText(getIntent().getStringExtra("tv_day")+"天") ;
		tv_earn.setText(stringCut.getNumKbs(count)+"元") ;
		if(getIntent().getStringExtra("jumpURLActivity")!=null){
			if(!getIntent().getStringExtra("jumpURLActivity").equalsIgnoreCase("")){
				ll_activity.setVisibility(View.VISIBLE);
				ll_activity.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Act_Pro_End.this,WebViewActivity.class)
								.putExtra("URL", getIntent().getStringExtra("jumpURLActivity"))
								.putExtra("TITLE", "活动")
								.putExtra("BANNER", "banner")
						);
						finish();
					}
				});
			}else{
				ll_activity.setVisibility(View.GONE);
			}
		}else{
			ll_activity.setVisibility(View.GONE);
		}

		if(!"5".equals(getIntent().getStringExtra("tv_red"))){
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy.MM.dd");
			String time=format.format(date);
			if(null != getIntent().getStringExtra("isPicture") && !"".equals(getIntent().getStringExtra("isPicture"))){
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						showPopupWindowConpons(getIntent().getStringExtra("jumpURL"));
					}
				}, 1000);
			}else{

			}
		}

		//去除续投功能
//		if("xinshou".equals(getIntent().getStringExtra("tv_red"))){
//			ll_shenmi.setVisibility(View.VISIBLE);
//			ll_bt.setVisibility(View.GONE);
//			image.setImageResource(R.mipmap.xinshou);
//		}
		if(getIntent().getStringExtra("isFrom")!=null){
			if("0".equalsIgnoreCase(getIntent().getStringExtra("isFrom"))){
				tv_earnname.setText("待收收益");
				tv_huikuai.setText("预期年化收益");
				tv_earn.setText(getIntent().getStringExtra("tv_earn")+"元");
				if(getIntent().getStringExtra("specialRate")!=null){
					if(Double.parseDouble(getIntent().getStringExtra("specialRate"))!=0){
						tv_end.setText(getIntent().getStringExtra("tv_rate")+"%+"+getIntent().getStringExtra("specialRate")+"%") ;
					}else{
						tv_end.setText(getIntent().getStringExtra("tv_rate")+"%") ;
					}
				}else{
					tv_end.setText(getIntent().getStringExtra("tv_rate")+"%") ;
				}
			}
		}
		if(getIntent().getStringExtra("endTime")!=null&&!"".equalsIgnoreCase(getIntent().getStringExtra("endTime"))){
            tv_end.setText(stringCut.getDateToString(Long.parseLong(getIntent().getStringExtra("endTime")))) ;
        }
		if(getIntent().getStringExtra("prizeName")!=null){
			prizeName = getIntent().getStringExtra("prizeName");
			tv_giftLeft.setText("礼品信息");
			tv_gift.setText(prizeName);
		}
	}
	private View layout;
	private PopupWindow popupWindow;
	public void showPopupWindowConpons(final String url) {
		// 加载布局
		layout = LayoutInflater.from(Act_Pro_End.this).inflate(R.layout.dialog_new_buy, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		TextView tv_queren = (TextView) (layout).findViewById(R.id.tv_queren);
		TextView iv_close = (TextView) (layout).findViewById(R.id.iv_close);
		SimpleDraweeView iv_act_xutou = (SimpleDraweeView) (layout).findViewById(R.id.iv_act_xutou);
		Uri uri = Uri.parse(getIntent().getStringExtra("isPicture"));
		iv_act_xutou.setImageURI(uri);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		// 监听
		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		tv_queren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				startActivity(new Intent(Act_Pro_End.this,WebViewActivity.class)
						.putExtra("URL", url)
						.putExtra("TITLE", "邀请好友")
						.putExtra("BANNER", "banner")
				);
			}
		});
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//		backgroundAlpha(0.5f);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onEvent(Act_Pro_End.this, UrlConfig.point+26+"");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

//	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
//	private void register_Reg() {
//		showWaitDialog("加载中...", true, "");
//		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL)
//				.addParams("pid", pid)
//				.addParams("uid", preferences.getString("uid", ""))
//				.addParams("version", UrlConfig.version)
//				.addParams("channel", "2").build()
//				.execute(new StringCallback() {
//
//					@Override
//					public void onResponse(String response) {
//						// TODO Auto-generated method stub
//						JSONObject obj = JSON.parseObject(response);
//						dismissDialog();
//						if (obj.getBoolean(("success"))) {
//							JSONObject map = obj.getJSONObject("map");
//							JSONObject info = map.getJSONObject("info");
//                          //成立日期,到期日期
////							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;
//							tv_end.setText(stringCut.getDateToString(Long.parseLong(info.getString("expireDate")))) ;
//						} else if ("9999".equals(obj.getString("errorCode"))) {
//							ToastMaker.showShortToast("系统异常");
//						} else if ("9998".equals(obj.getString("errorCode"))) {
//							new show_Dialog_IsLogin(Act_Pro_End.this).show_Is_Login();
//						} else {
//							ToastMaker.showShortToast("系统异常");
//						}
//					}
//					@Override
//					public void onError(Call call, Exception e) {
//						// TODO Auto-generated method stub
//						dismissDialog();
//						ToastMaker.showShortToast("网络错误，请检查");
//					}
//				});
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_righttextview:
//			LocalApplication.getInstance().getDetail_Piaoju_ActFirst().finish() ;
			finish() ;
			break;
		case R.id.tv_xutou:
			startActivity(new Intent(Act_Pro_End.this,Act_Xutou.class));
			finish() ;
			break;
		case R.id.tv_giveup:
			DialogMaker.showRedSureDialog(this, "提示", "领奖机会仅一次,确定放弃？", "放弃机会", "我再想想", new DialogMaker.DialogCallBack() {
				@Override
				public void onButtonClicked(Dialog dialog, int position, Object tag) {
					dismissDialog();
				}

				@Override
				public void onCancelDialog(Dialog dialog, Object tag) {
					finish() ;
				}
			},null);
			break;

		case R.id.tv_gouser:
			MobclickAgent.onEvent(Act_Pro_End.this, UrlConfig.point+27+"");
			startActivity(new Intent(Act_Pro_End.this, NewMyInvestmentActivity.class).putExtra("flag", "0"));
			setResult(4);
			finish();
			break;
		case R.id.tv_goinvest:
			LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
			setResult(4);
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		if (position == 0) {
			finish();
		}else{
			dialog.dismiss();
		}
	}

}
