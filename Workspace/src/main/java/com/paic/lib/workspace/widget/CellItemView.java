package com.paic.lib.workspace.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paic.lib.workspace.Model.CellItemStruct;
import com.paic.lib.workspace.R;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des CellItemView 工作台展示的具体一项Cell
 * @modify On 2018-05-30 by author for reason ...
 */
public class CellItemView extends RelativeLayout {
    private static final String TAG = CellItemView.class.getSimpleName();

    public static final int TITLE_ON_CARD_TYPE = 0;     //标题在上
    public static final int TITLE_DOWN_CARD_TYPE = 1;   //标题在下
    public static final int CUSTOMIZED_CARD_TYPE = 2;   //自定义

    private int mCardType = TITLE_DOWN_CARD_TYPE;

    private int mActionType = 0;
    private String mAction;

    private ImageView mBackgroundImage;
    private LinearLayout mLinearContainer = null;   //Add container - mainly for adapting various screens
    private ImageView mIcon = null;     //in mLinearContainer
    private TextView mTitle = null;     //in mLinearContainer

    private boolean mIsFocus = false;
    private int mTextColor_normal = Color.WHITE;
    private int mTextColor_focus = Color.WHITE;
    private GradientDrawable mGradientDrawable = null;
    private final float mDefaultTextSize;

    //0 - on; 1 - down;
    private final int mDefaultTitleLeftMargin0, mDefaultTitleTopMargin0, mDefaultTitleBottomMargin1;
    private final int getmDefaultTitleMarginIcon;

    //For DividerDecoration drawing split lines
    private int mPositionInGroup = -1;

    public CellItemView(Context context) {
        this(context, null);
    }

