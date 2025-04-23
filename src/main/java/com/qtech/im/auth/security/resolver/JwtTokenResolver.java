package com.qtech.im.auth.security.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 15:00:28
 * desc   :  统一接口
 */

public interface JwtTokenResolver {
    String resolveToken(HttpServletRequest request);
}
