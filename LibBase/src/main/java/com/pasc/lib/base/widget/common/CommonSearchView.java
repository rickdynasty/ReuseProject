package com.pasc.lib.base.widget.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.lib.base.R;
import com.pasc.lib.base.widget.ClearEditText;
import com.pasc.lib.base.widget.dialog.SelectReminderDialog;
import com.pasc.lib.base.widget.flowlayout.FlowLayout;
import com.pasc.lib.base.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的搜索控件---目前适用于首页搜索、更多服务搜索、政务指南下点击的搜索。
 * Created by chendaixi947 on 2018/6/11.
 * Modified by chenruihan410 on 2018/07/14.
 *
 * @since 1.0
 */
public class CommonSearchView extends FrameLayout {
    /**
     * 搜索输入框
     */
    public ClearEditText etSearch;

    /**
     * 搜索历史TagFlowLayout
     */
    TagFlowLayout searchHistoryFlow;

    /**
     * 热词搜索提示
     */
    TextView tvHotSearch;

    /**
     * 热词搜索TagFlowLayout
     */
    TagFlowLayout searchHotFlow;

    /**
     * 用于展示搜索结果的RecyclerView
     */
    RecyclerView recyclerView;

    /**
     * 搜索历史提示
     */
    LinearLayout llHistoryTip;

    /**
     * 搜索历史区域
     */
    LinearLayout searchHistoryContainer;

    /**
     * 删除历史记录按钮
     */
    ImageView ivHistoryDelete;

    private List<IGetString> searchHistoryList = new ArrayList<>();
    private List<IGetString> searchHotList = new ArrayList<>();
    private SearchHistoryTagAdapter searchHistoryTagAdapter;
    private SearchHistoryTagAdapter searchHotTagAdapter;
    private OnClickListener onDeleteHistoryClickListener;
    private TagFlowLayout.OnTagClickListener onHistoryTagClickListener;


    public CommonSearchView(@NonNull Context context) {
        this(context, null);
    }

