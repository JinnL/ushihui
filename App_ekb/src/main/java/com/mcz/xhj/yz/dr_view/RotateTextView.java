package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 项目名称：js_app
 * 类描述：  右上角45度文字
 * 创建人：shuc
 * 创建时间：2016/12/28 14:26
 * 修改人：DELL
 * 修改时间：2016/12/28 14:26
 * 修改备注：
 */
public class RotateTextView extends TextView{
    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(45, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }

}
