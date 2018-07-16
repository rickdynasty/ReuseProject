package com.pasc.lib.base.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by duyuan797 on 18/1/30.
 */

public class SmtScrollView extends NestedScrollView
        implements NestedScrollView.OnScrollChangeListener {
    private ViewGroup innerLayout;//ScrololView里的布局
    private float downY;//手指按下的Y坐标
    private float offset;

    public SmtScrollView(Context context) {
        this(context, null);
    }

    public SmtScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmtScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollChangeListener(this);
    }

    //布局已经加载完成后调用  一些params参数在这里都能取到值了
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        if (childCount == 1) {
            innerLayout = (ViewGroup) getChildAt(0);
        } else {
            throw new RuntimeException("ScrollView 只能有一个子布局");
        }
        offset = innerLayout.getMeasuredHeight() - getHeight();//偏移量
    }

    public void setOpenViewListener(OpenViewListener openViewListener) {
        this.openViewListener = openViewListener;
    }

    OpenViewListener openViewListener;

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                               int oldScrollY) {
        openViewListener.onSliding(scrollY);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (clampedY) {
            if (scrollY == 0) {
                openViewListener.onScroll2Top();
            } else if (scrollY == offset) {
                openViewListener.onScroll2Bottom();
            }
        }
        openViewListener.onSliding(scrollY);
    }

    public interface OpenViewListener {
        void onScroll2Top();//滑动到顶部

        void onScroll2Bottom();//滑动到底部

        void onSlidingDown(float offsetY);//屏幕下滑

        void onSlidingUp(float offsetY);//屏幕上滑

        void onSliding(float y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float tempY = ev.getRawY();
                float deltaY = tempY - downY;//手指竖直方向滑动的距离
                downY = tempY;
                if (deltaY > 0) {
                    openViewListener.onSlidingUp(deltaY);
                }
                if (deltaY < 0) {
                    openViewListener.onSlidingDown(deltaY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP://手指弹开
                downY = 0;
                break;
        }
        return super.onTouchEvent(ev);
    }
}
