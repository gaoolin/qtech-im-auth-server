package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.Role;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:29:36
 * desc   :  角色管理服务
 */


public interface IRoleService {
    Role createRole(Role role);

    Role updateRole(Long id, Role roleDetails);

    void deleteRole(Long id);

    List<Role> findAllRoles();

    Role findRoleByRoleName(String roleName);
}
