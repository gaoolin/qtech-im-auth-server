package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.RolePermission;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 14:43:36
 * desc   :
 */

public interface IRolePermissionService {
    RolePermission assignPermissionToRole(Role role, Permission permission);

    void removeRolePermission(Role role, Permission permission);

    List<RolePermission> findPermissionsByRoleId(Role role);

    List<RolePermission> findRolesByPermissionId(Permission permission);
}
