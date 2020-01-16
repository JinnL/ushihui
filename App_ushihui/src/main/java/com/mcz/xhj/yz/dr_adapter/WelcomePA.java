package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class WelcomePA extends PagerAdapter {
	private List<View> lsview;
	private Context context;
	
	public WelcomePA(List<View> lsview, Context context) {
		super();
		this.lsview = lsview;
		this.context = context;
	}

	@Override
	public int getCount() {
		return lsview.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(lsview.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(lsview.get(position));
		return lsview.get(position);
	}
	
	

}
