package com.pasc.lib.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by wujianning385 on 2018/4/26.
 * <p>
 * 分组滑动控件（更多服务使用）
 */

public class GroupScrollView extends ScrollView {
    private int handlerWhatId = 65984;
    private ScrollViewListener scrollViewListener = null;
    private int timeInterval = 20;
    private int lastY = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == handlerWhatId) {
                if (lastY == getScrollY()) {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(true);
                    }
                } else {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(false);
                    }
                    handler.sendMessageDelayed(handler.obtainMessage(handlerWhatId, this), timeInterval);
                    lastY = getScrollY();
                }
            }
        }
    };

    public GroupScrollView(Context context) {
        super(context);
    }

    public GroupScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GroupScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(handler.obtainMessage(handlerWhatId, this), timeInterval);
        }
        return super.onTouchEvent(ev);
    }


    public interface ScrollViewListener {
        /**
         * 滑动监听
         *
         * @param scrollView ScrollView控件
         * @param x          x轴坐标
         * @param y          y轴坐标
         * @param oldx       上一个x轴坐标
         * @param oldy       上一个y轴坐标
         */
        void onScrollChanged(GroupScrollView scrollView, int x, int y, int oldx, int oldy);

        /**
         * 是否滑动停止
         *
         * @param isScrollStop true:滑动停止;false:未滑动停止
         */
        void onScrollStop(boolean isScrollStop);


    }

}
