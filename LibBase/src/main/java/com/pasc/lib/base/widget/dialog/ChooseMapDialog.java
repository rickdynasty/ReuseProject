package com.pasc.lib.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.ScreenUtils;
import com.pasc.lib.base.util.SizeUtils;

public class ChooseMapDialog extends Dialog {
    TextView tvGaode;
    TextView tvBaidu;
    TextView tvCancel;

    public ChooseMapDialog(Context context) {
        super(context, R.style.style_dialog_select_item);
        int widthPixels = ScreenUtils.getScreenWidth();
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_map, null);
        setContentView(contentView);
        tvGaode = (TextView) findViewById(R.id.tv_gaode);
        tvBaidu = (TextView) findViewById(R.id.tv_baidu);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = widthPixels - SizeUtils.dp2px(12);
        params.bottomMargin = SizeUtils.dp2px(10);
        contentView.setLayoutParams(params);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * 确认和取消按钮
     */
    public interface OnSelectedListener {
        void selectBaidu();

        void selectGaode();

        void onCancel();
    }

    public ChooseMapDialog setOnSelectedListener(final OnSelectedListener onSelectedListener) {

        tvGaode.setOnClickListener(new View.OnClickListener() {//高德
            @Override
            public void onClick(View v) {
                onSelectedListener.selectGaode();
                dismiss();
            }
        });

        tvBaidu.setOnClickListener(new View.OnClickListener() {//百度
            @Override
            public void onClick(View v) {
                onSelectedListener.selectBaidu();
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                onSelectedListener.onCancel();
                dismiss();
            }
        });
        return this;
    }
}
