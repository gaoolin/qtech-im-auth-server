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
-- **************************************
-- 插入系统表数据
-- **************************************
INSERT INTO im_auth_system (id, sys_name, sys_code, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_system.NEXTVAL, '智能制造系统', 'intelligent_mfg', '0', '0', 'admin', 'admin', '智能制造系统描述');
INSERT INTO im_auth_system (id, sys_name, sys_code, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_system.NEXTVAL, '生产监控系统', 'production_monitor', '0', '0', 'admin', 'admin', '生产监控系统描述');
INSERT INTO im_auth_system (id, sys_name, sys_code, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_system.NEXTVAL, '仓库管理系统', 'warehouse_mgmt', '0', '0', 'admin', 'admin', '仓库管理系统描述');
INSERT INTO im_auth_system (id, sys_name, sys_code, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_system.NEXTVAL, 'ERP系统', 'erp_system', '0', '0', 'admin', 'admin', '企业资源计划系统');

-- **************************************
-- 插入部门表数据
-- **************************************
INSERT INTO im_auth_dept (id, parent_id, ancestors, dept_name, order_num, leader, email, phone, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_dept.NEXTVAL, 0, '', '研发部', 1, '张经理', 'dev@company.com', '13800000000', '0', '0', 'admin', 'admin', '研发部门负责产品设计和开发');
INSERT INTO im_auth_dept (id, parent_id, ancestors, dept_name, order_num, leader, email, phone, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_dept.NEXTVAL, 0, '', '生产部', 2, '李主管', 'prod@company.com', '13811111111', '0', '0', 'admin', 'admin', '生产部门负责生产线管理');
INSERT INTO im_auth_dept (id, parent_id, ancestors, dept_name, order_num, leader, email, phone, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_dept.NEXTVAL, 0, '', '财务部', 3, '王会计', 'finance@company.com', '13822222222', '0', '0', 'admin', 'admin', '财务部门负责公司账务');
INSERT INTO im_auth_dept (id, parent_id, ancestors, dept_name, order_num, leader, email, phone, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_dept.NEXTVAL, 0, '', '市场部', 4, '刘经理', 'marketing@company.com', '13833333333', '0', '0', 'admin', 'admin', '市场部门负责产品推广');

-- **************************************
-- 插入用户表数据
-- **************************************
INSERT INTO im_auth_user (id, emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, del_flag, login_ip, login_dt, created_by, updated_by, description)
VALUES (seq_im_auth_user.NEXTVAL, 'E001', 'zhangsan', '张三', 'hash_value_for_zhangsan', '1', (SELECT id FROM im_auth_dept WHERE dept_name = '研发部'), '', 'zhangsan@company.com', '13812345678', '0', '0', '0', NULL, SYSTIMESTAMP, 'admin', 'admin', '研发部开发人员');
INSERT INTO im_auth_user (id, emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, del_flag, login_ip, login_dt, created_by, updated_by, description)
VALUES (seq_im_auth_user.NEXTVAL, 'E002', 'lisi', '李四', 'hash_value_for_lisi', '2', (SELECT id FROM im_auth_dept WHERE dept_name = '生产部'), '', 'lisi@company.com', '13823456789', '0', '0', '0', NULL, SYSTIMESTAMP, 'admin', 'admin', '生产部人员');
INSERT INTO im_auth_user (id, emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, del_flag, login_ip, login_dt, created_by, updated_by, description)
VALUES (seq_im_auth_user.NEXTVAL, 'E003', 'wangwu', '王五', 'hash_value_for_wangwu', '1', (SELECT id FROM im_auth_dept WHERE dept_name = '财务部'), '', 'wangwu@company.com', '13834567890', '0', '0', '0', NULL, SYSTIMESTAMP, 'admin', 'admin', '财务部人员');
INSERT INTO im_auth_user (id, emp_id, username, nickname, pw_hash, gender, dept_id, avatar, email, phone, user_type, status, del_flag, login_ip, login_dt, created_by, updated_by, description)
VALUES (seq_im_auth_user.NEXTVAL, 'E004', 'zhaoliu', '赵六', 'hash_value_for_zhaoliu', '2', (SELECT id FROM im_auth_dept WHERE dept_name = '市场部'), '', 'zhaoliu@company.com', '13845678901', '0', '0', '0', NULL, SYSTIMESTAMP, 'admin', 'admin', '市场部人员');

