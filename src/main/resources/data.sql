-- 一、初始化数据 SQL 脚本（制造行业特点）
-- 用户表（im_auth_user）：包含用户信息（工号、用户名、电子邮件、部门等）。
-- 角色表（im_auth_role）：包含不同角色的描述，如 Admin、Engineer、LineLeader 等。
-- 权限表（im_auth_permission）：定义了可用的权限，如 READ_USER、WRITE_USER、VIEW_REPORTS 等，具体权限可以基于你的需求进一步扩展。
-- 用户与角色关系表（im_auth_user_role）：分配用户到相应角色。
-- 角色与权限关系表（im_auth_role_permission）：分配角色对应的权限。
-- API Key 和 OAuth 客户端：为用户生成 API Key，并插入 OAuth 客户端配置。
-- 会话管理：插入示例会话数据。
-- 审计日志：插入用户操作的审计日志。
-- 插入用户（示例 10 个用户）
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1001', 'zhangsan', '$2a$10$Rh.CtQ2MiB3gKbBrTTj2sOXsikqzSNrPast9P/N82Kn66MoiHjVOS', 'zhangsan@company.com', '生产部', 'SMT一线', '男', 'ACTIVE');  -- password: admin123
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1002', 'lisi', 'hashed_password_2', 'lisi@company.com', '设备部', '设备维护组', '男', 'ACTIVE');
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1003', 'wangwu', 'hashed_password_3', 'wangwu@company.com', '工艺部', '工艺工程师', '男', 'ACTIVE');
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1004', 'zhaoliu', 'hashed_password_4', 'zhaoliu@company.com', '品质部', '品质工程师', '女', 'ACTIVE');
INSERT INTO im_auth_user (employee_id, username, password_hash, email, department, section, gender, status)
VALUES ('E1005', 'sunqi', 'hashed_password_5', 'sunqi@company.com', '生产部', 'SMT二线', '男', 'ACTIVE');
-- (继续添加 5 条同类数据，包括工艺工程师、品质工程师、封测班组长等)

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
INSERT INTO im_auth_user_role (user_id, role_id) VALUES (3, 3); -- Wangwu 为工艺工程师
INSERT INTO im_auth_user_role (user_id, role_id) VALUES (4, 4); -- Zhaoliu 为品质工程师
INSERT INTO im_auth_user_role (user_id, role_id) VALUES (5, 5); -- Sunqi 为操作员
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
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('EXPORT_REPORT', '导出生产报表', 'REPORT', 'WRITE', '制造管理系统', 'Dashboard');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('APPROVE_EXCEPTION', '审批异常单', 'EXCEPTION', 'WRITE', '设备管理系统', 'API');
INSERT INTO im_auth_permission (permission_name, description, resource_name, action_type, system_name, application_name)
VALUES ('VIEW_MAINTENANCE', '查看维修记录', 'EQUIPMENT', 'READ', '设备管理系统', 'Dashboard');
-- (继续补充权限，如查看日志、查看设备状态等)

-- 分配角色-权限
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (1, 1); -- Admin 角色拥有 READ_USER
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (1, 2); -- Admin 角色拥有 WRITE_USER
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (2, 4); -- Engineer 可以查看报表
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (3, 5); -- LineLeader 角色可以调整机台参数
INSERT INTO im_auth_role_permission (role_id, permission_id) VALUES (4, 7); -- QualityControl 角色可以审批异常单
-- (继续添加映射关系)

-- 插入 API Key 示例
INSERT INTO im_auth_api_key (user_id, api_key)
VALUES (1, 'API_KEY_ADMIN_EXAMPLE');

-- 插入 OAuth 客户端
INSERT INTO im_auth_oauth_client (client_id, client_name, system_name, client_secret, grant_types, redirect_uris, scopes, created_at)
VALUES ('mes_client', 'MES系统客户端', 'MES', 'secret123', 'client_credentials', 'http://localhost:8080/callback', 'read,write', SYSTIMESTAMP);

INSERT INTO im_auth_oauth_client (client_id, client_name, system_name, client_secret, grant_types, redirect_uris, scopes, created_at)
VALUES ('erp_integration', 'ERP系统客户端', 'ERP', 'secret456', 'client_credentials', 'http://localhost:8081/callback', 'read', SYSTIMESTAMP);

INSERT INTO im_auth_oauth_client (client_id, client_name, system_name, client_secret, grant_types, redirect_uris, scopes, created_at)
VALUES ('wms_client', 'WMS系统客户端', 'WMS', 'secret789', 'client_credentials', 'http://localhost:8082/callback', 'read,write,delete', SYSTIMESTAMP);

-- 插入测试会话
INSERT INTO im_auth_session (user_id, session_token, expires_at)
VALUES (1, 'SESSIONTOKEN123', SYSTIMESTAMP + INTERVAL '7' DAY);

-- 插入审计日志
INSERT INTO im_auth_audit_log (user_id, action, ip_address)
VALUES (1, '登录系统', '192.168.0.10');
INSERT INTO im_auth_audit_log (user_id, action, ip_address)
VALUES (2, '调整参数', '192.168.0.11');

-- (继续补充模拟历史数据)
