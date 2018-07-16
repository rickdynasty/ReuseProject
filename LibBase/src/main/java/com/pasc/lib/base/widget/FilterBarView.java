package com.pasc.lib.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.pasc.lib.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选bar
 * Created by zc on 2018年01月24日11:08:51
 */

public class FilterBarView extends LinearLayout implements View.OnClickListener {


    protected View view;
    RadioButton radioButton01;
    RadioButton radioButton02;
    RadioButton radioButton03;

    private int currentPosition = -1;
    private List<RadioButton> radioButtonsList = new ArrayList<>();

    private FilterCallBack callBack;


    public FilterBarView(Context context) {
        super(context);
        initView(context, null);
    }

    public FilterBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        // 设置背景
        setBackgroundColor(getResources().getColor(R.color.title_bar));

        view = LayoutInflater.from(context).inflate(R.layout.filter_bar_view, null);
        radioButton01 = (RadioButton) view.findViewById(R.id.rb_01);
        radioButton02 = (RadioButton) view.findViewById(R.id.rb_02);
        radioButton03 = (RadioButton) view.findViewById(R.id.rb_03);
        radioButton01.setOnClickListener(this);
        radioButton02.setOnClickListener(this);
        radioButton03.setOnClickListener(this);
        radioButtonsList.add(radioButton01);
        radioButtonsList.add(radioButton02);
        radioButtonsList.add(radioButton03);

        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, params);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rb_01) {
            setChecked(0);
        } else if (id == R.id.rb_02) {
            setChecked(1);
        } else if (id == R.id.rb_03) {
            setChecked(2);
        }
    }

    private void setChecked(int position) {
        if (currentPosition == -1) {
            radioButtonsList.get(position).setChecked(true);
            currentPosition = position;
        } else if (currentPosition == position) {
            radioButtonsList.get(position).setChecked(false);
            currentPosition = -1;
        } else {
            radioButtonsList.get(position).setChecked(true);
            radioButtonsList.get(currentPosition).setChecked(false);
            currentPosition = position;
        }

        if (callBack != null) {
            callBack.callBack(position, radioButtonsList.get(position));
        }

    }

    /**
     * 设置点击回调
     *
     * @param callBack
     */
    public void setCallBack(FilterCallBack callBack) {
        this.callBack = callBack;
    }


    public interface FilterCallBack {
        void callBack(int position, RadioButton radioButton);
    }

    public void clearStatus() {
        if (currentPosition != -1 && radioButtonsList != null) {
            radioButtonsList.get(currentPosition).setChecked(false);
            currentPosition = -1;
        }
    }
}
