package com.mcz.xhj.yz.dr_app;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.MydetailAdapter;
import com.mcz.xhj.yz.dr_adapter.TextAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.MyDetailBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
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

public class MyDetailAct extends BaseActivity {
	@BindView(R.id.lv_mydetail)
	ListView lv_mydetail;
	@BindView(R.id.cb_select)
	CheckBox cb_select;
	@BindView(R.id.line)
	View line;
	@BindView(R.id.popview)
	LinearLayout popview;
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.title_leftimageview)
	ImageView leftima;
	
	@BindView(R.id.tv_type)
	TextView tv_type;
	
	@BindView(R.id.ptr_mydetail)
	PtrClassicFrameLayout ptr_mydetail;

	private View view;
	
	private boolean isLastitem = false;
	private boolean isLoading;
	private View footer;
	private LinearLayout footerlayout;
	private TextView tv_footer;
	private ProgressBar pb;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_mydetail;
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	@Override
	protected void initParams() {
		centertv.setText("我的明细");
		leftima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		createLeftFloatView();
		getData(type);
		ptr_mydetail.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				page = 1;
				getData(type);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						lv_mydetail, header);
			}
		});
		footer = View.inflate(this, R.layout.footer_layout, null);
		footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
		pb = (ProgressBar) footer.findViewById(R.id.pb);
		tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
		footerlayout.setVisibility(View.GONE);
		lv_mydetail.addFooterView(footer);
		lv_mydetail.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					isLastitem = true;
				}else{
					isLastitem = false;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
					if (!isLoading) {
						isLoading = true;
						footerlayout.setVisibility(View.VISIBLE);
						getData(type);
					}
				}
			}
		});
		cb_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					popupWindow.setFocusable(true);
					popupWindow.showAsDropDown(line);
				} else {
					
				}

			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				cb_select.setChecked(false);
				// backgroundAlpha(0.5f);
			}
		});
	}
	
	public void loadComplete(){
		isLoading = false;
		footerlayout.setVisibility(View.GONE);
	}
	private PopupWindow popupWindow;
	private ListView lv_pop_mydetail;
	private static final String[] strs = new String[] { "全部", "充值", "提现", "投资","回款","活动"};

	/**
	 * 创建悬浮按钮
	 */
	private void createLeftFloatView() {
		view = LayoutInflater.from(MyDetailAct.this).inflate(
				R.layout.lv_pop_mydetail, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lv_pop_mydetail = (ListView) view.findViewById(R.id.lv_pop);
		TextAdapter adapter = new TextAdapter(strs, this);
		lv_pop_mydetail.setAdapter(adapter);
		lv_pop_mydetail.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().getParent()
						.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		lv_pop_mydetail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				page=1;
				tv_type.setText(strs[position]);
				if(position==4){
					type = 6;
					position=6;
				}else if(position==5){
					type = 4;
					position=4;
				}else{
					type = position;
				}
				getData(position);
				popupWindow.dismiss();
//				backgroundAlpha(1f);
			}
		});
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	private int page = 1;
	private int type;
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private MydetailAdapter adapter;
	private List<MyDetailBean> lslb = new ArrayList<MyDetailBean>();
	private List<MyDetailBean> lslbs = new ArrayList<MyDetailBean>();

	private void getData(int a) {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.MYDETAIL)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("tradeType", type+"")
				.addParams("pageSize", "10")
				.addParams("pageOn", page + "")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						dismissDialog();
						ptr_mydetail.refreshComplete();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean("success")) {
							JSONObject objmap = obj.getJSONObject("map");
							JSONObject objpage = objmap.getJSONObject("page");
							JSONArray objrows = objpage.getJSONArray("rows");
							lslbs = JSON.parseArray(objrows.toJSONString(),
									MyDetailBean.class);
							if(lslbs.size()>0){
								if(page>1){
									lslb.addAll(lslbs);
								}
								else{
									lslb=lslbs;
								}
								if (adapter == null||page==1) {
									adapter = new MydetailAdapter(MyDetailAct.this, lslb);
									lv_mydetail.setAdapter(adapter);
								} else {
									adapter.onDateChange(lslb);
								}
								page++;
								loadComplete();
								if(lslbs.size()==10){
									tv_footer.setText("上拉加载更多");
									footerlayout.setVisibility(View.VISIBLE);
									pb.setVisibility(View.GONE);
								}else{
									tv_footer.setText("全部加载完毕");
									footerlayout.setVisibility(View.VISIBLE);
									pb.setVisibility(View.GONE);
								}
							}else{
								if(page==1){
									adapter = new MydetailAdapter(MyDetailAct.this, lslbs);
									lv_mydetail.setAdapter(adapter);
								}
								tv_footer.setText("全部加载完毕");
								footerlayout.setVisibility(View.VISIBLE);
								pb.setVisibility(View.GONE);
							}
						} 
						else if ("9998".equals(obj.getString("errorCode"))) {
							finish();
//							new show_Dialog_IsLogin(MyDetailAct.this).show_Is_Login() ;
						} 
						else {
							ToastMaker.showShortToast("服务器异常");
						}

					}

					@Override
					public void onError(Call call, Exception e) {
						dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});

	}
}
