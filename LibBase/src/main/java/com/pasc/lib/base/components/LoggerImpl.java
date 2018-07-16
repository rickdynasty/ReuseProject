package com.pasc.lib.base.components;

import android.util.Log;

/**
 * 系统日志实现类。
 * 如果想打开tag为xx的日志，则执行adb shell setprop log.tag.xx DEBUG
 * 如果想打开所有日志，则在手机执行adb shell setprop log.tag.pasc DEBUG
 * Created by chenruihan410 on 2018/07/16.
 */
public class LoggerImpl implements Logger {

    private static final String ALL_LOGS_NAME = "pasc";

    private String tag;

    public LoggerImpl(String tag) {
        this.tag = tag;
    }

    @Override
    public void debug(String msg) {
        if (isLoggableLevel(Log.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    @Override
    public void info(String msg) {
        if (isLoggableLevel(Log.INFO)) {
            Log.i(tag, msg);
        }
    }

    @Override
    public void error(String msg) {
        if (isLoggableLevel(Log.ERROR)) {
            Log.e(tag, msg);
        }
    }

    /**
     * 判断是否可打日志。
     *
     * @param level 日志等级
     * @return 布尔值
     */
    private boolean isLoggableLevel(int level) {
        return Log.isLoggable(tag, level) || Log.isLoggable(ALL_LOGS_NAME, level);
    }
}
