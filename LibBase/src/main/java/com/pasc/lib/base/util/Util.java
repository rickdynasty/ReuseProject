package com.pasc.lib.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by linhaiyang807 on 17/11/17.
 */

public class Util {

    /**
     * 调用拨号界面
     *
     * @param context
     * @param phone
     */
    public static void call(Context context, String phone) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取时间戳  限平安付调用
     *
     * @return
     */
    public static Date getDateByTimeZone() {
        long time = new Date().getTime() - Calendar.getInstance().getTimeZone().getRawOffset() + TimeZone
                .getTimeZone("GMT+8").getRawOffset();
        return new Date(time);
    }

    /**
     * 获取打电话的权限
     *
     * @param activity
     * @param phone
     */
    public static void callPermission(Activity activity, String phone) {

        //Android6.0以后的动态获取打电话权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            call(activity, phone);
        } else {
            //申请权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

}
