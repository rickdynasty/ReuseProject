package com.pasc.lib.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pasc.lib.base.ApplicationProxy;


/**
 * Created by yangwen881 on 17/5/3.
 */
public class SpUtils {

    private static final String SP_FILE_NAME = "sp_file_default";

    private SpUtils() {
    }

    public static SpUtils getInstance() {
        return SpUtilsHolder.instance;
    }

    private static class SpUtilsHolder {
        public static SpUtils instance = new SpUtils();
    }

    /**
     * 保存数据到sp中
     *
     * @param fileName sp文件名字
     * @param key
     * @param value
     */
    public void setParam(String fileName, String key, Object value) {
        SharedPreferences sp = ApplicationProxy.getApplication().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        save(sp.edit(), key, value);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void setParam(String key, Object object) {
        SharedPreferences sp = ApplicationProxy.getApplication().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        save(sp.edit(), key, object);
    }

    private void save(SharedPreferences.Editor editor, String key, Object object) {
        if (object == null) {
            return;
        }
        String type = object.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public Object getParam(String fileName, String key, Object defaultValue) {
        SharedPreferences sp = ApplicationProxy.getApplication().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return get(sp, key, defaultValue);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject) {
        SharedPreferences sp = ApplicationProxy.getApplication().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return get(sp, key, defaultObject);
    }

    private Object get(SharedPreferences sp, String key, Object defaultObject) {
        if (defaultObject == null) {
            return null;
        }
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }
}
