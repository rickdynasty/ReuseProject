package com.pasc.lib.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pasc.lib.base.R;

/**
 * 通用弹窗
 * 可一个按钮，可两个按钮
 */
public class PublicDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog mDialog;

    TextView mTvHint;
    TextView mTvLeft;
    TextView mTvRight;

    private Callback mCallBack;

    /**
     * 构造方法
     */
    private PublicDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.common_loading_dialog);
        mDialog.setContentView(R.layout.dialog_public);

        mTvHint = mDialog.findViewById(R.id.tv_hint);
        mTvLeft = mDialog.findViewById(R.id.tv_left);
        mTvRight = mDialog.findViewById(R.id.tv_right);

        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }

    public static PublicDialog getDialog(Context context) {
        return new PublicDialog(context);
    }

    /**
     * 设置左侧按钮文字
     */
    public PublicDialog setLeftText(String content) {
        if (null != mTvLeft) {
            mTvLeft.setText(content);
        }
        return this;
    }

    public PublicDialog setLeftText(int resId) {
        return setLeftText(mContext.getResources().getString(resId));
    }

    /**
     * 设置左侧按钮颜色
     */
    public PublicDialog setLeftTextColor(int colorId) {
        if (null != mTvLeft) {
            mTvLeft.setTextColor(mContext.getResources().getColor(colorId));
        }
        return this;
    }

    /**
     * 设置右侧按钮文字
     */
    public PublicDialog setRightText(String content) {
        if (null != mTvRight) {
            mTvRight.setText(content);
        }
        return this;
    }

    public PublicDialog setRightText(int resId) {
        return setRightText(mContext.getResources().getString(resId));
    }

    /**
     * 设置右侧按钮颜色
     */
    public PublicDialog setRightTextColor(int colorId) {
        if (null != mTvLeft) {
            mTvRight.setTextColor(mContext.getResources().getColor(colorId));
        }
        return this;
    }

    /**
     * 设置提示语
     */
    public PublicDialog setHint(String content) {
        if (null != mTvHint) {
            mTvHint.setText(content);
        }
        return this;
    }

    /**
     * 设置提示语
     */
    public PublicDialog setHint(int resourceId) {
        if (null != mTvHint) {
            mTvHint.setText(resourceId);
        }
        return this;
    }

    /**
     * 显示弹出框
     */
    public void show() {
        mDialog.show();
    }

    /**
     * 隐藏弹出框
     */
    public void hide() {
        mDialog.hide();
    }

    /**
     * 取消弹出框
     */
    public void dismiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_left) {
            if (mCallBack != null) {
                mCallBack.onLeftClicked(view);
            }
            dismiss();
        } else if (id == R.id.tv_right) {
            if (mCallBack != null) {
                mCallBack.onRightClicked(view);
            }
            dismiss();
        }
    }

    public PublicDialog setCallback(Callback callback) {
        this.mCallBack = callback;
        return this;
    }

    public interface Callback {

        void onLeftClicked(View v);

        void onRightClicked(View v);
    }

    public static abstract class RightCallback implements Callback {

        @Override
        public void onLeftClicked(View v) {

        }

        public abstract void onRightClicked(View v);
    }
}
