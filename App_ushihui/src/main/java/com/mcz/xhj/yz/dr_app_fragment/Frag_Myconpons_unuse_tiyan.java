package com.mcz.xhj.yz.dr_app_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.NewConponsAdapter;
import com.mcz.xhj.yz.dr_app.Detail_Tiyan;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;


public class Frag_Myconpons_unuse_tiyan extends BaseFragment {
	@BindView(R.id.lv_conpons_unused)
	ListView lv_conpons;
	@BindView(R.id.ptr_conponsunuse)
	PtrClassicFrameLayout ptr_conponsunuse;
	@BindView(R.id.ll_norecord)
	LinearLayout ll_norecord;
	@BindView(R.id.rl_content)
	RelativeLayout rl_content;

	private List<ConponsBean> lslbs = new ArrayList<ConponsBean>();
	private NewConponsAdapter adapter;
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_conpons_unuse_tiyan;
	}
	@Override
	public void onResume() {
		super.onResume();
		getData();
	}
	@Override
	protected void initParams() {
		getData();
		ptr_conponsunuse.setPtrHandler(new PtrHandler() {
			
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				getData();
			}
			
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
					View header) {
				if(lslbs.size()> 0){
					if (lv_conpons.getChildAt(0).getTop() == 0) {
						return true;
					}else {
						return false;
					}
				}else{
					return false;
				}
//				return PtrDefaultHandler.checkContentCanBePulledDown(frame, rl_content, header);
			}
		});
		lv_conpons.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(lslbs.get(position).getType()==3){
					startActivity(new Intent(mContext, Detail_Tiyan.class));
					getActivity().finish();
				}
//				else if(lslbs.get(position).getPid()!=null){
//					startActivity(new Intent(mContext,Detail_Piaoju_ActFirst.class)
//					.putExtra("pid",lslbs.get(position).getPid())
//					.putExtra("ptype", lslbs.get(position).getType()+"")
//					);
//				}
//				else{
//					LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
//					getActivity().finish();
//				}
			}
		});
	}

	private void getData() {
		OkHttpUtils.post()
		.url(UrlConfig.CONPONSUNUSE)
		.addParams("uid", preferences.getString("uid", ""))
		.addParams("status","0")
		.addParams("flag", "0")
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2")
		.build().execute(new StringCallback() {
			
			@Override
			public void onResponse(String result) {
				ptr_conponsunuse.refreshComplete();
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONArray objrows = objmap.getJSONArray("list");
					lslbs = JSON.parseArray(objrows.toJSONString(), ConponsBean.class);
					//版本兼容
//					for (int i = 0; i < lslbs.size(); i++) {
//						if(lslbs.get(i).getType()!=4&lslbs.get(i).getType()!=1&lslbs.get(i).getType()!=2&lslbs.get(i).getType()!=3){
//							lslbs.remove(i);
//							i--;
//						}
//					}
					if(lslbs.size()>0){
						ll_norecord.setVisibility(View.GONE);
						ConponsBean cbean = null;
						Iterator<ConponsBean> it = lslbs.iterator();
						while(it.hasNext()){
							if("3".equals(((ConponsBean)it.next()).getType())){
								cbean = (ConponsBean)it.next();
								it.remove();
							}
						}
						if(cbean != null){
							lslbs.add(0, cbean);
						}
						if (adapter == null) {
							adapter = new NewConponsAdapter(mContext, lslbs,"",new NewConponsAdapter.onButtonClickL() {
								
								@Override
								public void onButtonClick(View view) {
									LocalApplication.getInstance().getMainActivity().isInvest = true;
									LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
									getActivity().finish();
									
								}
							});
							lv_conpons.setAdapter(adapter);
						} else {
							adapter.onDateChange(lslbs);
						}
					}else{
						lslbs.clear();
						if (adapter == null) {
							lv_conpons.setAdapter(adapter);
						} else {
							adapter.onDateChange(lslbs);
						}
						ll_norecord.setVisibility(View.VISIBLE);
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
				ToastMaker.showShortToast("请检查网络");
			}
		});
		
	}
}
