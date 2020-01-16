package com.mcz.xhj.yz.dr_app;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.mcz.xhj.yz.dr_adapter.AdProDet;
import com.mcz.xhj.yz.dr_adapter.Ad_Item_Auditing;
import com.mcz.xhj.yz.dr_adapter.ProduceAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info_pic;
import com.mcz.xhj.yz.dr_bean.creditorBean;
import com.mcz.xhj.yz.dr_bean.itemAuditing_bean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.Get_Date;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CustomShareBoard;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ExpandInSroll;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;
public class Detail_Piaoju_ActFirst extends BaseActivity implements OnClickListener, OnPageChangeListener, OnTouchListener {
	@BindView(R.id.title_centertextview)
	@Nullable
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
    @Nullable
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
    @Nullable
    ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
    @Nullable
	ImageView title_leftimageview;
	@BindView(R.id.tv_name_detail)
    @Nullable
	TextView tv_name_detail; // 产品名称
	@BindView(R.id.tv_rate)
    @Nullable
	TextView tv_rate; // 产品名称
	@BindView(R.id.tv_repaytype)
    @Nullable
	TextView tv_repaytype; // 产品名称
	@BindView(R.id.progressbar_pert)
    @Nullable
    ProgressBar progressbar_pert; // 进度
	@BindView(R.id.tv_deadline)
    @Nullable
    TextView tv_deadline; // 产品期限
	@BindView(R.id.tv_leastaAmount)
    @Nullable
	TextView tv_leastaAmount; // 起投金额
	@BindView(R.id.touzi_ac_now)
    @Nullable
    Button touzi_ac_now;
	@BindView(R.id.image_isInterest)
    @Nullable
	ImageView image_isInterest;
	@BindView(R.id.image_isCash)
    @Nullable
	ImageView image_isCash;
	@BindView(R.id.ptr_invest)
    @Nullable
	PtrClassicFrameLayout ptr_invest; // jiaxi
	@BindView(R.id.lin_shouyi)
	@Nullable
	LinearLayout lin_shouyi;
	@BindView(R.id.lv_frag_pro_record)
    @Nullable
	ListView lv_frag_pro_record;
	@BindView(R.id.appointment_bt_ok)
    @Nullable
	Button appointment_bt_ok;   //立即预约
	@BindView(R.id.lv_detail_left)
    @Nullable
	LinearLayout lv_detail_left;
	@BindView(R.id.lv_detail_right)
    @Nullable
	LinearLayout lv_detail_right;
	@BindView(R.id.tv_windMeasure)
    @Nullable
	TextView tv_windMeasure;
	@BindView(R.id.tv_introduce)
	@Nullable
	TextView tv_introduce;
	@BindView(R.id.tv_repaySource)
	@Nullable
	TextView tv_repaySource;
	@Nullable
	@BindView(R.id.tv_borrower)
	TextView tv_borrower;
	@BindView(R.id.tv_ip7)
    @Nullable
	TextView tv_ip7;
	@BindView(R.id.gv_frag_pro_picture)
	@Nullable
	GridView gv_frag_pro_picture;
	@BindView(R.id.image_billtype)
    @Nullable
	ImageView image_billtype;

	@BindView(R.id.lv_frag_pro_picture)
    @Nullable
	LinearLayout lv_frag_pro_picture;

	private String pid, ptype, uid,atid;
	private SharedPreferences preferences;

