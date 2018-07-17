package com.pasc.lib.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.lib.base.widget.roundview.RoundTextView;
import com.pasc.lib.base.ICallBack;
import com.pasc.lib.base.R;
import com.pasc.lib.base.util.CommonUtils;

/**
 * 通用空布局、错误布局  View
 * Created by zc 2018年03月19日16:04:01
 */

public class EmptyView extends LinearLayout {

    private View view;
    private TextView tvTips;
    private ImageView ivIcon;
    private RoundTextView rtvRetry;
    private View loadingLayout;

    public EmptyView(Context context) {
        super(context);
        initView(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /*
     * 原生空页面,传attr改控件比较麻烦，改用根据type展示预定义页面
     */
    protected void initView(Context context, AttributeSet attrs) {
        // 设置背景
        setBackgroundColor(getResources().getColor(R.color.title_bar));
        view = LayoutInflater.from(context).inflate(R.layout.empty_layout, null);
        ivIcon = (ImageView) view.findViewById(R.id.iv_empty_icon);
        tvTips = (TextView) view.findViewById(R.id.tv_empty_tips);
        rtvRetry = (RoundTextView) view.findViewById(R.id.rtv_retry);
        loadingLayout = view.findViewById(R.id.layout_loading);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setGravity(Gravity.CENTER);
        addView(view, params);

        if (attrs != null) {
            //获得这个控件对应的属性。
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyView);

            try {
                String tips = a.getString(R.styleable.EmptyView_tips);
                String retry_text = a.getString(R.styleable.EmptyView_retry_text);
                if (!TextUtils.isEmpty(tips)) tvTips.setText(tips);
                if (!TextUtils.isEmpty(retry_text)) rtvRetry.setText(retry_text);

                boolean isShowRetry = a.getBoolean(R.styleable.EmptyView_isShowRetry, false);
                rtvRetry.setVisibility(isShowRetry ? View.VISIBLE : View.GONE);

                int icon = a.getResourceId(R.styleable.EmptyView_icon, R.drawable.ic_common_empty);
                ivIcon.setImageResource(icon);
            } finally {
                //回收这个对象
                a.recycle();
            }
        }
    }


    public EmptyView setErrorIconRes(int resId) {
        ivIcon.setImageResource(resId);
        return this;
    }

    public EmptyView setErrorTips(String text) {
        tvTips.setText(text);
        return this;
    }

    public EmptyView setRetryText(String text) {
        rtvRetry.setText(text);
        return this;
    }


    /**
     * 展示默认无网络布局
     *
     * @param callBack 点击重试的回调
     */
    public EmptyView showDefaultNoNetWork(ICallBack callBack) {
        setErrorTips(getResources().getString(R.string.common_network_error_tips));
        if (rtvRetry != null) rtvRetry.setVisibility(View.VISIBLE);
        setRetryText(getResources().getString(R.string.retry_now));
        setErrorIconRes(R.drawable.ic_common_no_network);
        setOnNetworkErrorClickRetry(callBack);
        setLoadingLayoutIsVisible(GONE);
        return this;
    }


    /**
     * 统一 默认 空布局展示
     */
    public EmptyView showDefaultEmptyLayout() {
        showCustomEmptyLayout(0, null);
        return this;
    }

    /**
     * 空布局  支持自定义文字
     *
     * @param text 提示文字
     * @return
     */
    public EmptyView showCustomTextEmptyLayout(String text) {
        showCustomEmptyLayout(0, text);
        return this;
    }

    /**
     * 空布局  支持自定义图标
     *
     * @param resId 资源id
     * @return
     */
    public EmptyView showCustomIconEmptyLayout(int resId) {
        showCustomEmptyLayout(resId, null);
        return this;
    }


    /**
     * 空布局
     *
     * @param resId 图标资源id
     * @param text  提示文字
     * @return
     */
    private EmptyView showCustomEmptyLayout(int resId, String text) {
        setErrorIconRes(resId == 0 ? R.drawable.ic_common_empty : resId);
        setErrorTips(TextUtils.isEmpty(text) ? getResources().getString(R.string.common_empty_tips) : text);
        if (rtvRetry != null) rtvRetry.setVisibility(View.GONE);
        setLoadingLayoutIsVisible(GONE);
        return this;
    }

    /**
     * 搜索五数据时显示
     */
    public EmptyView showSearchNodataLayout() {
        setErrorIconRes(R.mipmap.ic_search_result_emtpy);
        setErrorTips("未搜索到相关内容哦");
        if (rtvRetry != null) rtvRetry.setVisibility(View.GONE);
        setLoadingLayoutIsVisible(GONE);
        return this;
    }

    /**
     * 错误布局展示(接口错误，H5加载错误等等)
     */
    public EmptyView showDefaultErrorLayout() {
        setErrorIconRes(R.drawable.ic_common_error);
        setErrorTips(getResources().getString(R.string.common_error_tips));
        if (rtvRetry != null) rtvRetry.setVisibility(View.GONE);
        setLoadingLayoutIsVisible(GONE);
        return this;
    }

    /**
     * 带有网络判断的错误布局展示（有些错误可能是无网络引起的，调用此方法区分此情况）
     *
     * @param callBack 无网络情况下的 点击回调
     */
    public EmptyView showErrorLayoutWithNetJudge(ICallBack callBack) {
        if (!CommonUtils.isNetworkAvailable()) {
            showDefaultNoNetWork(callBack);
        } else {
            showDefaultErrorLayout();
        }

        return this;
    }


    /**
     * 显示加载中默认布局
     */
    public EmptyView showDefaultLoadingLayout() {
        setLoadingLayoutIsVisible(VISIBLE);
        return this;
    }

    /**
     * 是否展示加载中布局
     *
     * @param visible
     */
    private void setLoadingLayoutIsVisible(int visible) {
        if (loadingLayout != null) loadingLayout.setVisibility(visible);
    }


    /**
     * @param callBack 点击重试 的回调
     * @return
     */
    private EmptyView setOnNetworkErrorClickRetry(final ICallBack callBack) {
        if (rtvRetry == null)
            return this;
        rtvRetry.setVisibility(View.VISIBLE);
        rtvRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) callBack.callBack();
            }
        });
        return this;
    }

}
