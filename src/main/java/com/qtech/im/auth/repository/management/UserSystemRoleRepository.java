package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserSystemRole;
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
public interface UserSystemRoleRepository extends JpaRepository<UserSystemRole, Integer> {
    List<UserSystemRole> findByUser(User user);

    List<UserSystemRole> findByRole(Role role);

    void deleteByUserAndRole(User user, Role role);

    List<UserSystemRole> findByUserEmpId(String empId);
    void deleteByUserEmpIdAndRoleId(String empId, Long roleId);
    void deleteByUserEmpId(String empId);
}
