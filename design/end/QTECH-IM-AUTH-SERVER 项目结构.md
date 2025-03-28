### QTECH-IM-AUTH-SERVER 项目结构
```plaintext
QTECH-IM-AUTH-SERVER
├── pom.xml                           # Maven 配置文件
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.qtech.im.auth     # 主要 Java 代码包
│   │   │       ├── config            # Spring Security 相关配置
│   │   │       │   ├── SecurityConfig.java         # Spring Security 配置类
│   │   │       │   ├── JwtAuthenticationFilter.java # JWT 过滤器
│   │   │       │   ├── JwtUtils.java               # JWT 生成与解析工具类
│   │   │       │   ├── CustomAuthenticationProvider.java # 自定义认证逻辑
│   │   │       │   ├── PasswordConfig.java         # 密码加密与验证配置
│   │   │       ├── controller
│   │   │       │   ├── AuthController.java         # 登录、注册、刷新 Token
│   │   │       │   ├── UserController.java        # 用户管理接口
│   │   │       ├── dto
│   │   │       │   ├── LoginRequest.java       # 登录请求数据模型
│   │   │       │   ├── AuthResponse.java       # 认证响应数据模型
│   │   │       │   ├── SignupRequest.java      # 注册请求数据模型
│   │   │       │   ├── UserDto.java            # 用户数据传输模型
│   │   │       ├── entity
│   │   │       │   ├── User.java               # 用户实体
│   │   │       │   ├── Role.java               # 角色实体
│   │   │       │   ├── Permission.java         # 权限实体
│   │   │       ├── repository
│   │   │       │   ├── UserRepository.java     # 用户数据访问层
│   │   │       │   ├── RoleRepository.java     # 角色数据访问
│   │   │       │   ├── PermissionRepository.java # 权限相关操作
│   │   │       ├── service
│   │   │       │   ├── AuthService.java        # 认证服务
│   │   │       │   ├── UserService.java        # 用户管理服务
│   │   │       │   ├── JwtService.java         # JWT 认证服务
│   │   │       │   ├── RoleService.java        # 角色管理服务
│   │   │       │   ├── PermissionService.java  # 权限管理服务
│   │   │       │   ├── TokenBlacklistService.java # 存储和验证黑名单 Token
│   │   │       │   ├── TwoFactorAuthService.java # 2FA 验证服务
│   │   │       ├── exception
│   │   │       │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │   │       ├── utils
│   │   │       │   ├── JwtUtils.java            # JWT 令牌管理工具
│   │   │       │   ├── PasswordEncoderUtil.java # 密码加密工具类
│   │   │       └── QTECHAuthServerApplication.java # 启动类
│   ├── test
│   │   ├── java
│   │   │   └── com.qtech.auth
│   │       ├── AuthServiceTest.java  # 认证服务测试
│   │       ├── UserServiceTest.java  # 用户管理测试
│   │       ├── JwtServiceTest.java    # JWT 相关测试
│   └── resources
│       ├── application.yml            # Spring 配置文件
│       ├── data-bak.sql                    # 初始化 SQL 脚本
│       ├── schema-bak.sql                   # 数据库表结构
│       ├── static/                     # 静态资源（前端静态文件）
│       ├── templates                   # Thymeleaf 模板
│       └── banner.txt                  # 启动页配置
└── .gitignore

```
### 功能模块划分
#### 你的系统可以分为以下主要功能模块：

1. 用户管理（User Management）
用户注册
账号、密码、邮箱/手机号、基本信息
密码加密存储（BCrypt）
用户认证
登录/注销（基于 Spring Security + JWT）
记住我功能
用户信息管理
修改基本信息（用户名、邮箱等）
修改密码
重置密码（基于邮箱、短信）
2. 认证与授权（Authentication & Authorization）
身份认证（Authentication）
支持 JWT 认证
支持 Refresh Token 机制
支持登录失败限制（防止暴力破解）
授权（Authorization）
基于 RBAC（角色访问控制）实现权限管理
动态权限控制（Spring Security + JWT）
自定义 UserDetailsService 和 AuthenticationProvider
单点登录（SSO）
支持 OAuth 2.1 / OpenID Connect
适配 Spring Security OIDC 客户端
API 认证和授权
设计 API Gateway 认证拦截逻辑
处理内部系统 API 访问权限
2. 角色和权限管理（Role & Permission Management）
角色管理
角色的创建、查询、修改、删除
给用户分配多个角色
权限管理
角色和权限的绑定
细粒度 API 权限控制（基于权限的访问控制）
访问控制策略（RBAC）
使用 @PreAuthorize 和 @PostAuthorize 进行方法级别的访问控制
3. 认证方式（Authentication Methods）
基于用户名 + 密码
JWT 认证
生成 Token、解析 Token、刷新 Token
支持 SSO（单点登录）
可扩展支持 OAuth 2.1、OIDC
双因素认证（2FA） (可选)
结合短信或邮件验证码进行双因子认证
4. 认证网关（API Gateway）
基于 Spring Cloud Gateway
JWT 验证拦截器
API 权限管理
微服务间通信认证（Service-to-Service Authentication）
跨域 CORS 处理
4. 数据存储（Database & Persistence Layer）
数据库：Oracle 19
用户、角色、权限的 ER 设计
Spring Data JPA 访问数据库
数据库初始化 SQL（data-bak.sql）
5. 安全性增强（Security Enhancements）
密码加密存储（BCrypt）
多因子认证（MFA，可选）
账户锁定策略
密码找回与重置
6. 配置管理
application.yml
配置 JWT 秘钥、过期时间
数据库连接
Spring Security 配置
跨域配置
7. 部署
支持 Docker 部署
支持 Kubernetes 部署
支持 Ingress 反向代理
集成 Oracle 数据库
自动化构建与 CI/CD
