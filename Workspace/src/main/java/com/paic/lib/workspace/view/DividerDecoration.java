package com.paic.lib.workspace.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.paic.lib.workspace.R;
import com.paic.lib.workspace.widget.CellItemView;
import com.paic.lib.workspace.widget.Workspace;

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = DividerDecoration.class.getSimpleName();
    private int dividerHeight;
    private Paint dividerPaint;
    final int mGridSpanCount;

    public DividerDecoration(Context context, int gridSpanCount) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.colorWorkspaceDivider));
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);

        //如果不是GridLayout建议外面设置为负数
        mGridSpanCount = gridSpanCount;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (mGridSpanCount < 2)
            return;

        int childCount = parent.getChildCount();

        int left, top, right, bottom;
        CellItemView cellItemView;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (!(view instanceof CellItemView))
                continue;

            cellItemView = (CellItemView) view;
            final int positionInGroup = cellItemView.getPositionInGroup();
            final int positionType = positionInGroup % mGridSpanCount;
            final boolean firstline = positionInGroup < mGridSpanCount;
            Log.i(TAG, cellItemView.getTitle() + "positionInGroup:" + positionInGroup + " mGridSpanCount " + mGridSpanCount + " positionType=" + positionType + " firstline=" + firstline);

            if (mGridSpanCount - 1 == positionType) {
                if (!firstline) {
                    // 绘制上边
                    left = view.getLeft();
                    top = view.getTop() - dividerHeight;
                    right = view.getRight();
                    bottom = top + dividerHeight;
                    c.drawRect(left, top, right, bottom, dividerPaint);
                }
            } else {
                if (!firstline) {
                    // 绘制上边
                    left = view.getLeft();
                    top = view.getTop() - dividerHeight;
                    right = view.getRight();
                    bottom = top + dividerHeight;
                    c.drawRect(left, top, right, bottom, dividerPaint);
                }

                left = view.getRight();
                top = view.getTop() - dividerHeight;
                right = view.getRight() + dividerHeight;
                bottom = view.getBottom() + dividerHeight;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
