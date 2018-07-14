package com.pasc.lib.webpage;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;

public interface WebChromeClientCallback {
    void onProgressChanged(int newProgress);
    void openFileChooser(ValueCallback<Uri> uploadMsg);
    void showFileChooser(ValueCallback<Uri[]> filePathCallback);
}
