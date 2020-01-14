package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.mcz.xhj.R;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

import lib.lhh.fiv.library.FrescoImageView;

/**
 * Created by linhonghong on 2015/8/10.
 */
public class APSTSViewPager extends ViewPager implements AdvancedPagerSlidingTabStrip.ViewTabProvider{
    private boolean mNoFocus = false; //if true, keep View don't move
    private static final int VIEW_FIRST 		= 0;
    private static final int VIEW_SECOND	    = 1;
    private static final int VIEW_THIRD       = 2;
    private static final int VIEW_FOURTH    = 3;

    private static final int VIEW_SIZE = 4;
    public APSTSViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public APSTSViewPager(Context context){
        this(context,null);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mNoFocus) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setNoFocus(boolean b){
        mNoFocus = b;
    }

    @Override
    public View onSelectIconView(int position, View view, ViewGroup parent) {
        FrescoImageView draweeView;
        if(view == null){
            draweeView = new FrescoImageView(getContext());
            draweeView.setLayoutParams(new RelativeLayout.LayoutParams(50,50));
            view = draweeView;
        }
        draweeView = (FrescoImageView)view;
        switch (position){
            case  VIEW_FIRST:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/categry_icon_p.png", R.mipmap.ico_home_press);
                break;
            case  VIEW_SECOND:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/classify_icon_p.png",R.mipmap.ico_fund_press);
                break;
            case  VIEW_THIRD:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/mine_icon_p.png",R.mipmap.ico_account_press);
                break;
            case  VIEW_FOURTH:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/update_dynamic_p.png",R.mipmap.ico_more_press);
                break;
            default:
                break;
        }
        return draweeView;
    }

    @Override
    public View onIconView(int position, View view, ViewGroup parent) {
        FrescoImageView draweeView;
        if(view == null){
            draweeView = new FrescoImageView(getContext());
            draweeView.setLayoutParams(new RelativeLayout.LayoutParams(50,50));
            view = draweeView;
        }
        draweeView = (FrescoImageView)view;
        switch (position){
            case  VIEW_FIRST:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/categry_icon_n.png",R.mipmap.ico_home);
                break;
            case  VIEW_SECOND:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/classify_icon_n.png",R.mipmap.ico_fund);
                break;
            case  VIEW_THIRD:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/mine_icon_n.png",R.mipmap.ico_account);
                break;
            case  VIEW_FOURTH:
                draweeView.loadView("https://raw.githubusercontent.com/HomHomLin/AdvancedPagerSlidingTabStrip/master/Pic/update_dynamic_n.png",R.mipmap.ico_more);
                break;
            default:
                break;
        }
        return draweeView;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        if(position >= 0 && position < VIEW_SIZE){
//            switch (position){
//                case  VIEW_FIRST:
//                    return  "first";
//                case  VIEW_SECOND:
//                    return  "second";
//                case  VIEW_THIRD:
//                    return  "third";
//                case  VIEW_FOURTH:
//                    return  "fourth";
//                default:
//                    break;
//            }
//        }
//        return null;
//    }

}