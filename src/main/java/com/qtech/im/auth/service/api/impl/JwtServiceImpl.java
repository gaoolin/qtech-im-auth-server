package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.service.api.IJwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 14:34:16
 * desc   :
 * 如果你目前的服务类（IOAuthClientService、IJwtService 等）只是一个具体实现，且不打算扩展出多个实现，那么可以暂时保持为具体类，而不是接口。
 * 但是，如果你预计未来会有更多认证方式（例如，OAuth2、LDAP等）或者多个 JWT 处理方式，或希望将来能方便地替换不同的实现（例如，可能使用不同的 JWT 库），那么将这些类改为接口将有助于增强代码的可维护性和扩展性。
 */

@Service
public class JwtServiceImpl implements IJwtService {
    @Override
    public String generateToken(UserDetails userDetails) {
        // 实现生成Token的逻辑
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        // 实现验证Token的逻辑
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        // 实现解析Token获取用户名的逻辑
        return null;
    }
}

