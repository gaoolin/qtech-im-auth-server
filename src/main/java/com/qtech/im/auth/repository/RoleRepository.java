package com.qtech.im.auth.repository;

import com.qtech.im.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:26:22
 * desc   :  角色数据访问
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 根据角色名称查询角色
    Role findByRoleName(String roleName);

    // 根据角色名称模糊查询角色
    List<Role> findByRoleNameContaining(String roleName);

    // 查询所有角色
    List<Role> findAll();
}
