package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:27:27
 * desc   :  权限相关操作
 * ✅ 提供 findByName 方法，允许按权限名称查询。
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // @Query(value = "SELECT * FROM permission WHERE permissionName = :name", nativeQuery = true)
    // Optional<Permission> findByPermissionName(@Param("name") String permissionName);

    Optional<Permission> findByPermNameAndSysNameAndAppName(String permName, String sysName, String appName);

    @Query(value = "SELECT * FROM permission WHERE empId = :empId AND sysName = :sysName", nativeQuery = true)
    Optional<List<Permission>> findByEmployeeIdAndSystemName(String empId, String sysName);

    // 根据权限名称查询权限
    Optional<Permission> findByPermName(String permName);

    // 查询所有权限
    List<Permission> findAll();

    // 根据系统名称查询权限
    List<Permission> findBySysName(String systemName);

    // 根据应用名称查询权限
    List<Permission> findByAppName(String applicationName);
}
