-- schema.sql: 优化后的认证系统数据库结构（Oracle 19c 及以上）

-- **************************************
-- 删除已有表和序列（如果存在）
-- **************************************
BEGIN
    -- 删除以 IM_AUTH_ 开头的所有表
    FOR r IN (SELECT table_name FROM user_tables WHERE table_name LIKE 'IM_AUTH_%') LOOP
        EXECUTE IMMEDIATE 'DROP TABLE ' || r.table_name || ' CASCADE CONSTRAINTS';
    END LOOP;

    -- 删除以 SEQ_ 开头的所有序列
    FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_%') LOOP
        EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.sequence_name;
    END LOOP;
END;
/

-- **************************************
-- 系统表：保存系统的基本信息，用于系统维度的授权管理
-- **************************************
CREATE TABLE im_auth_system (
    id NUMBER PRIMARY KEY, -- 主键 ID
    system_name VARCHAR2(100) NOT NULL UNIQUE, -- 系统名称（唯一）
    description VARCHAR2(255), -- 系统描述
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP -- 更新时间
);

-- 系统表序列
CREATE SEQUENCE seq_im_auth_system START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 系统表触发器
CREATE OR REPLACE TRIGGER trg_im_auth_system
    BEFORE INSERT ON im_auth_system
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_system.NEXTVAL;
END;
/

-- **************************************
-- 用户表：保存系统用户基础信息
-- **************************************
CREATE TABLE im_auth_user (
    id NUMBER PRIMARY KEY, -- 主键 ID
    employee_id VARCHAR2(20) NOT NULL UNIQUE, -- 工号（唯一）
    username VARCHAR2(50) NOT NULL UNIQUE, -- 用户名（唯一）
    password_hash VARCHAR2(255) NOT NULL, -- 密码哈希值
    email VARCHAR2(100), -- 邮箱
    department VARCHAR2(100), -- 部门
    section VARCHAR2(100), -- 课别/小组
    gender VARCHAR2(10), -- 性别
    status VARCHAR2(20) DEFAULT 'ACTIVE', -- 状态：ACTIVE 或 DISABLED
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP -- 更新时间
);

-- 用户表序列
CREATE SEQUENCE seq_im_auth_user START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 用户表触发器，自动填充 ID
CREATE OR REPLACE TRIGGER trg_im_auth_user
    BEFORE INSERT ON im_auth_user
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_user.NEXTVAL;
END;
/

-- **************************************
-- 角色表：定义系统中各类角色
-- **************************************
CREATE TABLE im_auth_role (
    id NUMBER PRIMARY KEY, -- 主键 ID
    role_name VARCHAR2(50) NOT NULL UNIQUE, -- 角色名称（唯一）
    description VARCHAR2(255) -- 角色描述
);

-- 角色表序列
CREATE SEQUENCE seq_im_auth_role START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 角色表触发器
CREATE OR REPLACE TRIGGER trg_im_auth_role
    BEFORE INSERT ON im_auth_role
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_role.NEXTVAL;
END;
/

-- **************************************
-- 用户-角色关联表：支持多对多关系
-- **************************************
CREATE TABLE im_auth_user_role (
    user_id NUMBER NOT NULL, -- 用户 ID
    role_id NUMBER NOT NULL, -- 角色 ID
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES im_auth_role(id) ON DELETE CASCADE
);

-- **************************************
-- 权限表：定义细粒度权限
-- **************************************
CREATE TABLE im_auth_permission (
    id NUMBER PRIMARY KEY, -- 主键 ID
    permission_name VARCHAR2(255) NOT NULL, -- 权限名称
    description VARCHAR2(255), -- 权限描述
    resource_name VARCHAR2(255), -- 资源名称
    action_type VARCHAR2(50), -- 动作类型（READ/WRITE/DELETE 等）
    system_name VARCHAR2(100), -- 所属系统名称
    application_name VARCHAR2(100), -- 所属应用名称
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    CONSTRAINT uq_auth_permission UNIQUE (permission_name, system_name, application_name) -- 唯一约束
);

-- 权限表序列
CREATE SEQUENCE seq_im_auth_permission START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 权限表插入触发器
CREATE OR REPLACE TRIGGER trg_im_auth_permission
    BEFORE INSERT ON im_auth_permission
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_permission.NEXTVAL;
END;
/

-- 权限表更新触发器：自动更新时间
CREATE OR REPLACE TRIGGER trg_im_auth_permission_update
    BEFORE UPDATE ON im_auth_permission
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- **************************************
-- 角色-权限关联表：多对多映射
-- **************************************
CREATE TABLE im_auth_role_permission (
    role_id NUMBER NOT NULL, -- 角色 ID
    permission_id NUMBER NOT NULL, -- 权限 ID
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES im_auth_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES im_auth_permission(id) ON DELETE CASCADE
);

-- **************************************
-- 用户-权限关联表：支持直接授予用户权限
-- **************************************
CREATE TABLE im_auth_user_permission (
    user_id NUMBER NOT NULL, -- 用户 ID
    permission_id NUMBER NOT NULL, -- 权限 ID
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES im_auth_permission(id) ON DELETE CASCADE
);

-- **************************************
-- OAuth2 客户端表：存储第三方应用接入信息
-- **************************************
CREATE TABLE im_auth_oauth_client (
    client_id VARCHAR2(100) PRIMARY KEY, -- 客户端 ID
    client_name VARCHAR2(100) NOT NULL, -- 客户端名称
    system_name VARCHAR2(100) NOT NULL, -- 归属系统标识（可用于在 token 中标识来源）
    client_secret VARCHAR2(255) NOT NULL, -- 客户端密钥
    grant_types VARCHAR2(255) NOT NULL, -- 授权类型
    redirect_uris VARCHAR2(1000), -- 回调 URI 列表
    scopes VARCHAR2(500), -- 权限范围
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP -- 创建时间
);

-- **************************************
-- API Key 表：用于非交互式认证
-- **************************************
CREATE TABLE im_auth_api_key (
    id NUMBER PRIMARY KEY, -- 主键 ID
    user_id NUMBER NOT NULL, -- 用户 ID
    api_key VARCHAR2(255) UNIQUE NOT NULL, -- API Key 值
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE
);

-- API Key 序列及触发器
CREATE SEQUENCE seq_im_auth_api_key START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_im_auth_api_key
    BEFORE INSERT ON im_auth_api_key
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_api_key.NEXTVAL;
END;
/

-- **************************************
-- 会话表：保存登录会话信息
-- **************************************
CREATE TABLE im_auth_session (
    id NUMBER PRIMARY KEY, -- 主键 ID
    user_id NUMBER NOT NULL, -- 用户 ID
    session_token VARCHAR2(255) UNIQUE NOT NULL, -- 会话 Token
    expires_at TIMESTAMP NOT NULL, -- 过期时间
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE
);

-- 会话表序列及触发器
CREATE SEQUENCE seq_im_auth_session START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_im_auth_session
    BEFORE INSERT ON im_auth_session
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_session.NEXTVAL;
END;
/

-- **************************************
-- 审计日志表：记录用户行为
-- **************************************
CREATE TABLE im_auth_audit_log (
    id NUMBER PRIMARY KEY, -- 主键 ID
    user_id NUMBER, -- 用户 ID，可为空
    action VARCHAR2(255) NOT NULL, -- 操作行为
    ip_address VARCHAR2(50), -- IP 地址
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE SET NULL
);

-- 审计日志序列及触发器
CREATE SEQUENCE seq_im_auth_audit_log START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_im_auth_audit_log
    BEFORE INSERT ON im_auth_audit_log
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_audit_log.NEXTVAL;
END;
/
