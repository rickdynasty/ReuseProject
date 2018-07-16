package com.pasc.lib.base.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.pasc.lib.base.ApplicationProxy;

/**
 * Created by yangwen881 on 17/2/23.
 */

public class ToastUtils {

    private static Context sCtx;
    private static Toast sToast;

    public static void toastMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        cancel();
        checkContext();
        sToast = Toast.makeText(sCtx, msg, Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.show();
    }

    public static void toastMsg(int msgId) {
        checkContext();
        toastMsg(sCtx.getResources().getString(msgId));
    }

    private static void checkContext() {
        if (sCtx == null) {
            sCtx = ApplicationProxy.getApplication();
        }
    }

    /**
     * 自定义toast样式
     *
     * @param toastVieww
     */
    public static void toastMsgWithStyle(View toastVieww) {
        if (sToast != null) {
            sToast.cancel();
        }
        checkContext();
        sToast = new Toast(sCtx);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.setView(toastVieww);
        sToast.show();
    }

    /**
     * 取消toast
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
