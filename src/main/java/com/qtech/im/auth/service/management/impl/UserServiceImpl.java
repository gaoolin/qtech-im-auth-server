package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.common.PasswordEncryptor;
import com.qtech.im.auth.exception.authentication.InvalidCredentialsException;
import com.qtech.im.auth.exception.biz.BusinessException;
import com.qtech.im.auth.exception.biz.UserNotFoundException;
import com.qtech.im.auth.model.entity.primary.Permission;
import com.qtech.im.auth.model.entity.primary.Role;
import com.qtech.im.auth.model.entity.primary.User;
import com.qtech.im.auth.model.entity.primary.UserPermission;
import com.qtech.im.auth.repository.primary.management.*;
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
    private final DeptRepository deptRepository;
    private final PermRepository permRepository;
    private final UserSysRoleRepository userSysRoleRepository;
    private final UserPermRepository userPermRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, DeptRepository deptRepository, PermRepository permRepository, UserSysRoleRepository userSysRoleRepository, UserPermRepository userPermRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.permRepository = permRepository;
        this.userSysRoleRepository = userSysRoleRepository;
        this.userPermRepository = userPermRepository;
    }

    /**
     * 通过工号查询用户的角色
     */
    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Set<Role> getUserRoles(String empId) {
        return userRepository.findByEmpId(empId).map(User::getRoles).orElse(Collections.emptySet());
    }

    /**
     * 通过工号查询用户的权限
     */
    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Set<Permission> getUserPerms(String empId) {
        return userRepository.findByEmpId(empId).map(user -> user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    /**
     * 为用户添加角色
     */
    @Override
    public void addRoleToUser(String empId, Long roleId) {
        User user = userRepository.findByEmpId(empId).orElseThrow(() -> new BusinessException(404, "用户不存在"));
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
    public void removeRoleFromUser(String empId, Long roleId) {
        userSysRoleRepository.deleteByUserEmpIdAndRoleId(empId, roleId);
    }

    /**
     * 为用户添加权限（额外权限，不通过角色）
     */
    @Override
    public void addPermissionToUser(String empId, Long permId) {
        User user = userRepository.findByEmpId(empId).orElseThrow(() -> new BusinessException(404, "用户不存在"));
        Permission perm = permRepository.findById(permId).orElseThrow(() -> new BusinessException(404, "权限不存在"));

        UserPermission userPerm = new UserPermission();
        userPerm.setUser(user);
        userPerm.setPermission(perm);
        userPermRepository.save(userPerm);
    }

    /**
     * 移除用户的指定权限
     */
    @Override
    public void removePermFromUser(String empId, Long permId) {
        userPermRepository.deleteByUserEmpIdAndPermission(empId, permId);
    }

    /**
     * 删除用户的所有角色
     */
    @Override
    public void removeAllRolesFromUser(String empId) {
        userSysRoleRepository.deleteByUserEmpId(empId);
    }

    /**
     * 删除用户的所有权限
     */
    @Override
    public void removeAllPermsFromUser(String empId) {
        userPermRepository.deleteByUserEmpId(empId);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Optional<User> findUserByEmpId(String empId) {
        return userRepository.findByEmpId(empId);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public List<User> searchUsers(String empId, String username) {
        return userRepository.findByEmpIdOrUsername(empId, username);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmpId(user.getEmpId())) {
            throw new BusinessException(400, "用户名已存在");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User authUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        authUser.setUsername(user.getUsername());

        deptRepository.findByDeptName(user.getDepartment().getDeptName()).ifPresentOrElse(authUser::setDepartment, () -> {
            throw new BusinessException(400, "部门不存在");
        });

        authUser.setGender(user.getGender());
        authUser.setEmail(user.getEmail());
        authUser.setUpdateTime(LocalDateTime.now());
        return userRepository.save(authUser);
    }

    @Override
    public Integer updateUserByEmpId(String empId, User user) {
        return userRepository.updateUserByEmpId(empId, user);
    }

    @Override
    public Integer updateUserRoles(String empId, List<Long> roleIds) {
        return null;
    }

    @Override
    public Integer updateUserPerms(String empId, List<Long> permIds) {
        return null;
    }

    @Override
    public void deleteUserByEmpId(String empId) {
        userRepository.deleteUserByEmpId(empId);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(String empId, String password) {
        if (userRepository.existsByEmpId(empId)) {
            Optional<User> byEmployeeId = userRepository.findByEmpId(empId);
            if (byEmployeeId.isPresent()) {
                User user = byEmployeeId.get();
                return PasswordEncryptor.matches(password, user.getPwHash());
            }
            throw new InvalidCredentialsException();
        }
        throw new UserNotFoundException();
    }

    @Override
    public Page<User> findUsersWithConditions(String empId, String username, Pageable pageable) {
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (empId != null && !empId.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("empId"), "%" + empId + "%"));
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
