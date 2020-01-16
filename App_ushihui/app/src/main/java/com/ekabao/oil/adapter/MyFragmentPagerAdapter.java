package com.ekabao.oil.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ekabao.oil.ui.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragmentList;
    private List<String> titleList;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }


   /* public static class PageInfo{
        public  Fragment fragment;
        public String title;
        public PageInfo(Fragment fragment, String title) {
            super();
            this.fragment = fragment;
            this.title = title;
        }
    }*/

}
