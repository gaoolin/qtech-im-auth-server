-- schema.sql: 优化后的认证系统数据库结构（Oracle 19c 及以上）

-- 删除已有表和序列（如果存在）
BEGIN
    FOR r IN (SELECT table_name FROM user_tables WHERE table_name LIKE 'IM_AUTH_%') LOOP
        EXECUTE IMMEDIATE 'DROP TABLE ' || r.table_name || ' CASCADE CONSTRAINTS';
    END LOOP;

    FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_%') LOOP
        EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.sequence_name;
    END LOOP;
END;
/

-- 用户表
CREATE TABLE im_auth_user (
    id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL UNIQUE,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password_hash VARCHAR2(255) NOT NULL,
    email VARCHAR2(100),
    department VARCHAR2(100),
    section VARCHAR2(100),
    gender VARCHAR2(10),
    status VARCHAR2(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE SEQUENCE seq_im_auth_user START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_user
    BEFORE INSERT ON im_auth_user
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_user.NEXTVAL;
END;
/

-- 角色表
CREATE TABLE im_auth_role (
    id NUMBER PRIMARY KEY,
    role_name VARCHAR2(50) NOT NULL UNIQUE,
    description VARCHAR2(255)
);

CREATE SEQUENCE seq_im_auth_role START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_role
    BEFORE INSERT ON im_auth_role
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_role.NEXTVAL;
END;
/

-- 用户-角色关联表
CREATE TABLE im_auth_user_role (
    user_id NUMBER NOT NULL,
    role_id NUMBER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES im_auth_role(id) ON DELETE CASCADE
);

-- 权限表
CREATE TABLE im_auth_permission (
    id NUMBER PRIMARY KEY,
    permission_name VARCHAR2(255) NOT NULL,
    description VARCHAR2(255),
    resource_name VARCHAR2(255),
    action_type VARCHAR2(50),
    system_name VARCHAR2(100),
    application_name VARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_auth_permission UNIQUE (permission_name, system_name, application_name)
);

CREATE SEQUENCE seq_im_auth_permission START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_permission
    BEFORE INSERT ON im_auth_permission
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_permission.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_im_auth_permission_update
    BEFORE UPDATE ON im_auth_permission
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- 角色-权限关联表
CREATE TABLE im_auth_role_permission (
    role_id NUMBER NOT NULL,
    permission_id NUMBER NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES im_auth_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES im_auth_permission(id) ON DELETE CASCADE
);

-- 用户-权限关联表
CREATE TABLE im_auth_user_permission (
    user_id NUMBER NOT NULL,
    permission_id NUMBER NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES im_auth_permission(id) ON DELETE CASCADE
);

-- OAuth2 客户端表
CREATE TABLE im_auth_oauth_client (
    client_id VARCHAR2(100) PRIMARY KEY,
    client_secret VARCHAR2(255) NOT NULL,
    grant_types VARCHAR2(255) NOT NULL,
    redirect_uris VARCHAR2(1000),
    scopes VARCHAR2(500),
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- API Key 表
CREATE TABLE im_auth_api_key (
    id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    api_key VARCHAR2(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_im_auth_api_key START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_api_key
    BEFORE INSERT ON im_auth_api_key
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_api_key.NEXTVAL;
END;
/

-- 会话表
CREATE TABLE im_auth_session (
    id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    session_token VARCHAR2(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_im_auth_session START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_session
    BEFORE INSERT ON im_auth_session
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_session.NEXTVAL;
END;
/

-- 审计日志表
CREATE TABLE im_auth_audit_log (
    id NUMBER PRIMARY KEY,
    user_id NUMBER,
    action VARCHAR2(255) NOT NULL,
    ip_address VARCHAR2(50),
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES im_auth_user(id) ON DELETE SET NULL
);

CREATE SEQUENCE seq_im_auth_audit_log START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER trg_im_auth_audit_log
    BEFORE INSERT ON im_auth_audit_log
    FOR EACH ROW
BEGIN
    :NEW.id := seq_im_auth_audit_log.NEXTVAL;
END;
/
