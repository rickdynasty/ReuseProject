package com.paic.lib.workspace.Model;

import android.support.v7.widget.GridLayoutManager;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des WorkspaceSpanSizeLookup 用于Header一行展示
 * @modify On 2018-05-30 by author for reason ...
 */
public class WorkspaceSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    protected BaseAdapter<?, ?, ?> adapter;
    protected GridLayoutManager layoutManager;

    public WorkspaceSpanSizeLookup(BaseAdapter<?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.isGroupHeaderPosition(position) || adapter.isGroupFooterPosition(position)) {
            return layoutManager.getSpanCount();
        } else {
            return 1;
        }
    }
}
