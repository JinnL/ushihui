/**
 * 
 */

package com.mcz.xhj.yz.dr_view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.mcz.xhj.yz.EventMessage.EvmInvest;
import com.mcz.xhj.yz.dr_app.Detail_Piaoju_ActFirst;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.EgeGetRate;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

//    private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
    private Activity mActivity;
    private String pid = "";
    private String url = "";
	private String flag= null;
	private String giftMoney = "";
    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    public CustomShareBoard(Activity mActivity, String pid, String url, String flag, String giftMoney) {
    	super();
    	this.mActivity = mActivity;
    	this.flag = flag;
		this.pid = pid;
		this.url = url;
		this.giftMoney = giftMoney;
    	initView(mActivity);
    }
    public CustomShareBoard(Activity mActivity, String pid, String url, String flag) {
    	super();
    	this.mActivity = mActivity;
		this.flag = flag;
    	this.pid = pid;
    	this.url = url;
    	initView(mActivity);
    }

	private String recommCodes ;
    @SuppressWarnings("deprecation")
    private void initView(Context context) {
    	SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    	recommCodes = preferences.getString("phone", "") ;
//    	if("".equals(preferences.getString("recommCodes", ""))){
//			recommCodes = preferences.getString("phone", "") ;
//		}else{
//			recommCodes = preferences.getString("recommCodes", "") ;
//		}
//    	Config.dialog = dialog;
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
        LinearLayout wechat = (LinearLayout) rootView.findViewById(R.id.wechat);
		LinearLayout wechat_circle = (LinearLayout) rootView.findViewById(R.id.wechat_circle);
		LinearLayout qq = (LinearLayout) rootView.findViewById(R.id.qq);
		wechat.setOnClickListener(this);
		wechat_circle.setOnClickListener(this);
		qq.setOnClickListener(this);
		if(flag!=null){
			if(flag.equalsIgnoreCase("zhengchang")){
				wechat.setVisibility(View.VISIBLE);
				qq.setVisibility(View.VISIBLE);
			}else if(flag.startsWith("newyearshare")){   //年末分享
				wechat.setVisibility(View.VISIBLE);
				qq.setVisibility(View.GONE);
			}else {
				wechat.setVisibility(View.VISIBLE);
				qq.setVisibility(View.VISIBLE);
			}
		}
        rootView.findViewById(R.id.bt_cancal).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismiss();
				return true;
			}
		});
    }
    private UMImage image;
	private String title;
    @Override
    public void onClick(View v) {
        int id = v.getId();
		if(flag!=null){
			if(flag.equalsIgnoreCase("zhengchang")){
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/newbie.png");
			}else if(flag.equalsIgnoreCase("shuangdan")){
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/activity_shangdan.png");
			}else if(flag.startsWith("newyearshare")){
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/luckyMoney.jpg");
			}
			else if(flag.startsWith("toujisong")){
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/app/toujisong_fenxiang.jpg");
			}
			else if(flag.startsWith("app2lottery")){
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/app2lottery.png");
			}
			else if (flag.startsWith("special")){//special
				image = new UMImage(mActivity, "https://www.zhihuicai360.com/images/ip7.png");
			}
		}else{
			image = new UMImage(mActivity, UrlConfig.TUPIAN);
		}
        switch (id) {
            case R.id.wechat:
				performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
            	performSharewechat_circle(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
				if(Build.VERSION.SDK_INT>=23){
					if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
						//申请权限
						ToastMaker.showLongToast("分享QQ需要允许此权限");
						String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
						ActivityCompat.requestPermissions(mActivity,mPermissionList,123);
//						ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},112);
					}else{
						performShare(SHARE_MEDIA.QQ);
					}
				}else {
					performShare(SHARE_MEDIA.QQ);
				}
//				if(Build.VERSION.SDK_INT>=23){
////				Manifest.permission.READ_PHONE_STATE,Manifest.permission.GET_ACCOUNTS,,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.WRITE_APN_SETTINGS
//					String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//					ActivityCompat.requestPermissions(mActivity,mPermissionList,123);
//				}
                break;
            case R.id.bt_cancal:
            	dismiss();
            	break;
            default:
                break;
        }
    }

    private void performSharewechat_circle(SHARE_MEDIA platform) {
		showWaitDialog("请稍后...", true, "");
		if(flag.equalsIgnoreCase("shuangdan")){
			UMWeb web = new UMWeb(url+"?uid="+preferences.getString("uid", ""));
			web.setTitle("我在参加双蛋party，抽中了"+giftMoney+"元现金,你也来参加吧！");//标题
			web.setThumb(image);  //缩略图
			web.setDescription("我在参加双蛋party，抽中了"+giftMoney+"元现金,你也来参加吧！");//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		}else if(flag.startsWith("toujisong")){
			UMWeb web = new UMWeb(url);
			web.setTitle("不花钱也能消费，爆款好礼免费送！");//标题
			web.setThumb(image);  //缩略图
			web.setDescription("智慧投资，0元消费~ ");//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		} else if(flag.startsWith("newyearshare")){
			UMWeb web = new UMWeb(url+"&recommPhone=" + recommCodes);
			web.setTitle("鸡冻！这个年纪居然领到了" + giftMoney + "元压岁钱,无关年龄，是\"宝宝\"的都快来领！");//标题
			web.setThumb(image);  //缩略图
			web.setDescription("鸡冻！这个年纪居然领到了" + giftMoney + "元压岁钱,无关年龄，是\"宝宝\"的都快来领！");//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
			if(("newyearsharefalse").equals(flag)){
				getLuckyMoney();  //第一次分享领取
			}

		} else if(flag.startsWith("app2lottery")){
			UMWeb web = new UMWeb(url);
			web.setTitle("翻出一个亿，我们不做平凡人!");//标题
			web.setThumb(image);  //缩略图
			web.setDescription("送福利！点击即可参与幸运翻翻乐，红包、加息券等大奖等你拿");//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		}else if(flag.startsWith("special")){
			UMWeb web = new UMWeb(url);
			web.setTitle("你换iPhone7我买单!");//标题
			web.setThumb(image);  //缩略图
			web.setDescription("惊“红”不止一瞥，红色特别版现已加入");//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		}  else if(!url.equalsIgnoreCase("")){
			UMWeb web = new UMWeb(url+"?recommCode="+recommCodes);
			web.setTitle(mActivity.getResources().getString(R.string.triple_title));//标题
			web.setThumb(image);  //缩略图
			web.setDescription(mActivity.getResources().getString(R.string.yaoqing2));//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
    	}else{
			UMWeb web = new UMWeb(UrlConfig.YAOZHUCE+"&recommCode=" + recommCodes);
			web.setTitle(mActivity.getResources().getString(R.string.yaoqing));//标题
			web.setThumb(image);  //缩略图
			web.setDescription(mActivity.getResources().getString(R.string.yaoqing));//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
    	}

    }
    private void performShare(SHARE_MEDIA platform) {
		showWaitDialog("请稍后...", false, "");
		if(flag!=null){
			if(flag.equalsIgnoreCase("zhengchang")){
				UMWeb web = new UMWeb(url+"?recommCode=" + recommCodes);
				web.setTitle(mActivity.getResources().getString(R.string.triple_title));//标题
				web.setThumb(image);  //缩略图
				web.setDescription(mActivity.getResources().getString(R.string.triple_dec));//描述
				new ShareAction(mActivity)
						.setPlatform(platform)
						.withMedia(web)
						.setCallback(umShareListener)
						.share();
			}else if(flag.startsWith("newyearshare")){
				UMWeb web = new UMWeb(url+"&recommPhone=" + recommCodes);
				web.setTitle("快来领压岁钱");//标题
				web.setThumb(image);  //缩略图
				web.setDescription("鸡冻！这个年纪居然领到了"+giftMoney+"元压岁钱,无关年龄，是\"宝宝\"的都快来领！");//描述
				new ShareAction(mActivity)
						.setPlatform(platform)
						.withMedia(web)
						.setCallback(umShareListener)
						.share();
				if(("newyearsharefalse").equals(flag)){
					getLuckyMoney();  //第一次分享领取
				}
			}else if(flag.startsWith("toujisong")) {
				UMWeb web = new UMWeb(url);
				web.setTitle("不花钱也能消费，爆款好礼免费送！");//标题
				web.setThumb(image);  //缩略图
				web.setDescription("智慧投资，0元消费~ ");//描述
				new ShareAction(mActivity)
						.setPlatform(platform)
						.withMedia(web)
						.setCallback(umShareListener)
						.share();
			}else if(flag.startsWith("app2lottery")){
				UMWeb web = new UMWeb(url);
				web.setTitle("翻出一个亿，我们不做平凡人！");//标题
				web.setThumb(image);  //缩略图
				web.setDescription("送福利！点击即可参与幸运翻翻乐，红包、加息券等大奖等你拿");//描述
				new ShareAction(mActivity)
						.setPlatform(platform)
						.withMedia(web)
						.setCallback(umShareListener)
						.share();
			}
			else if(flag.startsWith("special")){
				UMWeb web = new UMWeb(url);
				web.setTitle("你换iPhone7我买单!");//标题
				web.setThumb(image);  //缩略图
				web.setDescription("惊“红”不止一瞥，红色特别版现已加入");//描述
				new ShareAction(mActivity)
						.setPlatform(platform)
						.withMedia(web)
						.setCallback(umShareListener)
						.share();
			}
		}

		else{
			UMWeb web = new UMWeb(UrlConfig.YAOZHUCE+"&recommCode=" + recommCodes);
			web.setTitle("小行家");//标题
			web.setThumb(image);  //缩略图
			web.setDescription(mActivity.getResources().getString(R.string.yaoqing));//描述
			new ShareAction(mActivity)
					.setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		}
    }
	protected Dialog dialog;
	/**
	 * 等待对话框
	 *
	 * @author blue
	 */
	public Dialog showWaitDialog(String msg, boolean isCanCancelabel, Object tag)
	{
		if (null == dialog || !dialog.isShowing())
		{
			if(!mActivity.isFinishing()){
				dialog = DialogMaker.showCommenWaitDialog(mActivity, msg, new DialogMaker.DialogCallBack() {
					@Override
					public void onButtonClicked(Dialog dialog, int position, Object tag) {

					}

					@Override
					public void onCancelDialog(Dialog dialog, Object tag) {

					}
				},isCanCancelabel, tag);
			}
		}
		return dialog;
	}

    private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA platform) {
			LogUtils.i("--->分享 onStart");
			dialog.dismiss();
		}
    	
        @Override
        public void onResult(SHARE_MEDIA platform) {
			LogUtils.i("--->分享 onResult");
			dialog.dismiss();
//          Toast.makeText(mActivity," 分享成功了", Toast.LENGTH_SHORT).show();
//        	InvestAdapter adapter = new InvestAdapter();
//        	adapter.dismis();
        	if(!pid.equalsIgnoreCase("")&&url.equalsIgnoreCase("")){
        		showPopupWindowRegist();
        	}
			else if(("newyearsharefalse").equals(flag)){
				mActivity.startActivity(new Intent(mActivity, WebViewActivity.class)
					.putExtra("URL", UrlConfig.NEWYEARSHARE +"?phone=" + preferences.getString("phone", ""))
					.putExtra("TITLE", "小行家陪你过大年")
					.putExtra("BANNER", "banner"));
			}else if(("app2lottery").equals(flag)){
				putFaipai();
			}

			dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
			dialog.dismiss();
            Toast.makeText(mActivity," 分享失败了", Toast.LENGTH_SHORT).show();
//            dismiss();
			String message = t.getMessage();
			//UmengTool.getSignature(LocalApplication.applicationContext);

			LogUtils.i("--->分享 "+message+t.getLocalizedMessage()+platform.toString());
		}

        @Override
        public void onCancel(SHARE_MEDIA platform) {
			LogUtils.i("--->分享取消了 onCancel");
			dialog.dismiss();
            Toast.makeText(mActivity," 分享取消了", Toast.LENGTH_SHORT).show();
//        	dismiss();
//            showPopupWindowRegist();
        }
    };
    
    
    private RelativeLayout layout;
	private PopupWindow popupWindow;
	@SuppressLint("NewApi")
	public void showPopupWindowDan2(final EgeGetRate egr1, EgeGetRate egr2) {
//		LocalApplication.getInstance().getMainActivity().onCreate();
		// 加载布局
		layout = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.pop_zadan2, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
		TextView iv_regist = (TextView) (layout).findViewById(R.id.iv_regist);
		TextView tv_rate = (TextView) (layout).findViewById(R.id.tv_rate);
		TextView tv_biao = (TextView) (layout).findViewById(R.id.tv_biao);
		TextView tv_use = (TextView) (layout).findViewById(R.id.tv_use);
		TextView tv_rate_fei = (TextView) (layout).findViewById(R.id.tv_rate_fei);
		TextView tv_biao_fei = (TextView) (layout).findViewById(R.id.tv_biao_fei);
		tv_rate.setText(stringCut.getNumKbs(Double.parseDouble(egr1.getRaisedRates())));
		tv_biao.setText("限"+egr1.getFullName()+"使用");
		tv_rate_fei.setText(stringCut.getNumKbs(Double.parseDouble(egr2.getRaisedRates())));
		tv_biao_fei.setText("限"+egr2.getFullName()+"使用");
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
				mActivity.startActivity(new Intent(mActivity,Detail_Piaoju_ActFirst.class)
				.putExtra("pid",egr1.getPid())
				.putExtra("ptype", egr1.getType())
				);
				
			}
		});
		iv_regist.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
