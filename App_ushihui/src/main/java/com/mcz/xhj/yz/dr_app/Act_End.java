package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.TileTextview;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mcz.xhj.R;
import butterknife.BindView;
import okhttp3.Call;

public class Act_End extends BaseActivity implements OnClickListener{
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
	@BindView(R.id.tv_rate)
	TextView tv_rate;
	@BindView(R.id.tv_earn)
	TextView tv_earn;
	@BindView(R.id.tv_red)
	TextView tv_red;
	@BindView(R.id.tv_buy)
	TextView tv_buy;
	@BindView(R.id.tv_start)
	TextView tv_start;
	@BindView(R.id.tv_end)
	TextView tv_end;
	@BindView(R.id.tv_1)
	TextView tv_1;
	@BindView(R.id.tv_2)
	TextView tv_2;
	@BindView(R.id.tv_3)
	TextView tv_3;
	@BindView(R.id.tv_codesize)
	TextView tv_codesize;
	@BindView(R.id.tv_empty)
	TextView tv_empty;
	@BindView(R.id.tv_xutou)
	TextView tv_xutou;
	@BindView(R.id.tv_giveup)
	TextView tv_giveup;
	@BindView(R.id.ll_code)
	LinearLayout ll_code;
	@BindView(R.id.ll_activity)
	LinearLayout ll_activity;
	@BindView(R.id.ll_size)
	LinearLayout ll_size;
	@BindView(R.id.ll_empty1)
	LinearLayout ll_empty1;
	@BindView(R.id.rl_empty)
	RelativeLayout rl_empty;
	@BindView(R.id.tv_noe)
	TileTextview tv_noe;

	@BindView(R.id.ll_lift_mac)
	LinearLayout ll_lift_mac;
	@BindView(R.id.tv_gift_name)
	TextView tv_gift_name;
	@BindView(R.id.tv_modify_address)
	TextView tv_modify_address;
	@BindView(R.id.tv_address)
	TextView tv_address;
	@BindView(R.id.tv_people_name)
	TextView tv_people_name;
	@BindView(R.id.tv_people_phone)
	TextView tv_people_phone;
	@BindView(R.id.ll_used_hongbao)
	LinearLayout ll_used_hongbao;
	@BindView(R.id.ll_tian_btn)
	LinearLayout ll_tian_btn;
	@BindView(R.id.ll_gift_address)
	LinearLayout ll_gift_address;
	@BindView(R.id.tv_tian_address)
	TextView tv_tian_address;

