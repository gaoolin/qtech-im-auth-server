package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.RolePermission;
import com.qtech.im.auth.repository.RolePermissionRepository;
import com.qtech.im.auth.service.api.IRolePermissionService;
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
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public RolePermission assignPermissionToRole(Role role, Permission permission) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public void removeRolePermission(Role role, Permission permission) {
        rolePermissionRepository.deleteByRoleIdAndPermissionId(role, permission);
    }

    @Override
    public List<RolePermission> findPermissionsByRoleId(Role role) {
        return rolePermissionRepository.findByRoleId(role);
    }

    @Override
    public List<RolePermission> findRolesByPermissionId(Permission permission) {
        return rolePermissionRepository.findByPermissionId(permission);
    }
}
