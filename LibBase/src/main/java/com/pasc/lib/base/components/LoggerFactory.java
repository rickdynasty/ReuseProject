package com.pasc.lib.base.components;

/**
 * 日志接口，用于接入日志库。
 * Created by chenruihan410 on 2018/07/16.
 */
public class LoggerFactory {

    public static final int TYPE_ANDROID = 1; // android原生的日志
    public static final int TYPE_X_LOG = 2; // x-log日志，如果日志类型选这个，请参考X-log的文档进行相应配置。

    private static int loggerType = TYPE_ANDROID; // 日志器类型，默认是Android类型。

    /**
     * 设置日志器类型。
     *
     * @param loggerType
     */
    public static void setLoggerType(int loggerType) {
        LoggerFactory.loggerType = loggerType;
    }

    /**
     * 获取日志记录器。
     *
     * @param tag 日志来源标签。
     * @return 日志记录器。
     */
    public static Logger getLogger(String tag) {
        switch (loggerType) {
            case TYPE_ANDROID: {
                return new LoggerImpl(tag);
            }
            case TYPE_X_LOG: {
                return new XLoggerImpl();
            }
            default: {
                return new LoggerImpl(tag);
            }
        }
    }
}
