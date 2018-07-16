package com.pasc.lib.base.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决ScrollView嵌套RecyclerView，RecyclerView显示不全的问题
 * Created by duyuan797 on 17/10/14.
 */

public class SmtRecyclerView extends RecyclerView {
    //设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 否则为 true
    private boolean needScrollBar = false;

    public SmtRecyclerView(Context context) {
        super(context);
    }

    public SmtRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmtRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!needScrollBar) {
            int expandSpec =
                    MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

}
