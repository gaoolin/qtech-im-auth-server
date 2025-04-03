package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.System;
import com.qtech.im.auth.repository.management.PermissionRepository;
import com.qtech.im.auth.repository.management.SystemRepository;
import com.qtech.im.auth.service.management.IPermissionService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final SystemRepository systemRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, SystemRepository systemRepository) {
        this.permissionRepository = permissionRepository;
        this.systemRepository = systemRepository;
    }

    @Override
    public Optional<Permission> findByPermName(String permName) {
        return permissionRepository.findByPermName(permName);
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
    public Permission getOrCreatePerm(String permName) {
        return permissionRepository.findByPermName(permName).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public Permission getOrCreatePerm(String sysName, String appName, String permName) {
        // 参数校验
        if (sysName == null || sysName.isEmpty() ||
                appName == null || appName.isEmpty() ||
                permName == null || permName.isEmpty()) {
            throw new IllegalArgumentException("System name, application name, and permission name cannot be null or empty.");
        }

        // 查询是否已经存在该权限
        System system = new System();
        system.setSysName(sysName);
        Optional<Permission> perm = permissionRepository.findByPermNameAndSystemAndAppName(permName, system, appName);
        if (perm.isPresent()) {
            return perm.get();
        }

        // 创建新的权限
        Permission newPermission = createNewPermission(sysName, appName, permName);

        try {
            // 尝试保存新权限
            return permissionRepository.save(newPermission);
        } catch (Exception e) {
            // 如果保存失败，可能是并发创建导致的唯一约束冲突，重新查询
            log.warn("Failed to save permission, retrying...", e);
            return permissionRepository.findByPermNameAndSystemAndAppName(permName, system, appName)
                    .orElseThrow(() -> new RuntimeException("Permission creation failed after retry.", e));
        }
    }

    // 提取创建权限的逻辑
    private Permission createNewPermission(String sysName, String appName, String permName) {
        Permission newPermission = new Permission();
        newPermission.setSystem(findOrCreateSystem(sysName)); // 动态设置系统对象
        newPermission.setPermName(permName); // 动态设置权限名称
        newPermission.setRemark(generateDescription(permName)); // 动态生成描述
        return newPermission;
    }

    // 提取生成描述的逻辑
    private String generateDescription(String permName) {
        return "Default description for " + permName;
    }

    // 查找或创建系统对象
    private System findOrCreateSystem(String sysName) {
        // 假设有一个 SystemRepository 用于查找和创建 System 对象
        Optional<System> systemOptional = systemRepository.findBySysName(sysName);
        return systemOptional.orElseGet(() -> {
            System newSystem = new System();
            newSystem.setSysName(sysName);
            return systemRepository.save(newSystem);
        });
    }

    @Override
    public List<String> getPermsByEmpIdAndSystem(String empId, String sysName) {
        System system = new System();
        system.setSysName(sysName);
        Optional<List<Permission>> byEmployeeIdAndSystemName = permissionRepository.findByEmpIdAndSystem(empId, system);
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
        permDb.setRemark(perm.getRemark());
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
    public List<Permission> findPermsBySystem(String sysName) {
        System system = new System();
        system.setSysName(sysName);
        return permissionRepository.findBySystem(system);
    }

    @Override
    public List<Permission> findPermsByApp(String appName) {
        return permissionRepository.findByAppName(appName);
    }
}