package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.primary.Role;
import com.qtech.im.auth.model.primary.User;
import com.qtech.im.auth.model.primary.UserSystemRole;
import com.qtech.im.auth.repository.primary.management.UserSysRoleRepository;
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
    private UserSysRoleRepository userSysRoleRepository;

    @Override
    public UserSystemRole assignRoleToUser(User user, Role role) {
        UserSystemRole userSystemRole = new UserSystemRole();
        userSystemRole.setUser(user);
        userSystemRole.setRole(role);
        return userSysRoleRepository.save(userSystemRole);
    }

    @Override
    public void removeUserRole(User user, Role role) {
        userSysRoleRepository.deleteByUserAndRole(user, role);
    }

    @Override
    public List<UserSystemRole> findRolesByUser(User user) {
        return userSysRoleRepository.findByUser(user);
    }

    @Override
    public List<UserSystemRole> findUsersByRole(Role role) {
        return userSysRoleRepository.findByRole(role);
    }
}