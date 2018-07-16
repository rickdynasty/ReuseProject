package com.pasc.lib.base.components;

import android.util.Log;

import com.elvishew.xlog.XLog;

/**
 * XLog日志实现类。相关配置请参考XLog的github文档。
 * Created by chenruihan410 on 2018/07/16.
 */
public class XLoggerImpl implements Logger {

    public XLoggerImpl() {
        XLog.init();
    }

    @Override
    public void debug(String msg) {
        XLog.d(msg);
    }

    @Override
    public void info(String msg) {
        XLog.i(msg);
    }

    @Override
    public void error(String msg) {
        XLog.e(msg);
    }
}
