package com.pasc.lib.base.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.pasc.lib.base.R;


public class CircleProcessView extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画圆环的画笔背景色
    private Paint mRingPaintBg;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环背景颜色
    private int mRingBgColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    //中心蒙板
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();//颜色渐变插值器
    private boolean isGradual = true;//是否显示渐变色
    // 控件背景
    private Paint mPaintBg;
    private int mBgStrokeWidth = 500;
    private float mBgRadius;
    RectF oval;

    public CircleProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    //属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
        mRingBgColor = typeArray.getColor(R.styleable.TasksCompletedView_ringBgColor, 0xFFFFFFFF);

        mRingRadius = mRadius + mStrokeWidth / 2 + 10;

        mBgRadius = mRadius + mBgStrokeWidth / 2;
    }

    //初始化画笔
    private void initVariable() {
        //内圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //外圆弧背景
        mRingPaintBg = new Paint();
        mRingPaintBg.setAntiAlias(true);
        mRingPaintBg.setColor(mRingBgColor);
        mRingPaintBg.setStyle(Paint.Style.STROKE);
        mRingPaintBg.setStrokeWidth(mStrokeWidth);


        //外圆弧
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        oval = new RectF();

        //控件背景
        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(Color.WHITE);
        mPaintBg.setStyle(Paint.Style.STROKE);
        mPaintBg.setStrokeWidth(mBgStrokeWidth);
    }

    //画图
    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;

        //内圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        //控件填充
        RectF oval2 = new RectF();
        oval2.left = (mXCenter - mBgRadius);
        oval2.top = mYCenter - mBgRadius;
        oval2.right = mBgRadius * 2 + (mXCenter - mBgRadius);
        ;
        oval2.bottom = mBgRadius * 2 + (mYCenter - mBgRadius);
        canvas.drawArc(oval2, 0, 360, false, mPaintBg);

        //外圆弧背景
        RectF oval1 = new RectF();
        oval1.left = (mXCenter - mRingRadius);
        oval1.top = (mYCenter - mRingRadius);
        oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(oval1, 0, 360, false, mRingPaintBg); //圆弧所在的椭圆对象、圆弧的起始角度、圆弧的角度、是否显示半径连线

        //外圆弧进度
        float startAngle = 270;
        float sweepAngle = 440;
        Matrix mMatrix = new Matrix();
        mMatrix.setRotate(86, mXCenter, mYCenter);
        SweepGradient mShader = new SweepGradient(startAngle, sweepAngle, new int[]{0x330000ff, 0x990000ee}, null);
        mShader.setLocalMatrix(mMatrix);
        mRingPaint.setShader(mShader);
        oval.left = (mXCenter - mRingRadius);
        oval.top = (mYCenter - mRingRadius);
        oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(oval, 90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint);


    }

    //设置进度
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();//重绘
    }

    //设置中心颜色
    public void setCenterColor(String color) {
        mCirclePaint.setColor(Color.parseColor(color));
        postInvalidate();//重绘
    }
}
