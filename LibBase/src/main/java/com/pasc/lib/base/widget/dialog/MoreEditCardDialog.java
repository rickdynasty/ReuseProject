package com.pasc.lib.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.ScreenUtils;
import com.pasc.lib.base.util.SizeUtils;

/**
 * 卡片编辑
 * Created by ex-wuhaiping001 on 2017/11/22.
 */
public class MoreEditCardDialog extends Dialog {

    private TextView mTvTake;
    private TextView mTvChoose;
    private TextView mTvCancel;

    public MoreEditCardDialog(Context context, String topText, String middleText, String bottomText) {
        super(context, R.style.style_dialog_select_item);
        setContentView(R.layout.dialog_more_edit_card);
        mTvTake = (TextView) findViewById(R.id.tv_take);
        mTvChoose = (TextView) findViewById(R.id.tv_get);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);

        if (!TextUtils.isEmpty(topText)) {
            mTvTake.setText(topText);
        }
        if (!TextUtils.isEmpty(middleText)) {
            mTvChoose.setText(middleText);
        }
        if (!TextUtils.isEmpty(bottomText)) {
            mTvCancel.setText(bottomText);
        }
        setLayoutParams();
    }

    public MoreEditCardDialog(Context context, int resId) {
        super(context, R.style.style_dialog_select_item);


//        if(!TextUtils.isEmpty(topText)){
//            mTvTake.setText(topText);
//        }
//        if(!TextUtils.isEmpty(middleText)){
//            mTvChoose.setText(middleText);
//        }
//        if(!TextUtils.isEmpty(bottomText)){
//            mTvCancel.setText(bottomText);
//        }
        setLayoutParams2(context, resId);
    }

    private void setLayoutParams2(Context context, int resId) {
        int widthPixels = ScreenUtils.getScreenWidth();
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
        View contentView = LayoutInflater.from(context).inflate(resId, null);
        setContentView(contentView);
        mTvTake = (TextView) findViewById(R.id.tv_take);
        mTvChoose = (TextView) findViewById(R.id.tv_get);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = widthPixels - SizeUtils.dp2px(12);
        params.bottomMargin = SizeUtils.dp2px(10);
        contentView.setLayoutParams(params);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        lp.gravity = Gravity.BOTTOM;
//        lp.height = SizeUtils.dp2px(190);
//        lp.width = widthPixels;
//        getWindow().setAttributes(lp);
        getWindow().setGravity(Gravity.BOTTOM);


    }

    public MoreEditCardDialog(Context context) {
        super(context, R.style.style_dialog_select_item);
        setContentView(R.layout.dialog_more_edit_card);
        mTvTake = (TextView) findViewById(R.id.tv_take);
        mTvChoose = (TextView) findViewById(R.id.tv_get);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);

        setLayoutParams();
    }


    private void setLayoutParams() {
        int widthPixels = ScreenUtils.getScreenWidth();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.gravity = Gravity.BOTTOM;
        lp.height = SizeUtils.dp2px(161);
        lp.width = widthPixels;
        getWindow().setAttributes(lp);
    }

    public interface OnSelectedListener {
        /**
         * 编辑
         */
        void onEdit();

        /**
         * 删除
         */
        void onDelete();


    }

    public interface OnEnsureOrCancelListener {
        void sure();

        void cancel();
    }

    public void setOnEnsureOrCancelListener(final OnEnsureOrCancelListener onEnsureOrCancelListener) {
        mTvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnsureOrCancelListener.sure();
                dismiss();
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onEnsureOrCancelListener.cancel();
                dismiss();
            }
        });
    }

    public void setOnSelectedListener(final OnSelectedListener onSelectedListener) {
        mTvTake.setOnClickListener(new View.OnClickListener() {//确认
            @Override
            public void onClick(View v) {
                onSelectedListener.onEdit();
                dismiss();
            }
        });

        mTvChoose.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                onSelectedListener.onDelete();
                dismiss();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
}
