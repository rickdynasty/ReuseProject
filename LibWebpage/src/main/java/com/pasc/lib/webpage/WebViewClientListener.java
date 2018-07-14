package com.pasc.lib.webpage;

import android.graphics.Bitmap;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;

/**
 * 用于业务监听Webview的状态行为
 */
public abstract class WebViewClientListener {
    public void onPageStarted(String url, Bitmap favicon) {
    }

    public void onPageFinished(String url) {
    }

    public void onLoadResource(String url) {
    }

    public void onReceivedError(int errorCode, String description, String failingUrl) {
    }

    public void onReceivedError(WebResourceRequest request, WebResourceError error) {
    }

    public void onReceivedHttpError(WebResourceRequest request, WebResourceResponse errorResponse) {
    }

    public void onReceivedSslError(SslErrorHandler handler, SslError error) {
    }

    public void onReceivedClientCertRequest(ClientCertRequest request) {
    }

    public void onUnhandledKeyEvent(KeyEvent event) {
    }

    public void onScaleChanged(float oldScale, float newScale) {
    }

    public void onReceivedLoginRequest(String realm, String account, String args) {
    }
}
