package com.pasc.lib.base.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * 继承android.support.v4.app.Fragment的Fragment基类。
 * Created by chenruihan410 on 2018/07/16.
 */
public class PascFragment extends Fragment {

    /**
     * 日志记录器，用于记录日志。
     */
    protected final Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogger.debug("onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        mLogger.debug("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mLogger.debug("onResume");
    }

    @Override
    public void onPause() {
        mLogger.debug("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        mLogger.debug("onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mLogger.debug("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLogger.debug("onAttach");
    }

    @Override
    public void onDestroyView() {
        mLogger.debug("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLogger.debug("onActivityCreated");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mLogger.debug("onHiddenChanged");
    }
}
