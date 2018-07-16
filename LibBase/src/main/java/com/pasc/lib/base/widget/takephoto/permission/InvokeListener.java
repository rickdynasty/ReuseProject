package com.pasc.lib.base.widget.takephoto.permission;

import com.pasc.lib.base.widget.takephoto.model.InvokeParam;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
