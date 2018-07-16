package com.pasc.lib.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决ScrollView嵌套GridView，GridView只显示一行的问题
 * create by wujianning385 on 2018/4/27.
 */
public class GroupGridView extends GridView {

    //设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 否则为 true
    private boolean needScrollBar = false;

    public GroupGridView(Context context) {
        super(context);
    }

    public GroupGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!needScrollBar) {
            int expandSpec =
                    MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
