package com.pasc.lib.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.ScreenUtils;
import com.pasc.lib.base.util.SizeUtils;

/**
 * Created by huangkuan on 2017/8/5.
 */
public class ChoosePhotoDialog extends Dialog {
    public static final String TAG = "ChoosePhotoDialog";
    private TextView mTvTake;
    private TextView mTvChoose;
    private TextView mTvCancel;

    public ChoosePhotoDialog(Context context) {
        super(context, R.style.style_dialog_select_item);
        setContentView(R.layout.dialog_choose_photo);
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
        void onTake();

        void onChoose();
    }

    public void setOnSelectedListener(final OnSelectedListener onSelectedListener) {
        mTvTake.setOnClickListener(new View.OnClickListener() {//确认
            @Override
            public void onClick(View v) {
                onSelectedListener.onTake();
                dismiss();
            }
        });

        mTvChoose.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                onSelectedListener.onChoose();
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
