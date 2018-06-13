package com.paic.lib.workspace.Model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/29 0029.
 */
public class HotseatDisplayItem implements Serializable {
    private static final String TAG = HotseatDisplayItem.class.getSimpleName();

    public static final int INVALID_POS = -1;
    /**
     * Diplay Type：用于对组件的类型标识
     * 对齐ActivityManager的定义
     */
    public static final int TYPE_UNKOWN = -1;
    public static final int TYPE_APPLICATION = 0;
    public static final int TYPE_BROADCAST = 1;
    public static final int TYPE_ACTIVITY = 2;
    public static final int TYPE_FRAGMENT = 3;
    public static final int TYPE_SERVICE = 4;
    public static final int TYPE_PROVIDER = 5;
    public static final int TYPE_VIEW = 6;

    public static final int RES_TYPE_STRING = 0;
    public static final int RES_TYPE_DRAWABLE = 1;

    //位置
    public int x = INVALID_POS;       //结合y一起决定显示位置
    public int y = INVALID_POS;       //结合x一起决定显示位置

    //内容
    public int action_type = TYPE_UNKOWN;        //action行为的表示Type符
    public String action_id = null;             //动作行为id标识符，一般是告诉宿主当前Item点击的跳转去向的类名信息
    public String fullName = "";               // 缓存名称信息
    public int title_res_id = 0;                 //显示的文本
    public int normal_icon_id = 0;              // normal状态显示的图标
    public int focus_icon_id = 0;              // focus状态显示的图标
    public String statistic_key = null;         //统计上报标识符
    public String extras = null;                //显示的扩展内容,比如：替换成插件的fragment后需要更改activity的标题

    public ActionBarDisplayItem actionBarDisplayItem = null;
    public int normal_textColor_res_id;
    public int focus_textColor_res_id;
    public int tagIndex;

    @Override
    public String toString() {
        return "DisplayItem x=" + x + " y=" + y + " action_type=" + action_type + " action_id=" + action_id
                + " title_res_id=0x" + Integer.toHexString(title_res_id) + " statistic_key=" + statistic_key + " extras=" + extras;
    }

    public void printf() {
        Log.i(TAG, toString());
    }


    public HotseatDisplayItem(final String action_id, int action_type, final String fullName, final int title_res_id,
                              final int normal_icon_id, final int focus_icon_id, final int normal_textColor_res_id,
                              final int focus_textColor_res_id, String statistic_key, String extras, int tagIndex) {
        this.action_id = action_id;
        this.action_type = action_type;
        this.fullName = fullName;

        this.title_res_id = title_res_id;
        this.normal_icon_id = normal_icon_id;
        this.focus_icon_id = focus_icon_id;
        this.normal_textColor_res_id = normal_textColor_res_id;
        this.focus_textColor_res_id = focus_textColor_res_id;

        this.statistic_key = statistic_key;
        this.extras = extras;
        this.tagIndex = tagIndex;
    }

    /**
     * resIds 规定为至少长度为5的数组【title、n_icon、f_icon、n_color、f_color(、layout_id)】
     */
    public HotseatDisplayItem(final String action_id,
                              int action_type,
                              final String fullName,
                              String statistic_key,
                              String extras,
                              int tagIndex,
                              int... resIds) {
        if (resIds.length < 5) {
            throw new IllegalArgumentException("");
        }

        this.action_id = action_id;
        this.action_type = action_type;

        this.fullName = fullName;

        this.title_res_id = resIds[0];
        this.normal_icon_id = resIds[1];
        this.focus_icon_id = resIds[2];
        this.normal_textColor_res_id = resIds[3];
        this.focus_textColor_res_id = resIds[4];

        this.statistic_key = statistic_key;
        this.extras = extras;
        this.tagIndex = tagIndex;
    }
}
