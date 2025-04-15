package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.entity.primary.Permission;
import com.qtech.im.auth.model.entity.primary.Role;
import com.qtech.im.auth.model.entity.primary.RoleSystemPermission;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 14:43:36
 * desc   :
 */

public interface IRolePermissionService {
    RoleSystemPermission assignPermissionToRole(Role role, Permission permission);

    void removeRolePermission(Role role, Permission permission);

    List<RoleSystemPermission> findPermissionsByRoleId(Role role);

    List<RoleSystemPermission> findRolesByPermissionId(Permission permission);
}
