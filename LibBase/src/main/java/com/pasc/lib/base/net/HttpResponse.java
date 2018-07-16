package com.pasc.lib.base.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class HttpResponse<T> {

    public static final int SUCCESS = 200; // 成功
    public static final int TOKEN_ILLEGAL = 101; // token不合法
    public static final int TOKEN_NOT_MATCHING_USER = 102; // token与当前用户不匹配
    public static final int TOKEN_INVALID = 103; // token失效
    public static final int TOKEN_EMPTY = 104; // token为空

    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @SerializedName("data")
    private T data;

    @Override
    public String toString() {
        return "HttpRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
