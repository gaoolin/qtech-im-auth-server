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
    public Optional<Permission> findByPermName(String permissionName) {
        return permissionRepository.findByPermName(permissionName);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission save(Permission perm) {
        return permissionRepository.save(perm);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getOrCreatePerm(String permissionName) {
        return permissionRepository.findByPermName(permissionName).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public Permission getOrCreatePerm(String systemName, String applicationName, String permissionName) {
        // 查询是否已经存在该权限
        Optional<Permission> perm = permissionRepository.findByPermNameAndSysNameAndAppName(
                permissionName, systemName, applicationName);
        if (perm.isEmpty()) {
            // 创建新的权限
            Permission newPermission = new Permission();
            newPermission.setSysName("IM");
            newPermission.setPermName("READ"); // 设置权限名称
            newPermission.setDescription("Default description for " + "READ"); // 设置描述
            return permissionRepository.save(newPermission);
        }
        return perm.get();
    }

    @Override
    public List<String> getPermsByEmpIdAndSystem(String employeeId, String systemName) {
        Optional<List<Permission>> byEmployeeIdAndSystemName = permissionRepository.findByEmployeeIdAndSystemName(employeeId, systemName);
        List<Permission> permissions = byEmployeeIdAndSystemName.orElse(null);
        if (permissions != null) {
            return permissions.stream().map(Permission::getPermName).toList();
        }
        return null;
    }


    @Override
    public Permission createPerm(Permission perm) {
        return permissionRepository.save(perm);
    }

    @Override
    public Permission updatePerm(Integer id, Permission perm) {
        return null;
    }

    @Override
    public Permission updatePerm(Long id, Permission perm) {
        Permission permDb = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permDb.setPermName(perm.getPermName());
        permDb.setDescription(perm.getDescription());
        return permissionRepository.save(perm);
    }

    @Override
    public void deletePerm(Integer id) {

    }

    @Override
    public List<Permission> findAllPerms() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findPermsBySystem(String systemName) {
        return permissionRepository.findBySysName(systemName);
    }

    @Override
    public List<Permission> findPermsByApp(String applicationName) {
        return permissionRepository.findByAppName(applicationName);
    }
}