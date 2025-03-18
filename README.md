# QTECH-IM-AUTH-SERVER

企业级智能制造认证中心服务端项目

## 📁 项目结构
```text
com.qtech.im.auth
│
├─config              # 配置类
├─controller          # 接口层
├─dto                 # 数据传输对象
├─entity              # 实体类
├─exception           # 异常处理
├─model               # 业务模型
├─repository          # 持久层接口
├─security            # 认证与授权安全逻辑
├─service             # 服务接口层
├─service.impl        # 服务实现层
└─utils               # 工具类
```

## ✅ 功能模块
- 用户管理（注册、登录、登出、查询）
- 角色管理
- 权限管理
- 角色与权限关联
- 用户与角色关联
- API Key 生成与管理
- OAuth2 支持
- JWT Token 认证
- 审计日志记录
- 单点登录 (SSO) 预留

## ✅ 技术栈
| 技术       | 版本              |
|------------|-------------------|
| Spring Boot| 3.3.7             |
| Java       | 17                |
| Oracle     | 19c               |
| Redis      | 6.x               |
| JJWT       | 最新稳定版本      |
| Spring Security | 最新稳定版本 |

## ✅ 运行方式
clone 本项目

配置 application.yml 数据源及 Redis

初始化数据库及表结构（参考 design 文件夹）

执行项目主类运行
## ✅ 接口文档
- 已集成 Swagger (建议使用 springdoc-openapi)
- 访问地址：http://localhost:8080/swagger-ui.html

## ✅ 日志路径
> 默认 logs 文件夹，路径可通过 `LOG_PATH` 环境变量指定。

## ✅ 维护者
- [Qtech](https://github.com/qtech)  
- 邮箱: gaoolin@gmail.com  
- 企业内部钉钉群: 【智能制造认证服务交流群】

