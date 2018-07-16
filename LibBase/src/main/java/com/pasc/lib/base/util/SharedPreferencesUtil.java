package com.pasc.lib.base.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xialo on 2016/7/25.
 */
public class SharedPreferencesUtil {

    private static final String spFileName = "welcomePage";
    public static final String FIRST_OPEN = "first_open";
    public static final String CITY_FLAG = "city_flag";
    public static final String FACE_PHONE = "face_phone";

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static Boolean getBoolean(Context context, String strKey,
                                     Boolean strDefault) {//strDefault	boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }

    public static String getString(Context context, String strKey,
                                   String strDefault) {
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strDefault);
        return result;
    }

    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }

    public static void putString(Context context, String strKey,
                                 String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }


    public static String getFacePhone(String key, String value) {

        String result = sp.getString(key, value);
        return result;
    }

    public static void putFacePhone(Context context, String strKey,
                                    String strData) {
        sp = context.getSharedPreferences(
                FACE_PHONE, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }


}
