package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.primary.Permission;
import com.qtech.im.auth.model.primary.Role;
import com.qtech.im.auth.model.primary.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
    Set<Role> getUserRoles(String empId);

    Set<Permission> getUserPerms(String empId);

    void addRoleToUser(String empId, Long roleId);

    void removeRoleFromUser(String empId, Long roleId);

    void addPermissionToUser(String empId, Long permId);

    void removePermFromUser(String empId, Long permId);

    void removeAllRolesFromUser(String empId);

    void removeAllPermsFromUser(String empId);

    Optional<User> findUserByEmpId(String empId);

    Optional<User> findUserByUsername(String username);

    @Transactional(readOnly = true) // 只读事务，优化查询性能
    List<User> searchUsers(String empId, String username);

    User createUser(User user);

    List<User> findAllUsers();

    User updateUser(Long id, User userDetails);

    Integer updateUserByEmpId(String empId, User user);

    void deleteUserByEmpId(String empId);

    void deleteUser(Long id);

    Integer updateUserRoles(String empId, List<Long> roleIds);

    Integer updateUserPerms(String empId, List<Long> permIds);

    boolean authenticate(String empId, String password);

    Page<User> findUsersWithConditions(String empId, String username, Pageable pageable);

    Page<User> findAll(Pageable pageable);
}
