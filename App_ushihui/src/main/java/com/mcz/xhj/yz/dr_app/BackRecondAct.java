package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_adapter.BackRecondAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BackRecond;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class BackRecondAct extends BaseActivity {
	@BindView(R.id.lv_mydetail)
	ListView lv_mydetail;
	@BindView(R.id.line)
	View line;
	@BindView(R.id.popview)
	LinearLayout popview;
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.title_leftimageview)
	ImageView leftima;

	private String pid;
	private String id;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_backrecod;
	}


	@Override
	protected void initParams() {
		centertv.setText("回款记录");
		leftima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent in = getIntent();
		pid = in.getStringExtra("pid");
		id = in.getStringExtra("id");
		getData();
	}
	
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private BackRecondAdapter adapter;
	private List<BackRecond> lslbs = new ArrayList<BackRecond>();

	private void getData() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.BACKRECOND)
				.addParams("pid", pid)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("id",id)
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						LogUtils.e("回款记录"+response);
						JSONObject objmap = obj.getJSONObject("map");
						if (obj.getBoolean("success")) {
//							JSONObject objmap = obj.getJSONObject("result");
							JSONArray objrows = objmap.getJSONArray("result");
							lslbs = JSON.parseArray(objrows.toJSONString(),BackRecond.class);
							if(lslbs.size()>0){
									adapter = new BackRecondAdapter(BackRecondAct.this, lslbs);
									lv_mydetail.setAdapter(adapter);
							}else{
//								adapter = new MydetailAdapter(BackRecondAct.this, lslbs);
//								lv_mydetail.setAdapter(adapter);
//								tv_footer.setText("全部加载完毕");
//								footerlayout.setVisibility(View.VISIBLE);
//								pb.setVisibility(View.GONE);
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
