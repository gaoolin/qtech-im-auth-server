package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.entity.primary.Permission;

import java.util.List;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:30:01
 * desc   :  权限管理服务
 */

public interface IPermissionService {
    Optional<Permission> findByPermName(String permName);

    List<Permission> findAll();

    Permission save(Permission perm);

    void deleteById(Long id);

    // 新增方法
    Permission getOrCreatePerm(String permName);

    Permission getOrCreatePerm(String sysName, String appName, String permName);

    List<String> getPermsByEmpIdAndSystem(String empId, String sysName);

    Permission createPerm(Permission perm);

    Permission updatePerm(Integer id, Permission perm);

    Permission updatePerm(Long id, Permission perm);

    void deletePerm(Integer id);

    List<Permission> findAllPerms();

    List<Permission> findPermsBySystem(String sysName);

    List<Permission> findPermsByApp(String appName);
}
