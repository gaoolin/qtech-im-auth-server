package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.common.PasswordEncryptor;
import com.qtech.im.auth.exception.authentication.InvalidCredentialsException;
import com.qtech.im.auth.exception.biz.BusinessException;
import com.qtech.im.auth.exception.biz.UserNotFoundException;
import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserPermission;
import com.qtech.im.auth.repository.management.*;
import com.qtech.im.auth.service.management.IUserService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 10:40:53
 * desc   :  对应系统重的用户
 */

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, UserRoleRepository userRoleRepository, UserPermissionRepository userPermissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPermissionRepository = userPermissionRepository;
    }

    /**
     * 通过工号查询用户的角色
     */
    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Set<Role> getUserRoles(String employeeId) {
        return userRepository.findByEmployeeId(employeeId).map(User::getRoles).orElse(Collections.emptySet());
    }

    /**
     * 通过工号查询用户的权限
     */
    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Set<Permission> getUserPermissions(String employeeId) {
        return userRepository.findByEmployeeId(employeeId).map(user -> user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    /**
     * 为用户添加角色
     */
    @Override
    public void addRoleToUser(String employeeId, Long roleId) {
        User user = userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new BusinessException(404, "用户不存在"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new BusinessException(404, "角色不存在"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    /**
     * 移除用户的指定角色
     */
    @Override
    public void removeRoleFromUser(String employeeId, Long roleId) {
        userRoleRepository.deleteByUserEmployeeIdAndRoleId(employeeId, roleId);
    }

    /**
     * 为用户添加权限（额外权限，不通过角色）
     */
    @Override
    public void addPermissionToUser(String employeeId, Long permissionId) {
        User user = userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new BusinessException(404, "用户不存在"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new BusinessException(404, "权限不存在"));

        UserPermission userPermission = new UserPermission();
        userPermission.setUser(user);
        userPermission.setPermission(permission);
        userPermissionRepository.save(userPermission);
    }

    /**
     * 移除用户的指定权限
     */
    @Override
    public void removePermissionFromUser(String employeeId, Long permissionId) {
        userPermissionRepository.deleteByUserEmployeeIdAndPermissionId(employeeId, permissionId);
    }

    /**
     * 删除用户的所有角色
     */
    @Override
    public void removeAllRolesFromUser(String employeeId) {
        userRoleRepository.deleteByUserEmployeeId(employeeId);
    }

    /**
     * 删除用户的所有权限
     */
    @Override
    public void removeAllPermissionsFromUser(String employeeId) {
        userPermissionRepository.deleteByUserEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Optional<User> findUserByEmployeeId(String employeeId) {
        return userRepository.findByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public List<User> findUsersBySection(String section) {
        return userRepository.findBySection(section);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public List<User> searchUsers(String employeeId, String username, String section) {
        return userRepository.findByEmployeeIdOrUsernameOrSection(employeeId, username, section);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmployeeId(user.getEmployeeId())) {
            throw new BusinessException(400, "用户名已存在");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User authUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        authUser.setUsername(user.getUsername());
        authUser.setDepartment(user.getDepartment());
        authUser.setGender(user.getGender());
        authUser.setEmail(user.getEmail());
        authUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(authUser);
    }

    @Override
    public Integer updateUserByEmployeeId(String employeeId, User user) {
        return userRepository.updateUserByEmployeeId(employeeId, user);
    }

    @Override
    public Integer updateUserRoles(String employeeId, List<Long> roleIds) {
        return null;
    }

    @Override
    public Integer updateUserPermissions(String employeeId, List<Long> permissionIds) {
        return null;
    }

    @Override
    public void deleteUserByEmployeeId(String employeeId) {
        userRepository.deleteUserByEmployeeId(employeeId);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(String employeeId, String password) {
        if (userRepository.existsByEmployeeId(employeeId)) {
            Optional<User> byEmployeeId = userRepository.findByEmployeeId(employeeId);
            if (byEmployeeId.isPresent()) {
                User user = byEmployeeId.get();
                return PasswordEncryptor.matches(password, user.getPasswordHash());
            }
            throw new InvalidCredentialsException();
        }
        throw new UserNotFoundException();
    }

    @Override
    public Page<User> findUsersWithConditions(String employeeId, String username, Pageable pageable) {
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (employeeId != null && !employeeId.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("employeeId"), "%" + employeeId + "%"));
            }
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
