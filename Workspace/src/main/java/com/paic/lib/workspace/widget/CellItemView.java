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
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paic.lib.workspace.Model.CellItemStruct;
import com.paic.lib.workspace.R;
import com.paic.lib.workspace.util.DensityUtils;

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

    private int mCardType = CellItemStruct.TITLE_DOWN_CARD_TYPE;

    private int mActionType = 0;
    private String mAction;
    private String mActionExtra;

    private ImageView mBackgroundImage;
    private LinearLayout mLinearContainer = null;   //Add container - mainly for adapting various screens
    private ImageView mIcon = null;     //in mLinearContainer
    private TextView mTitle = null;     //in mLinearContainer

    private boolean mIsFocus = false;
    private int mTextColor_normal = Color.WHITE;
    private int mTextColor_focus = Color.WHITE;
    private GradientDrawable mGradientDrawable = null;
    private final float mDefaultTextSize;
    private final int mDefaultTextColor;

    //0 - on; 1 - down;
    private final int mDefaultTitleLeftMargin0;
    private final int mDefaultContainerInnerMargin;

    //For DividerDecoration drawing split lines
    private int mPositionInGroup = -1;
    private int mBrothersCount = 1;

    public CellItemView(Context context) {
        this(context, null);
    }

    public CellItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultTitleLeftMargin0 = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_left_0);

        mDefaultContainerInnerMargin = getResources().getDimensionPixelSize(R.dimen.cell_item_title_margin_icon);
        mDefaultTextSize = getResources().getInteger(R.integer.cell_item_title_textSize);
        mDefaultTextColor = getResources().getColor(R.color.color_cellitem_title_default);
    }

    public void setCardType(int cardType) {
        mCardType = cardType;
    }

    public void init(CellItemStruct cardStruct) {
        if (null == cardStruct) {
            throw new IllegalArgumentException("cardStruct cannot be empty!");
        }

        // ========================= begin:如果没有设置大小 统一赋初值 =========================
        if (!DensityUtils.effectiveValue(cardStruct.item_height)) {
            cardStruct.item_height = getResources().getDimensionPixelSize(R.dimen.cell_item_height);
        }

        if (!DensityUtils.effectiveValue(cardStruct.icon_width)) {
            cardStruct.icon_width = getResources().getDimensionPixelSize(R.dimen.cell_item_cion_size);
        }

        if (!DensityUtils.effectiveValue(cardStruct.icon_height)) {
            cardStruct.icon_height = getResources().getDimensionPixelSize(R.dimen.cell_item_cion_size);
        }
        // ========================= end:如果没有设置大小 统一赋初值 =========================

        ViewGroup.LayoutParams lp = getLayoutParams();
        if (cardStruct.needFixWidth()) {
            if (!DensityUtils.effectiveValue(cardStruct.item_width)) {
                cardStruct.item_width = getResources().getDimensionPixelSize(R.dimen.cell_item_width);
            }
            lp.width = cardStruct.item_width;
        } else {
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        lp.height = cardStruct.item_height;
        setLayoutParams(lp);

        initWidget(cardStruct);

        mActionType = cardStruct.getActionType();
        mAction = cardStruct.getAction();
        mActionExtra = cardStruct.getActionExtra();
    }

    // Note: This can't be filtered - whether it's already init, RecycleView has a recycle reuse
    // mechanism, probably the same CellItemView will be init multiple times
    private void initWidget(final CellItemStruct cardStruct) {
        boolean needRestChild = false;
        if (null == mLinearContainer) {
            removeAllViews();

            mLinearContainer = new LinearLayout(getContext());
            mLinearContainer.setOrientation(LinearLayout.VERTICAL);
            mIcon = null;
            mTitle = null;
            mBackgroundImage = null;

            needRestChild = true;
        }

        if (mCardType != cardStruct.getCardType()) {
            needRestChild = true;
        }

        mCardType = cardStruct.getCardType();

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
            } else {
                if (cardStruct.bkColorEffective()) {
                    setBackgroundColor(cardStruct.getBackgroundColor());
                } else {
                    setBackgroundColor(Color.WHITE);
                }
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

        if (needRestChild) {
            removeView(mLinearContainer);
            mLinearContainer.removeAllViews();

            switch (mCardType) {
                case CellItemStruct.TITLE_ON_CARD_TYPE: {
                    LayoutParams lpContainer = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (cardStruct.containerPaddingTopEffective()) {
                        lpContainer.topMargin = cardStruct.getContainerPaddingTop();
                        lpContainer.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    } else {
                        lpContainer.addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                    addView(mLinearContainer, lpContainer);

                    // add tile view first
                    LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lpTitle.leftMargin = mDefaultTitleLeftMargin0;
                    lpTitle.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mTitle, lpTitle);

                    LinearLayout.LayoutParams lpIcon = new LinearLayout.LayoutParams(cardStruct.icon_width, cardStruct.icon_height);
                    if (cardStruct.containerInnerMarginEffective()) {
                        lpIcon.topMargin = cardStruct.getContainerInnerMargin();
                    } else {
                        lpIcon.topMargin = mDefaultContainerInnerMargin;
                    }
                    lpIcon.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mIcon, lpIcon);
                }
                break;
                case CellItemStruct.TITLE_DOWN_CARD_TYPE: {
                    LayoutParams lpContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (cardStruct.containerPaddingTopEffective()) {
                        lpContainer.topMargin = cardStruct.getContainerPaddingTop();
                        lpContainer.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    } else {
                        lpContainer.addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                    addView(mLinearContainer, lpContainer);
                    // add icon view first
                    LinearLayout.LayoutParams lpIcon = new LinearLayout.LayoutParams(cardStruct.icon_width, cardStruct.icon_height);
                    lpIcon.gravity = Gravity.CENTER_HORIZONTAL;
                    mLinearContainer.addView(mIcon, lpIcon);

                    LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    if (cardStruct.containerInnerMarginEffective()) {
                        lpTitle.topMargin = cardStruct.getContainerInnerMargin();
                    } else {
                        lpTitle.topMargin = mDefaultContainerInnerMargin;
                    }
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

        if (!cardStruct.titleTextColorEffective()) {
            cardStruct.setTitleTextColor(mDefaultTextColor);
        }
        mTitle.setTextColor(cardStruct.getTitleTextColor());

        mTitle.setText(cardStruct.getTitle());
        if (!TextUtils.isEmpty(cardStruct.getIconName())) {
            mIcon.setImageResource(getResources().getIdentifier(cardStruct.getIconName(), "drawable", getContext().getApplicationInfo().packageName));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                beginScale(R.anim.cellitem_zoom_in);
                break;
            case MotionEvent.ACTION_UP:
                beginScale(R.anim.cellitem_zoom_out);
                break;
            case MotionEvent.ACTION_CANCEL:
                beginScale(R.anim.cellitem_zoom_out);
                break;
        }

        return true;
    }

    private synchronized void beginScale(int animation) {
        if (null == mLinearContainer)
            return;

        Animation an = AnimationUtils.loadAnimation(getContext(), animation);
        an.setDuration(50);
        an.setFillAfter(true);
        mLinearContainer.startAnimation(an);
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

    public String getTitle() {
        if (null == mTitle) {
            throw new RuntimeException("The \"null == mTileTv\" scenario is theoretically absent~!");
        }

        return "" + mTitle.getText();
    }

    public int getActionType() {
        return mActionType;
    }

    public String getAction() {
        return mAction;
    }

    public String getActionExtra() {
        return mActionExtra;
    }

    public void setPositionInGroup(int positionInGroup) {
        this.mPositionInGroup = positionInGroup;
    }

    public int getPositionInGroup() {
        return mPositionInGroup;
    }

    public void setBrothersCount(int brothersCount) {
        mBrothersCount = brothersCount;
    }

    public boolean isLast() {
        return mBrothersCount <= mPositionInGroup + 1;
    }
}
