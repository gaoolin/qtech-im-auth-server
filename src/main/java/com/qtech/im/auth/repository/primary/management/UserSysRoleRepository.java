package com.qtech.im.auth.repository.primary.management;

import com.qtech.im.auth.model.entity.primary.Role;
import com.qtech.im.auth.model.entity.primary.User;
import com.qtech.im.auth.model.entity.primary.UserSystemRole;
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
public interface UserSysRoleRepository extends JpaRepository<UserSystemRole, Integer> {
    List<UserSystemRole> findByUser(User user);

    List<UserSystemRole> findByRole(Role role);

    void deleteByUserAndRole(User user, Role role);

    List<UserSystemRole> findByUserEmpId(String empId);
    void deleteByUserEmpIdAndRoleId(String empId, Long roleId);
    void deleteByUserEmpId(String empId);
}
