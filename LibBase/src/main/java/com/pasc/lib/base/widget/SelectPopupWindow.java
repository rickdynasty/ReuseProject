package com.pasc.lib.base.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pasc.lib.base.R;

/**
 * Created by struggle_liping on 2017/4/3.
 */
public class SelectPopupWindow extends PopupWindow {

    private RecyclerView.Adapter mAdapter;
    private LayoutInflater layoutInflater;
    private RecyclerView rvSelectPop;
    private int mWidth;
    private Activity mActivity;
    private boolean mBackgroundDark;
    //蒙层（用于某些特殊效果）
    private View viewCover;
    private View popupWindowView;
    private View view1;

    public interface IPopWindow {
        void onClick(BaseQuickAdapter adapter, View view, int position);
    }

    public SelectPopupWindow(Activity context, int width, RecyclerView.Adapter adapter,
                             boolean isBackgroundDark) {
        super(context);
        mActivity = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWidth = width;
        this.mAdapter = adapter;
        this.mBackgroundDark = isBackgroundDark;
        initView();
    }

    private void initView() {
        popupWindowView = layoutInflater.inflate(R.layout.popwindow_select, null);
        rvSelectPop = (RecyclerView) popupWindowView.findViewById(R.id.rv_select_pop);
        viewCover = popupWindowView.findViewById(R.id.v_cover);
        view1 = popupWindowView.findViewById(R.id.view1);
        this.setContentView(popupWindowView);
        this.setWidth(mWidth);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        rvSelectPop.setLayoutManager(new LinearLayoutManager(mActivity));
        rvSelectPop.setAdapter(mAdapter);
        rvSelectPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = popupWindowView.findViewById(R.id.rv_select_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        viewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager, int decoration, int colorId) {
        rvSelectPop.setLayoutManager(layoutManager);
        RecycleViewSpace space = new RecycleViewSpace(mActivity, decoration, colorId);
        rvSelectPop.addItemDecoration(space);
        view1.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = rvSelectPop.getLayoutParams();
        params.height = 950;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rvSelectPop.setLayoutParams(params);
        rvSelectPop.setBackgroundColor(mActivity.getResources().getColor(R.color.white_ffffff));
        mAdapter.notifyDataSetChanged();
    }

    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = f;
        if (f == 1) {
            mActivity.getWindow()
                    .clearFlags(
                            WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            mActivity.getWindow()
                    .addFlags(
                            WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        mActivity.getWindow().setAttributes(lp);
    }

    public SelectPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mBackgroundDark) {
            setBackgroundAlpha(1.0f);
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        if (mBackgroundDark) {
            setBackgroundAlpha(0.5f);
        }
    }

    /**
     * 显示蒙层
     */
    public void showCoverView() {
        if (viewCover != null) {
            viewCover.setVisibility(View.VISIBLE);
        }
    }

    public void setBackGround(int res) {
        if (popupWindowView != null) popupWindowView.setBackgroundResource(res);
    }

//    public void setBackGroundColor(int color) {
//        if (popupWindowView != null) popupWindowView.setBackgroundColor(color);
//    }
    //    @Override
    //    public void showAsDropDown(View anchorView, int xoff, int yoff) {
    //        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
    //            int[] a = new int[2];
    //            anchorView.getLocationInWindow(a);
    //            showAtLocation(anchorView, Gravity.NO_GRAVITY, xoff, a[1] + anchorView.getHeight() + yoff);
    //        } else {
    //            super.showAsDropDown(anchorView, xoff, yoff);
    //        }
    //    }
    //

    /**
     * 针对性处理
     */
    public void showAsDropDownOnN(View anchorView) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            anchorView.getLocationInWindow(a);
            showAtLocation(anchorView, Gravity.NO_GRAVITY, 0, a[1] + anchorView.getHeight() + 0);
        } else {
            super.showAsDropDown(anchorView);
        }
    }

}
