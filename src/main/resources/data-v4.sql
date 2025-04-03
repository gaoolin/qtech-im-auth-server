-- data.sql: 初始化智能制造行业特征的数据
-- 清空表数据（谨慎执行）
TRUNCATE TABLE IM_AUTH_SYSTEM CASCADE;
TRUNCATE TABLE IM_AUTH_DEPT CASCADE;
TRUNCATE TABLE IM_AUTH_USER CASCADE;
TRUNCATE TABLE IM_AUTH_ROLE CASCADE;
TRUNCATE TABLE IM_AUTH_PERMISSION CASCADE;
TRUNCATE TABLE IM_AUTH_USER_SYSTEM_ROLE CASCADE;
TRUNCATE TABLE IM_AUTH_ROLE_SYSTEM_PERMISSION CASCADE;
TRUNCATE TABLE IM_AUTH_OAUTH_CLIENT CASCADE;
TRUNCATE TABLE IM_AUTH_API_KEY CASCADE;
TRUNCATE TABLE IM_AUTH_SESSION CASCADE;
TRUNCATE TABLE IM_AUTH_AUDIT_LOG CASCADE;


-- 插入系统信息
INSERT INTO im_auth_system (sys_name, status, created_by, updated_by, description)
VALUES ('智能制造系统', '0', 'admin', 'admin', '智能制造系统的认证和授权管理');

-- 插入部门信息
INSERT INTO im_auth_dept (parent_id, ancestors, dept_name, order_num, leader, email, phone, status, created_by, updated_by, description)
VALUES (0, '0', '生产部', 1, '张三', 'zhangsan@example.com', '13800138000', '0', 'admin', 'admin', '负责生产管理');
INSERT INTO im_auth_dept (parent_id, ancestors, dept_name, order_num, leader, email, phone, status, created_by, updated_by, description)
VALUES (0, '0', '研发部', 2, '李四', 'lisi@example.com', '13800138001', '0', 'admin', 'admin', '负责产品研发');

-- 插入用户信息
INSERT INTO im_auth_user (emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, created_by, updated_by, description)
VALUES ('001', 'zhangsan', '张三', 'hashed_password', '1', 1, 'avatar1.jpg', 'zhangsan@example.com', '13800138000', '0', '0', 'admin', 'admin', '生产部经理');
INSERT INTO im_auth_user (emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, created_by, updated_by, description)
VALUES ('002', 'lisi', '李四', 'hashed_password', '1', 2, 'avatar2.jpg', 'lisi@example.com', '13800138001', '0', '0', 'admin', 'admin', '研发部经理');

-- 插入角色信息
INSERT INTO im_auth_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, created_by, updated_by, description)
VALUES ('生产管理员', 'ROLE_PROD_ADMIN', 1, '1', '1', '1', '0', 'admin', 'admin', '生产部管理员');
INSERT INTO im_auth_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, created_by, updated_by, description)
VALUES ('研发管理员', 'ROLE_DEV_ADMIN', 2, '1', '1', '1', '0', 'admin', 'admin', '研发部管理员');

-- 插入权限信息
INSERT INTO im_auth_permission (perm_name, sys_id, app_name, rsrc_name, action_type, status, created_by, updated_by, description)
VALUES ('生产管理', 1, '生产管理系统', '生产订单', 'READ', '0', 'admin', 'admin', '生产订单读取权限');
INSERT INTO im_auth_permission (perm_name, sys_id, app_name, rsrc_name, action_type, status, created_by, updated_by, description)
VALUES ('研发管理', 1, '研发管理系统', '研发项目', 'READ', '0', 'admin', 'admin', '研发项目读取权限');

-- 插入角色-权限关联
INSERT INTO im_auth_role_system_permission (role_id, system_id, perm_id)
VALUES (1, 1, 1);
INSERT INTO im_auth_role_system_permission (role_id, system_id, perm_id)
VALUES (2, 1, 2);

-- 插入用户-角色关联
INSERT INTO im_auth_user_system_role (user_id, system_id, role_id)
VALUES (1, 1, 1);
INSERT INTO im_auth_user_system_role (user_id, system_id, role_id)
VALUES (2, 1, 2);

-- 插入OAuth2客户端信息
INSERT INTO im_auth_oauth_client (client_id, client_name, sys_name, client_secret, grant_types, redirect_uris, scopes, status, created_by, updated_by, description)
VALUES ('auth-center', '认证中心', '智能制造系统', 'secret123456', 'password,client_credentials', 'http://localhost:8080/callback', 'read,write', '0', 'admin', 'admin', '认证中心客户端');

-- 插入API Key信息
INSERT INTO im_auth_api_key (user_id, api_key, expires_at, created_by, updated_by, description)
VALUES (1, 'api_key_001', SYSTIMESTAMP + INTERVAL '30' DAY, 'admin', 'admin', '张三的API Key');
INSERT INTO im_auth_api_key (user_id, api_key, expires_at, created_by, updated_by, description)
VALUES (2, 'api_key_002', SYSTIMESTAMP + INTERVAL '30' DAY, 'admin', 'admin', '李四的API Key');

-- 插入会话信息
INSERT INTO im_auth_session (user_id, session_token, expires_at, created_by, updated_by, description)
VALUES (1, 'session_token_001', SYSTIMESTAMP + INTERVAL '1' HOUR, 'admin', 'admin', '张三的会话');
INSERT INTO im_auth_session (user_id, session_token, expires_at, created_by, updated_by, description)
VALUES (2, 'session_token_002', SYSTIMESTAMP + INTERVAL '1' HOUR, 'admin', 'admin', '李四的会话');

-- 插入审计日志信息
INSERT INTO im_auth_audit_log (user_id, action, ip_address, description)
VALUES (1, '登录', '192.168.1.1', '张三登录系统');
INSERT INTO im_auth_audit_log (user_id, action, ip_address, description)
VALUES (2, '登录', '192.168.1.2', '李四登录系统');
