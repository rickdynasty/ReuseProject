package com.paic.lib.workspace.Model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.paic.lib.workspace.R;
import com.paic.lib.workspace.util.DensityUtils;

import java.util.ArrayList;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台Wrokspace数据结构，主要用于适配GSON解析
 * @modify On 2018-05-30 by author for reason ...
 */
public class WorkspaceData {
    private boolean unprocessing = true;
    public ArrayList<WorkspaceGroupContent> workspaceGroups;
    private boolean needDivider = true;

    public boolean getNeedDivider() {
        return needDivider;
    }

    /**
     * workspaceGroups节点 可以定义组下面所有CardItem的一些共性属性：
     * item_width\item_height\icon_width\icon_height
     * item_textSize\item_title_padding\item_icon_padding_top
     */
    public void processing(Context context) {
        if (unprocessing) {
            boolean uniformConfig_item_width, uniformConfig_item_height, uniformConfig_icon_width, uniformConfig_icon_height;
            boolean uniformConfig_item_textSize, uniformConfig_item_textColor, uniformConfig_container_padding_top, uniformConfig_container_inner_margin;
            for (WorkspaceGroupContent content : workspaceGroups) {
                if (!content.needDivider) {
                    //只要有地方配置了不需要divider就不设置DividerDecoration
                    needDivider = false;
                }

                //行高 dp到pix
                if (DensityUtils.effectiveValue(content.header_height)) {
                    content.header_height = DensityUtils.dip2px(context, content.header_height);
                }

                //将json配置的header文本颜色转成可用的int
                if (!TextUtils.isEmpty(content.header_textColor)) {
                    content.setHeaderTextColor(Color.parseColor(content.header_textColor));
                }

                //将json配置的header背景颜色转成可用的int
                if (!TextUtils.isEmpty(content.header_background)) {
                    content.setHeaderBackgroundColor(Color.parseColor(content.header_background));
                }

                uniformConfig_item_width = content.item_width != CellItemStruct.INVALID_VALUE;
                if (uniformConfig_item_width) {
                    content.item_width = DensityUtils.dip2px(context, content.item_width);
                }

                uniformConfig_item_height = content.item_height != CellItemStruct.INVALID_VALUE;
                if (uniformConfig_item_height) {
                    content.item_height = DensityUtils.dip2px(context, content.item_height);
                }

                uniformConfig_icon_width = content.icon_width != CellItemStruct.INVALID_VALUE;
                if (uniformConfig_icon_width) {
                    content.icon_width = DensityUtils.dip2px(context, content.icon_width);
                }

                uniformConfig_icon_height = content.icon_height != CellItemStruct.INVALID_VALUE;
                if (uniformConfig_icon_height) {
                    content.icon_height = DensityUtils.dip2px(context, content.icon_height);
                }

                uniformConfig_item_textSize = DensityUtils.effectiveValue(content.cell_title_textSize);

                uniformConfig_container_padding_top = DensityUtils.effectiveValue(content.cell_container_padding_top);
                if (uniformConfig_container_padding_top) {
                    content.cell_container_padding_top = DensityUtils.dip2px(context, content.cell_container_padding_top);
                }

                uniformConfig_item_textColor = !TextUtils.isEmpty(content.cell_title_textColor);
                if (uniformConfig_item_textColor) {
                    content.cellTitle_textColor = Color.parseColor(content.cell_title_textColor);
                }

                uniformConfig_container_inner_margin = DensityUtils.effectiveValue(content.cell_container_inner_margin);
                if (uniformConfig_container_inner_margin) {
                    content.cell_container_inner_margin = DensityUtils.dip2px(context, content.cell_container_inner_margin);
                }

                for (CellItemStruct itemStruct : content.cellItemList) {
                    itemStruct.needFixWidth = content.needFixWidth;

                    if (DensityUtils.effectiveValue(itemStruct.item_width)) {
                        itemStruct.item_width = DensityUtils.dip2px(context, itemStruct.item_width);
                    } else if (uniformConfig_item_width) {
                        itemStruct.item_width = content.item_width;
                    } else {
                        itemStruct.item_width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }

                    if (DensityUtils.effectiveValue(itemStruct.item_height)) {
                        itemStruct.item_height = DensityUtils.dip2px(context, itemStruct.item_height);
                    } else if (uniformConfig_item_height) {
                        itemStruct.item_height = content.item_height;
                    }

                    if (DensityUtils.effectiveValue(itemStruct.icon_width)) {
                        itemStruct.icon_width = DensityUtils.dip2px(context, itemStruct.icon_width);
                    } else if (uniformConfig_icon_width) {
                        itemStruct.icon_width = content.icon_width;
                    }

                    if (DensityUtils.effectiveValue(itemStruct.icon_height)) {
                        itemStruct.icon_height = DensityUtils.dip2px(context, itemStruct.icon_height);
                    } else if (uniformConfig_icon_height) {
                        itemStruct.icon_height = content.icon_height;
                    }

                    if (!TextUtils.isEmpty(itemStruct.textColor)) {
                        itemStruct.setTitleTextColor(Color.parseColor(itemStruct.textColor));
                    } else if (uniformConfig_item_textColor) {
                        itemStruct.setTitleTextColor(content.cellTitle_textColor);
                    } else{
                        itemStruct.setTitleTextColor(CellItemStruct.INVALID_VALUE);
                    }

                    //如果json没配置，会在view内容初始化的时候指定默认值
                    if (!DensityUtils.effectiveValue(itemStruct.textSize) && uniformConfig_item_textSize) {
                        itemStruct.textSize = content.cell_title_textSize;
                    }

                    if (!TextUtils.isEmpty(itemStruct.background)) {
                        itemStruct.setBackgroundColor(Color.parseColor(itemStruct.background));
                    }

                    if (!TextUtils.isEmpty(itemStruct.startColor)) {
                        itemStruct.setGradientStartColor(Color.parseColor(itemStruct.startColor));
                    }


                    if (!TextUtils.isEmpty(itemStruct.centerColor)) {
                        itemStruct.setGradientCenterColor(Color.parseColor(itemStruct.centerColor));
                    }

                    if (!TextUtils.isEmpty(itemStruct.endColor)) {
                        itemStruct.setGradientEndColor(Color.parseColor(itemStruct.endColor));
                    }

                    if (DensityUtils.effectiveValue(itemStruct.container_padding_top)) {
                        itemStruct.container_padding_top = DensityUtils.dip2px(context, itemStruct.container_padding_top);
                    } else if (uniformConfig_container_padding_top) {
                        itemStruct.container_padding_top = content.cell_container_padding_top;
                    }

                    if (DensityUtils.effectiveValue(itemStruct.container_inner_margin)) {
                        itemStruct.container_inner_margin = DensityUtils.dip2px(context, itemStruct.container_inner_margin);
                    } else if (uniformConfig_container_inner_margin) {
                        itemStruct.container_inner_margin = content.cell_container_inner_margin;
                    }
                }
            }

            unprocessing = true;
        }
    }
}
