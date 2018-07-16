package com.pasc.lib.base.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 输入法工具类
 * Created by duyuan797 on 18/3/10.
 */

public class InputMethodUtils {

    /**
     * 隐藏输入法
     */
    public static void hideInputMethod(Activity activity) {
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            InputMethodManager imeManager =
                    (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imeManager != null) {
                imeManager.hideSoftInputFromWindow(
                        activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示输入法
     */
    public static void showInputMethod(Activity activity) {
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            InputMethodManager imeManager =
                    (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imeManager != null) {
                imeManager.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