	@BindView(R.id.rg_invis)
    @Nullable
	RadioGroup rg_invis;
	@BindView(R.id.rb_jieshao)
    @Nullable
	RadioButton rb_jieshao;
	@BindView(R.id.rb_record)
    @Nullable
	RadioButton rb_record;
	@BindView(R.id.rb_shuoming)
    @Nullable
	RadioButton rb_shuoming;
	@BindView(R.id.rl_no_nomessage)
    @Nullable
	RelativeLayout rl_no_nomessage;
	@BindView(R.id.image_isDouble)
    @Nullable
	ImageView image_isDouble;
	@BindView(R.id.lv_detail_mid)
    @Nullable
	LinearLayout lv_detail_mid;
	@BindView(R.id.ll_top)
    @Nullable
	LinearLayout ll_top;
	@BindView(R.id.ll_gift_act)
	@Nullable
	LinearLayout ll_gift_act;
	@BindView(R.id.ll_iphone)
	@Nullable
	LinearLayout ll_iphone;
	private String typeDetail = "1";
	@BindView(R.id.lv_creditor)
    @Nullable
	LinearLayout lv_creditor;
	@BindView(R.id.lv_frag_pro_explan)
    @Nullable
	LinearLayout lv_frag_pro_explan;
	@BindView(R.id.ll_gift)
	@Nullable
	LinearLayout ll_gift;
	@BindView(R.id.tv_creditor_num)
    @Nullable
	TextView tv_creditor_num;
	@BindView(R.id.b1)
    @Nullable
	LinearLayout b1;
	@BindView(R.id.b2)
    @Nullable
	LinearLayout b2;
	@BindView(R.id.vp_ad)
    @Nullable
	ViewPager vp_ad;
	private AdProDet adapter;
	private int CurrentNum = 0;
	@BindView(R.id.web_id)
    @Nullable
	WebView web_id;
	@BindView(R.id.im1)
    @Nullable
	ImageView im1;
	@BindView(R.id.im2)
    @Nullable
	ImageView im2;
	@BindView(R.id.pro_start)
    @Nullable
	TextView pro_start ;
	@BindView(R.id.pro_end)
    @Nullable
	TextView pro_end ;
	@BindView(R.id.iv_dan)
	@Nullable
	ImageView iv_dan ;
	@BindView(R.id.iv_dankai)
	@Nullable
	ImageView iv_dankai ;
	@BindView(R.id.tv_zuigao)
	@Nullable
	TextView tv_zuigao ;
	@BindView(R.id.tv_kairate)
	@Nullable
	TextView tv_kairate ;
	@BindView(R.id.tv_activity_rate)
    @Nullable
	TextView tv_activity_rate ;
	@BindView(R.id.lv_item_auditing)
    @Nullable
	ListView lv_item_auditing ;
	@BindView(R.id.lv_produce)
    @Nullable
	ExpandInSroll  lv_produce;
	itemAuditing_bean item_audit  ;
	private ArrayList<String> auditing_item = new ArrayList<>() ;
	private boolean realverify; // 用户是否实名
	private int prid; // 预约规则id
	private String name;// 预约规则名称
	private boolean isReservation = false; // 活动标是否可以预约下一期
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_detail_piaojufirst;
	}

	@Override
	protected void initParams() {
		ll_top.setFocusable(true);
		ll_top.setFocusableInTouchMode(true);
		ll_top.requestFocus();
		LocalApplication.getInstance().setmDetail_Piaoju_ActFirst(Detail_Piaoju_ActFirst.this);
		WebSettings settings = web_id.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setSupportZoom(true);
		web_id.setWebViewClient(new WebViewClient() {
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
				//handler.cancel(); // Android默认的处理方式
				handler.proceed();  // 接受所有网站的证书
				// handleMessage(Message msg); // 进行其他处理
			}
		});
		web_id.getSettings().setJavaScriptEnabled(true);
        web_id.getSettings().setUseWideViewPort(true);
        web_id.getSettings().setLoadWithOverviewMode(true);
		web_id.loadUrl(UrlConfig.PRODUCT_SEE);
		vp_ad.setOnTouchListener(this);
		preferences = LocalApplication.getInstance().sharereferences;
		Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            pid = uri.getQueryParameter("pid");
            ptype = uri.getQueryParameter("ptype");
            atid = uri.getQueryParameter("atid");
        }else{
        	pid = getIntent().getStringExtra("pid");
    		ptype = getIntent().getStringExtra("ptype");
    		atid = getIntent().getStringExtra("atid");
        }
        if(atid!=null){
        	ll_iphone.setVisibility(View.VISIBLE);
        	image_isCash.setVisibility(View.VISIBLE);
        	image_isCash.setImageResource(R.mipmap.songip7_d);
        }
		title_centertextview.setText("企融计划");
		lv_frag_pro_picture.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		// gv_frag_pro_picture.setOnClickListener(this);
		touzi_ac_now.setOnClickListener(this);
		lv_creditor.setOnClickListener(this);
		vp_ad.setOnPageChangeListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		vp_ad.setOnClickListener(this);
		ll_gift.setOnClickListener(this);
		iv_dan.setOnClickListener(this);
		ll_gift_act.setOnClickListener(this);
		tv_ip7.setOnClickListener(this);
		appointment_bt_ok.setOnClickListener(this);
		ptr_invest.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				getDate();
				CurrentNum = 0 ;
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});
		//
		rg_invis.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_jieshao:
					lv_detail_left.setVisibility(View.VISIBLE);
					lv_detail_mid.setVisibility(View.GONE);
					lv_detail_right.setVisibility(View.GONE);
					break;
				case R.id.rb_shuoming:
					lv_detail_mid.setVisibility(View.VISIBLE);
					lv_detail_left.setVisibility(View.GONE);
					lv_detail_right.setVisibility(View.GONE);
					break;
				case R.id.rb_record:
					lv_detail_right.setVisibility(View.VISIBLE);
					lv_detail_mid.setVisibility(View.GONE);
					lv_detail_left.setVisibility(View.GONE);
