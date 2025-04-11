package com.qtech.im.auth.repository.primary.management;

import com.qtech.im.auth.model.primary.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:26:22
 * desc   :  角色数据访问
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    // 根据角色名称查询角色
    Role findByRoleName(String roleName);
}
