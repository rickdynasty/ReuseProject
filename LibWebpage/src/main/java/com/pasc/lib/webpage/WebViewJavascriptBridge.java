package com.pasc.lib.webpage;


import com.pasc.lib.webpage.callback.CallBackFunction;

public interface WebViewJavascriptBridge {

    public void send(String data);

    public void send(String data, CallBackFunction responseCallback);
}
