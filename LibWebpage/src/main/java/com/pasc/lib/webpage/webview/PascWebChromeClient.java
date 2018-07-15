package com.pasc.lib.webpage.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.pasc.lib.webpage.R;
import com.pasc.lib.webpage.callback.WebChromeClientCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 1.0 (the "License");
 * <p>
 * hybird内核自定义WebChromeClient，主要完成JsAlert原生窗口化与WebView的加载进度和文件get
 *
 * @author chenshangyong872
 * @version 1.0
 * @date 2018-07-15
 */
public class PascWebChromeClient extends WebChromeClient {
    private final String TAG = PascWebChromeClient.class.getSimpleName();

    private Context mContext = null;
    private WebChromeClientCallback mCallback = null;

    public void setContext(Context context) {
        mContext = context;
    }

    public void setWebChromeClientCallback(WebChromeClientCallback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setTitle("JsAlert").setIcon(R.drawable.ic_launcher)
                .setMessage("JsAlert 信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        /**解决alert只弹出一次的问题*/
                        result.confirm();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        /**解决alert只弹出一次的问题*/
                        result.cancel();
                    }
                });
        builder.create().show();

        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Log.d(TAG, "进度发生改变::" + newProgress);
        if (null != mCallback) {
            mCallback.onProgressChanged(newProgress);
        }

        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
//            if (isChangeTitleByLoad) {
//                setWebTitle(title);
//            }
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
//            showCustomeView(view, callback);
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
//            hideCustomeView();
        super.onHideCustomView();
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        Log.i(TAG, "openFileChooser 01");
        if (null != mCallback) {
            mCallback.openFileChooser(valueCallback);
        }
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        Log.i(TAG, "openFileChooser 02");
        if (null != mCallback) {
            mCallback.openFileChooser(valueCallback);
        }
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        Log.i(TAG, "openFileChooser 03");
        if (null != mCallback) {
            mCallback.openFileChooser(valueCallback);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
//            return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
        if (null != mCallback) {
            mCallback.showFileChooser(valueCallback);
        }
        return true;
    }
}
