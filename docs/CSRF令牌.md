# CSRF 令牌

## 什么是 CSRF？

**CSRF（Cross-Site Request Forgery，跨站请求伪造）** 是一种攻击方式，攻击者通过诱导用户在已登录的网站上执行非预期的操作。攻击者利用用户在目标网站上的已认证状态，构造恶意请求，使用户在不知情的情况下执行某些操作，如转账、修改信息等。

## CSRF 令牌的作用

**CSRF 令牌** 是一种防止 CSRF 攻击的安全机制。其主要作用是确保请求是由合法的用户发起的，而不是由攻击者构造的恶意请求。

### 具体作用

1. **验证请求来源**：通过在每个请求中包含一个唯一的、随机生成的令牌，服务器可以验证请求是否来自合法的表单提交。
2. **防止恶意请求**：即使攻击者能够构造请求，没有有效的 CSRF 令牌，请求也会被服务器拒绝。

## CSRF 令牌的用途

1. **表单提交**：在表单中包含 CSRF 令牌，确保表单提交的安全性。
2. **AJAX 请求**：在 AJAX 请求的头部包含 CSRF 令牌，确保 AJAX 请求的安全性。
3. **敏感操作**：在涉及敏感操作的请求中使用 CSRF 令牌，如转账、修改密码等。

## CSRF 令牌的利弊

### 利点

1. **增强安全性**：有效防止 CSRF 攻击，保护用户数据和操作安全。
2. **简单易用**：实现简单，只需在表单和请求中包含令牌即可。
3. **广泛支持**：大多数 Web 框架和库都支持 CSRF 令牌机制。

### 弊端

1. **复杂性增加**：需要在每个表单和请求中包含令牌，增加了开发和维护的复杂性。
2. **用户体验**：某些情况下，可能会影响用户体验，尤其是在 AJAX 请求中需要手动处理令牌。
3. **存储问题**：令牌需要安全存储和传输，否则可能成为新的攻击目标。

## CSRF 令牌的使用场景

1. **表单提交**：在所有表单中包含 CSRF 令牌，确保表单提交的安全性。
2. **AJAX 请求**：在所有涉及敏感操作的 AJAX 请求中包含 CSRF 令牌。
3. **敏感操作**：在涉及敏感操作的请求中使用 CSRF 令牌，如转账、修改密码等。
4. **多用户环境**：在多用户环境下，确保每个用户的请求都是合法的。

## CSRF 令牌与 JWT 的关系

**JWT（JSON Web Token）** 是一种开放标准（RFC 7519），用于在网络应用环境间安全地将信息作为 JSON 对象传输。JWT 通常用于身份验证和信息交换。

### JWT 的特点

1. **无状态**：JWT 不需要在服务器端存储会话信息，所有信息都包含在令牌中。
2. **安全性**：通过签名和加密，确保令牌的安全性和完整性。
3. **灵活性**：可以包含任意信息，如用户 ID、权限等。

### JWT 与 CSRF 的关系

1. **无状态特性**：由于 JWT 是无状态的，每个请求都需要包含 JWT 令牌，这在一定程度上减少了 CSRF 攻击的风险，因为攻击者需要获取用户的 JWT 令牌才能构造有效的请求。
2. **独立性**：JWT 和 CSRF 令牌可以独立使用，也可以结合使用。例如，可以在 JWT 中包含 CSRF 令牌，或者在每个请求中同时包含 JWT 和 CSRF 令牌。
3. **安全性增强**：结合使用 JWT 和 CSRF 令牌可以进一步增强安全性，确保请求的合法性和安全性。

### 示例：结合使用 JWT 和 CSRF 令牌

#### 后端配置

```
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login", "/auth/logout", "/").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

---

### 示例 1：前端表单

```html
<form action="/submit" method="post">
    <input type="hidden" name="_csrf" value="abc123">
    <button type="submit">提交</button>
</form>
```


- **解释**：`_csrf` 是服务器生成的令牌，表单提交时会携带该令牌，服务器验证后才处理请求。

---

### 示例 2：前端 AJAX 请求

```javascript
fetch('/api/endpoint', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'X-CSRF-Token': 'abc123'
    },
    body: JSON.stringify({ key: 'value' })
});
```


- **解释**：`X-CSRF-Token` 是通过 HTTP Header 发送的 CSRF 令牌，服务器会验证该令牌是否有效。

---

### 示例 3：JWT 和 CSRF 结合使用

```html
<meta name="_csrf" content="abc123">
<meta name="_csrf_header" content="X-CSRF-Token">
<!--
或以下格式：
<meta name="csrf-token" content="{{ csrf_token() }}"/>
-->
<script>
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/api/endpoint', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer jwt-token',
            [csrfHeader]: csrfToken
        }
    });
</script>
```


- **解释**：
  - JWT 用于身份验证。
  - CSRF 令牌用于防止跨站请求伪造。
  - 两者结合使用可以增强安全性。

---
