package com.ekabao.oil.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.base.BaseFragmentPagerAdapter;
import com.ekabao.oil.ui.fragment.BaseFragment;

import java.util.List;


/***
 *
 * author: tonglj
 * date:2020/1/9
 */
public class MainPagerAdapter extends BaseFragmentPagerAdapter {

	
	private String [] tabTitles={"首页","加油","发现","我的"};
	
	private int[] imageResId={R.drawable.tab_menu_home, R.drawable.tab_menu_shop,R.drawable.tab_menu_find,R.drawable.tab_menu_mine};
	public MainPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
		super(fm, fragments);
	}
	
	/**
	 * 获取Tab视图
	 * @param context 上下文
	 * @param position 位置
	 * @return view
	 */
	public View getTabView(Context context,int position){
        View view = LayoutInflater.from(context).inflate(R.layout.widget_tab_item, null);
        TextView tv_tab= (TextView) view.findViewById(R.id.tv_tab);
        tv_tab.setText(tabTitles[position]);
        ImageView iv_tab = (ImageView) view.findViewById(R.id.iv_tab);
        iv_tab.setImageResource(imageResId[position]);
        return view;
    }
	
	
}
