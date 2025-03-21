package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.repository.management.PermissionRepository;
import com.qtech.im.auth.service.management.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 14:10:56
 * desc   :
 * getOrCreatePermission() 自动创建权限，避免手动插入数据库。
 */

@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<Permission> findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getOrCreatePermission(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public Permission getOrCreatePermission(String systemName, String applicationName, String permissionName) {
        // 查询是否已经存在该权限
        Optional<Permission> permission = permissionRepository.findByPermissionNameAndSystemNameAndApplicationName(
                permissionName, systemName, applicationName);
        if (permission.isEmpty()) {
            // 创建新的权限
            Permission newPermission = new Permission();
            newPermission.setSystemName("IM");
            newPermission.setPermissionName("READ"); // 设置权限名称
            newPermission.setDescription("Default description for " + "READ"); // 设置描述
            return permissionRepository.save(newPermission);
        }
        return permission.get();
    }

    @Override
    public List<String> getPermissionsByEmployeeIdAndSystem(String employeeId, String systemName) {
        Optional<List<Permission>> byEmployeeIdAndSystemName = permissionRepository.findByEmployeeIdAndSystemName(employeeId, systemName);
        List<Permission> permissions = byEmployeeIdAndSystemName.orElse(null);
        if (permissions != null) {
            return permissions.stream().map(Permission::getPermissionName).toList();
        }
        return null;
    }


    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Integer id, Permission permissionDetails) {
        return null;
    }

    @Override
    public Permission updatePermission(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setPermissionName(permissionDetails.getPermissionName());
        permission.setDescription(permissionDetails.getDescription());
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Integer id) {

    }

    @Override
    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findPermissionsBySystem(String systemName) {
        return permissionRepository.findBySystemName(systemName);
    }

    @Override
    public List<Permission> findPermissionsByApplication(String applicationName) {
        return permissionRepository.findByApplicationName(applicationName);
    }
}