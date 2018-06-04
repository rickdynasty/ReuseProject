package com.paic.lib.workspace.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.paic.lib.workspace.Model.CellItemStruct;
import com.paic.lib.workspace.R;
import com.paic.lib.workspace.widget.CellItemView;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des CellItem的工厂
 * @modify On 2018-05-30 by author for reason ...
 */
public class CellItemFactory {
    public static final int NOTIFY_CARD = 0;       //特别关注
    public static final int DATE_CARD = 1;          //我的日程

    public static final int MEETING_MANAGE_CARD = 2;    //会议管理
    public static final int LEADER_AGENDA_CARD = 3;     //日程

    public static final int WAIT_SINGIN_CARD = 4;       //待签收
    public static final int ARCHIVING_CARD = 5;         //归档
    public static final int OFFICIAL_CAR_CARD = 6;      //用车

    public static final int OA_CARD = 7;                //OA站点
    public static final int ATTENDANCE_NOTICE = 8;      //考勤通知

    public static CellItemStruct createCellItemStruct(int type) {
        String title = "unknow";
        int iconRes = R.drawable.ic_launcher;
        int shadowRes = R.drawable.ic_launcher;
        int startColor = -1;
        int centerColor = -1;
        int endColor = -1;
        int weight = 1;

        int cardType = 1;
        String action = "action";
        int actionType = 0;

        switch (type) {
            case NOTIFY_CARD:
                title = "特别关注";
                shadowRes = R.drawable.shadow_specialcare;
                startColor = Color.LTGRAY;
                endColor = Color.LTGRAY;
                weight = 2;
                break;
            case DATE_CARD:
                title = "我的日程";
                shadowRes = R.drawable.shadow_black_red;
                startColor = Color.RED;
                endColor = Color.RED;
                weight = 1;
                break;
            case MEETING_MANAGE_CARD:
                title = "会议管理";
                shadowRes = R.drawable.shadow_main_39b1b7;
                iconRes = R.drawable.work_main_ic_hygl;
                startColor = 0xFF39B1B7;
                endColor = 0xFF1EDE81;
                break;
            case LEADER_AGENDA_CARD:
                title = "日程";
                shadowRes = R.drawable.shadow_main_52b0ff;
                iconRes = R.drawable.work_main_leader_schedule_icon;
                startColor = 0xFF1E78FF;
                endColor = 0xFF52B0FF;
                break;
            case WAIT_SINGIN_CARD:
                title = "待签收";
                shadowRes = R.drawable.shadow_main_ff9c68;
                iconRes = R.drawable.work_main_waitsign_icon;
                startColor = 0xFFFF6451;
                endColor = 0xFFFF9C68;
                break;
            case ARCHIVING_CARD:
                title = "归档";
                shadowRes = R.drawable.shadow_main_d38ffe;
                iconRes = R.drawable.work_main_collect_icon;
                startColor = 0xFF8E33FE;
                endColor = 0xFFD38FFE;
                break;
            case OFFICIAL_CAR_CARD:
                title = "用车";
                shadowRes = R.drawable.shadow_main_fd8da5;
                iconRes = R.drawable.work_main_item_car_icon;
                startColor = 0xFFFA5779;
                endColor = 0xFFFD8DA5;
                break;
            case OA_CARD:
                title = "OA站点";
                shadowRes = R.drawable.shadow_main_fd8da5;
                iconRes = R.drawable.work_main_item_car_icon;
                startColor = 0xFFFA5779;
                endColor = 0xFFFD8DA5;
                weight = 1;
                break;

            case ATTENDANCE_NOTICE:
                title = "考勤通知";
                shadowRes = R.drawable.shadow_blug;
                iconRes = R.drawable.work_main_task_icon;
                startColor = 0xFFFA5779;
                endColor = 0xFFFD8DA5;
                weight = 2;
                break;
            default:
                break;
        }

        return new CellItemStruct(title, iconRes, shadowRes, cardType, startColor, centerColor, endColor, actionType, action, weight);
    }

    public static CellItemView createCellItemView(Context context, CellItemStruct cardStruct) {
        if (null == cardStruct) {
            return null;
        }

        CellItemView itemView = new CellItemView(context);
        itemView.init(cardStruct);

        if (context instanceof View.OnClickListener) {
            itemView.setOnClickListener((View.OnClickListener) context);
        }

        return itemView;
    }
}
