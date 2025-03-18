package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.Permission;

import java.util.List;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:30:01
 * desc   :  权限管理服务
 */

public interface IPermissionService {
    Optional<Permission> findByName(String permissionName);

    List<Permission> findAll();

    Permission save(Permission permission);

    void deleteById(Long id);

    // 新增方法
    Permission getOrCreatePermission(String permissionName);

    Permission getOrCreatePermission(String systemName, String applicationName, String permissionName);
}
