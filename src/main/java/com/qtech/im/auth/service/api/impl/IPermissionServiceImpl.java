package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.repository.PermissionRepository;
import com.qtech.im.auth.service.api.IPermissionService;
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
public class IPermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public IPermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<Permission> findByName(String permissionName) {
        return permissionRepository.findByName(permissionName);
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
        return permissionRepository.findByName(permissionName).orElseGet(() -> {
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

}