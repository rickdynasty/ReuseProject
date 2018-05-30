package com.paic.lib.workspace.view;

import com.paic.lib.workspace.Model.WorkspaceData;

/**
 * Copyright (C) 2018 pa_zwt Licensed under the Apache License, Version 1.0 (the "License");
 *
 * @author yongchen
 * @version v1.0
 * @date 2018-05-30
 * @des MVP中P持有V的接口定义
 * @modify On 2018-05-30 by author for reason ...
 */
public interface IWorkspaceUI {
    void initWorkspace(WorkspaceData result);

    void loadJsonfailure(String errorDes, Exception e);
}
