package com.paic.lib.workspace.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.paic.lib.workspace.util.FileUtils;

import java.io.IOException;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台Wrokspace所在容器的数据加载model
 * @modify On 2018-05-30 by author for reason ...
 */
public class WorkspaceModel implements BaseJsonModel {
    private static final String TAG = WorkspaceModel.class.getSimpleName();

    @Override
    public void loadJsonData(String jsonFilePath, BaseJsonModel.NavigationLoadJsonCallback callback) {
        try {
            String content = FileUtils.getJsonFromFile(jsonFilePath);
            Gson gson = new Gson();
            WorkspaceData entity = gson.fromJson(content, WorkspaceData.class);
            callback.success(entity);
        } catch (IOException e) {
            callback.failure("loadJsonData:" + jsonFilePath, e);
        } catch (JsonSyntaxException e) {
            callback.failure("loadJsonData:" + jsonFilePath, e);
        }
    }

    @Override
    public void loadJsonFromAssets(Context context, String jsonFileName, BaseJsonModel.NavigationLoadJsonCallback callback) {
        try {
            String content = FileUtils.getJsonFromAssets(context, jsonFileName);
            Gson gson = new Gson();
            WorkspaceData workspaceData = gson.fromJson(content, WorkspaceData.class);
            if (null != workspaceData) {
                workspaceData.processing(context);
            }
            callback.success(workspaceData);
        } catch (IOException e) {
            callback.failure("loadJsonFromAssets:" + jsonFileName, e);
        } catch (JsonSyntaxException e) {
            callback.failure("loadJsonFromAssets:" + jsonFileName, e);
        }
    }
}
