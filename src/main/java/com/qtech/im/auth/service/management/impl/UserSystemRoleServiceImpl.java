package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.model.UserSystemRole;
import com.qtech.im.auth.repository.management.UserSystemRoleRepository;
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
public class UserSystemRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserSystemRoleRepository userSystemRoleRepository;

    @Override
    public UserSystemRole assignRoleToUser(User user, Role role) {
        UserSystemRole userSystemRole = new UserSystemRole();
        userSystemRole.setUser(user);
        userSystemRole.setRole(role);
        return userSystemRoleRepository.save(userSystemRole);
    }

    @Override
    public void removeUserRole(User user, Role role) {
        userSystemRoleRepository.deleteByUserAndRole(user, role);
    }

    @Override
    public List<UserSystemRole> findRolesByUser(User user) {
        return userSystemRoleRepository.findByUser(user);
    }

    @Override
    public List<UserSystemRole> findUsersByRole(Role role) {
        return userSystemRoleRepository.findByRole(role);
    }
}