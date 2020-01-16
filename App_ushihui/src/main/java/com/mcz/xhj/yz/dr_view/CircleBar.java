package com.mcz.xhj.yz.dr_view;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleBar extends View {

	private RectF mColorWheelRectangle = new RectF();
	private Paint mDefaultWheelPaint;
	private Paint mColorWheelPaint;
	private Paint mColorWheelPaintCentre;
	private Paint mTextNumber;
	private Paint mTextSymbol;
	private Paint mTextName;
	private Paint mTextNo;
	
	private float circleStrokeWidth;
	private float mSweepAnglePer;
	private String mPercent="";
//	private String mPercent="0.00";
//	private String productName="新手标";
	private String productName="";
	private String productNo="";
	private int rate, stepnumber, stepnumbernow;
	private float pressExtraStrokeWidth;
	private BarAnimation anim;
	private int stepnumbermax = 100;// 默认最大步数
	private float mName_y, mNo_y, mPercent_y,mPeriods_y;
	private DecimalFormat fnum = new DecimalFormat(".00");// 格式为保留小数点后一位

	public CircleBar(Context context) {
		super(context);
		init(context, null, 0);
	}

	public CircleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public CircleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		mColorWheelPaint = new Paint();
		mColorWheelPaint.setColor(Color.rgb(250, 191, 19));
		mColorWheelPaint.setStyle(Paint.Style.STROKE);// 空心
		mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
		mColorWheelPaint.setAntiAlias(true);// 去锯齿

		mColorWheelPaintCentre = new Paint();
		mColorWheelPaintCentre.setColor(Color.rgb(250, 250, 250));
		mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
		mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
		mColorWheelPaintCentre.setAntiAlias(true);

		mDefaultWheelPaint = new Paint();
		mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
		mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
		mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
		mDefaultWheelPaint.setAntiAlias(true);

		mTextNumber = new Paint();
		mTextNumber.setAntiAlias(true);
		mTextNumber.setColor(Color.parseColor("#fe7634"));
		
		mTextSymbol = new Paint();
		mTextSymbol.setAntiAlias(true);
		mTextSymbol.setColor(Color.parseColor("#fe7634"));
		
		mTextName = new Paint();
		mTextName.setAntiAlias(true);
		mTextName.setColor(Color.parseColor("#333333"));
		
		mTextNo = new Paint();
		mTextNo.setAntiAlias(true);
		mTextNo.setColor(Color.parseColor("#000000"));
		
		anim = new BarAnimation();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(mColorWheelRectangle, 0, 359, false, mDefaultWheelPaint);
		canvas.drawArc(mColorWheelRectangle, 0, 359, false,
				mColorWheelPaintCentre);
		canvas.drawArc(mColorWheelRectangle, 270, mSweepAnglePer, false,
				mColorWheelPaint);

//		canvas.drawText(mPercent + "%", mColorWheelRectangle.centerX()
//				- (mTextP.measureText(String.valueOf(mPercent) + "%") / 2),
//				mPercent_y, mTextP);
		// 百分比
		canvas.drawText(mPercent+"" , mColorWheelRectangle.centerX()
				- (mTextNumber.measureText(String.valueOf(mPercent)) / 2),
				mPercent_y, mTextNumber);
//		canvas.drawText("%" , mColorWheelRectangle.centerX()
//				+ (mTextNumber.measureText(String.valueOf(mPercent)) / 2),
//				mPercent_y, mTextSymbol);
		
		// 项目名称
		canvas.drawText(productName , mColorWheelRectangle.centerX()
				- (mTextNumber.measureText(String.valueOf(productName)) / 3),
				mName_y, mTextName);
//		// 项目编号
//		canvas.drawText(productNo , mColorWheelRectangle.centerX()
//				- (mTextNumber.measureText(String.valueOf(productNo)) / 8),
//				mNo_y, mTextNo);
		
		
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int min = Math.min(width, height);// 获取View最短边的长度
		setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
		circleStrokeWidth = Textscale(19, min);// 圆弧的宽度
		pressExtraStrokeWidth = Textscale(2, min);// 圆弧离矩形的距离
		mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth,
				circleStrokeWidth + pressExtraStrokeWidth, min
						- circleStrokeWidth - pressExtraStrokeWidth, min
						- circleStrokeWidth - pressExtraStrokeWidth);// 设置矩形
		mTextNumber.setTextSize(Textscale(98, min));
		mTextSymbol.setTextSize(Textscale(60, min));
		mTextNo.setTextSize(Textscale(26, min));
		mTextName.setTextSize(Textscale(60, min));
		mName_y = Textscale(150, min);
		mNo_y = Textscale(160, min);
		mPercent_y = Textscale(280, min);
		mPeriods_y = Textscale(370, min);
		mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
		mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth);
		mDefaultWheelPaint
				.setStrokeWidth(circleStrokeWidth - Textscale(2, min));
		mDefaultWheelPaint.setShadowLayer(Textscale(10, min), 0, 0,
				Color.rgb(127, 127, 127));// 设置阴影
	}

	/**
	 * 进度条动画
	 * 
	 * @author Administrator
	 * 
	 */
	public class BarAnimation extends Animation {
		public BarAnimation() {

		}

		/**
		 * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
		 * 然后调用postInvalidate()不停的绘制view。
		 */
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				mPercent = fnum.format(rate);// 将浮点值四舍五入保留一位小数
				mSweepAnglePer = interpolatedTime * stepnumber * 360
						/ stepnumbermax;
				stepnumbernow = (int) (interpolatedTime * stepnumber);
			} else {
				mPercent = fnum.format(rate);// 将浮点值四舍五入保留一位小数
				mSweepAnglePer = stepnumber * 360 / stepnumbermax;
				stepnumbernow = stepnumber;
			}
			postInvalidate();
		}
	}

	/**
	 * 根据控件的大小改变绝对位置的比例
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public float Textscale(float n, float m) {
		return n / 500 * m;
	}

	/**
	 * 更新步数和设置一圈动画时间
	 * 
	 * @param stepnumber
	 * @param time
	 */
	public void update(int rate, int stepnumber, int time,String no,String name) {
		this.rate = rate;
		this.stepnumber = stepnumber;
		this.productNo = no;
		this.productName = name;
		anim.setDuration(time);
		// setAnimationTime(time);
		this.startAnimation(anim);
	}

	/**
	 * 设置每天的最大步数
	 * 
	 * @param Maxstepnumber
	 */
	public void setMaxstepnumber(int Maxstepnumber) {
		this.stepnumbermax = Maxstepnumber;
	}

	/**
	 * 设置进度条颜色
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		mColorWheelPaint.setColor(Color.rgb(red, green, blue));
	}

	/**
	 * 设置动画时间
	 * 
	 * @param time
	 */
	public void setAnimationTime(int time) {
		anim.setDuration(time * stepnumber / stepnumbermax);// 按照比例设置动画执行时间
	}

}
