package com.paic.lib.workspace.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paic.lib.workspace.R;
import com.paic.lib.workspace.widget.CellItemHolder;
import com.paic.lib.workspace.widget.HeaderHolder;
import com.paic.lib.workspace.widget.Workspace;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des
 * @modify On 2018-05-30 by author for reason ...
 */
public class CellLayoutAdapter extends BaseAdapter<HeaderHolder, CellItemHolder, RecyclerView.ViewHolder> {
    protected static final String TAG = "rick_Print:CellLayout";
    public ArrayList<WorkspaceGroupContent> groupDataList;
    private String mSwith_off = "收起";
    private String mSwith_on = "展开";

    private Context mContext;
    private LayoutInflater mInflater;
    // 是否是收起的状态【默认是展开的】
    private SparseBooleanArray mCollapseStateMap;

    private View.OnClickListener mCellItemOnClickListener = null;

    public void setCellItemOnClickListener(View.OnClickListener listener) {
        mCellItemOnClickListener = listener;
    }

    public CellLayoutAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCollapseStateMap = new SparseBooleanArray();
    }

    public void setData(ArrayList<WorkspaceGroupContent> groupDataList) {
        this.groupDataList = groupDataList;
        notifyDataSetChanged();
    }

    @Override
    protected int getGroupCount() {
        return isEmpty(groupDataList) ? 0 : groupDataList.size();
    }

    @Override
    protected int getItemCountForGroup(int group) {
        final WorkspaceGroupContent groupContent = groupDataList.get(group);
        int count = isEmpty(groupContent.cellItemList) ? 0 : groupContent.cellItemList.size();

        //判断是否是收起状态，并且当前count超过了收起状态的显示个数
        if (mCollapseStateMap.get(group) && count >= Workspace.GRID_SPANCOUNT * Workspace.GRID_GROUP_OFF_MULTIPLE_SPANCOUNT) {
            count = Workspace.GRID_SPANCOUNT * Workspace.GRID_GROUP_OFF_MULTIPLE_SPANCOUNT;
        }

        return count;
    }

    @Override
    protected boolean hasFooterInGroup(int group) {
        return false;
    }

    @Override
    protected HeaderHolder onCreateGroupHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.workspace_header_item, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateGroupFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected CellItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new CellItemHolder(mInflater.inflate(R.layout.workspace_cell_item, parent, false));
    }

    @Override
    protected void onBindGroupHeaderViewHolder(final HeaderHolder holder, final int group) {
        final WorkspaceGroupContent groupContent = groupDataList.get(group);
        holder.headerView.setBackgroundColor(groupContent.getHeaderBackgroundColor());

        holder.titleView.setText(groupContent.getName());
        if (!groupContent.header_textSizeEffective()) {
            groupContent.setHeaderTextSize(holder.titleView.getResources().getInteger(R.integer.workspace_group_header_textSize));
        }
        holder.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, groupContent.getHeaderTextSize());

        holder.titleView.getPaint().setFakeBoldText(groupContent.getIsHeaderBoldText());

        if (!groupContent.headerTextColorEffective()) {
            groupContent.setHeaderTextColor(holder.titleView.getResources().getColor(R.color.workpsace_group_header_textColor));
        }
        holder.titleView.setTextColor(groupContent.getHeaderTextColor());

        if (groupContent.getIsShrink() && Workspace.GRID_SPANCOUNT * Workspace.GRID_GROUP_OFF_MULTIPLE_SPANCOUNT < groupContent.cellItemList.size()) {
            holder.openView.setVisibility(View.VISIBLE);

            holder.openView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCollapse = mCollapseStateMap.get(group);
                    String text = isCollapse ? mSwith_off : mSwith_on;
                    mCollapseStateMap.put(group, !isCollapse);
                    holder.openView.setText(text);
                    notifyDataSetChanged();
                }
            });

            holder.openView.setText(mCollapseStateMap.get(group) ? mSwith_on : mSwith_off);
        } else {
            holder.openView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(groupContent.getGIcon())) {
            holder.groupIcon.setVisibility(View.VISIBLE);
            int resID = mContext.getResources().getIdentifier(groupContent.getGIcon(), "drawable", mContext.getApplicationInfo().packageName);
            holder.groupIcon.setImageResource(resID);
        } else {
            holder.groupIcon.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onBindGroupFooterViewHolder(RecyclerView.ViewHolder holder, int group) {

    }

    @Override
    protected void onBindItemViewHolder(CellItemHolder holder, int group, int position) {
        final CellItemStruct itemStruct = groupDataList.get(group).cellItemList.get(position);
        holder.card.init(itemStruct);
        holder.card.setBrothersCount(getItemCountForGroup(group));
        holder.card.setPositionInGroup(position);

        if (null != mCellItemOnClickListener) {
            holder.card.setOnClickListener(mCellItemOnClickListener);
        }
    }

    private static <D> boolean isEmpty(List<D> list) {
        return list == null || list.isEmpty();
    }
}
