package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.DisplayUtil;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class Detail_Tiyan extends BaseActivity implements OnClickListener {
	@BindView(R.id.ll_all)
	LinearLayout ll_all;
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_rate)
	TextView tv_rate;
	@BindView(R.id.tv_h)
	TextView tv_h;
	@BindView(R.id.tv_deadline)
	TextView tv_deadline;
	@BindView(R.id.tv_lizi)
	TextView tv_lizi;
	@BindView(R.id.tv_num)
	TextView tv_num;
	@BindView(R.id.touzi_now)
	TextView touzi_now;
	@BindView(R.id.tv_kefu)
	TextView tv_kefu;
	@BindView(R.id.sv_title)
	ScrollView sv_title;
	@BindView(R.id.ptr_invest)
	PtrClassicFrameLayout ptr_invest; // jiaxi

	@Override
	protected int getLayoutId() {
		return R.layout.act_detail_tiyan;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 4 && resultCode == 4){
			setResult(4);
			finish();
		}
	}
//	@Override
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onEvent(Login_End.this, "100009");
//	}

	Long lastClick = 0l;
	// 投资
	private void product_Invest() {
		if (System.currentTimeMillis() - lastClick <= 1000)
		{
//					ToastMaker.showShortToast("点那么快干什么");
			return;
		}
		lastClick = System.currentTimeMillis();
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.TIYAN_INVEST)
				.addParams("pid", pid)
				.addParams("ids", ids)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						ptr_invest.refreshComplete();
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							redTotal = map.getString("redTotal");
							realverify = map.getString("realverify");
							String sendRate;
							if(activityRate>0){
								sendRate = rate + "%+"+activityRate;
							}else{
								sendRate = rate+"";
							}

							startActivityForResult(new Intent(Detail_Tiyan.this,Act_Pro_End.class)
									.putExtra("tv_name", "体验金专享标")
									.putExtra("tv_money", money+"")
									.putExtra("tv_red", "5")
									.putExtra("isFrom", "0")
									.putExtra("tv_rate", rate+"")
									.putExtra("specialRate", activityRate+"")
									.putExtra("tv_earn", Money_Get())
									.putExtra("tv_day", (int)deadline+"")
//                                    .putExtra("endTime", map.getString("expireDate"))
									,4
							);
							finish();
						}
						else if ("XTWH".equals(obj.getString("errorCode"))) {
							startActivity(new Intent(Detail_Tiyan.this,WebViewActivity.class)
									.putExtra("URL", UrlConfig.WEIHU)
									.putExtra("TITLE", "系统维护"));
							return;
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您还未登录");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您不是体验标渠道用户");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您没有有效的体验金");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Tiyan.this).show_Is_Login();
						}
						else {
							ToastMaker.showShortToast("系统错误");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						ptr_invest.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("服务器异常");
					}
				});
	}

	private double deadline = 0;
	private double rate = 0;
	private double activityRate = 0;
	private double money = 0;
	//	private Integer day = 0;
//	private String resouce;
	private Integer qixian;
	private String pid;
	private String ids;
	private String redTotal;
	private String realverify;
	//	private String time;
	private void getData() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL_TIYAN)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						LogUtils.i("--->product_detail_tiyan "+response);
						ptr_invest.refreshComplete();
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						if (obj.getBoolean(("success"))) {
							touzi_now.setVisibility(View.VISIBLE);
							JSONObject map = obj.getJSONObject("map");
							JSONObject info = map.getJSONObject("Info");
							JSONObject experienceAmount = map.getJSONObject("experienceAmount");
							if(experienceAmount!=null){
								money = experienceAmount.getDoubleValue("experAmount");
								ids = experienceAmount.getString("ids");
							}
							tv_num.setText("已有"+map.getInteger("investCount")+"人赚到了钱");
							deadline = info.getInteger("deadline");
							rate = info.getDouble("rate");
							activityRate = info.getDouble("activityRate");
							pid = info.getString("id");
							String deadl = stringCut.getNumKbs(deadline)+"天";
							String rat = stringCut.getNumKbs(rate)+"%";
							SpannableStringBuilder style = new SpannableStringBuilder(deadl);
							style.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(Detail_Tiyan.this,30)),deadl.length()-1,deadl.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							tv_deadline.setText(style);
							SpannableStringBuilder styler = new SpannableStringBuilder(rat);
							styler.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(Detail_Tiyan.this,30)),rat.length()-1,rat.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							tv_rate.setText(styler);
							if(activityRate>0){
								tv_h.setText("+"+activityRate+"%");
								tv_h.setVisibility(View.VISIBLE);
							}else{
								tv_h.setVisibility(View.GONE);
							}
