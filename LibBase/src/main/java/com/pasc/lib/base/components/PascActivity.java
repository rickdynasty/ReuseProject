package com.pasc.lib.base.components;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 继承AppCompatActivity的Activity基类。
 * Created by chenruihan410 on 2018/07/16.
 */
public class PascActivity extends AppCompatActivity {

    /**
     * 日志记录器，用于记录日志。
     */
    protected final Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogger.debug("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLogger.debug("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLogger.debug("onResume");
    }

    @Override
    protected void onPause() {
        mLogger.debug("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        mLogger.debug("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLogger.debug("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLogger.debug("onRestart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mLogger.debug("onNewIntent");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLogger.debug("onConfigurationChanged");
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mLogger.debug("onContentChanged");
    }
}
