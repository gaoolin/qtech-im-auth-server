package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserRole;
import com.qtech.im.auth.repository.management.UserRoleRepository;
import com.qtech.im.auth.service.management.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 16:16:07
 * desc   :
 */

@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole assignRoleToUser(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

    @Override
    public void removeUserRole(User user, Role role) {
        userRoleRepository.deleteByUserAndRole(user, role);
    }

    @Override
    public List<UserRole> findRolesByUser(User user) {
        return userRoleRepository.findByUser(user);
    }

    @Override
    public List<UserRole> findUsersByRole(Role role) {
        return userRoleRepository.findByRole(role);
    }
}