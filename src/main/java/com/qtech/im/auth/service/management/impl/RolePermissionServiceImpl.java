package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.RoleSystemPermission;
import com.qtech.im.auth.repository.management.RoleSystemPermissionRepository;
import com.qtech.im.auth.service.management.IRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 14:52:35
 * desc   :
 */

@Service
public class RolePermissionServiceImpl implements IRolePermissionService {

    @Autowired
    private RoleSystemPermissionRepository roleSystemPermissionRepository;

    @Override
    public RoleSystemPermission assignPermissionToRole(Role role, Permission permission) {
        RoleSystemPermission roleSystemPermission = new RoleSystemPermission();
        roleSystemPermission.setRole(role);
        roleSystemPermission.setPermission(permission);
        return roleSystemPermissionRepository.save(roleSystemPermission);
    }

    @Override
    public void removeRolePermission(Role role, Permission permission) {
        roleSystemPermissionRepository.deleteByRoleIdAndPermissionId(role, permission);
    }

    @Override
    public List<RoleSystemPermission> findPermissionsByRoleId(Role role) {
        return roleSystemPermissionRepository.findByRoleId(role);
    }

    @Override
    public List<RoleSystemPermission> findRolesByPermissionId(Permission permission) {
        return roleSystemPermissionRepository.findByPermissionId(permission);
    }
}
