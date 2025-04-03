package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.RoleSystemPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 16:39:46
 * desc   :
 */

@Repository
public interface RoleSystemPermissionRepository extends JpaRepository<RoleSystemPermission, Integer> {
    void deleteByRoleIdAndPermissionId(Role role, Permission permission);

    List<RoleSystemPermission> findByRoleId(Role role);

    List<RoleSystemPermission> findByPermissionId(Permission permission);
}
