package com.pasc.lib.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

    //检查是否有网络连接
    public static boolean testNetWork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
