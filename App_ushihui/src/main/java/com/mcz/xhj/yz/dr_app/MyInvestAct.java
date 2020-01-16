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
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myinvest_do;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myinvest_finish;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myinvest_wait;

import butterknife.BindView;

public class MyInvestAct extends BaseActivity {
	private String[] tab;//标题
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
		return R.layout.act_myinvest;
	}
	private Fragment frag;
	private Fragment frag1;
	private Fragment frag2;
	@Override
	protected void initParams() {
		centertv.setText("我的投资");
		leftima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		tab = new String[] { "待回款", "已回款", "募集中"};
		// 给viewpager设置适配器
		tabPA = new TabFragPA(getSupportFragmentManager());// 继承fragmentactivity
		vp_conpons.setAdapter(tabPA);
		// viewpagerindictor和viewpager关联
		tabin.setViewPager(vp_conpons);

	}
	class TabFragPA extends FragmentPagerAdapter {

		public TabFragPA(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0://待回款
				frag = frag==null?new Frag_Myinvest_wait():frag;
				return frag;
			case 1://已回款
				frag1 = frag1==null?new Frag_Myinvest_finish():frag1;
				return frag1;
			case 2://投资中
				frag2 = frag2==null?new Frag_Myinvest_do():frag2;
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
