package com.pasc.lib.base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by chendaixi947 on 2018/6/8
 *
 * @since 1.0
 */
public class ResizableImageView extends AppCompatImageView {
    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);

            float scale = (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth();
            float maxScale = (float) (25.0 / 18);
            if (scale >= maxScale) {
                scale = maxScale;
            }
            //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * scale);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
