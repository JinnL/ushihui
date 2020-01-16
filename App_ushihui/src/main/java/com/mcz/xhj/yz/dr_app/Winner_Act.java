package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.WinnerBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.TileTextview;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 本期中奖者
 * @author DELL
 *
 */
public class Winner_Act extends BaseActivity{
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.iv_pic)
	SimpleDraweeView iv_pic;
	@BindView(R.id.tv_qishu)
	TextView tv_qishu;
	@BindView(R.id.tv_phone)
	TextView tv_phone;
	@BindView(R.id.tv_code)
	TextView tv_code;
	@BindView(R.id.tv_text)
	TextView tv_text;
	@BindView(R.id.ll_giftemp)
	LinearLayout ll_giftemp;
	@BindView(R.id.sv_group)
	ScrollView sv_group;
	@BindView(R.id.bottom_pic)
	SimpleDraweeView bottom_pic;//bottom大图
	@BindView(R.id.pending_publication_iv)
	ImageView pending_publication_iv;  //待公布
	@BindView(R.id.play_iv)
	ImageView play_iv;  //播放的iv
	@BindView(R.id.few_tv)
	TileTextview few_tv;  //第几期的view


	private String pid;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_giftinfo;
	}

	@Override
	protected void initParams() {
		centertv.setText("中奖者");
		title_leftimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pid = getIntent().getStringExtra("pid");
		getData();
	}
	private void getData() {
		showWaitDialog("正在加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.WINNERINFO)
				.addParams("id",pid )
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject objmap = obj.getJSONObject("map");
							JSONArray arr = objmap.getJSONArray("list");
							if(arr.size()>0){
								List<WinnerBean> lswb = JSON.parseArray(arr.toJSONString(), WinnerBean.class);
								final WinnerBean wb = lswb.get(0);
								tv_qishu.setText("第"+wb.getActivityPeriods()+"期中奖者");
								if(wb.getPrizeCode()!=null){
									tv_code.setText(wb.getPrizeCode());
								}else{
									tv_code.setText("待公布");
								}
								if(wb.getPrizeMobile()!=null){
									tv_phone.setText(wb.getPrizeMobile());
								}else{
									tv_phone.setText("待公布");
								}
								if(wb.getPrizeContent()!=null){
									tv_text.setText(wb.getPrizeContent());
								}else{
									tv_text.setText("待公布");
								}
								if(wb.getPrizeHeadPhoto()!=null){
									Uri uri = Uri.parse(wb.getPrizeHeadPhoto());
									iv_pic.setImageURI(uri);
								}
								if(wb.getPrizeImgUrl()!=null){
									Uri uri1 = Uri.parse(wb.getPrizeImgUrl());
									bottom_pic.setImageURI(uri1);
									if(wb.getPrizeVideoLink()!=null){
										play_iv.setVisibility(View.VISIBLE);
										play_iv.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												startActivity(new Intent(Winner_Act.this,WebViewActivity.class)
														.putExtra("URL", wb.getPrizeVideoLink())
														.putExtra("TITLE", "开奖视频")
												);

											}
										});
									}
								}else{
									pending_publication_iv.setVisibility(View.VISIBLE);
								}
								few_tv.setDegrees(45);
								few_tv.setText("第 "+wb.getActivityPeriods()+" 期");
							}else{
								sv_group.setVisibility(View.GONE);
								ll_giftemp.setVisibility(View.VISIBLE);
							}

						}
//				else if ("9998".equals(obj.getString("errorCode"))) {
//					finish();
//					new show_Dialog_IsLogin(Winner_Act.this).show_Is_Login() ;
//				} 
						else {
							ToastMaker.showShortToast("系统异常!");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						dismissDialog();
						ToastMaker.showShortToast("请检查网络!");

					}
				});

	}
}
