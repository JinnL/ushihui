package com.mcz.xhj.yz.dr_view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhy on 16/5/7.
 */
public abstract class BasePageTransformer implements ViewPager.PageTransformer
{
    protected ViewPager.PageTransformer mPageTransformer = NonPageTransformer.INSTANCE;
    public static final float DEFAULT_CENTER = 0.5f;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void transformPage(View view, float position)
    {
        if (mPageTransformer != null)
        {
            mPageTransformer.transformPage(view, position);
        }

        pageTransform(view, position-1);
    }

    protected abstract void pageTransform(View view, float position);


}
