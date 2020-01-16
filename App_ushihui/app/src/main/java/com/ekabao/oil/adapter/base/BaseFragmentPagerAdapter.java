package com.ekabao.oil.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ekabao.oil.ui.fragment.BaseFragment;

import java.util.List;

/***
 *  fragments 适配器
 * author: tonglj
 * date:2020/1/9
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;


    protected String[] mTitle = null;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }


    public BaseFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] title) {
        super(fm);
        this.mFragments = fragments;
        this.mTitle = title;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        if (mTitle == null || mTitle.length == 0) {
            return "";
        }
        return mTitle[position];
    }

}
