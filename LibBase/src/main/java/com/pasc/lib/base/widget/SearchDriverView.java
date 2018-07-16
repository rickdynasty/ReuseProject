package com.pasc.lib.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.SizeUtils;

/**
 * 一键召车寻找司机提示
 * Created by duyuan797 on 18/2/10.
 */
public class SearchDriverView extends AppCompatTextView {

    private Context context;

    public SearchDriverView(Context context) {
        this(context, null);
    }

    public SearchDriverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setBackgroundResource(R.mipmap.ic_search_driver_bg);
        setGravity(Gravity.CENTER);
        setText("正在为您寻找车辆...");
        setTextColor(ContextCompat.getColor(context, R.color.black_222222));
        setTextSize(SizeUtils.dp2px(4.5f));
        //setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10),
        //        SizeUtils.dp2px(10));
    }

    public void setTime(long time) {
        setText(Html.fromHtml(context.getString(R.string.wait_time, getTimeStr(time))));
    }

    private String getTimeStr(long time) {
        int minute = (int) (time / 60);
        int second = (int) (time % 60);
        StringBuilder timeStr = new StringBuilder();
        if (minute < 10) {
            timeStr.append("0");
        }
        timeStr.append(String.valueOf(minute)).append(":");
        if (second < 10) {
            timeStr.append("0");
        }
        timeStr.append(String.valueOf(second));
        return timeStr.toString();
    }
}
