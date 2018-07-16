package com.pasc.lib.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by linhaiyang807 on 17/12/7.
 * <p>
 * 间隔相同的TextView，只能显示中文，如果显示英文或者中英文混和，都会显示异常
 *
 * @author linhaiyang807
 */

public class EqualTextView extends AppCompatTextView {

    private Paint mPaint;
    private String mText;
    private int mWidth;

    public EqualTextView(Context context) {
        this(context, null);
    }

    public EqualTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mText = getText().toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth != 0) {
            setMeasuredDimension(mWidth, getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (mText != null && mPaint != null) {
            float divide = (mWidth - mPaint.measureText(mText) - paddingLeft - paddingRight)
                    / (mText.length() - 1);
            float height =
                    (-mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().descent) * 0.5f
                            + getMeasuredHeight() * 0.5f;
            for (int i = 0; i < mText.length(); i++) {
                String str = mText.substring(i, i + 1);
                if (i == 0) {
                    canvas.drawText(str, paddingLeft, height, mPaint);
                } else if (i == mText.length() - 1) {
                    canvas.drawText(str, i * (mPaint.getTextSize() + divide) - paddingRight, height,
                            mPaint);
                } else {
                    canvas.drawText(str, i * (mPaint.getTextSize() + divide), height, mPaint);
                }
            }
        }
    }

    /**
     * 与tv等长，DivideTextView的字间距将根据tv来设置
     */
    public void with(final TextView tv) {
        Log.e("EqualTextView", "with");
        tv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tv.getViewTreeObserver().removeOnPreDrawListener(this);
                mWidth = tv.getMeasuredWidth();
                Log.e("EqualTextView", "mWidth -> " + mWidth);
                mPaint = tv.getPaint();
                mPaint.setColor(getCurrentTextColor());
                requestLayout();
                invalidate();
                return EqualTextView.super.onPreDraw();
            }
        });
    }
}
