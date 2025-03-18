package com.qtech.im.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:29:16
 * desc   :  JWT 认证服务
 */


public interface IJwtService {
    String generateToken(UserDetails userDetails);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}

