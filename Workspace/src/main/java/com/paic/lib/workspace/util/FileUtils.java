package com.paic.lib.workspace.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String getJsonFromAssets(Context context, String fileName) throws IOException {
        //获取assets资源管理器
        AssetManager assetManager = context.getAssets();

        return getJsonFromStream(new InputStreamReader(assetManager.open(fileName)));
    }

    public static String getJsonFromFile(String file) throws IOException {
        return getJsonFromStream(new InputStreamReader(new FileInputStream(file)));
    }

    private static String getJsonFromStream(final InputStreamReader stream) throws IOException {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        //通过管理器打开文件并读取
        BufferedReader bf = new BufferedReader(stream);
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
