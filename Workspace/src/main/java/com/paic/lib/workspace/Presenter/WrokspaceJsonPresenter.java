package com.paic.lib.workspace.Presenter;

import android.content.Context;

import com.paic.lib.workspace.Model.BaseJsonModel;
import com.paic.lib.workspace.Model.WorkspaceData;
import com.paic.lib.workspace.Model.WorkspaceModel;
import com.paic.lib.workspace.view.IWorkspaceUI;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des 工作台Workspace的父容器采用json方式加载数据的Presenter
 * @modify On 2018-05-30 by author for reason ...
 */
public class WrokspaceJsonPresenter implements BasePresenter, BaseJsonModel.NavigationLoadJsonCallback {

    private final IWorkspaceUI mView;
    private final WorkspaceModel mModel;

    public WrokspaceJsonPresenter(IWorkspaceUI view) {
        if (null == view) {
            throw new RuntimeException("The \"null == mView\" scenario is theoretically absent~!");
        }

        mView = view;
        mModel = new WorkspaceModel();
    }

    @Override
    public void loadJsonFromAssets(Context context, String jsonFileName) {
        mModel.loadJsonFromAssets(context, jsonFileName, this);
    }

    @Override
    public void loadJson(String jsonFilePath) {
        mModel.loadJsonData(jsonFilePath, this);
    }

    @Override
    public void success(WorkspaceData result) {
        if (null == mView) {
            throw new RuntimeException("The \"null == mView\" scenario is theoretically absent~!");
        }

        mView.initWorkspace(result);
    }

    @Override
    public void failure(String errorDes, Exception e) {
        if (null == mView) {
            throw new RuntimeException("The \"null == mView\" scenario is theoretically absent~!");
        }

        mView.loadJsonfailure(errorDes, e);
    }

    @Override
    public void loadDefaultData() {
    }
}
