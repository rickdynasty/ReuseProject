package com.pasc.lib.base.util;

import com.pasc.lib.base.ApplicationProxy;

/**
 * Created by yangwen881 on 17/2/27.
 */

public class ResourceUtils {

    public static String getString(int resId) {
        try {
            return ApplicationProxy.getApplication().getString(resId);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(int resId, Object... formatArgs) {
        try {
            return ApplicationProxy.getApplication().getString(resId, formatArgs);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getInteger(int resId) {
        try {
            return ApplicationProxy.getContext().getResources().getInteger(resId);
        } catch (Exception e) {
            return 0;
        }
    }
}