	private String isFrom ; //0:有奖品  1：没奖品
	private String prizeName;
	private String name;
	private String pid;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_end;
	}

	@Override
	protected void initParams() {
		title_centertextview.setText("投资成功") ;
		title_righttextview.setVisibility(View.VISIBLE) ;
		title_righttextview.setText("关闭") ;
		title_leftimageview.setVisibility(View.GONE) ;
		pid = getIntent().getStringExtra("pid");
		if(!"5".equals(getIntent().getStringExtra("tv_red"))){
			register_Reg();
		}
		title_righttextview.setOnClickListener(this) ;
		tv_name.setText(getIntent().getStringExtra("tv_name")) ;
		tv_money.setText(getIntent().getStringExtra("tv_money")+"元") ;
		tv_day.setText(getIntent().getStringExtra("tv_day")+"天") ;
//		tv_rate.setText(getIntent().getStringExtra("tv_rate")+"%") ;
		tv_earn.setText(getIntent().getStringExtra("tv_earn")+"元") ;
		if(getIntent().getStringExtra("specialRate")!=null){
			if(Double.parseDouble(getIntent().getStringExtra("specialRate"))!=0){
				tv_rate.setText(getIntent().getStringExtra("tv_rate")+"%+"+getIntent().getStringExtra("specialRate")+"%") ;
			}else{
				tv_rate.setText(getIntent().getStringExtra("tv_rate")+"%") ;
			}
		}else{
			tv_rate.setText(getIntent().getStringExtra("tv_rate")+"%") ;
		}
		if(getIntent().getStringExtra("jumpURLActivity")!=null){
			if(!getIntent().getStringExtra("jumpURLActivity").equalsIgnoreCase("")){
				ll_activity.setVisibility(View.VISIBLE);
				ll_activity.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Act_End.this,WebViewActivity.class)
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
		if(getIntent().getStringExtra("luckcodesize")!=null){
			tv_codesize.setText(getIntent().getStringExtra("luckcodesize"));
		}else{
			ll_size.setVisibility(View.GONE);
		}
		if(getIntent().getStringExtra("luckcode")!=null){
			String code = getIntent().getStringExtra("luckcode");
			for (int i = 0; i < Integer.parseInt(getIntent().getStringExtra("luckcodesize")); i++) {
				if(i==0){
					tv_1.setVisibility(View.VISIBLE);
					tv_1.setText(code.split(",")[0]);
				}
				else if(i==1){
					tv_2.setVisibility(View.VISIBLE);
					tv_2.setText(code.split(",")[1]);
				}
				else if(i==2){
					tv_3.setVisibility(View.VISIBLE);
					tv_3.setText(code.split(",")[2]);
				}
			}
		}else{
			ll_code.setVisibility(View.GONE);
		}
		if("1".equals(getIntent().getStringExtra("tv_red"))){
			tv_red.setText("现金券"+getIntent().getStringExtra("nothing")+"元") ;
		}else if("2".equals(getIntent().getStringExtra("tv_red"))){
			tv_red.setText("加息券"+getIntent().getStringExtra("nothing")+"%") ;
		}else if("4".equals(getIntent().getStringExtra("tv_red"))){
			tv_red.setText("翻倍券"+getIntent().getStringExtra("nothing")+"倍") ;
		}
		else if("5".equals(getIntent().getStringExtra("tv_red"))){
			tv_red.setText("未使用") ;
			ll_empty1.setVisibility(View.GONE);
			rl_empty.setVisibility(View.GONE);
			tv_noe.setVisibility(View.VISIBLE);
			tv_empty.setVisibility(View.VISIBLE);
			tv_xutou.setVisibility(View.VISIBLE);
			tv_giveup.setVisibility(View.VISIBLE);
			image.setImageResource(R.mipmap.xinshou);
			tv_noe.setDegrees(-15);
		}
		else{
			tv_red.setText("未使用") ;
		}
		if(!"5".equals(getIntent().getStringExtra("tv_red"))){
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy.MM.dd");
			String time=format.format(date);
			tv_buy.setText(time) ;
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
		if(getIntent().getStringExtra("isFrom")!=null){
			isFrom = getIntent().getStringExtra("isFrom");
			if("0".equalsIgnoreCase(isFrom)){
				prizeName = getIntent().getStringExtra("prizeName");
				tv_gift_name.setText(prizeName);
				ll_lift_mac.setVisibility(View.VISIBLE);
				ll_used_hongbao.setVisibility(View.GONE);
			}else{
				ll_used_hongbao.setVisibility(View.VISIBLE);
				ll_lift_mac.setVisibility(View.GONE);
			}
		}else{
			ll_used_hongbao.setVisibility(View.VISIBLE);
			ll_lift_mac.setVisibility(View.GONE);
		}

//		tv_start.setText(getIntent().getStringExtra("tv_start")) ;
//		tv_end.setText(getIntent().getStringExtra("tv_end")) ;
		ll_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Act_End.this, ActivityCenterAct.class));
				finish();
			}
		});
		tv_xutou.setOnClickListener(this);
		tv_giveup.setOnClickListener(this);
		tv_tian_address.setOnClickListener(this);
		tv_modify_address.setOnClickListener(this);
	}
	private View layout;
	private PopupWindow popupWindow;
	public void showPopupWindowConpons(final String url) {
		// 加载布局
		layout = LayoutInflater.from(Act_End.this).inflate(R.layout.dialog_new_buy, null);
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
				startActivity(new Intent(Act_End.this,WebViewActivity.class)
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
	protected void onResume() {
		super.onResume();
		getReceiptAddress();
	}

	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private void register_Reg() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {
	
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							JSONObject info = map.getJSONObject("info");
                          //成立日期,到期日期
//							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;
							tv_start.setText(stringCut.getDateToString(Long.parseLong(info.getString("establish")))) ;
							tv_end.setText(stringCut.getDateToString(Long.parseLong(info.getString("expireDate")))) ;
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_End.this).show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_righttextview:
//			LocalApplication.getInstance().getDetail_Piaoju_ActFirst().finish() ;
			finish() ;
			break;
		case R.id.tv_xutou:
			startActivity(new Intent(Act_End.this,Act_Xutou.class));
			finish() ;
			break;
		case R.id.tv_giveup:
			dialog = showAlertDialog("提示","领奖机会仅一次，确定放弃?",new String[]{"放弃机会","我再想想"}, false, true, "");
			break;
		case R.id.tv_modify_address:
		case R.id.tv_tian_address:
			//到地址修改页面

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

	//获取收货地址
    public void	getReceiptAddress(){
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.GETRECEIPTADDRESS)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							if(map.getJSONObject("jsMemberInfo") != null){
								JSONObject jsMemberInfo = map.getJSONObject("jsMemberInfo");
								tv_address.setText(jsMemberInfo.getString("address")) ;
								tv_people_name.setText(jsMemberInfo.getString("name")) ;
								tv_people_phone.setText(jsMemberInfo.getString("phone")) ;
								ll_gift_address.setVisibility(View.VISIBLE);
								ll_tian_btn.setVisibility(View.GONE);
							}else{
								ll_tian_btn.setVisibility(View.VISIBLE);
								ll_gift_address.setVisibility(View.GONE);
							}
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_End.this).show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}
}
