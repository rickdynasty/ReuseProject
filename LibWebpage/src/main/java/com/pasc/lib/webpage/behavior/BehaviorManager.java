package com.pasc.lib.webpage.behavior;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.pasc.lib.webpage.Message;
import com.pasc.lib.webpage.callback.CallBackFunction;

import java.util.HashMap;
import java.util.Map;

public class BehaviorManager {
    private static final String TAG = BehaviorManager.class.getSimpleName();

    public static final int ACTION_BEHAVIOR_TOAST = 0x270F;

    // 功能协议名，对于的功能
    private Map<String, BehaviorHandler> mDefaultBehaviorHandlers = new HashMap<>(32);
    // 定制的行为功能[大部分是针对default做复写] — Rick_Note:暂时不做场景区分
    private Map<String, BehaviorHandler> mCustomBehaviorCache = new HashMap<>(16);

    private BehaviorHandler mDefaultHandler = null;
    private Handler mUIHandler = null;    //这里仅能持有homeActivity,否则泄漏你晓得后果的...

    private BehaviorManager() {
        initDefaultBehaviorHandlers();
    }

    public static BehaviorManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final BehaviorManager instance = new BehaviorManager();
    }

    public void setUIHandler(Handler handler) {
        mUIHandler = handler;
    }

    /**
     * 触发行为
     *
     * @param message：每个参数一行，注明其取值范围等
     * @param responseFunction:行为处理后的回调
     * @author chenshangyong872
     */
    public void actionBehavior(Message message, CallBackFunction responseFunction) {
        BehaviorHandler handler;
        if (!TextUtils.isEmpty(message.getHandlerName())) {
            handler = mDefaultBehaviorHandlers.get(message.getHandlerName());
        } else {
            handler = mDefaultHandler;
        }

        if (null == handler) {
            sureRegisterDefaultHandler();
            handler = mDefaultHandler;
        }

        handler.handler(message.getData(), responseFunction);
    }

    public void sureRegisterDefaultHandler() {
        if (null == mDefaultHandler) {
            mDefaultHandler = new DefaultHandler();
        }
    }

    private void initDefaultBehaviorHandlers() {
        // 这里构建所有的默认BehaviorHandler[包括ui：ActionBar的和功能]
        registerDefaultBehavior(ConstantBehaviorName.WEB_CONTROL_BACK, new BehaviorHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                showToast("PASC.app.webControlBackItem：" + data);

                function.onCallBack("Native get 到了(*￣︶￣)");
            }
        });

        // ...
    }

    /**
     * 注册 Behavior,javascript可以通过协议名来和本地native进行通讯
     *
     * @param protocolName 行为的协议名 - 由H5和java确定
     * @param handler      handler
     */
    private void registerDefaultBehavior(String protocolName, BehaviorHandler handler) {
        if (!TextUtils.isEmpty(protocolName) && !mDefaultBehaviorHandlers.containsKey(protocolName)) {
            Log.i(TAG, "registerDefaultBehavior: " + protocolName);
            mDefaultBehaviorHandlers.put(protocolName, handler);
        }
    }

    /**
     * 注册 Behavior,javascript可以通过协议名来和本地native进行通讯
     *
     * @param protocolName 行为的协议名 - 由H5和java确定
     * @param handler      handler
     */
    public void registerBehavior(String protocolName, BehaviorHandler handler) {
        if (!TextUtils.isEmpty(protocolName) && !mCustomBehaviorCache.containsKey(protocolName)) {
            Log.i(TAG, "registerBehavior: " + protocolName);
            mCustomBehaviorCache.put(protocolName, handler);
        }
    }

    public void clearCustomBehavior() {
        if (null == mCustomBehaviorCache) {
            mCustomBehaviorCache = new HashMap<>(16);
        }

        mCustomBehaviorCache.clear();
    }

    class DefaultHandler implements BehaviorHandler {
        @Override
        public void handler(String data, CallBackFunction function) {
            showToast("这个是DefaultHandler data is " + data);

            if (function != null) {
                function.onCallBack("DefaultHandler response data");
            }
        }
    }

    private void showToast(String text) {
        if (null != mUIHandler) {
            android.os.Message msg = mUIHandler.obtainMessage();
            msg.what = ACTION_BEHAVIOR_TOAST;
            msg.obj = text;
            mUIHandler.sendMessage(msg);
        }
    }
}
