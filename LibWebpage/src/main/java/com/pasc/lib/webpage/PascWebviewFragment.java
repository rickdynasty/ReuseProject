package com.pasc.lib.webpage;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pasc.lib.webpage.util.FileUiUtils;
import com.pasc.lib.webpage.util.Utils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class PascWebviewFragment extends BaseFragment {
    private static String ERROR_PAGE = "file:///android_asset/failload/failLoadPage.html";
    private final String TAG = PascWebviewFragment.class.getSimpleName();
    public WebView mWebView = null;

    ViewGroup mRootView;
    ValueAnimator mValueAnimator = null;
    private ProgressBar mProgressbar;
    private int mCurrentProgress;

    public Handler mHandler = null; //baseFragment
    /**
     * 加载地址
     */
    private String mUrl = "";
    private boolean isLoadFinish = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_webview, container, false);
            mWebView = mRootView.findViewById(R.id.pase_webview);
            mProgressbar = (ProgressBar) mRootView.findViewById(R.id.mprogressBar);
            mProgressbar.setVisibility(View.VISIBLE);
            sureHandler();
            initWebView();
        }

        return mRootView;
    }


    private void initWebView() {
        initConfig();
        setWebContentsDebuggingEnabled();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setWebContentsDebuggingEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
    }

    /**
     * 初始化webview 的设置信息
     */
    private void initConfig() {
        initConfigSetting();
        initConfigWebClient();
    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    private void initConfigSetting() {
        if (mActivity != null) {
//            String userAgent;
//            String version = Utils.getLocalVersionName(mActivity);
//            mWebView.requestFocus(View.FOCUS_DOWN);// 手动加入输入焦点, 有些手机不支持键盘弹出
            WebSettings settings = mWebView.getSettings();

//            settings.setSavePassword(false);//h5界面不准保存密码

            settings.setJavaScriptEnabled(true);

//            settings.setUseWideViewPort(true);
//            settings.setLoadWithOverviewMode(true);
//            settings.setBuiltInZoomControls(true);
//            settings.setDisplayZoomControls(false);
//
//            userAgent = " PAChat(Android APP/" + version + " Build/" + version + " pasc)";
//
//            settings.setUserAgentString(settings.getUserAgentString() + userAgent);
//            settings.setAppCacheEnabled(true);
//
//            String appCachePath = FileUiUtils.getExternalCacheDir(mActivity).getAbsolutePath();
//            Log.d(TAG, "appCachePath::" + appCachePath);
//            settings.setAppCachePath(appCachePath);
//            settings.setAppCacheMaxSize(100 * 1024 * 1024);
//            settings.setDatabaseEnabled(true);
//            settings.setDomStorageEnabled(true);
//            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//            settings.setAllowFileAccess(true);
//            setWebContentsDebuggingEnabled();
//            if (Build.VERSION.SDK_INT >= 19) {
//                mWebView.getSettings().setLoadsImagesAutomatically(true);
//            } else {
//                mWebView.getSettings().setLoadsImagesAutomatically(false);
//            }
//            mWebView.setDownloadListener(new MyWebViewDownLoadListener());

            // 加载url
//            mWebView.loadUrl("file:///android_asset/test.html");
            mWebView.loadUrl("http://news.baidu.com/");
        }
    }

    /**
     * 添加注入对象，对象名称"android"
     */
    private void initConfigWebClient() {
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
//        if (plugin != null) {
//            mWebView.addJavascriptInterface(plugin, JAVASCRIPT_NAME);
//        } else {
//            BasePlugin basePlugin = new BasePlugin();
//            basePlugin.setHandler(mHandler);
//            basePlugin.setWebviewFragment(this);
//            basePlugin.setWebView(mWebView);
//            mWebView.addJavascriptInterface(basePlugin, JAVASCRIPT_NAME);
//        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG, "进度发生改变::" + newProgress);
            if (newProgress >= 100) {
                mProgressbar.setProgress(100);
                if (null != mHandler) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressbar.setProgress(0);
                            mProgressbar.setVisibility(View.GONE);
                        }
                    }, 500);
                }

            } else if (newProgress - mCurrentProgress > 0) {
                Log.w("hiyi", "newProgress: " + newProgress + ", mCurrentProgress: " + mCurrentProgress);
                mCurrentProgress = newProgress;
                startProgressAnim();
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
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
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

    };


    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished::" + url);
            isLoadFinish = true;
        }

        @SuppressLint("NewApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted::" + url);
            isLoadFinish = false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(TAG, "onReceivedError...");
            mWebView.loadUrl(ERROR_PAGE);
        }

        /**
         *在return前 执行webview.loadurl()会导致重新加载webview回到return不执行
         * @param view
         * @param url
         * @return false webview处理url是在webview内部执行，链接中该重定向的继续重定向。
         * 由于是系统进行的重定向，默认绑定为一个整体的url，点击回退，直接返回上一页面，而不是重定向页面
         *          true 根据程序逻辑来处理执行，即使需要重定向也会停止重定向
         *          super 如果是链接，则会跳出webview到浏览器中执行
         *
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading url:" + url);
            if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {//继续重定向
                return false;
            }

            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
            return super.shouldInterceptRequest(webView, s);
        }

        /**
         * 当后台不是ca认证的时候，手动校验https证书，目前没有检验，所以所有返回值都调用
         * sslErrorHandler.proceed();如若后期做成完全https，只需在checkSSLFalure调用
         * sslErrorHandler.cancel()，以及在WebViewSSlCheck中更换读取的证书路径即可
         * @param webView
         * @param sslErrorHandler
         * @param sslError
         */
        @Override
        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            sslErrorHandler.proceed();
        }
    };

    private void startProgressAnim() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            mValueAnimator.setDuration(400);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    int progress = (int) (value * mCurrentProgress);
                    if (progress > mProgressbar.getProgress()) {
                        mProgressbar.setProgress(progress);
                    }
                }
            });
        }
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        Log.i("hiyi", "mValueAnimator.start() ------");
        mValueAnimator.start();
    }

    /**
     * 下载监听
     *
     * @param
     * @return
     * @date 2018/4/23
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @SuppressLint("NewApi")
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (mActivity != null) {
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLoadFinish && mWebView.canGoBack()) {/*&& !isDirectlyFinish*/
            mWebView.goBack();
            return true;
        } else {
            if (mActivity != null) {
                mActivity.finish();
            }
            return true;
        }
    }
}
