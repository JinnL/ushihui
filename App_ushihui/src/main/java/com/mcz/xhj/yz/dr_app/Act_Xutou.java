package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_adapter.RedListAdapter;
import com.mcz.xhj.yz.dr_app.me.TransactionDetailsActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.LinquListBean;
import com.mcz.xhj.yz.dr_bean.LinquListHongbaoBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.MarqueeView;
import com.mcz.xhj.yz.dr_view.ScaleInTransformer;
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

public class Act_Xutou extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.marqueeView)
	MarqueeView marqueeView;
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
	@BindView(R.id.tv_empty1)
	TextView tv_empty1;
	@BindView(R.id.tv_empty2)
	TextView tv_empty2;
	@BindView(R.id.tv_xutou)
	TextView tv_xutou;
	@BindView(R.id.tv_title_day)
	TextView tv_title_day;
	@BindView(R.id.tv_title_money)
	TextView tv_title_money;
	@BindView(R.id.tv_giveup)
	TextView tv_giveup;
	@BindView(R.id.mViewPager)
	ViewPager mViewPager;
//	private PagerAdapter mAdapter;
	@BindView(R.id.ptr_myassets)
	PtrClassicFrameLayout ptr_myassets;

	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private List<LinquListBean> numlist = new ArrayList<LinquListBean>();
	private List<LinquListHongbaoBean> hongbaolist = new ArrayList<LinquListHongbaoBean>();
	private List<LinquListHongbaoBean> mHongbaolist = new ArrayList<LinquListHongbaoBean>();
	private int day = 0;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_xutou;
	}

	@Override
	protected void initParams() {
		ptr_myassets.disableWhenHorizontalMove(true);
		ptr_myassets.setPtrHandler(new PtrHandler() {
		//ddfdsafd
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				if(isRefrush){
					getData();
				}else{
					ptr_myassets.refreshComplete();
				}
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
											 View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			}
		});
//		LinquListHongbaoBean test = new LinquListHongbaoBean(1,2,3,4,5);
//		LinquListHongbaoBean test1 = new LinquListHongbaoBean(6,6,7,7,8);
//		LinquListHongbaoBean test2 = new LinquListHongbaoBean(34,5,36,43,9);
//		hongbaolist.add(test);
//		hongbaolist.add(test1);
//		hongbaolist.add(test2);
		mViewPager.setPageMargin(10);
		mViewPager.setOffscreenPageLimit(3);
		//左边padding部分宽度
		int paddingLeft = mViewPager.getPaddingLeft();
		//一页的宽度
		int imgWidth = mViewPager.getWidth() - paddingLeft*2;
		//padding部分所占百分比
		float excursion = -(float)paddingLeft/(float)imgWidth;
		mViewPager.setPageTransformer(true, new ThreeDPageTransformer(excursion));
