package com.paic.lib.workspace.Model;

import android.content.Context;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des MVP Json解析的BaseJsonModel
 * @modify On 2018-05-30 by author for reason ...
 */
public interface BaseJsonModel {
    void loadJsonData(String jsonFilePath, final NavigationLoadJsonCallback callback);

    void loadJsonFromAssets(Context context, String jsonFileName, final NavigationLoadJsonCallback callback);


    interface NavigationLoadJsonCallback {
        void success(WorkspaceData result);

        void failure(String errorDes, Exception e);
    }
}
