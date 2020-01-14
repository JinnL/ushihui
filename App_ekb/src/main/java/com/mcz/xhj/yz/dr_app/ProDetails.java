package com.mcz.xhj.yz.dr_app;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.viewpagerindicator.TabPageIndicator;
/*import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_introduce;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_picture;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_record;*/
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class ProDetails extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview ;
	@BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.title_rightimageview)
    ImageView title_rightimageview ;
	private String[] tab;//标题
	//private TabFragPA tabPA;
	@BindView(R.id.vp_conpons)
	ViewPager vp_conpons;
	@BindView(R.id.more_indicator)
	TabPageIndicator tabin;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_prodetails;
	}

	String uid ;
	@Override
	protected void initParams() {
		title_rightimageview.setVisibility(View.GONE) ;
		title_centertextview.setText("产品详情") ;
		title_leftimageview.setOnClickListener(this) ;
		tab = new String[] { "投资记录", "产品介绍","产品图片"};
		uid = getIntent().getStringExtra("pid") ;
		// 给viewpager设置适配器
		//tabPA = new TabFragPA(getSupportFragmentManager());// 继承fragmentactivity
		//vp_conpons.setAdapter(tabPA);
		// viewpagerindictor和viewpager关联
		//tabin.setViewPager(vp_conpons);
	}
	/*class TabFragPA extends FragmentPagerAdapter {

		public TabFragPA(FragmentManager fragmentManager) {
			super(fragmentManager);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0://投资记录
				Fragment frag = new Frag_ProDetails_record();
				return frag;
			case 1://产品介绍
				Fragment frag1 = new Frag_ProDetails_introduce();
				return frag1;
			case 2://产品图片
				Fragment frag3 = new Frag_ProDetails_picture();
				return frag3;
//			case 3://风控措施
//				Fragment frag2 = new Frag_ProDetails_measure();
//				return frag2;
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tab[position % tab.length];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tab.length;
		}

	}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_leftimageview:
			this.finish() ;
			break;

		default:
			break;
		}
	}
	
	private void detail_info() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.DETAIL_INFO)
				.addParams("pid", "113")
				.addParams("type", "1")
                .addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map") ;
							JSONArray investList = map.getJSONArray("investList");
//							lsad = JSON.parseArray(investList.toJSONString(), bean_Detail_Info.class);
//							lsad.toString() ;
//							 Bundle data = new Bundle();
//						        data.putString("TEXT", "2222222222222222222222222");
//						        data.putSerializable("TEXT", (Serializable) lsad); 
//						        frag.setArguments(data);
//							JSONArray picList = map.getJSONArray("picList");
//							ls = JSON.parseArray(picList.toJSONString(), bean_Detail_Info_pic.class);
							
//							Bundle data = new Bundle();
//							data.putStringArrayList("lsad", lsad.toString()) ;
//					        data.putString("TEXT", "1111111111111111111111");
//					        frag.setArguments(data);
							
							
//							balance = map.getString("balance") ;
//							balance = "500" ;
//							tv_balance.setText(balance) ;
//							
//							JSONObject info = map.getJSONObject("info") ;
//							tv_name_detail.setText(info.getString("fullName")) ;
//							tv_rate.setText(info.getString("rate")) ;
//							
//							progressbar_pert.setSecondaryProgress(Integer.parseInt(info.getString("pert"))) ;
//							tv_pert_num.setText(info.getString("pert")+"%") ;
//							tv_amount.setText(info.getString("amount")) ;
//							tv_deadline.setText(info.getString("deadline")) ;
//							tv_leastaAmount.setText(info.getString("leastaAmount")) ;
//							
//							maxAmount = info.getString("maxAmount") ;
//							increasAmount = info.getString("increasAmount") ;
//							maxAmount = "100" ;
//							increasAmount = "3" ;
//							tv_maxAmount.setText(maxAmount) ;
////							tv_person_now.setText(info.getString("rate")) ;
////							tv_last_day.setText(info.getString("rate")) ;
//							
//							tv_surplusAmount.setText(info.getString("surplusAmount")) ;
//							tv_rate.setText(info.getString("rate")) ;
//							tv_rate.setText(info.getString("rate")) ;
//							
							
							
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else {
							ToastMaker.showShortToast("系统异常");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("系统异常");
					}
				});
	}
}
