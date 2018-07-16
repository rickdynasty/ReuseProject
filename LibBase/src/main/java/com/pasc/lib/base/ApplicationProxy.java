package com.pasc.lib.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by duyuan797 on 17/2/23.
 */

public class ApplicationProxy {

    private static Application sApplication;

    public static void init(Application ctx) {
        ApplicationProxy.sApplication = ctx;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

}
