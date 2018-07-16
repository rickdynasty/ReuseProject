package com.pasc.lib.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.ScreenUtils;

/**
 * Created by ruanwei489 on 2017/12/5.
 */
public class BuilderDialog {
    private Context context;
    private View mLayout;
    private TextView sureButton;
    private TextView cancleButton;
    private boolean mCancelable = false;
    private View.OnClickListener sureClickListener,
            cancelClickListener;
    Dialog dialog;

    public BuilderDialog(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = inflater.inflate(R.layout.base_dialog_layout, null);
    }

    //能否返回键取消
    public BuilderDialog setCancelable(Boolean boolean1) {
        this.mCancelable = boolean1;
        return this;
    }

    public BuilderDialog title(int title) {
        ((TextView) mLayout.findViewById(R.id.title)).setText(title);
        return this;
    }

    public BuilderDialog title(String title) {
        ((TextView) mLayout.findViewById(R.id.title)).setText(title);
        return this;
    }

    public BuilderDialog titleColor(int color) {
        ((TextView) mLayout.findViewById(R.id.title)).setTextColor(context.getResources().getColor(color));

        return this;
    }

    public BuilderDialog setSureOnClickListener(View.OnClickListener listener) {
        this.sureClickListener = listener;
        return this;
    }

    public BuilderDialog message(String message) {
        ((TextView) mLayout.findViewById(R.id.message)).setText(message);
        return this;
    }

    public BuilderDialog message(int message) {
        ((TextView) mLayout.findViewById(R.id.message)).setText(message);
        return this;
    }

    public BuilderDialog sureTextColor(int color) {
        ((TextView) mLayout.findViewById(R.id.sure)).setTextColor(context.getResources().getColor(color));
        return this;
    }

    //确定按钮文本
    public BuilderDialog SureText(String str) {
        ((TextView) mLayout.findViewById(R.id.sure)).setText(str);
        return this;
    }

    public BuilderDialog justMessageDialog() {
        sureClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
        return this;
    }

    public Dialog builder() {
        dialog = new Dialog(context, R.style.base_dialog);
        dialog.setCancelable(mCancelable);
        dialog.setContentView(mLayout);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (ScreenUtils.getScreenWidth() / (375.0 / 270.0));
        dialog.getWindow().setAttributes(attributes);

        if (sureClickListener != null) {
            mLayout.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sureClickListener.onClick(v);
                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }

}
