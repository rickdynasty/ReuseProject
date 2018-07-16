package com.pasc.lib.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.facebook.stetho.Stetho;
import com.pasc.lib.base.util.SDCardUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class BaseLibs {

    private static String SDCARD_LOG_FILE_DIR = "log";
    private static String DEFAULT_LOG_TAG = "BaseLib";

    public static void init(Application app, boolean productModel) {
        ApplicationProxy.init(app);
        //InjectCenter.init(app);

        initXLog(app);
        initStetho(app, productModel);
        initLeakCanary();
        Log.d(BaseLibs.class.getSimpleName(), "debug?=>" + BuildConfig.DEBUG);
    }

    public static void initStetho(Context context, boolean productModel) {
        if (!productModel) {
            Stetho.initialize(Stetho.newInitializerBuilder(context)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                    .build());
        }
    }

    private static void initXLog(Context context) {
        LogConfiguration config =
                new LogConfiguration.Builder().tag(DEFAULT_LOG_TAG).nt().st(1).b().build();
        Printer androidPrinter = new AndroidPrinter();
        Printer filePrinter = null;
        String logFileDir = SDCardUtils.getAppDir(context, SDCARD_LOG_FILE_DIR);
        if (!TextUtils.isEmpty(logFileDir)) {
            filePrinter = new FilePrinter.Builder(logFileDir).fileNameGenerator(
                    new DateFileNameGenerator()).build();
        }
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE, config, androidPrinter, filePrinter);
    }

    private static RefWatcher refWatcher;

    /**
     * 初始化LeakCanary
     */
    private static void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(ApplicationProxy.getApplication())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(ApplicationProxy.getApplication());
    }
}
