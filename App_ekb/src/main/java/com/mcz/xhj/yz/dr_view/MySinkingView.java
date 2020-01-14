
package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mcz.xhj.R;

/**
 * 水波浪球形进度View
 * @author caizhiming
 *
 */
public class MySinkingView extends FrameLayout {
    private static final int DEFAULT_TEXTCOLOT = 0xFFFFFFFF;

    private static final int DEFAULT_TEXTSIZE = 250;

    private float mPercent;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//圆外环

    private Paint mFillCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//实心圆

    private Bitmap mBitmap;

    private Bitmap mScaledBitmap;

    private float mLeft;

    private int mSpeed = 10;

    private int mRepeatCount = 0;

    private Status mFlag = Status.NONE;

    private int mTextColor = DEFAULT_TEXTCOLOT;

    private int mTextSize = DEFAULT_TEXTSIZE;

    public MySinkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setPercent(float percent) {
        mFlag = Status.RUNNING;
        mPercent = percent;
        postInvalidate();

    }

    public void setStatus(Status status) {
        mFlag = status;
    }

    public void clear() {
        mFlag = Status.NONE;
        if (mScaledBitmap != null) {
            mScaledBitmap.recycle();
            mScaledBitmap = null;
        }

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // 绘制外圆环
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(4);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(0XFFFF8247);
        canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mCirclePaint);

        //绘制实心圆
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setAntiAlias(true);
        mFillCirclePaint.setColor(0X8853B0FF);
        canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mFillCirclePaint);
        
        //裁剪成圆区域
        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(width / 2, height / 2, width / 2, Direction.CCW);
        canvas.clipPath(path, Op.REPLACE);

        if (mFlag == Status.RUNNING) {
            if (mScaledBitmap == null) {
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wave2);
                mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), getHeight(), false);
                mBitmap.recycle();
                mBitmap = null;
                mRepeatCount = (int) Math.ceil(getWidth() / mScaledBitmap.getWidth() + 0.5) + 1;
            }
            for (int idx = 0; idx < mRepeatCount; idx++) {
                canvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1) * mScaledBitmap.getWidth(), (1-mPercent) * getHeight(), null);
            }
            //绘制百分比的文字
            String str = (int) (mPercent * 100) + "%";
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(dp2px(12));
            mPaint.setStyle(Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            Rect tv_Rect = new Rect();
            mPaint.getTextBounds(str,0, str.length(), tv_Rect);
            canvas.drawText(str, getWidth()/2 + dp2px(1), getHeight() / 2 + dp2px(5), mPaint);

            mLeft += mSpeed;
            if (mLeft >= mScaledBitmap.getWidth())
                mLeft = 0;

            postInvalidateDelayed(20);
        }
        canvas.restore();

    }

    public enum Status {
        RUNNING, NONE
    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

}
