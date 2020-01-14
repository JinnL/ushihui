package com.ekabao.oil.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lj on 2017/8/2.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int top,bottom,left,right;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (space!=0){
            outRect.top = space;
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = 0;
        }else {
            outRect.top = 0;
            outRect.left = 0;
            outRect.right = right;
            outRect.bottom = bottom;
        }

    }
}
