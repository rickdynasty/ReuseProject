package com.pasc.lib.base.components;

/**
 * 日志接口，用于接入日志库。
 * Created by chenruihan410 on 2018/07/16.
 */
public interface Logger {

    void debug(String msg);

    void info(String msg);

    void error(String msg);
}
