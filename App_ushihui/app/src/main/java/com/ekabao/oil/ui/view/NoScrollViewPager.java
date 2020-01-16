package com.ekabao.oil.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/***
 *  可禁用滚动效果的viewpager
 * author: tonglj
 * date:2020/1/10
 */
public class NoScrollViewPager extends ViewPager {
    //默认可滑动
    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }


    /**
     * 设置viewpager是否可滑动
     *
     * @param noScroll
     */
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(event);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);

    }

    @Override
    public void setCurrentItem(int item) {
        this.setCurrentItem(item, true);
    }

}