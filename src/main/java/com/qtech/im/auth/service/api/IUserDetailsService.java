package com.qtech.im.auth.service.api;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 10:01:00
 * desc   :
 */


public interface IUserDetailsService {
    @Transactional(readOnly = true)
    UserDetails loadUserByEmployeeId(String employeeId) throws UsernameNotFoundException;
}
