package com.pasc.lib.base.widget;

/**
 * Created by linhaiyang807 on 17/11/7.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.pasc.lib.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 上滚广告
 * Created by linhaiyang807 on 17/11/7.
 */
public class UpRollView extends LinearLayout implements OnLayoutChangeListener {

    private static final String TAG = "UpRollView";

    private static final int WHAT_SCROLL = 0;
    /**
     * 默认滚动耗时
     */
    private static final int DEFAULT_SCROLL_TIME = 1000;
    /**
     * 默认滚动间隔
     */
    private static final int DEFAULT_DELAY_TIME = 3000;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 正在展示的View的Index
     */
    private int mCurrentItem = 0;
    /**
     * 是否需要滚动
     */
    private boolean mIsAutoScrolled = false;
    /**
     * 是否需要增删item-在滚动完成之后，删除上一个item，添加下一个item
     */
    private boolean mNeedChangeItem = false;

    private int mItemHeight;
    private int mItemWidth;

    //控制显示行数
    private int extraHeight;

    /**
     * 滚动的耗时
     */
    private int scrollTime = DEFAULT_SCROLL_TIME;
    /**
     * 滚动的间隔
     */
    private int delayTime = DEFAULT_DELAY_TIME;

    private Scroller mScroller;
    private BaseAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;

    public UpRollView(Context context) {
        this(context, null);
    }

    public UpRollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpRollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);
        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UpRollView);
        try {
            extraHeight = a.getDimensionPixelSize(R.styleable.UpRollView_extraHeight, 0);
        } catch (Exception e) {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, getDefaultSize(0, MeasureSpec.AT_MOST));
            mItemWidth = Math.max(child.getMeasuredWidth(), mItemWidth);
            mItemHeight = child.getMeasuredHeight();
        }
        setMeasuredDimension(widthMeasureSpec, 2 * mItemHeight + extraHeight);
    }

    public void notifyDataChanged() {
        setItemClickListener();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            return;
        }
        if (mScroller.isFinished() && mNeedChangeItem) {
            changeViewItem();
            mNeedChangeItem = false;
        }
        super.computeScroll();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        removeOnLayoutChangeListener(this);
        scrollTo(0, 0);
        sendScrollMessage(delayTime);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
        setItemClickListener();
    }

    public void setItemHeight(int height) {
        this.mItemHeight = height;
        invalidate();
    }

    /**
     * 开始自动轮播
     */
    public void startAutoScroll() {
        if (mAdapter == null) return;
        stopAutoScroll();
        removeAllViewsInLayout();
        mCurrentItem = 0;
        mIsAutoScrolled = true;

        int count = mAdapter.getCount();
        if (count > 2) {
            if (count > 3) {
                addView(mAdapter.getView(0));
                addView(mAdapter.getView(1));
                addView(mAdapter.getView(2));
                addView(mAdapter.getView(3));
            } else {
                addView(mAdapter.getView(0));
                addView(mAdapter.getView(1));
                addView(mAdapter.getView(2));
            }
            sendScrollMessage(delayTime);
        } else if (count == 2) {
            addView(mAdapter.getView(0));
            addView(mAdapter.getView(1));
        } else if (count == 1) {
            addView(mAdapter.getView(0));
        } else {
            //            throw new IllegalArgumentException("No view to roll");
        }
    }

    public void stopAutoScroll() {
        mIsAutoScrolled = false;
        mHandler.removeMessages(WHAT_SCROLL);
    }

    public boolean isScrolling() {
        return mIsAutoScrolled;
    }

    public int getScrollTime() {
        return scrollTime;
    }

    /**
     * 设置滚动耗时
     */
    public void setScrollTime(int scrollTime) {
        this.scrollTime = scrollTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    /**
     * 设置滚动间隔
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 设置适配器
     */
    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        mAdapter.registerObservable(this);
        setItemClickListener();
    }

    private void sendScrollMessage(long delayTimeInMills) {
        mHandler.removeMessages(WHAT_SCROLL);
        mHandler.sendEmptyMessageDelayed(WHAT_SCROLL, delayTimeInMills);
    }

    private void changeViewItem() {
        addOnLayoutChangeListener(this);
        int count = mAdapter.getCount();
        if (count > 3) {//当前滚动新闻条数大于3条
            removeView(mAdapter.getView(mCurrentItem % mAdapter.getCount()));
            removeView(mAdapter.getView((mCurrentItem + 1) % mAdapter.getCount()));
            mCurrentItem += 2;
            addNextItemView(mCurrentItem + 2);
            addNextItemView(mCurrentItem + 3);
        } else {
            removeView(mAdapter.getView(mCurrentItem % mAdapter.getCount()));
            mCurrentItem += 1;
            addNextItemView(mCurrentItem + 2);
        }
    }

    private void addNextItemView(int index) {
        View view = mAdapter.getView(index % mAdapter.getCount());
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        addView(view);
    }

    private void setItemClickListener() {
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View itemView = mAdapter.getView(i);
                itemView.setOnClickListener(mOnClickListener);
            }
        }
    }

    public boolean isNeedChangeItem() {
        return mNeedChangeItem;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SCROLL:
                    mNeedChangeItem = true;
                    mScroller.startScroll(0, 0, 0, mItemHeight * 2, scrollTime);
                    invalidate();
                    break;
            }
            return false;
        }
    });

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, mAdapter.getItemId(v));
            }
        }
    };

    public abstract static class BaseAdapter<T> {

        protected Context mContext;
        protected List<T> mDatas;
        protected List<View> mViews;
        protected UpRollView mObservable;

        public BaseAdapter(Context context, List<T> dataList) {
            this.mContext = context;
            this.mDatas = dataList;
            mViews = new ArrayList<>();
        }

        public void registerObservable(UpRollView observable) {
            this.mObservable = observable;
        }

        public void setDataList(List<T> dataList) {
            this.mDatas = dataList;
            mViews.clear();
            notifyDataChanged();
        }

        public void addDataList(List<T> dataList) {
            this.mDatas.addAll(dataList);
            notifyDataChanged();
        }

        public void notifyDataChanged() {
            mObservable.notifyDataChanged();
        }

        public int getCount() {
            if (mDatas != null) {
                return mDatas.size();
            } else {
                return 0;
            }
        }

        public int getItemId(View v) {
            return mViews.indexOf(v);
        }

        public abstract View getView(int position);

        public T getItem(int position) {
            if (mDatas == null || mDatas.size() - 1 < position) {
                return null;
            }
            return mDatas.get(position);
        }
    }

    public static class SimpleTextAdapter extends BaseAdapter<String> {

        public SimpleTextAdapter(Context context, List<String> dataList) {
            super(context, dataList);
        }

        @Override
        public View getView(int position) {
            if (mDatas == null || mDatas.isEmpty()) {
                return null;
            }
            TextView tv;
            if (mViews.size() == position) {
                tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.item_simple_text, null);
                tv.setText(mDatas.get(position));
                mViews.add(tv);
            } else {
                tv = (TextView) mViews.get(position);
            }
            return tv;
        }
    }


}
