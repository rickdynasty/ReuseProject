package com.pasc.lib.webpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pasc.lib.webpage.util.WebViewVirtualBoardAndInputTools;

final public class PascWebviewActivity extends FragmentActivity {
    private PascWebviewFragment mWebviewFragment;

    // 启动WebviewActivity
    public static void startWebviewActivity(final Context context) {
        Intent intent = new Intent(context, PascWebviewActivity.class);
//        intent.putExtra(PAGE_TYPE, pageType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        WebViewVirtualBoardAndInputTools.assistActivity(this);
        initWebview();
    }

    @Override
    public void setContentView(int layoutResID) {
        if (!isNeedshowTitle()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

        super.setContentView(layoutResID);
    }

    public boolean isNeedshowTitle() {
        return true;
    }

    public void initWebview() {
        initView();
        Fragment fragment = getSupportFragmentManager().findFragmentById(getContentId());
        if (fragment == null) {
            fragment = new PascWebviewFragment();
            Bundle bundle = getIntent().getExtras();
            fragment.setArguments(bundle);
            showFragment(getContentId(), fragment);
        }

        mWebviewFragment = (PascWebviewFragment) fragment;
    }

    protected void showFragment(int resId, Fragment fg) {
        showFragment(resId, fg, false);
    }

    protected void showFragment(int resId, Fragment fg, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(resId, fg);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    private int getContentId() {
        return R.id.fl_content;
    }

    public void initView() {
        // 处理actionbar
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (mWebviewFragment != null)
            mWebviewFragment.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (mWebviewFragment != null) {
            mWebviewFragment.onBackPressed();
        }
    }
}