//		adapter.onDateChange(list);
		LocalApplication.getInstance().getMainActivity().getFrag().updateThis();
		popupWindow.showAsDropDown(iv_regist);
		EventBus.getDefault().post(new EvmInvest(true));
	}
	
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	public void showPopupWindowRegist() {
		// 加载布局
		layout = (RelativeLayout) LayoutInflater.from(mActivity).inflate(
				R.layout.pop_zaiza, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
		final ImageView iv_regist = (ImageView) layout.findViewById(R.id.imageView4);
		ImageView iv_regist2 = (ImageView) layout.findViewById(R.id.imageView2);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		// 监听
		/** 设置旋转动画 */ 
		final RotateAnimation animation = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		animation.setDuration(40000);//设置动画持续时间 
		/** 常用方法 */ 
		animation.setRepeatCount(20);//设置重复次数 
		
		iv_regist2.setAnimation(animation);
		TranslateAnimation animation1 = new TranslateAnimation(0, -5, 0, 0);  
        animation1.setInterpolator(new OvershootInterpolator());  
        animation1.setDuration(200);  
        animation1.setRepeatCount(10000); 
        animation1.setRepeatMode(Animation.REVERSE);  
        iv_regist.startAnimation(animation1);
		/** 开始动画 */ 
		animation.startNow(); 
		iv_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);  
		        animation.setInterpolator(new OvershootInterpolator());  
		        animation.setDuration(100);  
		        animation.setRepeatCount(10000);  
		        animation.setRepeatMode(Animation.REVERSE);  
		        iv_regist.startAnimation(animation);  
//		        vh.iv_dan.clearAnimation();
		        getEgeRate(pid);
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
//							iv_danact.setClickable(false);
							JSONObject objmap = obj.getJSONObject("map");
							JSONObject objpage1 = objmap.getJSONObject("newActivityCoupon");
							JSONObject objpage2 = objmap.getJSONObject("oldActivityCoupon");
							EgeGetRate egrnew = JSON.parseObject(objpage1.toJSONString(), EgeGetRate.class);
							EgeGetRate egrold = JSON.parseObject(objpage2.toJSONString(), EgeGetRate.class);
							popupWindow.dismiss();
							showPopupWindowDan2(egrnew,egrold);
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						ToastMaker.showShortToast("请检查网络");
					}
				});
			}
		});
		iv_regist2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.showAsDropDown(iv_regist);
	}

	public void showPopupWindowFaiPai() {
		// 加载布局
		layout = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.pop_fanpai, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		ImageView iv_close = (ImageView) (layout).findViewById(R.id.iv_close);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		iv_close.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		popupWindow.showAsDropDown(iv_close);
	}

	private void getLuckyMoney(){
		OkHttpUtils.post().url(UrlConfig.GETLUCKYMONEY)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onResponse(String response) {
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("领取成功");
						} else {
							ToastMaker.showShortToast("领取失败");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}
	//翻翻乐
	private void putFaipai(){
		OkHttpUtils.post().url(UrlConfig.FANPAIAGIN)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onResponse(String response) {
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							showPopupWindowFaiPai();
//							ToastMaker.showShortToast("领取成功");
						} else {
//							ToastMaker.showShortToast("只能领取一次");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

}
