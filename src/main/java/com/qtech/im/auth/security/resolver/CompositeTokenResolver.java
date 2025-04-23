package com.qtech.im.auth.security.resolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 15:03:10
 * desc   :  组合解析器：支持多个 Resolver 顺序尝试
 */
public class CompositeTokenResolver implements JwtTokenResolver {
    private final List<JwtTokenResolver> resolvers;

    public CompositeTokenResolver(List<JwtTokenResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        for (JwtTokenResolver resolver : resolvers) {
            String token = resolver.resolveToken(request);
            if (token != null) return token;
        }
        return null;
    }
}
