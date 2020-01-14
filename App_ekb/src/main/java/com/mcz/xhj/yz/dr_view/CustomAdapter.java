package com.mcz.xhj.yz.dr_view;

import android.view.View;

/**
 * Created by zhulang on 2017/8/25.
 *
 * 自定义的Adapter
 */

public abstract class CustomAdapter {
    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public abstract View getView(int i);

    public void notifyDataSetChanged(){
        if(null != mOnNotifyDataSetChangedListener){
            mOnNotifyDataSetChangedListener.OnNotifyDataSetChanged();
        }
    }


    /**
     *  释放一个接口 串联adapter与view中间的数据刷新
     */
    public interface OnNotifyDataSetChangedListener{
        void OnNotifyDataSetChanged();
    }
    private OnNotifyDataSetChangedListener mOnNotifyDataSetChangedListener;
    public void setOnNotifyDataSetChangedListener(OnNotifyDataSetChangedListener listener){
        mOnNotifyDataSetChangedListener = listener;
    }
}