//		mViewPager.setAdapter(new mAdapter());
//		mViewPager.setPageTransformer(true, new ScaleInTransformer());
//		mViewPager.setCurrentItem(1);
		title_centertextview.setText("领取现金") ;
		title_righttextview.setVisibility(View.VISIBLE) ;
		title_righttextview.setText("关闭") ;
		title_leftimageview.setVisibility(View.GONE) ;
		title_righttextview.setOnClickListener(this);
		tv_xutou.setOnClickListener(this);
		tv_giveup.setOnClickListener(this);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				double rate1 = hongbaolist.get(position).getRate();
				double activityRate1 = hongbaolist.get(position).getActivityRate();
				tv_money.setText(stringCut.getNumKbs(hongbaolist.get(position).getInvestAmount())+"元");
				tv_day.setText(hongbaolist.get(position).getDeadline()+"天");
				if(activityRate1!=0){
					tv_rate.setText(stringCut.getNumKbs(rate1)+"%+"+stringCut.getNumKbs(activityRate1)+"%");
				}else{
					tv_rate.setText(stringCut.getNumKbs(rate1)+"%");
				}
				tv_earn.setText(stringCut.getNumKbs(hongbaolist.get(position).getProfitAmount())+"元");
				tv_red.setText(stringCut.getNumKbs(hongbaolist.get(position).getAmount())+"元");
				day = hongbaolist.get(position).getDeadline();
				tv_title_money.setText(stringCut.getNumKbs(hongbaolist.get(position).getAmount())+"");
				tv_title_day.setText(hongbaolist.get(position).getDeadline()+"");
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		getData();
	}
	public class ThreeDPageTransformer implements ViewPager.PageTransformer {

		private float excursion = 0;

		public ThreeDPageTransformer(float excursion){
			this.excursion = excursion;
		}

		public void transformPage(View view, float position) {
			//纠正
			position = position + excursion;

		}
	}
	private View view;
	public class mAdapter extends PagerAdapter{

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			view  = getLayoutInflater().inflate(R.layout.item_hongbaolist,null);
			TextView tv_red_money = (TextView) view.findViewById(R.id.tv_red_money);
			TextView tv_hongbao_day = (TextView) view.findViewById(R.id.tv_hongbao_day);
			tv_red_money.setText(hongbaolist.get(position).getAmount()+"元");
			tv_hongbao_day.setText("续投"+hongbaolist.get(position).getDeadline()+"天");
			container.addView(view);
//          view.setAdjustViewBounds(true);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return hongbaolist.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	private View layout;
	private PopupWindow popupWindow;
	private RedListAdapter rlAdapter;
	public void showPopupWindowConpons(int day) {
		// 加载布局
		layout = LayoutInflater.from(Act_Xutou.this).inflate(R.layout.dialog_xutou, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setContentView(layout);
		TextView tv_title = (TextView) (layout).findViewById(R.id.tv_title);
		final TextView tv_tongyi = (TextView) (layout).findViewById(R.id.tv_tongyi);
		final TextView tv_queren = (TextView) (layout).findViewById(R.id.tv_queren);
		TextView tv_close = (TextView) (layout).findViewById(R.id.tv_close);
		SeekBar seekbar = (SeekBar) (layout).findViewById(R.id.seekbar);
		tv_title.setText("1天新手标到期后\n自动续投"+day+"天产品");
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress==100){
					tv_queren.setVisibility(View.VISIBLE);
					tv_tongyi.setVisibility(View.GONE);
				}else if(progress==0){
					tv_queren.setVisibility(View.GONE);
					tv_tongyi.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//				popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//				// 设置popupWindow弹出窗体的背景
//				WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		// 监听
		tv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				backgroundAlpha(1f);
				popupWindow.dismiss();
			}
		});
		tv_queren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				backgroundAlpha(1f);
				popupWindow.dismiss();
				doXutou();
			}
		});
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//		backgroundAlpha(0.5f);
	}
	private Boolean isRefrush = false;
	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}
	private void getData() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.XUTOUDTAIL)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						ptr_myassets.refreshComplete();
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						if (obj.getBoolean(("success"))) {
							isRefrush = false;
							JSONObject map = obj.getJSONObject("map");
							JSONArray parcelList = map.getJSONArray("parcelList");
							if(parcelList!=null){
								numlist = JSON.parseArray(parcelList.toJSONString(),LinquListBean.class);
								List<String> info = new ArrayList<>();
								for (int i = 0; i < numlist.size(); i++) {
									info.add(numlist.get(i).getMobilePhone().toString()+"        领取"+numlist.get(i).getAmount().toString()+"元");
								}
								marqueeView.startWithList(info);
							}else{
								List<String> infoEmpty = new ArrayList<>();
								infoEmpty.add("133****1898       领取388元");
								infoEmpty.add("132****4589       领取166元");
								infoEmpty.add("153****7867       领取88元");
							}
							JSONArray rewardList = map.getJSONArray("rewardList");
							if(rewardList!=null){
								mHongbaolist = JSON.parseArray(rewardList.toJSONString(),LinquListHongbaoBean.class);
								hongbaolist.add(mHongbaolist.get(0));
								hongbaolist.add(mHongbaolist.get(2));
								hongbaolist.add(mHongbaolist.get(1));
								mViewPager.setAdapter(new mAdapter());
//								mViewPager.setPageTransformer(true, new AlphaPageTransformer());
								mViewPager.setPageTransformer(true, new ScaleInTransformer());
								mViewPager.setCurrentItem(1);
								double rate = mHongbaolist.get(2).getRate();
								double activityRate = mHongbaolist.get(2).getActivityRate();
								tv_money.setText(stringCut.getNumKbs(mHongbaolist.get(2).getInvestAmount())+"元");
								tv_day.setText(mHongbaolist.get(2).getDeadline()+"天");
								if(activityRate!=0){
									tv_rate.setText(stringCut.getNumKbs(rate)+"%+"+stringCut.getNumKbs(activityRate)+"%");
								}else{
									tv_rate.setText(stringCut.getNumKbs(rate)+"%");
								}
//								tv_rate.setText(mHongbaolist.get(2).getRate()+"%");
								tv_earn.setText(stringCut.getNumKbs(mHongbaolist.get(2).getProfitAmount())+"元");
								tv_red.setText(stringCut.getNumKbs(mHongbaolist.get(2).getAmount())+"元");
								day = mHongbaolist.get(2).getDeadline();
								tv_title_money.setText(stringCut.getNumKbs(mHongbaolist.get(2).getAmount())+"");
								tv_title_day.setText(mHongbaolist.get(2).getDeadline()+"");

							}
						}
						else if ("9999".equals(obj.getString("errorCode"))) {
							isRefrush = true;
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							isRefrush = true;
							new show_Dialog_IsLogin(Act_Xutou.this).show_Is_Login();
						} else {
							isRefrush = true;
							ToastMaker.showShortToast("系统异常");
						}
					}
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						isRefrush = true;
						ptr_myassets.refreshComplete();
						dismissDialog();
						ToastMaker.showShortToast("网络错误，请检查");
					}
				});
	}
	private void doXutou() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.XUTOU)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("period", day+"")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						JSONObject obj = JSON.parseObject(response);
						dismissDialog();
						if (obj.getBoolean(("success"))) {
							DialogMaker.showOneBtDialog(Act_Xutou.this, "续投成功", tv_red.getText().toString()+"已躺在您的账户中！", "立即查看", new DialogMaker.DialogCallBack() {
								@Override
								public void onButtonClicked(Dialog dialog, int position, Object tag) {
									startActivity(new Intent(Act_Xutou.this,TransactionDetailsActivity.class));
									finish();
								}

								@Override
								public void onCancelDialog(Dialog dialog, Object tag) {
								}
							},null);
						}
						else if ("1003".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("您已经续投过了");
						}
						else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						}
						else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Act_Xutou.this).show_Is_Login();
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
	protected synchronized void onDestroy() {
		super.onDestroy();
		marqueeView.stop();
	}
	private Dialog dialog;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_righttextview:
			finish() ;
			break;
		case R.id.tv_xutou:
			if(day!=0){
//				showPopupWindowConpons(day);
				DialogMaker.showXutouDialog(this, "提示", day+"天产品","确认", new DialogMaker.DialogCallBack() {
					@Override
					public void onButtonClicked(Dialog dialog, int position, Object tag) {
						dismissDialog();
						doXutou();
					}

					@Override
					public void onCancelDialog(Dialog dialog, Object tag) {
					}
				},null);
			}
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
		default:
			break;
		}
	}
	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		if (position == 0) {
			if(tag.equals("mingxi")){
				startActivity(new Intent(Act_Xutou.this,MyDetailAct.class));
				finish();
			}else{
				finish();
			}
		}else if(position == 1){
			dialog.dismiss();
		}
	}
}
