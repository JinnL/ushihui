package com.mcz.xhj.yz.dr_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.viewpagerindicator.TabPageIndicator;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_offtime_tiyan;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_unuse_tiyan;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_used_tiyan;

import butterknife.BindView;


public class ConponsActTiyan extends BaseActivity {
	private String[] tab;//标题
	// 初始化数据
	private TabFragPA tabPA;
	@BindView(R.id.vp_conpons)
	ViewPager vp_conpons;
	@BindView(R.id.more_indicator)
	TabPageIndicator tabin;
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.title_leftimageview)
	ImageView leftima;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_conpons_tiyan;
	}
	private Fragment frag;
	private Fragment frag1;
	private Fragment frag2;
	@Override
	protected void initParams() {
		centertv.setText("我的体验金");
		leftima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		tab = new String[] { "未使用", "已使用", "已失效"};
		// 给viewpager设置适配器
		tabPA = new TabFragPA(getSupportFragmentManager());//继承fragmentactivity
		vp_conpons.setAdapter(tabPA);
		// viewpagerindictor和viewpager关联
		tabin.setViewPager(vp_conpons);
		vp_conpons.setOffscreenPageLimit(2);
//
	}
	
	class TabFragPA extends FragmentPagerAdapter {

		public TabFragPA(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0://未用
				frag = frag==null?new Frag_Myconpons_unuse_tiyan():frag;
				return frag;
			case 1://已用
				frag1 = frag1==null?new Frag_Myconpons_used_tiyan():frag1;
				return frag1;
			case 2://已过期
				frag2 = frag2==null?new Frag_Myconpons_offtime_tiyan():frag2;
				return frag2;
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

	}

}