//							tv_title.setText(info.getString("fullName"));
						}
						else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您还未登录");
						} else if ("1002".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您不是体验标渠道用户");
						} else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("此产品不存在");
						}
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Tiyan.this).show_Is_Login();
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						ptr_invest.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}
	private SharedPreferences preferences;
	@Override
	protected void initParams() {
		title_centertextview.setText("体验标");
		title_leftimageview.setOnClickListener(this);
		title_rightimageview.setVisibility(View.GONE);
		touzi_now.setOnClickListener(this);
		preferences = LocalApplication.getInstance().sharereferences;
		String telPhone = preferences.getString("telPhone","400-******");
		tv_kefu.setText("•如有疑问请拨打客服热线："+telPhone+"；");
		ptr_invest.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
//				ptr_invest.refreshComplete();
				getData();
			}
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, sv_title, header) ;
			}
		});
		getData();
	}

	private View layout;
	private PopupWindow popupWindow;
	public void showPopupWindow() {
		// 加载布局
		layout = LayoutInflater.from(Detail_Tiyan.this).inflate(R.layout.pop_chose_red, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		TextView tv_yiyan_amount = (TextView) (layout).findViewById(R.id.tv_yiyan_amount);
		ImageView iv_close = (ImageView) (layout).findViewById(R.id.iv_close);
		final TextView tv_queren = (TextView) layout.findViewById(R.id.tv_queren);
		tv_yiyan_amount.setText(stringCut.getNumKbs(money));
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		tv_queren.setOnClickListener(this);
//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		// 监听
		tv_queren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Invest_Begin() ;
					product_Invest();
//				popupWindow.dismiss();
			}
		});
		iv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.showAsDropDown(tv_yiyan_amount);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.touzi_now:
				if(money!=0){
					showPopupWindow();
				}else{
					ToastMaker.showLongToast("您还没有体验金");
				}
				break;
			case R.id.title_leftimageview:
				finish();
				break;
			default:
				break;
		}

	}

	private String Money_Get() {
		return stringCut.getNumKbs(money
				* ((rate + activityRate)/ 360 / 100)
				* deadline);
	}

	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		if(((String) tag).equalsIgnoreCase("shezhi")){
			if (position == 0) {
				return ;
			}
			if (position == 1) {
				//startActivityForResult(new Intent(Detail_Tiyan.this, TransactionPswAct.class),1);
				startActivityForResult(new Intent(Detail_Tiyan.this, NewForgetPswActivity.class).putExtra("isFrom", 1),1);
			}
		}
	}

	/**
	 * 投资前判断以及投资
	 */
	private boolean isfirstPwd = false;//是否是第一次设置密码
	private TradePwdPopUtils pop;
	String firstPwd;
	String secondPwd;
	private void Invest_Begin(){
		firstPwd = "" ;
//		if(!hongbao_check && mlslb2 != null && !mlslb2.equals("") && fid == ""){
//			showAlertDialog("提示", "您还有可用优惠券是否使用？", new String[] { "忽略","使用" }, true, true, "hongbao_user");
//			return;
//		}
		memberSetting();
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
                                pop.showPopWindow(Detail_Tiyan.this, Detail_Tiyan.this, ll_all);
                                pop.ll_invest_money.setVisibility(View.GONE);
                                pop.ll_pwd_title.setVisibility(View.VISIBLE);
                                pop.ll_pwd.setVisibility(View.VISIBLE);
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
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
                                        //startActivityForResult(new Intent(Detail_Tiyan.this, TransactionPswAct.class),1);
										startActivityForResult(new Intent(Detail_Tiyan.this, NewForgetPswActivity.class).putExtra("isFrom", 1),1);
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
								MobclickAgent.onEvent(Detail_Tiyan.this, "100023");
								pop = new TradePwdPopUtils();
								pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

									@Override
									public void callBaceTradePwd(String pwd) {
										firstPwd = pwd;
										product_Invest();
									}
								});
								pop.showPopWindow(Detail_Tiyan.this, Detail_Tiyan.this, ll_all);
								pop.ll_invest_money.setVisibility(View.VISIBLE);
//								pop.tv_key_money.setText(et_prodetail_money.getText().toString());
								pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss();
									}
								});
								pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

									@Override
									public void onDismiss() {
									}
								}) ;
								pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										pop.popWindow.dismiss() ;
										//startActivityForResult(new Intent(Detail_Tiyan.this, TransactionPswAct.class),1);
										startActivityForResult(new Intent(Detail_Tiyan.this, NewForgetPswActivity.class).putExtra("isFrom", 1),1);
									}
								});
							}

						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Tiyan.this)
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
							new show_Dialog_IsLogin(Detail_Tiyan.this).show_Is_Login() ;
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
