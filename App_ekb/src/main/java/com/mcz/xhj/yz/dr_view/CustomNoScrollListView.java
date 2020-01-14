package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by zhulang on 2017/8/25.
 * 描述：自定义：使用LinearLayout实现ListView的功能，解决listview和scrollview的冲突
 */

public class CustomNoScrollListView extends LinearLayout{
    private CustomAdapter mAdapter;
    private OnItemClickListener onItemClickListener;

    public CustomNoScrollListView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public CustomNoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    public CustomNoScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }


    public void setAdapter(CustomAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("CustomAdapter is null, please check setAdapter(CustomAdapter adapter)...");
        }
        mAdapter = adapter;
        adapter.setOnNotifyDataSetChangedListener(new CustomAdapter.OnNotifyDataSetChangedListener() {
            @Override
            public void OnNotifyDataSetChanged() {
                notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    public CustomAdapter getAdapter() {
        return mAdapter;
    }


    private void notifyDataSetChanged() {

        removeAllViews();
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int index = i;
            final View view = mAdapter.getView(index);
            final Object obj = mAdapter.getItem(index);
            if (view == null) {
                throw new NullPointerException("item layout is null, please check getView()...");
            } else {
                // view 点击事件触发时回调我们自己的接口
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClicked(view,obj,index);
                        }
                    }
                });
            }

            addView(view, index, layoutParams);
        }

    }

    /**
     *
     * 回调接口
     */
    public interface OnItemClickListener {
        /**
         *
         * @param v 点击的 view
         *
         * @param obj 点击的 view 所绑定的对象
         *
         * @param position  点击位置的 index
         *
         */
        public void onItemClicked(View v, Object obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
}
