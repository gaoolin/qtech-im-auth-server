package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 15:44:24
 * desc   :
 */

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUser(User user);

    List<UserRole> findByRole(Role role);

    void deleteByUserAndRole(User user, Role role);

    List<UserRole> findByUserEmployeeId(String employeeId);
    void deleteByUserEmployeeIdAndRoleId(String employeeId, Long roleId);
    void deleteByUserEmployeeId(String employeeId);
}