//					detail_info();
					CurrentNum = 0 ;
					break;

				default:
					break;
				}

			}
		});
		getDate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onEvent(Detail_Piaoju_ActFirst.this, "100019");
	}

	double enableAmount = 0, amount = 0, raisedRate = 0;
	Double shouyi;
	// TextView 加图片
	Drawable img_on, img_off;

	private void Pic() {
		// TODO Auto-generated method stub
		Resources res = getResources();
		img_off = res.getDrawable(R.mipmap.ico_arrows_up);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_off.setBounds(0, 0, img_off.getMinimumWidth(),
				img_off.getMinimumHeight());
		img_on = res.getDrawable(R.mipmap.ico_arrows_down);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_on.setBounds(0, 0, img_off.getMinimumWidth(),
				img_off.getMinimumHeight());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 3) {
			finish();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_xieyi_pro_zhuanrang:
			startActivity(new Intent(Detail_Piaoju_ActFirst.this,
					WebViewActivity.class)
					.putExtra("URL", UrlConfig.XIEYITITLE).putExtra("TITLE","权益转让及受让协议"));
			break;
		case R.id.title_leftimageview:
			finish();
			break;
		case R.id.title_righttextview:
			startActivity(new Intent(Detail_Piaoju_ActFirst.this,
					ProDetails.class).putExtra("pid", pid)
					.putExtra("ptype", ptype)
					.putExtra("windMeasure", windMeasure)
					.putExtra("introduce", introduce)
					.putExtra("repaySource", repaySource)
					.putExtra("borrower", borrower));
			break;

		case R.id.touzi_ac_now:
			if (!preferences.getBoolean("login", false)) {
				startActivityForResult(new Intent(Detail_Piaoju_ActFirst.this,NewLoginActivity.class), 1);
			} else {
				MobclickAgent.onEvent(Detail_Piaoju_ActFirst.this, "100020");
				memberSetting();
			}
			break;
			case R.id.appointment_bt_ok:
				if (!preferences.getBoolean("login", false)) {
					startActivityForResult(new Intent(Detail_Piaoju_ActFirst.this,NewLoginActivity.class), 1);
				} else {
					if (isReservation) {
						if (!realverify) {//否实名
							//跳到实名页面
							DialogMaker.showYuyueDialog(Detail_Piaoju_ActFirst.this, R.mipmap.user_iv,
									getString(R.string.yuyue_renzheng), new DialogMaker.DialogCallBack() {

										@Override
										public void onCancelDialog(Dialog dialog, Object tag) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onButtonClicked(Dialog dialog, int position, Object tag) {
											// TODO Auto-generated method stub
											switch (position) {
												case 1:   //右上角的取消按钮
													dialog.cancel();
													break;
												case 2:
													startActivity(new Intent(Detail_Piaoju_ActFirst.this, FourPartAct.class)
															.putExtra("flag", ""));
													dialog.cancel();
													break;
												default:
													break;
											}


										}
									}, false, true, "");
						} else {
							//跳到预约页面
							startActivity(new Intent(Detail_Piaoju_ActFirst.this, ReservationActivity.class)
									.putExtra("prid", prid).putExtra("name", name));
						}
					}
				}
				break;
		case R.id.lv_creditor:
			startActivityForResult(
					new Intent(Detail_Piaoju_ActFirst.this,
							choose_Coupons.class)
							.putExtra("creditor_pid", creditor_pid)
							.putExtra("ptype", ptype)
							.putExtra("list_creditor", "list_creditor"), 2);
			break;
		case R.id.b1:
			CurrentNum--;
			if (CurrentNum <= 0) {
				CurrentNum = 0;
			}
			vp_ad.setCurrentItem(CurrentNum);
			break;
		case R.id.b2:
			CurrentNum++;
			if (CurrentNum >= lsad_Pic.size()) {
				CurrentNum = lsad_Pic.size() - 1;
			}
			vp_ad.setCurrentItem(CurrentNum);
			break;
		case R.id.vp_ad:
			showPopupWindow1(lsad_Pic.get(vp_ad.getCurrentItem()).getBigUrl());
			break;
		case R.id.ll_gift:
			startActivity(new Intent(Detail_Piaoju_ActFirst.this, Winner_Act.class).putExtra("pid", pid));
			break;
		case R.id.ll_gift_act:
		case R.id.tv_ip7:
			if(linkUrl!=null&&!linkUrl.equalsIgnoreCase("")){
				startActivity(new Intent(Detail_Piaoju_ActFirst.this,WebViewActivity.class)
				.putExtra("URL", linkUrl)
				.putExtra("TITLE", appTitle)
				.putExtra("BANNER", "banner")
				);
			}
			break;
		case R.id.iv_dan:
			TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
	        animation.setInterpolator(new OvershootInterpolator());
	        animation.setDuration(100);
	        animation.setRepeatCount(3);
	        animation.setRepeatMode(Animation.REVERSE);
//	        vh.iv_dan.clearAnimation();
	        iv_dan.startAnimation(animation);
	        getEgeRate(pid);
			break;
		default:
			break;
		}
	}
	private void getEgeRate(String pid) {
		OkHttpUtils.post().url(UrlConfig.ZADAN)
		.addParams("id", pid)
		.addParams("uid", preferences.getString("uid", ""))
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2").build()
		.execute(new StringCallback() {

			@Override
			public void onResponse(String result) {
				JSONObject obj = JSON.parseObject(result);
				if (obj.getBoolean("success")) {
					iv_dan.setVisibility(View.GONE);
					JSONObject objmap = obj.getJSONObject("map");
					JSONObject objpage = objmap.getJSONObject("newActivityCoupon");
					iv_dankai.setVisibility(View.VISIBLE);
					tv_zuigao.setText("满标后作废哦");
					tv_kairate.setVisibility(View.VISIBLE);
					tv_kairate.setText("+"+stringCut.getNumKbs(Double.parseDouble(objpage.getString("raisedRates")))+"%");
					iv_dan.setImageResource(R.mipmap.dankai_list);
					objpage.getString("fullName");
					LocalApplication.getInstance().getMainActivity().getFrag().updateThis();
					showPopupWindowDan1(
							stringCut.getNumKbs(Double.parseDouble(objpage.getString("raisedRates"))),
							objpage.getString("fullName"),
							objpage.getString("pid"),
							objpage.getString("type")
							);
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				iv_dan.clearAnimation();
				ToastMaker.showShortToast("请检查网络");
			}
		});
	}
	private RelativeLayout layout1;
	@SuppressLint("NewApi")
	public void showPopupWindowDan1(String rate,String name,final String pid,final String ptype) {
		// 加载布局
		layout1 = (RelativeLayout) LayoutInflater.from(Detail_Piaoju_ActFirst.this).inflate(R.layout.pop_zadan1, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout1, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
		TextView iv_regist = (TextView) (layout1).findViewById(R.id.iv_regist);
		TextView tv_rate = (TextView) (layout1).findViewById(R.id.tv_rate);
		TextView tv_biao = (TextView) (layout1).findViewById(R.id.tv_biao);
		TextView tv_use = (TextView) (layout1).findViewById(R.id.tv_use);
		TextView tv_share = (TextView) (layout1).findViewById(R.id.tv_share);
		tv_biao.setText("限"+name+"使用");
		tv_rate.setText(rate);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		// 监听
		tv_use.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		tv_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				postShare(pid);
			}
		});
		iv_regist.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		popupWindow.showAsDropDown(iv_regist);
	}
	private LinearLayout layout;
	private PopupWindow popupWindow;
	private SimpleDraweeView iv_regist;

	public void showPopupWindow1(String url_image) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater
				.from(Detail_Piaoju_ActFirst.this).inflate(
						R.layout.dialog_image_big, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		iv_regist = (SimpleDraweeView) (layout).findViewById(R.id.iv_regist);
		Uri uri = Uri.parse(url_image);
		DraweeView<DraweeHierarchy> mDraweeView = new DraweeView<>(
				Detail_Piaoju_ActFirst.this);
		iv_regist.setAspectRatio(1f);
		int width = 110, height = 110;
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
				.setResizeOptions(new ResizeOptions(width, height)).build();
		PipelineDraweeController controller = (PipelineDraweeController) Fresco
				.newDraweeControllerBuilder()
				.setOldController(mDraweeView.getController())
				.setImageRequest(request).build();
		iv_regist.setController(controller);

		iv_regist.setImageURI(uri);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
		popupWindow.showAsDropDown(iv_regist);
		// backgroundAlpha(0.5f);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = Detail_Piaoju_ActFirst.this.getWindow()
				.getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		Detail_Piaoju_ActFirst.this.getWindow().setAttributes(lp);
	}

	private void checked_hongbao() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mlslb2.size(); i++) {
			if (mlslb2.get(i).getEnableAmount() > Double
					.parseDouble(surplusAmount)) {
				mlslb2.remove(i);
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

	private String surplusAmount; // 剩余金额
	private String deadline;
	private String endDate;
	private String rate_h, rate;
	private String fid = ""; // 可用红包id
	private SpannableString ss, ss_record;
	private ArrayList<ConponsBean> mlslb2; // 红包列表
	private ArrayList<creditorBean> creditorList;
	private String linkUrl = null;
	private String appTitle = null;
	private double specialRate;
	private boolean isOldUser = false;
	private String doubleEggrule;

	private void getDate() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						detail_info();
						ptr_invest.refreshComplete();
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							balance = map.getString("balance");
							appTitle = map.getString("appTitle");
							specialRate = map.getDouble("specialRate");
							doubleEggrule = map.getString("doubleEggrule");
							JSONObject info = map.getJSONObject("info");
							double exRate = specialRate + info.getDouble("activityRate");
							if(exRate!=0){
								tv_activity_rate.setVisibility(View.VISIBLE);
								tv_activity_rate.setText("+"+exRate+"%");
							}else {
//								tv_activity_require.setVisibility(View.GONE);
								tv_activity_rate.setVisibility(View.GONE);
							}
							if(appTitle!=null){
								if(!appTitle.equalsIgnoreCase("")){
									ll_gift_act.setVisibility(View.VISIBLE);
									tv_ip7.setText(appTitle);
									if(map.getString("linkURL")!=null){
										if(!map.getString("linkURL").equalsIgnoreCase("")){
											linkUrl = map.getString("linkURL");
										}
									}
								}else{
									ll_gift_act.setVisibility(View.GONE);
								}
							}else{
								ll_gift_act.setVisibility(View.GONE);
							}
							JSONArray list = map.getJSONArray("extendInfos");
							if (null == list) {
							} else {
								if (list.size() <= 0) {
									lv_frag_pro_explan.setVisibility(View.GONE);
								} else {
									creditorList = (ArrayList<creditorBean>) JSON.parseArray(list.toJSONString(),creditorBean.class);
									lv_produce.setAdapter(new ProduceAdapter(Detail_Piaoju_ActFirst.this,creditorList));
								}
							}
							// 票据类型 1=商票，2=银票
							status_type(info.getString("billType"), false);
							if(info.getString("maxActivityCoupon")!=null){
//								iv_danact.setClickable(false);
								tv_zuigao.setVisibility(View.VISIBLE);
								if(info.getInteger("isEgg")==1){
									iv_dan.setVisibility(View.VISIBLE);
									iv_dankai.setVisibility(View.GONE);
									tv_zuigao.setText("最高"+stringCut.getNumKbs(Double.parseDouble(info.getString("maxActivityCoupon")))+"%加息");
									tv_kairate.setVisibility(View.GONE);
									iv_dan.setImageResource(R.mipmap.dan_list);
								}
								else if (info.getInteger("isEgg")==2){
									iv_dan.setVisibility(View.GONE);
									iv_dankai.setVisibility(View.VISIBLE);
									tv_zuigao.setText("满标后作废哦");
									tv_kairate.setVisibility(View.VISIBLE);
									tv_kairate.setText("+"+stringCut.getNumKbs(Double.parseDouble(info.getString("maxActivityCoupon")))+"%");
									iv_dan.setImageResource(R.mipmap.dankai_list);
								}
							}else{

							}
							//成立日期,到期日期
//							pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;

							is_fid(info.getString("fid"), map.getString("preProInvestNums"));
							if(atid!=null){
					        	image_isCash.setVisibility(View.VISIBLE);
					        	image_isCash.setImageResource(R.mipmap.songip7_d);
					        }else{
					        	if ("1".equals(info.getString("isInterest"))) {
									image_isInterest.setVisibility(View.VISIBLE);
								}
								if ("1".equals(info.getString("isCash"))) {
									image_isCash.setVisibility(View.VISIBLE);
								}
								if ("1".equals(info.getString("isDouble"))) {
									image_isDouble.setVisibility(View.VISIBLE);
								}
					        }
							// if(info.getString("accept")!=null&&!info.getString("accept").equalsIgnoreCase("")){
							// tv_piaofrom.setText(info.getString("accept"));
							// tv_piaofrom.setVisibility(View.VISIBLE);
							// }else{
							// tv_piaofrom.setVisibility(View.GONE);
							// }
							tv_name_detail.setText(info.getString("fullName"));
							if(info.getInteger("repayType")==1){
								tv_repaytype.setText("到期还本付息");
							}else if(info.getInteger("repayType")==2){
								tv_repaytype.setText("按月付息到期还本");
							}
							proAnimator(Integer.valueOf(stringCut.pertCut(info.getString("pert"))).intValue()) ;
							rate = info.getString("rate");
							deadline = info.getString("deadline");
							maxAmount = info.getString("maxAmount");
							increasAmount = info.getString("increasAmount");
							surplusAmount = info.getString("surplusAmount");
							leastaAmount = info.getString("leastaAmount");

							pro_start.setText("剩余可投(元)    "+stringCut.getNumKbs(Double.parseDouble(surplusAmount)));
							if(info.getString("establish")!=null){
								pro_end.setText("剩余期限    "+stringCut.getSpecifiedDay(Long.parseLong(info.getString("establish"))));
							}
							rate_h = stringCut.getNumKbs(Double.parseDouble(rate)) + "%";
							tv_rate.setText(rate_h);
							tv_deadline.setText("期限" + deadline + "天 ");
							tv_leastaAmount.setText("起投"
									+ stringCut.getNumKbs(Double
											.parseDouble(leastaAmount)) + "元");
							if ("6".equals(info.getString("status"))) {
								no_checked();
								status_type(info.getString("billType"), true);
								touzi_ac_now.setText("募集结束 ");
								// tv_last_day.setText("已结束");
							} else if ("8".equals(info.getString("status"))) {
								no_checked();
								status_type(info.getString("billType"), true);
								touzi_ac_now.setText("已计息");
							} else if ("9".equals(info.getString("status"))) {
								no_checked();
								status_type(info.getString("billType"), true);
								touzi_ac_now.setText("已回款");
							} else {
								endDate = info.getString("endDate");
							}
							if(!"5".equals(info.getString("status"))){
								if(!"".equals(map.get("isReservation"))&&map.get("isReservation")!=null){
									prid = map.getIntValue("prid"); // 预约规则id
									name = map.getString("name");// 预约规则名称
									isReservation = map.getBoolean("isReservation"); // 活动标是否可以预约下一期
									if(!"".equals(map.getString("realverify"))&&map.getString("realverify")!=null){
										if(!"".equals(map.getBoolean("realverify"))&&map.getBoolean("realverify")!=null){
											realverify = map.getBoolean("realverify"); // 用户是否实名
										}
									}
									if(isReservation){  //可以预约
										appointment_bt_ok.setVisibility(View.VISIBLE);
									}else{
										appointment_bt_ok.setVisibility(View.GONE);
									}
								}
							}else{
								appointment_bt_ok.setVisibility(View.GONE);
							}
							// 详情信息
							windMeasure = info.getString("windMeasure");
							introduce = info.getString("introduce");
							repaySource = info.getString("repaySource");
							borrower = info.getString("borrower");
//							detailText();
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Piaoju_ActFirst.this)
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
						ToastMaker.showShortToast("请检查网络");
					}
				});

	}

	private String creditor_pid;

	// 续发
	private void is_fid(String fid, String preProInvestNums) {
		if (!("".equals(fid) || null == fid)) {
			lv_creditor.setVisibility(View.VISIBLE);
			creditor_pid = fid;
			if (!("".equals(preProInvestNums) || null == preProInvestNums)) {
				tv_creditor_num.setText("共有" + preProInvestNums + "人持有");
			}
			// detail_info_creditor(fid) ;
		} else {
			lv_creditor.setVisibility(View.GONE);
		}
	}

	private void status_type(String billType, Boolean nochecked) {
		// 票据类型 1=商票，2=银票
		if (!("".equals(billType) || null == billType)) {
			if (nochecked) {
				if ("1".equals(billType)) {
					image_billtype
							.setBackgroundResource(R.mipmap.icon_trade_p_gray);
				} else if ("2".equals(billType)) {
					image_billtype
							.setBackgroundResource(R.mipmap.icon_bank_p_gray);
				}
			} else {
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

	// 不可以
	private void no_checked() {
		tv_activity_rate.setVisibility(View.GONE);
		tv_rate.setTextColor(0xffA0A0A0);
		progressbar_pert.setSecondaryProgress(0);
		// tv_pert_num.setTextColor(0xffA0A0A0);
		touzi_ac_now.setFocusable(false);
		touzi_ac_now.setEnabled(false);
		image_isInterest.setImageResource(R.mipmap.icon_raise_g);
		image_isCash.setImageResource(R.mipmap.icon_cash_g);
		image_isDouble.setImageResource(R.mipmap.double_g);
		touzi_ac_now.setBackgroundColor(0xffA0A0A0);

	}

	// 投资
	private void product_Invest() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.INVEST).addParams("pid", pid)
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
							ToastMaker.showShortToast("投资成功");
							finish();
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("交易密码错误");
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
							new show_Dialog_IsLogin(Detail_Piaoju_ActFirst.this)
									.show_Is_Login();
						} else if ("1010".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("优惠券不可用");
						} else {
							ToastMaker.showShortToast("系统异常");
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
							List<InvestListBean> mlslb2 = JSON.parseArray(
									list.toJSONString(), InvestListBean.class);
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Piaoju_ActFirst.this)
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

	private class GetDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			return Get_Date.get_Internet_Time(endDate);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// tv_last_day.setText(result);
		}

	}

	class lv_adapter extends BaseAdapter {
		private List<bean_Detail_Info> al;
		private String type;

		public lv_adapter(List<bean_Detail_Info> lsad, String type) {
			this.al = lsad;
			this.type = type;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return al.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = new ViewHolder();

			if (convertView == null) {
				convertView = View.inflate(Detail_Piaoju_ActFirst.this,
						R.layout.adapter_frag_prodetail_record, null);
				vh.adapter_view = convertView.findViewById(R.id.adapter_view);
				vh.tv_person = (TextView) convertView
						.findViewById(R.id.tv_person);
				vh.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// if ("1".equals(lsad.get(position).getSex())) {
			// vh.tv_person.setText(lsad.get(position).getRealName()
			// .substring(0, 1)
			// + "先生");
			// } else if ("2".equals(lsad.get(position).getSex())) {
			// vh.tv_person.setText(lsad.get(position).getRealName()
			// .substring(0, 1)
			// + "女士");
			// }
			if (lsad.get(position).getMobilephone() != null) {
				vh.tv_person.setText(stringCut.phoneCut(lsad.get(position).getMobilephone()));
			}
			vh.tv_money.setText(lsad.get(position).getAmount() + "元");
			vh.tv_time.setText(stringCut.getDateYearToString(Long
					.parseLong(lsad.get(position).getInvestTime()))
					+ "\n"
					+ stringCut.getDateHourToString(Long.parseLong(lsad.get(
							position).getInvestTime())));

			if (al.size() - 1 != position) {
				vh.adapter_view.setBackgroundColor(0xffD2D2D2);
			}

			return convertView;
		}

	}

	private class ViewHolder {
		private TextView tv_person, tv_money, tv_time;

		private TextView title, tv_name;
		private View adapter_view;
		private SimpleDraweeView image;

	}

	private List<bean_Detail_Info> lsad = new ArrayList<bean_Detail_Info>();
	private List<bean_Detail_Info_pic> lsad_Pic = new ArrayList<bean_Detail_Info_pic>();

	private void detail_info() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.DETAIL_INFO).addParam("pid", pid)
				.addParam("type", ptype)
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2")
				.build().execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							is_projectList(map.getJSONArray("projectList")) ;
							JSONArray investList = map
									.getJSONArray("investList");
							lsad = JSON.parseArray(investList.toJSONString(),
									bean_Detail_Info.class);
							if (lsad.size() <= 0) {
								rl_no_nomessage.setVisibility(View.VISIBLE);
								lv_frag_pro_record.setVisibility(View.GONE);
							} else {
								rl_no_nomessage.setVisibility(View.GONE);
								lv_frag_pro_record.setVisibility(View.VISIBLE);
								lv_frag_pro_record.setAdapter(new lv_adapter(lsad, typeDetail));
								String str_record = "" ;
								if(lsad.size() > 99){
									str_record  = "投资记录(99+)";
								}else{
									str_record = "投资记录(" + lsad.size()
										+ "人)";
								}
								ss_record = new SpannableString(str_record);
								ss_record.setSpan(new RelativeSizeSpan(1f), 0,
										4, TypedValue.COMPLEX_UNIT_PX);
								ss_record.setSpan(new RelativeSizeSpan(0.8f),
										4, str_record.length(),
										TypedValue.COMPLEX_UNIT_PX);
								rb_record.setText(ss_record);
								fixListViewHeight(lv_frag_pro_record);
							}
							JSONArray picList = map.getJSONArray("picList");
							lsad_Pic = JSON.parseArray(picList.toJSONString(),
									bean_Detail_Info_pic.class);
							if (lsad_Pic.size() <= 0) {
								lv_frag_pro_picture.setVisibility(View.GONE);
							} else {
								lv_frag_pro_picture.setVisibility(View.VISIBLE);
								L_O_R() ;
								adapter = new AdProDet(lsad_Pic,Detail_Piaoju_ActFirst.this, pid);
								vp_ad.setAdapter(adapter);

							}

						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
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
	private void is_projectList(JSONArray jsonArray) {
		if (!("".equals(jsonArray) || null == jsonArray)) {
			auditing_item.clear() ;
		    for(int i = 0 ;i<jsonArray.size();i++){
			  auditing_item.add(jsonArray.get(i).toString()) ;
		    }
		    lv_item_auditing.setAdapter(new Ad_Item_Auditing(Detail_Piaoju_ActFirst.this,auditing_item)) ;
		}
	}
	private void L_O_R() {
		if(lsad_Pic.size() == 1){
			im1.setVisibility(View.GONE);
			im2.setVisibility(View.GONE);
			b1.setFocusable(false);
			b1.setEnabled(false);
			b2.setFocusable(false);
			b2.setEnabled(false);
		}else if(lsad_Pic.size() > 1){
			im1.setVisibility(View.GONE);
			im2.setVisibility(View.VISIBLE);
			b2.setFocusable(true);
			b2.setEnabled(true);
		}
	}
	// 控制listview高度
	public void fixListViewHeight(ListView listView) {

		// 如果没有设置数据适配器，则ListView没有子项，返回。

		ListAdapter listAdapter = listView.getAdapter();

		int totalHeight = 0;

		if (listAdapter == null) {

			return;

		}

		for (int index = 0, len = listAdapter.getCount(); index < len; index++) {

			View listViewItem = listAdapter.getView(index, null, listView);

			// 计算子项View 的宽高

			listViewItem.measure(0, 0);

			// 计算所有子项的高度和

			totalHeight += listViewItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		// listView.getDividerHeight()获取子项间分隔符的高度

		// params.height设置ListView完全显示需要的高度

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

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
							String realVerify = map.getString("realVerify");
							String tpwdFlag = map.getString("tpwdFlag");
							SharedPreferences.Editor editor = preferences
									.edit();
							editor.putString("realVerify", realVerify);
							editor.putString("tpwdFlag", tpwdFlag);
							editor.commit();

							if ("2".equals(ptype)) {
								startActivityForResult(
										new Intent(Detail_Piaoju_ActFirst.this,
												Detail_Piaoju_Act.class)
												.putExtra("pid", pid)
												.putExtra("atid", atid)
												.putExtra("ptype", ptype)
												.putExtra("tpwdFlag", tpwdFlag)
												.putExtra("realVerify",realVerify), 1);
							} else if ("3".equals(ptype)) {
								startActivityForResult(
										new Intent(Detail_Piaoju_ActFirst.this,
												Detail_Piaoju_Act.class)
												.putExtra("pid", pid)
												.putExtra("atid", atid)
												.putExtra("ptype", ptype)
												.putExtra("tpwdFlag", tpwdFlag)
												.putExtra("realVerify",realVerify), 1);

							}

						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Detail_Piaoju_ActFirst.this)
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		CurrentNum = arg0;
		if (CurrentNum <= 0) {
			b1.setFocusable(false);
			b1.setEnabled(false);
			b2.setFocusable(true);
			b2.setEnabled(true);
			im1.setVisibility(View.GONE);
			im2.setVisibility(View.VISIBLE);
		} else if (CurrentNum == lsad_Pic.size() - 1) {
			b2.setFocusable(false);
			b2.setEnabled(false);
			b1.setFocusable(true);
			b1.setEnabled(true);
			im1.setVisibility(View.VISIBLE);
			im2.setVisibility(View.GONE);
		}else{
			im1.setVisibility(View.VISIBLE);
			im2.setVisibility(View.VISIBLE);
			b2.setFocusable(true);
			b2.setEnabled(true);
			b1.setFocusable(true);
			b1.setEnabled(true);
		}

	}
	public void postShare(String pid) {
        CustomShareBoard shareBoard = new CustomShareBoard(Detail_Piaoju_ActFirst.this,pid,"","");
        shareBoard.showAtLocation(Detail_Piaoju_ActFirst.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.vp_ad:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (lsad_Pic.size() > 0) {
					showPopupWindow1(lsad_Pic.get(CurrentNum).getBigUrl());
				}
			}
			break;

		default:
			break;
		}
		return true;
	}

}
