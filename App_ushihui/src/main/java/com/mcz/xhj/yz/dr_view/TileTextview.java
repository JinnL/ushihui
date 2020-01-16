package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 倾斜45度角的textview
 * @author DELL
 *
 */

public class TileTextview extends TextView{
        private int du;
	
        public TileTextview(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

    public void setDegrees(int mDegrees){
        this.du = mDegrees;
    }
    @Override
        protected void onDraw(Canvas canvas) {
            //倾斜度45,上下左右居中
            canvas.rotate(-du, getMeasuredWidth()/2, getMeasuredHeight()/2);
            super.onDraw(canvas);
        }
        
}

