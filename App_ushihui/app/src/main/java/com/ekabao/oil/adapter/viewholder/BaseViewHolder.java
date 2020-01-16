package com.ekabao.oil.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/12.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static BaseViewHolder get(ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return get(itemView);
    }

    public static BaseViewHolder get(View view){
        return new BaseViewHolder(view);
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setTextView(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }


    /**
     * 设置TextView的内容
     * @param itemId
     * @param text
     */
    public void setText(int itemId,String text){
        TextView tv = getView(itemId);
        tv.setText(text);
    }

    /**
     * 设置图片
     * @param itemId
     * @param imageId
     */
    public void setBitmapImage(int itemId,int imageId){
        ImageView iv = getView(itemId);
        iv.setImageResource(imageId);
    }
}
