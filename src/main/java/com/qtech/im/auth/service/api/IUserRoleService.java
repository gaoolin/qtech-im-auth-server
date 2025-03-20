package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserRole;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 16:15:45
 * desc   :
 */

public interface IUserRoleService {
    // UserRole assignRoleToUser(Integer userId, Integer roleId);
    // void removeUserRole(Integer userId, Integer roleId);
    // List<UserRole> findRolesByUserId(Integer userId);
    // List<UserRole> findUsersByRoleId(Integer roleId);

    UserRole assignRoleToUser(User user, Role role);
    void removeUserRole(User user, Role role);
    List<UserRole> findRolesByUser(User user);
    List<UserRole> findUsersByRole(Role role);
}