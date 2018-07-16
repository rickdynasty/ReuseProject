package com.pasc.lib.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.lib.base.R;

/**
 * Created by zc on 2018年02月23日15:12:23
 */
public class FooterView extends LinearLayout {
    //结束加载显示
    private LinearLayout mLayoutEndLoading;
    //加载中显示
    private LinearLayout mLayoutLoading;
    //结束加载提示文字
    private TextView mTvEndLoading;

    /**
     * @param context
     */
    public FooterView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public FooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //防止footView被点击导致崩溃
        return true;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_footer_normal, this, true);
        mLayoutEndLoading = (LinearLayout) view.findViewById(R.id.layout_new_footer_no_more);
        mLayoutLoading = (LinearLayout) view.findViewById(R.id.layout_new_footer);
        mTvEndLoading = (TextView) view.findViewById(R.id.tv_new_footer_tips);
    }

    /**
     * 开始加载
     */
    public void startLoading() {
        mLayoutEndLoading.setVisibility(View.INVISIBLE);
        mLayoutLoading.setVisibility(View.VISIBLE);
    }

    /**
     * 结束加载
     *
     * @param tips 结束提示语
     */
    public void endLoading(String tips) {
        mLayoutEndLoading.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.INVISIBLE);
        if (TextUtils.isEmpty(tips)) {
            tips = "没有更多数据";
        }
        mTvEndLoading.setText(tips);
    }

    /**
     * 重置
     */
    public void reset() {
        endLoading("下拉加载更多数据");
    }


}
