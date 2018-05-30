package com.paic.lib.workspace.Presenter;

import android.content.Context;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des BasePresenter接口
 * @modify On 2018-05-30 by author for reason ...
 */
public interface BasePresenter {
    void loadJsonFromAssets(Context context, String jsonFileName);

    void loadJson(String fileName);

    void loadDefaultData();
}
