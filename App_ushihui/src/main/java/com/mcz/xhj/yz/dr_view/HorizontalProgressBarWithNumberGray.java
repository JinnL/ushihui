package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.mcz.xhj.R;

public class HorizontalProgressBarWithNumberGray extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
    private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_CIRCLE_COLOR = 0XFF3F51B5;

    protected Paint mPaint = new Paint();
    // 字体颜色
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    // 字体大小
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    // 覆盖进度高度
    protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);
    // 覆盖进度颜色
    protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
    // 未覆盖进度高度
    protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);
    // 未覆盖进度颜色
    protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
    // 圆的颜色
    protected int mCircleColor = DEFAULT_CIRCLE_COLOR;

    protected int mRealWidth;

    protected boolean mIfDrawText = true;
    protected boolean mIfDrawCircle = true;

    protected static final int VISIBLE = 0;

    public HorizontalProgressBarWithNumberGray(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithNumberGray(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        obtainStyledAttributes(attrs);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);
    }

    private void obtainStyledAttributes(AttributeSet attrs) {
        // 获取自定义属性
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithNumber);
        mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progressBar_text_color, DEFAULT_TEXT_COLOR);
        mTextSize = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progressBar_text_size, mTextSize);
        mCircleColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_circle_color, DEFAULT_CIRCLE_COLOR);
        mReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color, mTextColor);
        mUnReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color, DEFAULT_COLOR_UNREACHED_COLOR);
        mReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height, mReachedProgressBarHeight);
        mUnReachedProgressBarHeight = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height, mUnReachedProgressBarHeight);
        int textVisible = attributes.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility, VISIBLE);
        if (textVisible != VISIBLE) {
            mIfDrawText = false;
        }
        attributes.recycle();
        int left = (int) (mReachedProgressBarHeight*0.1), right = (int) (mReachedProgressBarHeight*0.1);
        int top = (int) (mReachedProgressBarHeight * 0.3 + dp2px(1)), bottom = (int) (mReachedProgressBarHeight*0.3 + dp2px(1));
        setPadding(left, top, right, bottom);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mRealWidth = getMeasuredWidth();
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            float textHeight = (mPaint.descent() - mPaint.ascent());
            result = (int) (getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight), Math.abs(textHeight)));
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedBg = false;
        float radius = (mReachedProgressBarHeight + getPaddingBottom() + getPaddingTop());//圆的半径
        float radio = getProgress() * 1.0f / getMax();
        float progressPosX = (int) (mRealWidth * radio);
        String text = getProgress() + "%";

        float textWidth = mPaint.measureText(text);
        //float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;
        Rect rect = new Rect();
        mPaint.getTextBounds(text,0,text.length(),rect);
        float textHeight = rect.bottom + rect.height();


        // 覆盖的进度
        float endX = progressPosX;
        if (endX > -1) {
            mPaint.setColor(mReachedBarColor);
            RectF rectF = new RectF(0, 0 - getPaddingTop() - getPaddingBottom(),
                    endX, mReachedProgressBarHeight - getPaddingBottom());
            canvas.drawRoundRect(rectF, 25, 25, mPaint);
        }

        // 未覆盖的进度
        if (!noNeedBg) {
            float start = progressPosX;
            mPaint.setColor(mUnReachedBarColor);
            RectF rectF = new RectF(start, 0 - getPaddingTop() - getPaddingBottom(),
                    mRealWidth + getPaddingRight(), mReachedProgressBarHeight - getPaddingBottom());
            canvas.drawRoundRect(rectF, 25, 25, mPaint);
        }

        if(endX <= radius){//覆盖的进度条小于圆球的直径
            //圆
            if(mIfDrawCircle){
                mPaint.setColor(mCircleColor);
                canvas.drawCircle(radius, 0, radius, mPaint);
            }

            //文本
            if(mIfDrawText){
                mPaint.setColor(mTextColor);
                canvas.drawText(text, radius-textWidth/3, -radius, mPaint);
            }

        } else if(radius<endX && endX<(mRealWidth - radius*2)){

            // 圆
            if (mIfDrawCircle) {
                mPaint.setColor(mCircleColor);
                canvas.drawCircle(progressPosX, 0, radius, mPaint);
            }

            // 文本
            if (mIfDrawText) {
                if(endX>(mRealWidth - radius*5)){
                    mPaint.setColor(mTextColor);
                    canvas.drawText(text, (mRealWidth - radius*4)-textWidth/3, -radius, mPaint);
                } else {
                    mPaint.setColor(mTextColor);
                    canvas.drawText(text, progressPosX-textWidth/2, -radius, mPaint);
                }
            }

        } else {
            //圆
            if(mIfDrawCircle){
                mPaint.setColor(mCircleColor);
                canvas.drawCircle((mRealWidth - radius), 0, radius, mPaint);
            }

            //文本
            if(mIfDrawText){
                mPaint.setColor(mTextColor);
                canvas.drawText(text, (mRealWidth - radius*4)-textWidth/3, -radius, mPaint);
            }
        }


        canvas.restore();

    }

    /**
     * dp 2 px
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}
