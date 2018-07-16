package com.pasc.lib.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pasc.lib.base.util.SizeUtils;

/**
 * Created by ex-lingchun001 on 2018/2/6.
 */

public class RecycleViewItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = SizeUtils.dp2px(4);//分割线高度，默认为10px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int firstItemMrginTop = 30;
    private boolean needDrawDecor = false;

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param orientation 列表方向
     */
    public RecycleViewItemDecoration(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL
                && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public RecycleViewItemDecoration(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewItemDecoration(Context context, int orientation, int dividerHeight,
                                     int dividerColor) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setFirstItemMaginTop(int firstItemMrginTop) {
        this.firstItemMrginTop = firstItemMrginTop;
    }

    public void setNeedDrawDecor(boolean needDrawDecor) {
        this.needDrawDecor = needDrawDecor;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int top = -6;
        int bottom = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            top = mDividerHeight * 2;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int itemCount = layoutManager.getItemCount();
        if (parent.getChildAdapterPosition(view) == itemCount - 1) {
            bottom = mDividerHeight * 2;
        }
        outRect.set(0, top, 0, bottom);
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (needDrawDecor) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();

        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + layoutParams.rightMargin;
            int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
