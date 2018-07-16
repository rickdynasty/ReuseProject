package com.pasc.lib.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.lib.base.R;

/**
 * 首页悬浮标题栏
 * Created by duyuan797 on 18/2/4.
 */
public class MainPageTitleView extends LinearLayout {

    TextView tvDu;

    TextView tvTemperature;

    TextView tvQuality;

    public MainPageTitleView(Context context) {
        this(context, null);
    }

    public MainPageTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_page_title, null);
        tvDu = view.findViewById(R.id.tv_du);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvQuality = view.findViewById(R.id.tv_quality);
        addView(view);
    }
}
