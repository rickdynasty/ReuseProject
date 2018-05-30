package com.paic.lib.workspace.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台控件
 * @modify On 2018-05-30 by author for reason ...
 */
public class Workspace extends RecyclerView {

    private static final String TAG = Workspace.class.getSimpleName();
    protected Context mContext;

    //列【即一行显示的个数】
    public static final int GRID_SPANCOUNT = 3;
    // 默认收起显示的行数
    public static final int GRID_GROUP_OFF_MULTIPLE_SPANCOUNT = 1;


    public Workspace(Context context) {
        this(context, null);
    }

    public Workspace(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Workspace(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
}
