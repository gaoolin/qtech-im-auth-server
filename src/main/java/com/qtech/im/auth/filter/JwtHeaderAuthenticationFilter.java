package com.qtech.im.auth.filter;

import com.qtech.im.auth.common.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 14:13:38
 * desc   :  用于 API 请求，解析请求头中的 Bearer Token
 */
public class JwtHeaderAuthenticationFilter extends AbstractJwtAuthenticationFilter {

    public JwtHeaderAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        super(jwtTokenProvider);
    }

    @Override
    protected String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

