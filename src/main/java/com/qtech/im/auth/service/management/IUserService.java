package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:28:54
 * desc   :  用户管理服务
 */


public interface IUserService {
    Set<Role> getUserRoles(String employeeId);

    Set<Permission> getUserPermissions(String employeeId);

    void addRoleToUser(String employeeId, Long roleId);

    void removeRoleFromUser(String employeeId, Long roleId);

    void addPermissionToUser(String employeeId, Long permissionId);

    void removePermissionFromUser(String employeeId, Long permissionId);

    void removeAllRolesFromUser(String employeeId);

    void removeAllPermissionsFromUser(String employeeId);

    Optional<User> findUserByEmployeeId(String employeeId);

    Optional<User> findUserByUsername(String username);

    List<User> findUsersBySection(String section);

    List<User> searchUsers(String employeeId, String username, String section);

    User createUser(User user);

    List<User> findAllUsers();

    User updateUser(Long id, User userDetails);

    void deleteUserByEmployeeId(String employeeId);

    void deleteUser(Long id);

    Integer updateUserRoles(String employeeId, List<Long> roleIds);

    Integer updateUserPermissions(String employeeId, List<Long> permissionIds);

    boolean authenticate(String employeeId, String password);
}
