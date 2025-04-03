package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserSystemRole;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 16:15:45
 * desc   :
 */

public interface IUserRoleService {
    // UserSystemRole assignRoleToUser(Integer userId, Integer roleId);
    // void removeUserRole(Integer userId, Integer roleId);
    // List<UserSystemRole> findRolesByUserId(Integer userId);
    // List<UserSystemRole> findUsersByRoleId(Integer roleId);

    UserSystemRole assignRoleToUser(User user, Role role);
    void removeUserRole(User user, Role role);
    List<UserSystemRole> findRolesByUser(User user);
    List<UserSystemRole> findUsersByRole(Role role);
}