package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 15:44:49
 * desc   :
 */

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUserEmployeeId(String employeeId);

    void deleteByUserEmployeeIdAndPermissionId(String employeeId, Long permissionId);

    void deleteByUserEmployeeId(String employeeId);
}

