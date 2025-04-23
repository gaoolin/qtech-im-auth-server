package com.qtech.im.auth.security.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 15:01:29
 * desc   :  Header解析
 */

public class HeaderTokenResolver implements JwtTokenResolver {
    @Override
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
