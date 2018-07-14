package com.pasc.lib.webpage;

import android.app.Activity;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;


/**
 * fragment基类
 */
public class BaseFragment extends Fragment {
    protected String TAG = BaseFragment.class.getSimpleName();
    protected Handler mHandler = null;
    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        sureHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void unregisterContentObserver(ContentObserver observer) {
        if (mActivity != null) {
            mActivity.getContentResolver().unregisterContentObserver(observer);
        }
    }

    protected void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer) {
        if (mActivity != null) {
            mActivity.getContentResolver().registerContentObserver(uri, notifyForDescendents, observer);
        }
    }


    protected void sureHandler() {
        if (null == mHandler) {
            mHandler = new BaseHandler(this);
        }
    }

    public void handleMessage(Message msg) {

    }

    public Handler getHandler() {
        return mHandler;
    }

    public boolean onBackPressed() {
        return false;
    }

    public static class BaseHandler extends Handler {
        private WeakReference<BaseFragment> weakReference;

        public BaseHandler(BaseFragment baseFragment) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<>(baseFragment);
        }

        public BaseFragment get() {
            return weakReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragment activity = get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
