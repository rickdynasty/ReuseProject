package com.pasc.lib.base.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pasc.lib.base.widget.takephoto.uitl.CleanPath;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwen881 on 17/3/27.
 */

public class GSON {

    private static class InstanceHolder {
        public static Gson gson = new Gson();
    }

    public static Gson getGson() {
        return InstanceHolder.gson;
    }

    public static String toJson(Object object) {
        return InstanceHolder.gson.toJson(object);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        json = CleanPath.cleanString(json);
        return InstanceHolder.gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return InstanceHolder.gson.fromJson(json, classOfT);
    }

    public static Map<String, String> mapFromJson(String json) {
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return fromJson(json, type);
    }

}
