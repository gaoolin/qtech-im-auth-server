package com.qtech.im.auth.config;

import com.qtech.im.auth.service.management.IPermissionService;
import jakarta.annotation.PostConstruct;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 14:16:48
 * desc   :  权限初始化器
 * ✅ 系统启动时自动创建权限，防止数据库为空。
 */

// @Component
public class PermissionInitializer {

    private final IPermissionService IPermissionService;

    public PermissionInitializer(IPermissionService IPermissionService) {
        this.IPermissionService = IPermissionService;
    }

    @PostConstruct
    public void init() {
        IPermissionService.getOrCreatePerm("READ_USER");
        IPermissionService.getOrCreatePerm("WRITE_USER");
        IPermissionService.getOrCreatePerm("DELETE_USER");
    }
}