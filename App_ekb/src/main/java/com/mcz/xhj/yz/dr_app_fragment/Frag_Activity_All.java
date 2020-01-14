package com.mcz.xhj.yz.dr_app_fragment;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.ActivityAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ActivityBean;
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


public class Frag_Activity_All extends BaseFragment {
	@BindView(R.id.lv_myinvest)
	ExpandableListView  lv_myinvest;
	@BindView(R.id.ptr_wait)
	PtrClassicFrameLayout ptr_wait;
	
	private boolean isLastitem = false;
	private boolean isLoading;
	private View footer;
	private LinearLayout footerlayout;
	private TextView tv_footer;
	private ProgressBar pb;
	private ActivityAdapter adapter;
	private int page;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_activity_yes;
	}

	@Override
	protected void initParams() {
		adapter = null;
		page=1;
		getData();
		ptr_wait.setPtrHandler(new PtrHandler() {
					
					@Override
					public void onRefreshBegin(PtrFrameLayout frame) {
						page = 1;
						getData();
					}
					
					@Override
					public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
							View header) {
						// TODO Auto-generated method stub
						return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_myinvest, header);
					}
				});
		footer = View.inflate(mContext,R.layout.footer_layout, null);
		footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
		pb = (ProgressBar) footer.findViewById(R.id.pb);
		tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
		footerlayout.setVisibility(View.GONE);
		lv_myinvest.addFooterView(footer);
		lv_myinvest.setOnScrollListener(new OnScrollListener() {
			
        	@Override
        	public void onScroll(AbsListView view, int firstVisibleItem,
        			int visibleItemCount, int totalItemCount) {
        		// TODO Auto-generated method stub
        		if(firstVisibleItem+visibleItemCount == totalItemCount){
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
        				getData();
        			}
        		}
        	}
		});
	}
	
	public void loadComplete(){
		isLoading = false;
		footerlayout.setVisibility(View.GONE);
	}
	
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private List<ActivityBean> lslb = new ArrayList<ActivityBean>();
	private List<ActivityBean> lslbs = new ArrayList<ActivityBean>();
	private void getData() {
		OkHttpUtils.post()
		.url(UrlConfig.ACTIVITYCENTER)
		.addParams("uid", preferences.getString("uid", ""))
//		.addParams("prizeStatus","")
		.addParams("pageSize", "10")
		.addParams("pageOn", page+"")
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2")
		.build().execute(new StringCallback() {
			
			@Override
			public void onResponse(String result) {
				ptr_wait.refreshComplete();
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONObject objpage = objmap.getJSONObject("pageInfo");
					JSONArray objrows = objpage.getJSONArray("rows");
					lslbs = JSON.parseArray(objrows.toJSONString(), ActivityBean.class);
					if(lslbs.size()>0){
						if(page>1){
							lslb.addAll(lslbs);
						}
						else{
							lslb=lslbs;
						}
						if (adapter == null) {
							adapter = new ActivityAdapter(mContext, lslb);
							lv_myinvest.setAdapter(adapter);
						} else {
							adapter.onDateChange(lslb);
						}
						loadComplete();
						page++;
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
							adapter = new ActivityAdapter(mContext, lslb);
							lv_myinvest.setAdapter(adapter);
						}
						tv_footer.setText("全部加载完毕");
						footerlayout.setVisibility(View.VISIBLE);
						pb.setVisibility(View.GONE);
					}
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
					getActivity().finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
				}
				else{
					ToastMaker.showShortToast("服务器异常");
				}
			}
			
			@Override
			public void onError(Call call, Exception e) {
				ptr_wait.refreshComplete();
				ToastMaker.showShortToast("请检查网络");
			}
		});
		
	}

}
