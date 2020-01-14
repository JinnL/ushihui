package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


import com.ekabao.oil.R;
import com.ekabao.oil.adapter.WelcomePA;
import com.ekabao.oil.global.LocalApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
* 引导页
* 首次安装APP进入的欢迎页
* */
public class LogAct extends BaseActivity {
	@BindView(R.id.vp_wellcome)
    ViewPager vp_wellcome;
	private String imgUrl;
	private ImageView view3;
	private ImageView view4;
	//private ImageView view4;

	@Override
	protected int getLayoutId() {

		return R.layout.welcome_act;
	}
	private List<View> lsview=new ArrayList<View>();
	private WelcomePA adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
	}

	@Override
	protected void initParams() {
		imgUrl = getIntent().getStringExtra("imgUrl");
		ImageView view1=new ImageView(this);
		ImageView view2=new ImageView(this);
		view3=new ImageView(this);
		view4 = new ImageView(this);
		vp_wellcome.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if(arg0==3){
					view4.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Editor editor = LocalApplication.getInstance().sharereferences.edit();
							editor.putBoolean("FirstLog", false);
							editor.commit();
							if("".equalsIgnoreCase(imgUrl)||imgUrl==null){
								startActivity(new Intent(LogAct.this, MainActivity.class));
							}else{
								startActivity(new Intent(LogAct.this, AdverActivity.class)
										.putExtra("imgUrl",imgUrl));
							}
							finish();
						}
					});
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {


			}

			@Override
			public void onPageScrollStateChanged(int arg0) {


			}
		});
		view1.setBackgroundResource(R.drawable.splash_1);
		view2.setBackgroundResource(R.drawable.splash_2);
		view3.setBackgroundResource(R.drawable.splash_3);
		view4.setBackgroundResource(R.drawable.splash_4);

		lsview.add(view1);
		lsview.add(view2);
		lsview.add(view3);
		lsview.add(view4);
		adapter = new WelcomePA(lsview, this);
		vp_wellcome.setAdapter(adapter);

		/*bt_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = LocalApplication.getInstance().sharereferences.edit();
				editor.putBoolean("FirstLog", false);
				editor.commit();
				if("".equalsIgnoreCase(imgUrl)||imgUrl==null){
					startActivity(new Intent(LogAct.this, MainActivity.class));
				}else{
					startActivity(new Intent(LogAct.this, AdverActivity.class)
							.putExtra("imgUrl",imgUrl));
				}
				finish();
			}
		});*/
	}
}