-- **************************************
-- 插入角色表数据
-- **************************************
INSERT INTO im_auth_role (id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_role.NEXTVAL, '系统管理员', 'ROLE_ADMIN', 1, '1', '1', '1', '0', '0', 'admin', 'admin', '拥有所有权限');
INSERT INTO im_auth_role (id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_role.NEXTVAL, '研发人员', 'ROLE_DEV', 2, '5', '1', '1', '0', '0', 'admin', 'admin', '只允许查看本部门数据');
INSERT INTO im_auth_role (id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_role.NEXTVAL, '财务人员', 'ROLE_FINANCE', 3, '5', '1', '1', '0', '0', 'admin', 'admin', '只允许查看财务数据');
INSERT INTO im_auth_role (id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_role.NEXTVAL, '市场人员', 'ROLE_MARKETING', 4, '5', '1', '1', '0', '0', 'admin', 'admin', '只允许查看市场数据');

-- **************************************
-- 插入用户-角色关联数据
-- **************************************
INSERT INTO im_auth_user_role (user_id, role_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'zhangsan'), (SELECT id FROM im_auth_role WHERE role_key = 'ROLE_ADMIN'));
INSERT INTO im_auth_user_role (user_id, role_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'lisi'), (SELECT id FROM im_auth_role WHERE role_key = 'ROLE_DEV'));
INSERT INTO im_auth_user_role (user_id, role_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'wangwu'), (SELECT id FROM im_auth_role WHERE role_key = 'ROLE_FINANCE'));
INSERT INTO im_auth_user_role (user_id, role_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'zhaoliu'), (SELECT id FROM im_auth_role WHERE role_key = 'ROLE_MARKETING'));

-- **************************************
-- 插入权限表数据
-- **************************************
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '查看系统信息', '智能制造系统', '管理平台', '系统信息', 'READ', '0', '0', 'admin', 'admin', '查看系统的基本信息');
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '编辑系统信息', '智能制造系统', '管理平台', '系统信息', 'WRITE', '0', '0', 'admin', 'admin', '编辑系统的基本信息');
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '查看财务数据', 'ERP系统', '财务管理', '财务数据', 'READ', '0', '0', 'admin', 'admin', '查看财务相关数据');
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '编辑财务数据', 'ERP系统', '财务管理', '财务数据', 'WRITE', '0', '0', 'admin', 'admin', '编辑财务数据');
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '查看市场数据', '智能制造系统', '市场分析', '市场数据', 'READ', '0', '0', 'admin', 'admin', '查看市场相关数据');
INSERT INTO im_auth_permission (id, perm_name, sys_name, app_name, rsrc_name, action_type, status, del_flag, created_by, updated_by, description)
VALUES (seq_im_auth_permission.NEXTVAL, '编辑市场数据', '智能制造系统', '市场分析', '市场数据', 'WRITE', '0', '0', 'admin', 'admin', '编辑市场相关数据');

-- **************************************
-- 插入用户-权限关联数据
-- **************************************
INSERT INTO im_auth_user_permission (user_id, perm_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'zhangsan'), (SELECT id FROM im_auth_permission WHERE perm_name = '查看系统信息'));
INSERT INTO im_auth_user_permission (user_id, perm_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'lisi'), (SELECT id FROM im_auth_permission WHERE perm_name = '查看系统信息'));
INSERT INTO im_auth_user_permission (user_id, perm_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'wangwu'), (SELECT id FROM im_auth_permission WHERE perm_name = '查看财务数据'));
INSERT INTO im_auth_user_permission (user_id, perm_id)
VALUES ((SELECT id FROM im_auth_user WHERE username = 'zhaoliu'), (SELECT id FROM im_auth_permission WHERE perm_name = '查看市场数据'));
