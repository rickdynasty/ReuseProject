package com.paic.lib.workspace.Model;

import android.graphics.Color;

import com.paic.lib.workspace.util.DensityUtils;

import java.util.ArrayList;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台Wrokspace数据
 * @modify On 2018-05-30 by author for reason ...
 */
public class WorkspaceGroupContent {
    public ArrayList<CellItemStruct> cellItemList;

    private String groupName;
    private int groupId;
    public int header_height = CellItemStruct.INVALID_VALUE;
    protected String gicon;

    @Deprecated
    protected String header_textColor;     //for json
    private int headerTextColor = CellItemStruct.INVALID_VALUE;

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public boolean headerTextColorEffective() {
        return CellItemStruct.INVALID_VALUE != this.headerTextColor;
    }

    public void setHeaderTextColor(int color) {
        this.headerTextColor = color;
    }

    public float header_textSize = CellItemStruct.INVALID_VALUE;

    public boolean header_textSizeEffective() {
        return DensityUtils.effectiveValue(header_textSize);
    }

    public float getHeaderTextSize() {
        return header_textSize;
    }

    private boolean header_boldText = false;

    public boolean getIsHeaderBoldText() {
        return header_boldText;
    }

    //是否可以伸展，默认不伸缩
    private boolean isShrink = false;

    public boolean getIsShrink() {
        return isShrink;
    }

    @Deprecated
    protected String header_background;     //for json
    private int header_backgroundColor = Color.WHITE;

    public int getHeaderBackgroundColor() {
        return header_backgroundColor;
    }

    public void setHeaderBackgroundColor(int color) {
        this.header_backgroundColor = color;
    }

    /////////////////////////////////// begin:为当前组统一配置 ///////////////////////////////////
    public int item_width = CellItemStruct.INVALID_VALUE;
    public int item_height = CellItemStruct.INVALID_VALUE;
    public int icon_width = CellItemStruct.INVALID_VALUE;
    public int icon_height = CellItemStruct.INVALID_VALUE;
    protected float cell_title_textSize = CellItemStruct.INVALID_VALUE;
    @Deprecated
    protected String cell_title_textColor = null;
    protected int cellTitle_textColor = CellItemStruct.INVALID_VALUE;
    protected int cell_container_padding_top = CellItemStruct.INVALID_VALUE;
    protected int cell_container_inner_margin = CellItemStruct.INVALID_VALUE;
    protected boolean needFixWidth = false;
    /////////////////////////////////// end:为当前组统一配置 ///////////////////////////////////

    public void setName(String name) {
        this.groupName = name;
    }

    public String getName() {
        return groupName;
    }

    public void setID(int id) {
        this.groupId = id;
    }

    public int getID() {
        return groupId;
    }

    public void setGIcon(String icon) {
        this.gicon = icon;
    }

    public String getGIcon() {
        return this.gicon;
    }

    protected boolean needDivider = true;
}
