package com.paic.lib.workspace.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台Workspace的BaseAdapter
 * @modify On 2018-05-30 by author for reason ...
 */
public abstract class BaseAdapter<H extends RecyclerView.ViewHolder,
        VH extends RecyclerView.ViewHolder,
        F extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_GROUP_HEADER = -1;
    protected static final int TYPE_GROUP_FOOTER = -2;
    protected static final int TYPE_ITEM = -3;

    private int[] groupForPosition = null;
    private int[] positionWithinGroup = null;
    private boolean[] isHeader = null;
    private boolean[] isFooter = null;
    private int count = 0;

    private GroupDataObserver mGroupDataObserver = null;

    public BaseAdapter() {
        super();
        mGroupDataObserver = new GroupDataObserver();
        registerAdapterDataObserver(mGroupDataObserver);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (null != mGroupDataObserver) {
            unregisterAdapterDataObserver(mGroupDataObserver);
        }
    }

    /**
     * Returns the sum of number of items for each group plus headers and footers if they
     * are provided.
     */
    @Override
    public int getItemCount() {
        return count;
    }

    private void setupIndices() {
        count = countItems();
        allocateAuxiliaryArrays(count);
        precomputeIndices();
    }

    private int countItems() {
        int count = 0;
        int groups = getGroupCount();

        for (int i = 0; i < groups; i++) {
            count += 1 + getItemCountForGroup(i) + (hasFooterInGroup(i) ? 1 : 0);
        }

        return count;
    }

    private void precomputeIndices() {
        int groups = getGroupCount();
        int index = 0;

        for (int i = 0; i < groups; i++) {
            setPrecomputedItem(index, true, false, i, 0);
            index++;

            for (int j = 0; j < getItemCountForGroup(i); j++) {
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }

            if (hasFooterInGroup(i)) {
                setPrecomputedItem(index, false, true, i, 0);
                index++;
            }
        }
    }

    private void allocateAuxiliaryArrays(int count) {
        groupForPosition = new int[count];
        positionWithinGroup = new int[count];
        isHeader = new boolean[count];
        isFooter = new boolean[count];
    }

    private void setPrecomputedItem(int index, boolean isHeader, boolean isFooter, int group, int position) {
        this.isHeader[index] = isHeader;
        this.isFooter[index] = isFooter;
        groupForPosition[index] = group;
        positionWithinGroup[index] = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (isGroupHeaderViewType(viewType)) {
            viewHolder = onCreateGroupHeaderViewHolder(parent, viewType);
        } else if (isGroupFooterViewType(viewType)) {
            viewHolder = onCreateGroupFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int group = groupForPosition[position];
        int index = positionWithinGroup[position];

        if (isGroupHeaderPosition(position)) {
            onBindGroupHeaderViewHolder((H) holder, group);
        } else if (isGroupFooterPosition(position)) {
            onBindGroupFooterViewHolder((F) holder, group);
        } else {
            onBindItemViewHolder((VH) holder, group, index);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (groupForPosition == null) {
            setupIndices();
        }

        int group = groupForPosition[position];
        int index = positionWithinGroup[position];

        int type = 0;
        if (isGroupHeaderPosition(position)) {
            type = getGroupHeaderViewType(group);
        } else if (isGroupFooterPosition(position)) {
            type = getGroupFooterViewType(group);
        } else {
            type = getGroupItemViewType(group, index);
        }

        return type;
    }

    public String getTypeDes(final int type) {
        switch (type) {
            case TYPE_GROUP_HEADER:
                return "Group Header";
            case TYPE_GROUP_FOOTER:
                return "Group Footer";
            case TYPE_ITEM:
                return "Item";
            default:
                return "Unknow";
        }
    }

    protected int getGroupHeaderViewType(int group) {
        return TYPE_GROUP_HEADER;
    }

    protected int getGroupFooterViewType(int group) {
        return TYPE_GROUP_FOOTER;
    }

    protected int getGroupItemViewType(int group, int position) {
        return TYPE_ITEM;
    }

    /**
     * Returns true if the argument position corresponds to a header
     */
    public boolean isGroupHeaderPosition(int position) {
        if (isHeader == null) {
            setupIndices();
        }
        return isHeader[position];
    }

    /**
     * Returns true if the argument position corresponds to a footer
     */
    public boolean isGroupFooterPosition(int position) {
        if (isFooter == null) {
            setupIndices();
        }
        return isFooter[position];
    }

    protected boolean isGroupHeaderViewType(int viewType) {
        return viewType == TYPE_GROUP_HEADER;
    }

    protected boolean isGroupFooterViewType(int viewType) {
        return viewType == TYPE_GROUP_FOOTER;
    }

    /**
     * Returns the number of group in the RecyclerView
     */
    protected abstract int getGroupCount();

    /**
     * Returns the number of items for a given group
     */
    protected abstract int getItemCountForGroup(int group);

    /**
     * Returns true if a given group should have a footer
     */
    protected abstract boolean hasFooterInGroup(int group);

    /**
     * Creates a ViewHolder of class H for a Header
     */
    protected abstract H onCreateGroupHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class F for a Footer
     */
    protected abstract F onCreateGroupFooterViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * Binds data to the header view of a given group
     */
    protected abstract void onBindGroupHeaderViewHolder(H holder, int group);

    /**
     * Binds data to the footer view of a given group
     */
    protected abstract void onBindGroupFooterViewHolder(F holder, int group);

    /**
     * Binds data to the item view for a given position within a group
     */
    protected abstract void onBindItemViewHolder(VH holder, int group, int position);

    class GroupDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            setupIndices();
        }
    }

    public int getItemPosition(int position) {
        return positionWithinGroup[position];
    }
}
