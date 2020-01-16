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
import com.mcz.xhj.yz.dr_adapter.NewConponsAdapter;
import com.mcz.xhj.yz.dr_app.find.CallCenterActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import okhttp3.Call;


public class ConponsUsed extends BaseActivity implements OnClickListener{
	@BindView(R.id.lv_conpons_unused)
	ListView lv_conpons;
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.ll_used)
	LinearLayout ll_used;
	@BindView(R.id.ll_expiry)
	LinearLayout ll_expiry;
	@BindView(R.id.title_used)
	TextView title_used;
	@BindView(R.id.title_expiry)
	TextView title_expiry;
	@BindView(R.id.img_used)
	ImageView img_used;
	@BindView(R.id.img_expiry)
	ImageView img_expiry;
	@BindView(R.id.ll_norecord)
	LinearLayout ll_norecord;
	@BindView(R.id.tv_commom_question)
	TextView tv_commom_question;
	@BindView(R.id.tv_contact_us)
	TextView tv_contact_us;

	private int status = 1;//1=已使用，2=已过期

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_conpons_used;
	}

	@Override
	protected void initParams() {
		title_centertextview.setText("历史优惠券") ;
		title_leftimageview.setOnClickListener(this);
		ll_used.setOnClickListener(this);
		ll_expiry.setOnClickListener(this);
		tv_commom_question.setOnClickListener(this);
		tv_contact_us.setOnClickListener(this);
		getData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_used:
				if(status == 1){
					return;
				}
				status = 1;
				title_used.setTextColor(0XFFFF8347);
				img_used.setVisibility(View.VISIBLE);
				title_expiry.setTextColor(0XFF333333);
				img_expiry.setVisibility(View.GONE);
				getData();
				break;
			case R.id.ll_expiry:
				if(status == 2){
					return;
				}
				status = 2;
				title_used.setTextColor(0XFF333333);
				img_used.setVisibility(View.GONE);
				title_expiry.setTextColor(0XFFFF8347);
				img_expiry.setVisibility(View.VISIBLE);
				getData();
				break;
			case R.id.title_leftimageview:
				finish();
				break;
			case R.id.tv_commom_question:
				startActivity(new Intent(ConponsUsed.this,CallCenterActivity.class));
				break;
			case R.id.tv_contact_us:
				DialogMaker.showKufuPhoneDialog(ConponsUsed.this);
				break;
		}
	}

	private List<ConponsBean> lslbs = new ArrayList<ConponsBean>();
	private NewConponsAdapter adapter;
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private void getData() {
		OkHttpUtils.post()
				.url(UrlConfig.CONPONSUNUSE)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("status",status+"")
				.addParam("flag","1")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build().execute(new StringCallback() {

			@Override
			public void onResponse(String result) {
				LogUtils.i("--->status=="+status+" ,result："+result);
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONArray objrows = objmap.getJSONArray("list");
					lslbs.clear();
					if(adapter != null){
						adapter.onDateChange(lslbs);
					}
					lslbs = JSON.parseArray(objrows.toJSONString(), ConponsBean.class);

					if(lslbs.size()>0){
						ll_norecord.setVisibility(View.GONE);
						ConponsBean cbean = null;
						if (adapter == null) {
							adapter = new NewConponsAdapter(ConponsUsed.this, lslbs,"shixiao",null);
							lv_conpons.setAdapter(adapter);
						} else {
							adapter.onDateChange(lslbs);
						}
					} else {
						ll_norecord.setVisibility(View.VISIBLE);

					}
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
					finish();
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