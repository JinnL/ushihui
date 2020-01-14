package com.mcz.xhj.yz.dr_app;

import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BankNameAdd_Pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class BankDetailAct extends BaseActivity {
	
	@BindView(R.id.iv_bank)
	ImageView iv_bank;
	@BindView(R.id.tv_banknum)
	TextView tv_banknum;
	@BindView(R.id.tv_name)
	TextView tv_name;
	@BindView(R.id.tv_idcard)
	TextView tv_idcard;
	@BindView(R.id.tv_phone)
	TextView tv_phone;
	@BindView(R.id.tv_limit)
	TextView tv_limit;
	@BindView(R.id.tv_limitday)
	TextView tv_limitday;
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.title_leftimageview)
	ImageView leftima;
	
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_bankdetail;
	}

	@Override
	protected void initParams() {
		centertv.setText("我的银行卡");
		leftima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getData();
	}
	
	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
	private void getData() {
		OkHttpUtils
		.post()
		.url(UrlConfig.BANKDETAIL)
		.addParams("uid", preferences.getString("uid", ""))
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2")
		.build()
		.execute(new StringCallback() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				JSONObject obj = JSON.parseObject(response);
				if (obj.getBoolean(("success"))) {
					JSONObject objmap = obj.getJSONObject("map");
					String bankid = objmap.getString("bankCode");
					String bankname = objmap.getString("bankName");
					String banknum = objmap.getString("bankNum");
					String idcard = objmap.getString("idCards");
					String name = objmap.getString("realName");
					String phone = objmap.getString("phone");
					Integer limit = objmap.getInteger("singleQuota");
					Integer limitday = objmap.getInteger("dayQuota");
					Integer pic = new BankNameAdd_Pic(bankid).bank_Pic();
					iv_bank.setImageResource(pic);
					tv_banknum.setText("尾号"+banknum);
					tv_idcard.setText(idcard);
					tv_name.setText(name);
					tv_phone.setText(stringCut.phoneCut(phone));
					if(limit!=null){
						tv_limit.append("银行充值单笔限额为"+stringCut.getNumKbs(limit)+"元");
					}
					if(limitday==0){
						tv_limitday.append("银行充值每日无限额");
					}else{
						tv_limitday.append("银行充值每日限额为"+stringCut.getNumKbs(limitday)+"元");
					}
					
				} else {
					ToastMaker.showShortToast("系统异常!");
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				ToastMaker.showShortToast("请检查网络!");

			}
		});
		
	}
}
