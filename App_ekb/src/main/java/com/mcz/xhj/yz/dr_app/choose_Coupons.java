package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_adapter.ConponsUnuseAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class choose_Coupons extends BaseActivity implements OnClickListener {
	@BindView(R.id.lv_act_coupons)
	ListView lv_act_coupons;

	private List<ConponsBean> str = new ArrayList<ConponsBean>();
	private List<bean_Detail_Info> lsad_creditor = new ArrayList<bean_Detail_Info>();
	private String fid ;
	private String list_creditor ,creditor_pid ,ptype;
	private String invertAomout; // 輸入的金額

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.title_leftimageview) {
			finish();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.act_choose_coupons;
	}

	private Intent intent;

	@Override
	protected void initParams() {
			str = (List<ConponsBean>) getIntent().getSerializableExtra("list");
			invertAomout = getIntent().getStringExtra("insestAmount");
			fid = getIntent().getStringExtra("fid") ;
			lv_act_coupons.setAdapter(new ConponsUnuseAdapter(choose_Coupons.this,str,"",null));
			// 1=返现券，2=加息券，3=体验金
			lv_act_coupons.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					if(invertAomout!=null&&!invertAomout.equalsIgnoreCase("")){
						if (str.get(position).getEnableAmount() > Double.parseDouble(invertAomout)) {
							return;
						}
					}
					if (1 == str.get(position).getType()) { // 1=返现券
						intent = new Intent();
						intent.putExtra("id", str.get(position).getId());
						intent.putExtra("enableAmount", str.get(position).getEnableAmount());
						intent.putExtra("amount", str.get(position).getAmount());
						setResult(1, intent);
						finish();
					} else if (2 == str.get(position).getType()) { // 2=加息券
						intent = new Intent();
						intent.putExtra("id", str.get(position).getId());
						intent.putExtra("enableAmount", str.get(position).getEnableAmount());
						intent.putExtra("raisedRate", str.get(position).getRaisedRates());
						setResult(2, intent);
						finish();
					} else if (3 == str.get(position).getType()) {
						intent = new Intent();
						intent.putExtra("id", str.get(position).getId());
						intent.putExtra("enableAmount", str.get(position).getEnableAmount());
						intent.putExtra("amount", str.get(position).getAmount());
						setResult(3, intent);
						finish();
					} else if (4 == str.get(position).getType()) { // 2=加息券
						intent = new Intent();
						intent.putExtra("id", str.get(position).getId());
						intent.putExtra("enableAmount", str.get(position).getEnableAmount());
						intent.putExtra("multiple", str.get(position).getMultiple());
						setResult(4, intent);
						finish();
					}
				}
			});
	}

	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private void getConponsList() {
		OkHttpUtils.post()
				.url(UrlConfig.CONPONS_CHOSE)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("pid","0")
				.addParams("amount", "3")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build().execute(new StringCallback() {

			@Override
			public void onResponse(String result) {
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONArray objrows = objmap.getJSONArray("list");
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
//					getActivity().finish();
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
