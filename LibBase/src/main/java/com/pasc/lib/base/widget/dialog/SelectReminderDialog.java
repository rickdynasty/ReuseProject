package com.pasc.lib.base.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.lib.base.R;

/**
 * 适用于UI统一规范  弹窗_多行文字&&弹窗_单行_电话呼出
 * Created by ruanwei489 on 18/1/17.
 * 统一使用 CommonDialog   此代码废弃
 */
@Deprecated
public class SelectReminderDialog extends Dialog {

    private TextView mTvOneConfirm;
    private LinearLayout mLlTwoButtonNum;//设置两个按钮的dialog
    private LinearLayout mLlOneButtonNum;//设置一个按钮的dialog
    private TextView mtvTitle;
    private TextView mTvContent;
    private TextView mTvConfirm;
    private TextView mTvCancel;
    private TextView mTvContext;

    public SelectReminderDialog(Context context, int layoutId) {
        super(context, R.style.RoundDialog);
        setContentView(layoutId);
        mtvTitle = (TextView) findViewById(R.id.dialog_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvOneConfirm = (TextView) findViewById(R.id.tv_one_confirm);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvContext = (TextView) findViewById(R.id.tv_context);

        mLlTwoButtonNum = (LinearLayout) findViewById(R.id.ll_two_confirm);
        mLlOneButtonNum = (LinearLayout) findViewById(R.id.ll_one_confirm);
        setAttributes((Activity) context);
    }

    public SelectReminderDialog setButtonNum(int num) {
        if (num == 1) {
            mLlOneButtonNum.setVisibility(View.VISIBLE);
            mLlTwoButtonNum.setVisibility(View.GONE);
        } else if (num == 2) {
            mLlOneButtonNum.setVisibility(View.GONE);
            mLlTwoButtonNum.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public SelectReminderDialog setAttributes(Activity activity) {
        Window window = getWindow();
        WindowManager windowManager = activity.getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth() * 0.72); // 宽度设置为屏幕的0.72
        window.setAttributes(params);
        return this;
    }

    public SelectReminderDialog(Context context) {
        this(context, R.layout.common_dialog);
    }

    public SelectReminderDialog setOnSelectedListener(final OnSelectedListener onSelectedListener) {

        mTvConfirm.setOnClickListener(new View.OnClickListener() {//确认
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectedListener.onSelected();
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectedListener.onCancel();
            }
        });
        return this;
    }

    /**
     * 设置对话框标题
     */
    public SelectReminderDialog setmTvTitle(String title) {//设置对话框标题
        mtvTitle.setVisibility(View.VISIBLE);
        mtvTitle.setText(title);
        return this;
    }

    public SelectReminderDialog setmTvContent(String content) {//设置对话框内容
        mTvContent.setText(content);
        return this;
    }

    public SelectReminderDialog setConfirmText(String context) {//设置确认按钮内容
        mTvConfirm.setText(context);
        return this;
    }

    public SelectReminderDialog setCancelText(String context) {//设置取消按钮内容
        mTvCancel.setText(context);
        return this;
    }

    public SelectReminderDialog setConfirmTextColor(int color) {
        mTvConfirm.setTextColor(getContext().getResources().getColor(color));
        return this;
    }

    public SelectReminderDialog setCancelTextColor(int color) {
        mTvCancel.setTextColor(getContext().getResources().getColor(color));
        return this;
    }

    /**
     * 确认和取消按钮
     */
    public interface OnSelectedListener {
        void onSelected();

        void onCancel();
    }

    /**
     * 一个确认按钮
     */
    public interface OnPositiveSelectedListener {
        void onSelected();
    }

    public SelectReminderDialog setOnPositiveSelectedListener(
            final OnPositiveSelectedListener onSelectedListener) {

        mTvOneConfirm.setOnClickListener(new View.OnClickListener() {//确认
            @Override
            public void onClick(View v) {
                onSelectedListener.onSelected();
                dismiss();
            }
        });
        return this;
    }

    public void setmTvContext(String context) {//设置对话框内容
        mTvContent.setText(context);
    }

}