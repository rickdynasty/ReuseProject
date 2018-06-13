package com.paic.lib.workspace.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paic.lib.workspace.Model.ActionBarDisplayItem;
import com.paic.lib.workspace.Model.HotseatDisplayItem;
import com.paic.lib.workspace.R;

// Hotseat上具体的Item
public class HotseatItemView extends RelativeLayout {
    //仅做提示用，不显示数量
    public static final int ONLY_HIGHLIGHT_NOTIFY_FLG = -100;

    private ImageView mImageView = null;
    private TextView mTextView = null;
    private TextView mNotifyMsgTv = null;     // 小红点 & 未读消息提示

    private int mComponentType = 0;
    private ComponentName mComponentName = new ComponentName();

    private boolean mIsFocus = false;
    private Drawable mNormalBackground;
    private Drawable mFocusBackground;
    private int mTextColor_normal;
    private int mTextColor_focus;

    // 用于显示的索引位置
    private int mLocation = 0;
    private int mTagIndex = 0;

    public ActionBarInfo mActionBarInfo = new ActionBarInfo();

    public HotseatItemView(Context context) {
        this(context, null);
    }

    public HotseatItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotseatItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

    public int getComponentType() {
        return mComponentType;
    }

    private void init(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams line_lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //line_lp.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(line_lp);

//        Space space = new Space(context);
//        linearLayout.addView(space, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.cell_item_padding_top)));

        mImageView = new ImageView(context);
        final float home_bottom_tab_img_width = getResources().getDimension(R.dimen.home_bottom_tab_img_width);
        final float home_bottom_tab_img_height = getResources().getDimension(R.dimen.home_bottom_tab_img_height);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams((int) home_bottom_tab_img_width, (int) home_bottom_tab_img_height);
        linearLayout.addView(mImageView, ivParams);

        // create TextView
        mTextView = new TextView(context);
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        linearLayout.addView(mTextView, tvParams);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        //mTextView.setVisibility(View.GONE);

        //将linearLayout添加到CellItem里面
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(linearLayout, lp);
    }

    public void setNormalBackground(int resid) {
        if (0 < resid) {
            mNormalBackground = getResources().getDrawable(resid);
        } else {
            mNormalBackground = null;
        }

        activationNormalBackground();
    }

    public void setNormalBackground(Drawable drawable) {
        mNormalBackground = drawable;
        activationNormalBackground();
    }

    public void setFocusBackground(int resid) {
        if (0 < resid) {
            mFocusBackground = getResources().getDrawable(resid);
        } else {
            mFocusBackground = null;
        }
        activationFocusBackground();
    }

    public void setFocusBackground(Drawable drawable) {
        mFocusBackground = drawable;
        activationFocusBackground();
    }

    private void activationNormalBackground() {
        if (mNormalBackground == null) {
            mNormalBackground = getResources().getDrawable(R.drawable.ic_launcher);
        }

        if (!mIsFocus) {
            mImageView.setBackground(mNormalBackground);
        }
    }

    private void activationFocusBackground() {
        if (mFocusBackground == null) {
            mFocusBackground = getResources().getDrawable(R.drawable.ic_launcher);
        }

        if (mIsFocus) {
            mImageView.setBackground(mFocusBackground);
        }
    }

    public void setTextColorNormal(int color) {
        mTextColor_normal = color;
        if (!mIsFocus) {
            mTextView.setTextColor(mTextColor_normal);
        }
    }

    public void setTextColorFocus(int color) {
        mTextColor_focus = color;
        if (mIsFocus) {
            mTextView.setTextColor(mTextColor_focus);
        }
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
    }

    public void setText(int resid) {
        mTextView.setText(resid);
    }

    public CharSequence getText() {
        if (mTextView != null) {
            return mTextView.getText();
        }

        return null;
    }

    public void setActionClass(String classId, String packageName, int componentType) {
        mComponentName.setValue(classId, packageName);
        mComponentType = componentType;
    }

    public void setFocus(boolean focus) {
        if (mIsFocus == focus)
            return;

        mIsFocus = focus;

        if (mIsFocus) {
            mTextView.setTextColor(mTextColor_focus);
            mImageView.setBackground(mFocusBackground);
        } else {
            mTextView.setTextColor(mTextColor_normal);
            mImageView.setBackground(mNormalBackground);
        }
    }

    public void setComponentName(ComponentName componentName) {
        mComponentName.setValue(componentName);
    }