    public CellItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultTitleLeftMargin0 = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_left_0);
        mDefaultTitleTopMargin0 = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_top_0);
        mDefaultTitleBottomMargin1 = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_bottom_1);

        getmDefaultTitleMarginIcon = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_icon);
        mDefaultTextSize = 13.0f;
    }

    public void setCardType(int cardType) {
        mCardType = cardType;
    }

    public void init(CellItemStruct cardStruct) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (cardStruct.needFixWidth()) {
            lp.width = cardStruct.item_width;
        }
        lp.height = cardStruct.item_height;
        setLayoutParams(lp);

        if (null == cardStruct) {
            throw new IllegalArgumentException("cardStruct cannot be empty!");
        }

        initWidget(cardStruct);

        mActionType = cardStruct.getActionType();
        mAction = cardStruct.getAction();
    }

    // Note: This can't be filtered - whether it's already init, RecycleView has a recycle reuse
    // mechanism, probably the same CellItemView will be init multiple times
    private void initWidget(final CellItemStruct cardStruct) {
        mCardType = cardStruct.getCardType();

        boolean needCreateContainer = false;
        if (null == mLinearContainer) {
            removeAllViews();

            mLinearContainer = new LinearLayout(getContext());
            mLinearContainer.setOrientation(LinearLayout.VERTICAL);
            mLinearContainer.removeAllViews();
            mIcon = null;
            mTitle = null;
            mBackgroundImage = null;

            needCreateContainer = true;
        }

        //dill gradient background
        int[] colors;
        if (cardStruct.gradientCenterEffective()) {
            colors = new int[]{cardStruct.getGradientStartColor(), cardStruct.getGradientCenterColor(), cardStruct.getGradientEndColor()};
        } else if (cardStruct.gradientEffective()) {
            colors = new int[]{cardStruct.getGradientStartColor(), cardStruct.getGradientEndColor()};
        } else if (!cardStruct.needFixWidth()) {
            // Note: If the json configuration does not require a fixed width, use BackgroundImage to fill the full width
            //If you do not use a gradient, use the background color
            if (cardStruct.bkColorEffective()) {
                colors = new int[]{cardStruct.getBackgroundColor(), cardStruct.getBackgroundColor()};
            } else {
                colors = new int[]{Color.WHITE, Color.WHITE};
            }
        } else {
            colors = null;
        }

        //Use gradients preferentially
        if (null != colors) {
            if (null == mBackgroundImage) {
                mBackgroundImage = new ImageView(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(mBackgroundImage, lp);
            }

            // dill shadowImageView mBackgroundImage
            mBackgroundImage.setVisibility(VISIBLE);
            setCardGradientColor(colors);

            if (!TextUtils.isEmpty(cardStruct.getShadowResName())) {
                setBackgroundResource(getResources().getIdentifier(cardStruct.getShadowResName(), "drawable", getContext().getApplicationInfo().packageName));
            }
        } else {
            if (null != mBackgroundImage) {
                mBackgroundImage.setVisibility(GONE);
            }

            //If you do not use a gradient, use the background color
            if (cardStruct.bkColorEffective()) {
                setBackgroundColor(cardStruct.getBackgroundColor());
            } else {
                setBackgroundColor(Color.WHITE);
            }
        }

        // sure Icon View
        if (null == mIcon) {
            mIcon = new ImageView(getContext());
        }

        // sure Title View
        if (null == mTitle) {
            mTitle = new TextView(getContext());
            if (cardStruct.titleSingleLine()) {
                mTitle.setSingleLine();
                mTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
        }

        if (needCreateContainer) {
            switch (mCardType) {
                case TITLE_ON_CARD_TYPE: {
                    LayoutParams lpContainer = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lpContainer.addRule(RelativeLayout.CENTER_IN_PARENT);
                    addView(mLinearContainer, lpContainer);

                    // add tile view first
                    LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lpTitle.leftMargin = mDefaultTitleLeftMargin0;
                    lpTitle.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mTitle, lpTitle);

                    LinearLayout.LayoutParams lpIcon = new LinearLayout.LayoutParams(cardStruct.icon_width, cardStruct.icon_height);
                    lpIcon.topMargin = getmDefaultTitleMarginIcon;
                    lpIcon.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mIcon, lpIcon);
                }
                break;
                case TITLE_DOWN_CARD_TYPE: {
                    LayoutParams lpContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lpContainer.addRule(RelativeLayout.CENTER_IN_PARENT);
                    addView(mLinearContainer, lpContainer);
                    // add icon view first
                    LinearLayout.LayoutParams lpIcon = new LinearLayout.LayoutParams(cardStruct.icon_width, cardStruct.icon_height);
                    lpIcon.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mIcon, lpIcon);

                    LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    lpTitle.topMargin = getmDefaultTitleMarginIcon;
                    lpTitle.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mTitle, lpTitle);
                }
                break;
                default:
                    break;
            }
        }

        if (cardStruct.titleTextSizeEffective()) {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, cardStruct.getTitleTextSize());
        } else {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mDefaultTextSize);
        }

        mTitle.setTextColor(cardStruct.getTitleTextColor());
        mTitle.setText(cardStruct.getTitle());
        if (!TextUtils.isEmpty(cardStruct.getIconName())) {
            mIcon.setImageResource(getResources().getIdentifier(cardStruct.getIconName(), "drawable", getContext().getApplicationInfo().packageName));
        }
    }

    public void setCardGradientColor(final int[] colors) {
        setCardGradientColor(GradientDrawable.Orientation.TR_BL, colors);
    }

    public void setCardGradientColor(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        if (null == mGradientDrawable) {
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        }

        mGradientDrawable.setOrientation(orientation);
        mGradientDrawable.setColors(colors);

        mBackgroundImage.setImageDrawable(mGradientDrawable);
    }

    public void setTextColorNormal(int color) {
        mTextColor_normal = color;
        if (!mIsFocus) {
            mTitle.setTextColor(mTextColor_normal);
        }
    }

    public void setTextColorFocus(int color) {
        mTextColor_focus = color;
        if (mIsFocus) {
            mTitle.setTextColor(mTextColor_focus);
        }
    }

    public void setFocus(boolean focus) {
        if (mIsFocus == focus)
            return;

        mIsFocus = focus;

        if (mIsFocus) {
            mTitle.setTextColor(mTextColor_focus);
        } else {
            mTitle.setTextColor(mTextColor_normal);
        }
    }

    public CharSequence getTitle() {
        if (null == mTitle) {
            throw new RuntimeException("The \"null == mTileTv\" scenario is theoretically absent~!");
        }

        return mTitle.getText();
    }

    public int getActionType() {
        return mActionType;
    }

    public String getAction() {
        return mAction;
    }

    public void setPositionInGroup(int positionInGroup) {
        this.mPositionInGroup = positionInGroup;
    }

    public int getPositionInGroup() {
        return mPositionInGroup;
    }
}
