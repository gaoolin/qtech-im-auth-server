package com.qtech.im.auth.security.resolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 15:04:36
 * desc   :  路径匹配策略调度器
 */
public class PathBasedTokenResolver implements JwtTokenResolver {

    private final Map<String, JwtTokenResolver> resolverMap;
    private final JwtTokenResolver defaultResolver;

    public PathBasedTokenResolver(Map<String, JwtTokenResolver> resolverMap, JwtTokenResolver defaultResolver) {
        this.resolverMap = resolverMap;
        this.defaultResolver = defaultResolver;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (Map.Entry<String, JwtTokenResolver> entry : resolverMap.entrySet()) {
            if (path.matches(entry.getKey())) {
                return entry.getValue().resolveToken(request);
            }
        }
        return defaultResolver.resolveToken(request);
    }
}