    public void setLocation(int location) {
        mLocation = location;
    }

    public int getLocation() {
        return mLocation;
    }

    public void setTagIndex(int tagIndex) {
        mTagIndex = tagIndex;
    }

    public int getTagIndex() {
        return mTagIndex;
    }

    public void setActionBarInfo(ActionBarDisplayItem actionBarDisplayItem) {
        if (null != actionBarDisplayItem) {
            mActionBarInfo.title_res_id = actionBarDisplayItem.title_res_id;

            mActionBarInfo.r_type = actionBarDisplayItem.r_action_type;
            mActionBarInfo.r_action_id = actionBarDisplayItem.r_action_id;
            mActionBarInfo.r_res_type = actionBarDisplayItem.r_res_type;
            mActionBarInfo.r_normal_icon_id = actionBarDisplayItem.r_normal_icon_id;
            mActionBarInfo.r_focus_icon_id = actionBarDisplayItem.r_focus_icon_id;
        }
    }

    public class ActionBarInfo {
        public ActionBarInfo() {
        }

        // ActionBar
        // 这里就不用分语言环境了，直接复制就行
        public int title_res_id = 0;
        // ActionBar右侧按钮上触发点击后行为的内容类型，同contentType【当前默认是activity，而且也暂时只有activity】
        public int r_type = HotseatDisplayItem.TYPE_ACTIVITY;// 默认是activity
        // ActionBar右侧按钮上触发点击后的行为内容
        public String r_action_id = null;

        // 显示在ActionBar右侧按钮上的内容类型 1、String文本资源 2、图标资源
        public int r_res_type = HotseatDisplayItem.RES_TYPE_STRING;
        // 显示在ActionBar右侧按钮上的内容，根据类型进行配置
        public int r_normal_icon_id = 0;              // normal状态显示的图标
        public int r_focus_icon_id = 0;              // focus状态显示的图标

        @Override
        public String toString() {
            return " r_type=" + r_type + " r_action_id=" + r_action_id + " r_res_type=" + r_res_type;
        }

    }

    public Object getFullName() {
        return mComponentName.getFullName();
    }

    public String getClassId() {
        return mComponentName.getClassId();
    }

    // 将fragment的ClassId和插件的包名捆绑在一起，这样有利于避开多插件的命名规范同时也可以优化查找速度
    public class ComponentName {
        private String mClassId;
        // 保存标识用于判断
        private String mFullName = "";

        public void setValue(ComponentName componentName) {
            this.mClassId = componentName.mClassId;
            this.mFullName = componentName.mFullName;
        }

        public void setValue(String classId, String packageName) {
            this.mClassId = classId;
            this.mFullName = packageName;
        }

        public String getClassId() {
            return mClassId;
        }

        public String getFullName() {
            return mFullName;
        }

        @Override
        public String toString() {
            return "mClassId = " + mClassId + " mFullName = " + mFullName;
        }
    }

    private void sureNotifyMsgTv() {
        if (null == mNotifyMsgTv) {
            // create ImageView
            mNotifyMsgTv = new TextView(getContext());
            final int tipsSize = getResources().getDimensionPixelSize(R.dimen.hotseat_cellitem_message_tip_height);
            LayoutParams nivParams = new LayoutParams(tipsSize, tipsSize);
            nivParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            nivParams.topMargin = 5;
            nivParams.addRule(RelativeLayout.ALIGN_RIGHT, mImageView.getId());
            // nivParams.addRule(RelativeLayout.ALIGN_TOP, mImageView.getId());
            mNotifyMsgTv.setLayoutParams(nivParams);
            mNotifyMsgTv.setBackground(getResources().getDrawable(R.drawable.chat_bottom_left_message_tips_bg));

            addView(mNotifyMsgTv);
        }
    }

    public void setHighlight(int withNum) {
        sureNotifyMsgTv();
        if (0 < withNum) {
            mNotifyMsgTv.setVisibility(View.VISIBLE);
            mNotifyMsgTv.setText(99 < withNum ? "99+" : "" + withNum);
        } else if (ONLY_HIGHLIGHT_NOTIFY_FLG == withNum) {
            mNotifyMsgTv.setVisibility(View.VISIBLE);
            mNotifyMsgTv.setText("");
        } else {
            mNotifyMsgTv.setVisibility(View.INVISIBLE);
        }
    }
}
