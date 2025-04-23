## 🛡️ JWT 认证过滤器设计文档（基于路径策略 + 插件式解析器）

### 📌 概述

本设计旨在构建一个 通用化、可扩展、灵活适配多种请求场景 的 JWT 认证系统，支持按照路径匹配策略自动选择不同的 Token 解析器（Header / Cookie / 多源组合等），以适配 API 请求与后台管理系统等多种应用认证需求。

---

### 📚 设计目标

* ✅ 路径级策略匹配（如 /admin/** 使用 Cookie，/api/** 使用 Header）

* ✅ 可扩展 Token 解析策略（支持 Header / Cookie / Param / 组合）

* ✅ 全局默认解析器兜底处理

* ✅ 使用 Spring Security 原生 Filter 接口集成认证逻辑

* ✅ 保持认证中心逻辑独立、可插拔、

---

### 🏗️ 模块结构图

```mermaid
+-----------------------------------------------------------+
|                   PathMatchingJwtFilter                   |
|   （根据请求路径调度对应的 JwtTokenResolver 策略）        |
+----------------------------+------------------------------+
                             |
                             v
            +--------------------------------------+
            |        JwtTokenResolver 接口         |
            |（可插拔：Header / Cookie / 多源组合）|
            +--------------------------------------+
                             |
                             v
                  [ JwtTokenProvider + 安全认证 ]
```

---

### 🧱 核心组件说明

1. JwtTokenResolver 接口

```java
public interface JwtTokenResolver {
    String resolveToken(HttpServletRequest request);
}
```

> 职责：定义统一的 JWT Token 解析接口

2. 内置解析器实现
   HeaderTokenResolver

```java
public class HeaderTokenResolver implements JwtTokenResolver {
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

CookieTokenResolver

```java
public class CookieTokenResolver implements JwtTokenResolver {
    private final String cookieName;

    public CookieTokenResolver(String cookieName) {
        this.cookieName = cookieName;
    }

    public String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
```

CompositeTokenResolver（组合策略）

```java
public class CompositeTokenResolver implements JwtTokenResolver {
    private final List<JwtTokenResolver> resolvers;

    public CompositeTokenResolver(List<JwtTokenResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public String resolveToken(HttpServletRequest request) {
        for (JwtTokenResolver resolver : resolvers) {
            String token = resolver.resolveToken(request);
            if (token != null) return token;
        }
        return null;
    }
}
```

3. PathBasedTokenResolver（路径策略调度器）

```java
public class PathBasedTokenResolver implements JwtTokenResolver {
    private final Map<String, JwtTokenResolver> resolverMap;
    private final JwtTokenResolver defaultResolver;

    public PathBasedTokenResolver(Map<String, JwtTokenResolver> resolverMap,
                                  JwtTokenResolver defaultResolver) {
        this.resolverMap = resolverMap;
        this.defaultResolver = defaultResolver;
    }

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
```

4. PathMatchingJwtFilter（核心认证过滤器）

```java
public class PathMatchingJwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenResolver tokenResolver;

    public PathMatchingJwtFilter(JwtTokenProvider jwtTokenProvider,
                                 JwtTokenResolver tokenResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenResolver = tokenResolver;
    }

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = tokenResolver.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getEmployeeIdFromToken(token);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, null);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

5. Security 配置类示例

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenResolver headerOnly = new HeaderTokenResolver();
        JwtTokenResolver cookieOnly = new CookieTokenResolver("ACCESS_TOKEN");

        JwtTokenResolver globalFallback = new CompositeTokenResolver(List.of(cookieOnly, headerOnly));

        Map<String, JwtTokenResolver> strategyMap = new LinkedHashMap<>();
        strategyMap.put("/admin/.*", cookieOnly);
        strategyMap.put("/api/.*", headerOnly);

        JwtTokenResolver pathResolver = new PathBasedTokenResolver(strategyMap, globalFallback);

        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new PathMatchingJwtFilter(jwtTokenProvider, pathResolver),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

---

### 📋 示例行为

|    请求路径     | Token来源 |           匹配策略            |
|:-----------:|:-------:|:-------------------------:|
| /admin/home | Cookie  |        cookieOnly         |
|  /api/data  | Header  |        headerOnly         |
| /misc/test  |   都有    | 默认组合策略（先 Cookie，后 Header） |

### 🔧 后续扩展建议

* 增加 ParamTokenResolver（支持 ?token=xxx）

* 支持配置文件定义路径规则与解析策略

* 支持缓存已验证 Token 减少频繁解析