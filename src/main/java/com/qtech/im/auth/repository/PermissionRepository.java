package com.qtech.im.auth.repository;

import com.qtech.im.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    @Query(value = "SELECT * FROM permission WHERE permissionName = :name", nativeQuery = true)
    Optional<Permission> findByName(@Param("name") String permissionName);

    Optional<Permission> findByPermissionNameAndSystemNameAndApplicationName(String permissionName, String systemName, String applicationName);
}