    public CommonSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_common_search, this, true);
        etSearch = view.findViewById(R.id.et_search);
        searchHistoryFlow = view.findViewById(R.id.search_history_flow);
        tvHotSearch = view.findViewById(R.id.tv_hot_search);
        searchHotFlow = view.findViewById(R.id.search_hot_flow);
        recyclerView = view.findViewById(R.id.recyclerView_service_search);
        llHistoryTip = view.findViewById(R.id.ll_history_tip);
        searchHistoryContainer = view.findViewById(R.id.search_history_container);
        ivHistoryDelete = view.findViewById(R.id.iv_search_history_delete);

        findViewById(R.id.tv_search_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) getContext();
                activity.onBackPressed();
            }
        });
        ivHistoryDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearHistoryDialog();
            }
        });

        initView();
        searchHistoryFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (onHistoryTagClickListener != null) {
                    onHistoryTagClickListener.onTagClick(view, position, parent);
                }
                return false;
            }
        });
    }

    private void initView() {
        //初始化搜索历史相关
        searchHistoryTagAdapter = new SearchHistoryTagAdapter<>(getContext(), searchHistoryList);
        searchHistoryFlow.setAdapter(searchHistoryTagAdapter);
        //初始化热词搜索相关
        searchHotTagAdapter = new SearchHistoryTagAdapter<>(getContext(), searchHotList);
        searchHotFlow.setAdapter(searchHotTagAdapter);
    }

    /**
     * 搜索历史标签点击事件监听
     *
     * @param onHistoryTagClickListener 监听回调
     */
    public CommonSearchView setOnHistoryTagClickListener(TagFlowLayout.OnTagClickListener onHistoryTagClickListener) {
        this.onHistoryTagClickListener = onHistoryTagClickListener;
        return this;
    }

    /**
     * 搜索热词搜索点击事件监听
     *
     * @param onHotTagClickListener 监听回调
     */
    public CommonSearchView setOnHotTagClickListener(TagFlowLayout.OnTagClickListener onHotTagClickListener) {
        searchHotFlow.setOnTagClickListener(onHotTagClickListener);
        return this;
    }

    /**
     * 设置搜索输入关键词变化监听
     *
     * @param editTextChangeListener 监听回调
     */
    public CommonSearchView setEditTextChangeListener(ClearEditText.EditTextChangeListener editTextChangeListener) {
        etSearch.setEditTextChangeListener(editTextChangeListener);
        return this;
    }

    /**
     * 设置输入法上按回车键或"搜索"按钮等时回调
     *
     * @param onEditorActionListener 监听回调
     */
    public CommonSearchView setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        etSearch.setOnEditorActionListener(onEditorActionListener);
        return this;
    }

    /**
     * 设置删除历史记录点击事件监听
     *
     * @param onDeleteHistoryClickListener 监听回调
     */
    public CommonSearchView setOnDeleteHistoryClickListener(OnClickListener onDeleteHistoryClickListener) {
        this.onDeleteHistoryClickListener = onDeleteHistoryClickListener;
        return this;
    }

    /**
     * 获取用于展示搜索结果的RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 设置搜索输入框提示语
     *
     * @param textHint 提示文字
     */
    public CommonSearchView setSearchEditTextHint(String textHint) {
        etSearch.setHint(textHint);
        return this;
    }

    /**
     * 设置搜索历史数据
     *
     * @param historyData 历史数据
     */
    public <T extends IGetString> CommonSearchView setSearchHistoryData(List<T> historyData) {
        searchHistoryList.clear();
        searchHistoryList.addAll(historyData);
        searchHistoryTagAdapter.notifyDataChanged();
        return this;
    }

    /**
     * 设置热词搜索数据
     *
     * @param hotData 热词数据
     */
    public <T extends IGetString> CommonSearchView setSearchHotData(List<T> hotData) {
        searchHotList.clear();
        searchHotList.addAll(hotData);
        searchHotTagAdapter.notifyDataChanged();
        return this;
    }

    /**
     * 通知界面刷新
     */
    public void notifyViewRefresh() {
        if (recyclerView.getVisibility() == VISIBLE && recyclerView.getChildCount() > 0) {
            return;
        }
        setHistoryVisible(searchHistoryList.isEmpty() ? View.GONE : View.VISIBLE);
        setHotVisible(searchHotList.isEmpty() ? View.GONE : View.VISIBLE);
        setHistoryAndHotContainerVisibility(searchHistoryList.isEmpty() && searchHotList.isEmpty() ? GONE : VISIBLE);
    }

    /**
     * 设置历史纪录和热词搜索区域是否要隐藏
     */
    public void setHistoryAndHotContainerVisibility(int visible) {
        searchHistoryContainer.setVisibility(visible);
    }


    /**
     * 隐藏历史纪录
     */
    private void setHistoryVisible(int visible) {
        llHistoryTip.setVisibility(visible);
        searchHistoryFlow.setVisibility(visible);
    }

    /**
     * 隐藏热词搜索纪录
     */
    private void setHotVisible(int visible) {
        tvHotSearch.setVisibility(visible);
        searchHotFlow.setVisibility(visible);
    }

    /**
     * 删除历史记录确认弹窗
     */
    private void showClearHistoryDialog() {
        SelectReminderDialog dialog = new SelectReminderDialog(getContext(),
                R.layout.dialog_select_reminder_search);
        dialog.setOnSelectedListener(new SelectReminderDialog.OnSelectedListener() {
            @Override
            public void onSelected() {
                dialog.dismiss();
                searchHistoryList.clear();
                setSearchHistoryData(searchHistoryList).notifyViewRefresh();
                if (onDeleteHistoryClickListener != null) {
                    onDeleteHistoryClickListener.onClick(ivHistoryDelete);
                }
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
