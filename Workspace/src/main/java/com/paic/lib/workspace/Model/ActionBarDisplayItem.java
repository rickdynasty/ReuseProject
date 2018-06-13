package com.paic.lib.workspace.Model;

/**
 * Created by Administrator on 2017/10/29 0029.
 */
public class ActionBarDisplayItem {
    // 注意这里的结构仅用于组件化框架，如果要应用于插件化框架请移步TPF
    public int title_res_id;
    public int subtitle_res_id;
    public int r_btn_text_res_id;

    // ActionBar右侧按钮上触发点击后行为的内容类型，同contentType【当前默认是activity，而且也暂时只有activity】
    public int r_action_type = HotseatDisplayItem.TYPE_ACTIVITY;// 默认是activity
    // ActionBar右侧按钮上触发点击后的行为内容
    public String r_action_id = null;

    // 显示在ActionBar右侧按钮上的内容类型 1、String文本资源 2、图标资源
    public int r_res_type = HotseatDisplayItem.RES_TYPE_STRING;
    // 显示在ActionBar右侧按钮上的内容，根据类型进行配置
    public int r_normal_icon_id = 0;
    public int r_focus_icon_id = 0;
}
