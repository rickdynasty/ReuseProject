package com.pasc.lib.webpage.webview;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.pasc.lib.webpage.Message;
import com.pasc.lib.webpage.callback.WebViewClientListener;
import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 如果要自定义WebViewClient必须要集成此类
 * Created by bruce on 10/28/15.
 */
public class PascWebViewClient extends WebViewClient {

    private static String ERROR_PAGE = "file:///android_asset/failload/failLoadPage.html";

    private PascWebView mWebView;

    private WebViewClientListener mListener = null;

    private boolean isLoadFinish = false;

    public PascWebViewClient(PascWebView webView) {
        this.mWebView = webView;
    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    // URL拦截判断逻辑
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            mWebView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            mWebView.flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    // 增加shouldOverrideUrlLoading在api》=24时
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                mWebView.handlerReturnData(url);
                return true;
            } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                mWebView.flushMessageQueue();
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }
        } else {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (null != mListener) {
            mListener.onPageStarted(url, favicon);
        }

        isLoadFinish = false;
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (null != mListener) {
            mListener.onPageFinished(url);
        }

        super.onPageFinished(view, url);
        isLoadFinish = true;

        if (PascWebView.JS_JAVASCRIPT_BRIDGE != null) {
            BridgeUtil.webViewLoadLocalJs(view, PascWebView.JS_JAVASCRIPT_BRIDGE);
        }

        //
        if (mWebView.getStartupMessage() != null) {
            for (Message m : mWebView.getStartupMessage()) {
                mWebView.dispatchMessage(m);
            }

            mWebView.setStartupMessage(null);
        }
    }

    /////////////////////////////一下复写仅做监听/////////////////////////////
    @Override
    public void onLoadResource(WebView view, String url) {
        if (null != mListener) {
            mListener.onLoadResource(url);
        }

        super.onLoadResource(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (null != mListener) {
            mListener.onReceivedError(errorCode, description, failingUrl);
        }

        mWebView.loadUrl(ERROR_PAGE);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (null != mListener) {
            mListener.onReceivedError(request, error);
        }

        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (null != mListener) {
            mListener.onReceivedHttpError(request, errorResponse);
        }

        super.onReceivedHttpError(view, request, errorResponse);
    }

    /**
     * 当后台不是ca认证的时候，手动校验https证书，目前没有检验，所以所有返回值都调用
     * sslErrorHandler.proceed();如若后期做成完全https，只需在checkSSLFalure调用
     * sslErrorHandler.cancel()，以及在WebViewSSlCheck中更换读取的证书路径即可
     *
     * @param view
     * @param handler
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (null != mListener) {
            mListener.onReceivedSslError(handler, error);
        }

        handler.proceed();
//        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (null != mListener) {
            mListener.onReceivedClientCertRequest(request);
        }

        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (null != mListener) {
            mListener.onUnhandledKeyEvent(event);
        }

        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (null != mListener) {
            mListener.onScaleChanged(oldScale, newScale);
        }

        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        if (null != mListener) {
            mListener.onReceivedLoginRequest(realm, account, args);
        }

        super.onReceivedLoginRequest(view, realm, account, args);
    }
}