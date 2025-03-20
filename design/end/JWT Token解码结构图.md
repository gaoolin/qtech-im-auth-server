✅ 用户 JWT Token 示例（User Token）
```text
{
  "sub": "userA",                // 用户名（Subject）
  "user_id": 101,                // 用户ID
  "roles": ["ADMIN", "USER"],    // 角色列表
  "permissions": ["read", "write", "delete"],  // 权限列表
  "system": "MES",               // 系统标识
  "client_id": "client_a",       // 发起认证的客户端ID
  "iat": 1640000000,             // 签发时间（Issued At）
  "exp": 1640036000              // 过期时间（Expiration）
}
```

✅ 系统调用 JWT Token 示例（Client Token）
```text
{
  "sub": "system_call",          // 固定标识，表示系统间调用
  "client_id": "client_a",       // 客户端ID
  "client_name": "MES系统客户端",  // 客户端名称
  "system": "MES",               // 系统标识
  "iat": 1640000000,             // 签发时间（Issued At）
  "exp": 1640036000              // 过期时间（Expiration）
}
```