-- 一、初始化数据 SQL 脚本（制造行业特点）

-- 插入用户（示例 10 个用户）
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1001', 'zhangsan', 'hashed_password_1', 'zhangsan@company.com', '生产部', 'SMT一线', '男', 'ACTIVE');
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1002', 'lisi', 'hashed_password_2', 'lisi@company.com', '设备部', '设备维护组', '男', 'ACTIVE');
-- (继续添加8条同类数据，包括工艺工程师、品质工程师、封测班组长等)

-- 插入角色（5个角色）
INSERT INTO im_auth_role (role_name, description)
VALUES ('Admin', '系统管理员 - 拥有所有权限');
INSERT INTO im_auth_role (role_name, description)
VALUES ('Engineer', '工艺工程师 - 可调整参数、查看日志');
INSERT INTO im_auth_role (role_name, description)
VALUES ('LineLeader', '生产班组长 - 管理人员与报表');
INSERT INTO im_auth_role (role_name, description)
VALUES ('QualityControl', '品质人员 - 查看与审核数据');
INSERT INTO im_auth_role (role_name, description)
VALUES ('Operator', '操作员 - 仅有基本操作权限');

-- 分配用户-角色
INSERT INTO im_auth_user_role (user_id, role_id) VALUES (1, 1); -- Zhangsan 为管理员
INSERT INTO im_auth_user_role (user_id, role_id) VALUES (2, 2); -- Lisi 为工程师
-- (继续添加对应关系)

-- 插入权限（示例 12 个权限）
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('READ_USER', '查看用户信息', 'USER', 'READ', '制造管理系统', 'Admin Portal');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('WRITE_USER', '修改用户信息', 'USER', 'WRITE', '制造管理系统', 'Admin Portal');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('DELETE_USER', '删除用户', 'USER', 'DELETE', '制造管理系统', 'Admin Portal');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('VIEW_REPORTS', '查看生产报表', 'REPORT', 'READ', '制造管理系统', 'Dashboard');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('ADJUST_PARAMS', '调整机台参数', 'EQUIPMENT', 'WRITE', '设备管理系统', 'API');
-- (继续补充权限，如导出报表、审批异常单、查看维修记录等)

-- 分配角色-权限
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (1, 1); -- Admin 角色拥有 READ_USER
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (1, 2); -- Admin 角色拥有 WRITE_USER
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (2, 4); -- Engineer 可以查看报表
-- (继续添加映射关系)

-- 插入 API Key 示例
INSERT INTO im_auth_api_key (user_id, api_key)
VALUES (1, 'API_KEY_ADMIN_EXAMPLE');

-- 插入 OAuth 客户端
INSERT INTO im_auth_oauth_client (client_id, client_secret, grant_types, redirect_uris, scopes)
VALUES ('mfg_dashboard', 'secret1234', 'authorization_code,refresh_token', 'https://mfg.example.com/callback', 'read,write');

-- 插入测试会话
INSERT INTO im_auth_session (user_id, session_token, expires_at)
VALUES (1, 'SESSIONTOKEN123', SYSTIMESTAMP + INTERVAL '7' DAY);

-- 插入审计日志
INSERT INTO im_auth_audit_log (user_id, action, ip_address)
VALUES (1, '登录系统', '192.168.0.10');
INSERT INTO im_auth_audit_log (user_id, action, ip_address)
VALUES (2, '调整参数', '192.168.0.11');
-- (继续补充模拟历史数据)


-- 二、ER图设计建议

1. 使用工具：推荐 Draw.io / dbdiagram.io / Oracle Data Modeler。

2. 实体关系：
- 用户表 (im_auth_user) 和 角色表 (im_auth_role) 多对多 -> im_auth_user_role。
- 角色表 (im_auth_role) 和 权限表 (im_auth_permission) 多对多 -> im_auth_role_permission。
- 用户表 (im_auth_user) 和 权限表 (im_auth_permission) 多对多 -> im_auth_user_permission。
- 用户与 API KEY、一对多关系。
- 用户与会话、一对多关系。
- 用户与审计日志、一对多关系。

3. 可视化元素建议：
- 表格：方形边框 + 表名顶部加粗 + 字段名按主键、外键、普通字段顺序排列。
- 关系：直线连接 + 菱形注释（如“1..*” 或 “0..*”）。
- 色彩：
  - 用户表蓝色，
  - 角色表绿色，
  - 权限表橙色，
  - 中间表灰色，
  - 会话、API Key 和日志表淡黄色。

如果需要，我可以根据这个结构帮你生成一个 dbdiagram.io 的完整导入格式。

