package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.model.User;
import com.qtech.im.auth.repository.management.UserRepository;
import com.qtech.im.auth.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 13:33:04
 * desc   :  提供给第三方的应用、系统、服务使用
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService, com.qtech.im.auth.service.api.IUserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，优化查询性能
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(CustomUserDetails::fromUser).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmployeeId(String employeeId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmployeeId(employeeId);
        return user.map(CustomUserDetails::fromUser).orElseThrow(() -> new UsernameNotFoundException("User not found: " + employeeId));
    }
}