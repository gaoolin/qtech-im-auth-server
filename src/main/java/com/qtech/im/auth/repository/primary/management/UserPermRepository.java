package com.qtech.im.auth.repository.primary.management;

import com.qtech.im.auth.model.entity.primary.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 15:44:49
 * desc   :
 */

@Repository
public interface UserPermRepository extends JpaRepository<UserPermission, Long> {
    @Query("DELETE FROM UserPermission up WHERE up.user.empId = :empId AND up.permission.id = :permId")
    void deleteByUserEmpIdAndPermission(String empId, Long permId);

    void deleteByUserEmpId(String empId);
}

