package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.primary.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    Role getOrCreateRole(String roleName);

    Page<Role> findRolesWithConditions(String keyword, PageRequest pageable);

    Page<Role> findAll(PageRequest pageable);
}
