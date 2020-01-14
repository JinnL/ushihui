package com.mcz.xhj.yz.dr_adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mcz.xhj.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BannerBean;

import java.util.List;


/**
 * Created by admin on 2015/12/6.
 */
public class AdAdapter extends PagerAdapter{

    private List<BannerBean> data;
    private Context context;
    private LayoutInflater inflater;
    private String pid;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;


    public AdAdapter(List<BannerBean> data, Context context, String pid) {
        this.data = data;
        this.context = context;
        this.pid = pid;
        inflater = LayoutInflater.from(context);
    }

    //更新数据源
    public void refreshData(List<BannerBean> data){
        this.data = data;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	PointF p = new PointF(0.5f, 0.5f);
        View view = inflater.inflate(R.layout.item_main,null);
        SimpleDraweeView iv = (SimpleDraweeView) view.findViewById(R.id.iv_pic);
//      iv.getHierarchy().setActualImageFocusPoint(p);
//      iv.setImageResource(data[position%data.length]);
        final BannerBean bean = data.get(position%data.size());
        bean.getImgUrl();
        if(bean.getImgUrl()!=null){
        	Uri uri = Uri.parse(bean.getImgUrl());
        	iv.setImageURI(uri);
        }
        ((ViewPager)container).addView(view);
        iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bean.getLocation()==null||bean.getLocation().equalsIgnoreCase("")){
					return;
				}
				if(bean.getTitle().indexOf("注册送礼")!=-1){
					context.startActivity(new Intent(context,WebViewActivity.class)
					.putExtra("URL", bean.getLocation()+"&app=true")
					.putExtra("TITLE", bean.getTitle())
					.putExtra("HTM", "立即注册")
					.putExtra("PID", pid)
					.putExtra("BANNER", "banner")
					);
				}else if(bean.getTitle().indexOf("邀请")!=-1){
					context.startActivity(new Intent(context,WebViewActivity.class)
					.putExtra("URL", bean.getLocation()+"&app=true")
					.putExtra("TITLE", bean.getTitle())
					.putExtra("PID", pid)
					.putExtra("HTM", "立即邀请")
					.putExtra("BANNER", "banner"));
				}else{
					context.startActivity(new Intent(context,WebViewActivity.class)
					.putExtra("URL", bean.getLocation()+"&app=true")
					.putExtra("TITLE", bean.getTitle())
					.putExtra("PID", pid)
					.putExtra("BANNER", "banner")
					);
				}
			}
		});
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }
}
