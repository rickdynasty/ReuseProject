package com.pasc.lib.webpage.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.pasc.lib.webpage.Message;
import com.pasc.lib.webpage.WebViewJavascriptBridge;
import com.pasc.lib.webpage.behavior.BehaviorManager;
import com.pasc.lib.webpage.callback.CallBackFunction;
import com.pasc.lib.webpage.util.FileUiUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class PascWebView extends WebView implements WebViewJavascriptBridge {

    private final String TAG = PascWebView.class.getSimpleName();

    public static final String JS_JAVASCRIPT_BRIDGE = "WebViewJavascriptBridge.js";
    Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
    private Context mContext;

    private List<Message> startupMessage = new ArrayList<Message>();

    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    private PascWebChromeClient mWebChromeClient = null;
    private PascWebViewClient mWebViewClient = null;

    public PascWebChromeClient getWebChromeClient() {
        sureWebChromeClient();
        return mWebChromeClient;
    }

    public boolean isLoadFinish() {
        if (null != mWebViewClient) {
            return mWebViewClient.isLoadFinish();
        }

        return true;
    }

    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    private long uniqueId = 0;

    public PascWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        init();
    }

    public PascWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        init();
    }

    public PascWebView(Context context) {
        super(context);
        mContext = context;

        init();
    }

    private void init() {
        requestFocus(View.FOCUS_DOWN);// 手动加入输入焦点, 有些手机不支持键盘弹出
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);//h5界面不准保存密码
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
//            String userAgent;
//            String version = Utils.getLocalVersionName(mActivity);
//            userAgent = " PAChat(Android APP/" + version + " Build/" + version + " pasc)";
//            settings.setUserAgentString(settings.getUserAgentString() + userAgent);
        settings.setAppCacheEnabled(true);
        String appCachePath = FileUiUtils.getExternalCacheDir(mContext).getAbsolutePath();
        Log.d(TAG, "appCachePath::" + appCachePath);
        settings.setAppCachePath(appCachePath);

        settings.setAppCacheMaxSize(100 * 1024 * 1024);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }

        sureWebViewClient();
        sureWebChromeClient();
    }

    private void sureWebViewClient() {
        if (null == mWebViewClient) {
            mWebViewClient = new PascWebViewClient(this);
            this.setWebViewClient(mWebViewClient);
        }
    }

    private void sureWebChromeClient() {
        if (null == mWebChromeClient) {
            mWebChromeClient = new PascWebChromeClient();
            mWebChromeClient.setContext(mContext);
            setWebChromeClient(mWebChromeClient);
        }
    }

    /**
     * 获取到CallBackFunction data执行调用并且从数据集移除
     *
     * @param url
     */
    void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        CallBackFunction f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.onCallBack(data);
            responseCallbacks.remove(functionName);
            return;
        }
    }

    @Override
    public void send(String data) {
        send(data, null);
    }

    @Override
    public void send(String data, CallBackFunction responseCallback) {
        doSend(null, data, responseCallback);
    }

    /**
     * 保存message到消息队列
     *
     * @param handlerName      handlerName
     * @param data             data
     * @param responseCallback CallBackFunction
     */
    private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
        Message m = new Message();
        if (!TextUtils.isEmpty(data)) {
            m.setData(data);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            responseCallbacks.put(callbackStr, responseCallback);
            m.setCallbackId(callbackStr);
        }
        if (!TextUtils.isEmpty(handlerName)) {
            m.setHandlerName(handlerName);
        }

        queueMessage(m);
    }

    /**
     * list<message> != null 添加到消息集合否则分发消息
     *
     * @param m Message
     */
    private void queueMessage(Message m) {
        if (startupMessage != null) {
            startupMessage.add(m);
        } else {
            dispatchMessage(m);
        }
    }

    /**
     * 分发message 必须在主线程才分发成功
     *
     * @param m Message
     */
    void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\')", "\\\\\'");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.loadUrl(javascriptCommand);
        }
    }

    /**
     * 刷新消息队列
     */
    void flushMessageQueue() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                    // deserializeMessage 反序列化消息
                    List<Message> list = null;
                    try {
                        list = Message.toArrayList(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Message m = list.get(i);
                        String responseId = m.getResponseId();
                        // 是否是response  CallBackFunction
                        if (!TextUtils.isEmpty(responseId)) {
                            CallBackFunction function = responseCallbacks.get(responseId);
                            String responseData = m.getResponseData();
                            function.onCallBack(responseData);
                            responseCallbacks.remove(responseId);
                        } else {
                            CallBackFunction responseFunction = null;
                            // if had callbackId 如果有回调Id
                            final String callbackId = m.getCallbackId();
                            if (!TextUtils.isEmpty(callbackId)) {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void onCallBack(String data) {
                                        Message responseMsg = new Message();
                                        responseMsg.setResponseId(callbackId);
                                        responseMsg.setResponseData(data);
                                        queueMessage(responseMsg);
                                    }
                                };
                            } else {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void onCallBack(String data) {
                                        // do nothing
                                    }
                                };
                            }

                            // BehaviorHandler执行
                            BehaviorManager.getInstance().actionBehavior(m, responseFunction);
                        }
                    }
                }
            });
        }
    }


    public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
        this.loadUrl(jsUrl);
        // 添加至 Map<String, CallBackFunction>
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
    }

    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     *
     * @param handlerName handlerName
     * @param data        data
     * @param callBack    CallBackFunction
     */
    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
    }
}
