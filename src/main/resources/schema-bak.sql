-- schema-bak.sql: 优化后的认证系统数据库结构（Oracle 19c 及以上）

-- **************************************
-- 删除已有表和序列（如果存在）
-- **************************************
BEGIN
    -- 定义一个局部过程来安全执行动态 SQL
    DECLARE
        PROCEDURE safe_execute(sql_stmt IN VARCHAR2) IS
        BEGIN
            EXECUTE IMMEDIATE sql_stmt;
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error executing: ' || sql_stmt);
                DBMS_OUTPUT.PUT_LINE('Error message: ' || SQLERRM);
        END safe_execute;
    BEGIN
        -- 删除以 IM_AUTH_ 开头的所有表
        FOR r IN (SELECT table_name FROM user_tables WHERE table_name LIKE 'IM_AUTH_%')
            LOOP
                safe_execute('DROP TABLE ' || r.table_name || ' CASCADE CONSTRAINTS');
            END LOOP;

        -- 删除以 SEQ_ 开头的所有序列
        FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_IM_AUTH_%')
            LOOP
                safe_execute('DROP SEQUENCE ' || s.sequence_name);
            END LOOP;

        -- 删除所有触发器
        FOR t IN (SELECT trigger_name FROM user_triggers WHERE trigger_name LIKE 'TRG_IM_AUTH_%')
            LOOP
                safe_execute('DROP TRIGGER ' || t.trigger_name);
            END LOOP;

        -- 删除所有索引
        FOR i IN (SELECT index_name FROM user_indexes WHERE index_name LIKE 'IDX_IM_AUTH_%')
            LOOP
                safe_execute('DROP INDEX ' || i.index_name);
            END LOOP;

        -- 删除所有约束
        FOR c IN (
            SELECT constraint_name, table_name
            FROM user_constraints
            WHERE constraint_name LIKE 'PK_IM_AUTH_%'
               OR constraint_name LIKE 'UQ_IM_AUTH_%'
            )
            LOOP
                safe_execute('ALTER TABLE ' || c.table_name || ' DROP CONSTRAINT ' || c.constraint_name);
            END LOOP;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('An error occurred during cleanup: ' || SQLERRM);
    END;
END;
/

/**
  主键：主键 ID 由序列生成，自增。在手动插入时，系统会自动生成主键 ID。切勿手动指定ID
  触发器：插入数据时，自动生成主键和create_at、update_at。更新数据时，自动更新 updated_at
  索引：仅对高频查询字段添加索引
 */

-- **************************************
-- 系统表：保存系统的基本信息，用于系统维度的授权管理
-- **************************************
CREATE TABLE im_auth_system
(
    id          NUMBER PRIMARY KEY,                                                       -- 主键 ID
    sys_name    VARCHAR2(100) NOT NULL,                                                   -- 所属系统名称（唯一）
    app_name    VARCHAR2(100),                                                            -- 所属应用名称
    rsrc_name   VARCHAR2(255),                                                            -- 资源名称
    action_type VARCHAR2(50),                                                             -- 动作类型（READ/WRITE/DELETE 等）
    status      CHAR(1)   DEFAULT '0' CHECK (status IN ('0', '1')),                       -- 状态：0 正常 1 停用
    del_flag    CHAR(1)   DEFAULT '0' CHECK (del_flag IN ('0', '1')),                     -- 删除标志（0代表存在 1代表删除）
    created_at  TIMESTAMP DEFAULT SYSTIMESTAMP,                                           -- 创建时间
    updated_at  TIMESTAMP DEFAULT SYSTIMESTAMP,                                           -- 更新时间
    created_by  VARCHAR2(50),                                                             -- 创建人
    updated_by  VARCHAR2(50),                                                             -- 更新人
    description CLOB,                                                                     -- 系统描述
    CONSTRAINT uq_im_auth_system_name UNIQUE (sys_name, app_name, rsrc_name, action_type) -- 唯一约束
);

-- 添加索引（优化高频查询字段）
CREATE INDEX idx_im_auth_system_status ON im_auth_system (status);


-- 组合索引

-- 系统表序列
CREATE SEQUENCE seq_im_auth_system START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 系统表触发器：自动生成主键并更新 updated_at
CREATE OR REPLACE TRIGGER trg_im_auth_system
    BEFORE INSERT OR UPDATE
    ON im_auth_system
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_system.NEXTVAL;
    END IF;

    -- 插入时设置创建时间
    IF INSERTING THEN
        :NEW.created_at := SYSTIMESTAMP;
    END IF;

    -- 插入、更新时设置时间戳
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := SYSTIMESTAMP;
    END IF;
END;
/

-- 创建视图，默认过滤已删除记录
CREATE OR REPLACE VIEW v_im_auth_system AS
SELECT *
FROM im_auth_system
WHERE del_flag = '0';


-- **************************************
-- 部门表：定义系统中部门
-- **************************************
CREATE TABLE im_auth_dept
(
    id          NUMBER(19, 0) PRIMARY KEY,                                -- 主键 ID
    parent_id   NUMBER(19, 0) DEFAULT 0,                                  -- 上级部门 ID
    ancestors   VARCHAR2(255) DEFAULT '',                                 -- 祖先部门路径
    dept_name   VARCHAR2(255) DEFAULT '',                                 -- 部门名称
    order_num   NUMBER(4, 0)  DEFAULT 0,                                  -- 排序
    leader      VARCHAR2(50)  DEFAULT '',                                 -- 部门领导（默认为空字符串）
    email       VARCHAR2(50)  DEFAULT '',                                 -- 邮箱（默认为空字符串）
    phone       VARCHAR2(20)  DEFAULT '',                                 -- 手机号码（支持国际号码，最大长度20）
    status      CHAR(1)       DEFAULT '0' CHECK (status IN ('0', '1')),   -- 状态：0 正常 1 停用
    del_flag    CHAR(1)       DEFAULT '0' CHECK (del_flag IN ('0', '1')), -- 删除标志（0代表存在 1代表删除）
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP,                       -- 创建时间
    updated_at  TIMESTAMP     DEFAULT SYSTIMESTAMP,                       -- 更新时间
    created_by  VARCHAR2(50)  DEFAULT '',                                 -- 创建人
    updated_by  VARCHAR2(50)  DEFAULT '',                                 -- 更新人
    description CLOB                                                      -- 部门描述
);

-- 添加索引
-- CREATE INDEX idx_im_auth_department_dept_id ON im_auth_dept (dept_id);
-- CREATE INDEX idx_im_auth_department_dept_name ON im_auth_dept (dept_name);
CREATE INDEX idx_im_auth_department_parent_id ON im_auth_dept (parent_id);
CREATE INDEX idx_im_auth_department_status ON im_auth_dept (status);
CREATE INDEX idx_im_auth_department_del_flag ON im_auth_dept (del_flag);

-- 部门表序列
CREATE SEQUENCE seq_im_auth_dept START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 部门表触发器，自动填充 ID、created_at 和 updated_at并更新 updated_at
CREATE OR REPLACE TRIGGER trg_im_auth_dept
    BEFORE INSERT OR UPDATE
    ON im_auth_dept
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_dept.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := SYSTIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := SYSTIMESTAMP;
    END IF;
END;
/

-- 创建视图，默认过滤已删除记录
CREATE OR REPLACE VIEW v_im_auth_dept AS
SELECT *
FROM im_auth_dept
WHERE del_flag = '0';

-- **************************************
-- 用户表：保存系统用户基础信息
-- **************************************
CREATE TABLE im_auth_user
(
    id          NUMBER(19, 0) PRIMARY KEY,                                     -- 主键 ID
    emp_id      VARCHAR2(20)  NOT NULL,                                        -- 工号（唯一）
    username    VARCHAR2(50)  NOT NULL,                                        -- 用户名（唯一）
    nickname    VARCHAR2(50)  DEFAULT '',                                      -- 昵称
    pw_hash     VARCHAR2(255) NOT NULL,                                        -- 密码哈希值（建议使用强加密算法如bcrypt）
    gender      CHAR(1)       DEFAULT '0' CHECK ( gender IN ('0', '1', '2') ), -- 性别：0 未知 1男 2女
    dept_id     NUMBER(19, 0) NOT NULL,                                        -- 部门 ID
    avatar      VARCHAR2(255) DEFAULT '',                                      -- 头像
    email       VARCHAR2(100) DEFAULT '',                                      -- 邮箱
    phone       VARCHAR2(20)  DEFAULT '',                                      -- 手机号码
    user_type   CHAR(1)       DEFAULT '0' CHECK ( user_type IN ('0', '1') ),   -- 用户类型：0 系统用户 1 普通用户
    status      CHAR(1)       DEFAULT '0' CHECK ( status IN ('0', '1') ),      -- 状态：0 正常 1 停用
    del_flag    CHAR(1)       DEFAULT '0' CHECK ( del_flag IN ('0', '1') ),    -- 删除标志（0代表存在 1代表删除）
    login_ip    VARCHAR2(128) DEFAULT '',                                      -- 最后登录IP,                                             -- 最后登录IP
    login_dt    TIMESTAMP,                                                     -- 最后登录时间
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP,                            -- 创建时间
    updated_at  TIMESTAMP     DEFAULT SYSTIMESTAMP,                            -- 更新时间
    created_by  VARCHAR2(50)  DEFAULT '',                                      -- 创建人
    updated_by  VARCHAR2(50)  DEFAULT '',                                      -- 更新人
    description CLOB,                                                          -- 用户描述
    CONSTRAINT uq_im_auth_user_employee_id UNIQUE (emp_id),
    CONSTRAINT uq_im_auth_user_username UNIQUE (username),
    FOREIGN KEY (dept_id) REFERENCES im_auth_dept (id) ON DELETE CASCADE
);

-- 添加索引（优化查询性能）
CREATE INDEX idx_auth_user_status ON im_auth_user (status); -- 新增索引：状态字段
CREATE INDEX idx_auth_user_del_flag ON im_auth_user (del_flag); -- 新增索引：删除标志字段
CREATE INDEX idx_auth_user_dept_id ON im_auth_user (dept_id);

-- 用户表序列
CREATE SEQUENCE seq_im_auth_user START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 用户表触发器，自动填充 ID
CREATE OR REPLACE TRIGGER trg_im_auth_user
    BEFORE INSERT OR UPDATE
    ON im_auth_user
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_user.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := SYSTIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := SYSTIMESTAMP;
    END IF;
END;
/

-- 创建视图，默认过滤已删除记录
CREATE OR REPLACE VIEW v_im_auth_user AS
SELECT *
FROM im_auth_user
WHERE del_flag = '0';

-- **************************************
-- 角色表：定义系统中各类角色
-- **************************************
CREATE TABLE im_auth_role
(
    id                  NUMBER(19, 0) PRIMARY KEY,                                                  -- 主键 ID
    role_name           VARCHAR2(100) NOT NULL,                                                     -- 角色名称（唯一）
    role_key            VARCHAR2(100) NOT NULL,                                                     -- 角色权限字符串
    role_sort           NUMBER(4, 0) DEFAULT 0,                                                     -- 显示顺序
    data_scope          CHAR(1)      DEFAULT '1' CHECK ( data_scope IN ('1', '2', '3', '4', '5') ), -- 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）
    menu_check_strictly CHAR(1)      DEFAULT '1' CHECK ( menu_check_strictly IN ('0', '1') ),       -- 菜单树选择项是否关联子级菜单
    dept_check_strictly CHAR(1)      DEFAULT '1' CHECK ( dept_check_strictly IN ('0', '1') ),       -- 部门树选择项是否关联子级部门
    status              CHAR(1)      DEFAULT '0' CHECK ( status IN ('0', '1') ),                    -- 状态：0 正常 1 停用
    del_flag            CHAR(1)      DEFAULT '0' CHECK ( del_flag IN ('0', '1') ),                  -- 删除标志（0代表存在 1代表删除）
    created_at          TIMESTAMP    DEFAULT SYSTIMESTAMP,
    updated_at          TIMESTAMP    DEFAULT SYSTIMESTAMP,                                          -- 初始默认值为创建时间
    created_by          VARCHAR2(50) DEFAULT '',                                                    -- 创建人
    updated_by          VARCHAR2(50) DEFAULT '',                                                    -- 更新人
    description         CLOB,                                                                       -- 角色描述
    CONSTRAINT uq_im_auth_role_name UNIQUE (role_name)
);
-- 角色表索引
-- CREATE INDEX idx_im_auth_role_id ON im_auth_role (role_id);
-- CREATE INDEX idx_im_auth_role_name ON im_auth_role (role_name);
CREATE INDEX idx_im_auth_role_key ON im_auth_role (role_key);
CREATE INDEX idx_im_auth_role_status ON im_auth_role (status);
CREATE INDEX idx_im_auth_role_del_flag ON im_auth_role (del_flag);

-- 角色表序列
CREATE SEQUENCE seq_im_auth_role START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 角色表触发器
CREATE OR REPLACE TRIGGER trg_im_auth_role
    BEFORE INSERT OR UPDATE
    ON im_auth_role
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_role.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := SYSTIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := SYSTIMESTAMP;
    END IF;
END;
/

-- 创建视图，默认过滤已删除记录
CREATE OR REPLACE VIEW v_im_auth_role AS
SELECT *
FROM im_auth_role
WHERE del_flag = '0';

-- **************************************
-- 用户-角色关联表：支持多对多关系
-- **************************************
CREATE TABLE
    im_auth_user_role
(
    user_id NUMBER NOT NULL, -- 用户 ID
    role_id NUMBER NOT NULL, -- 角色 ID
    CONSTRAINT pk_im_auth_user_role PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES im_auth_role (id) ON DELETE CASCADE
);

-- **************************************
-- 权限表：定义细粒度权限
-- **************************************
CREATE TABLE
    im_auth_permission
(
    id          NUMBER PRIMARY KEY,                                        -- 主键 ID
    perm_name   VARCHAR2(255) NOT NULL,                                    -- 权限名称
    sys_id      NUMBER(19, 0) NOT NULL,                                    -- 系统 ID
    status      CHAR(1)      DEFAULT '0' CHECK ( status IN ('0', '1') ),   -- 状态：0 正常 1 停用
    del_flag    CHAR(1)      DEFAULT '0' CHECK ( del_flag IN ('0', '1') ), -- 删除标志（0代表存在 1代表删除）
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,                    -- 创建时间
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,                    -- 更新时间
    created_by  VARCHAR2(50) DEFAULT '',                                   -- 创建人
    updated_by  VARCHAR2(50) DEFAULT '',                                   -- 更新人
    description CLOB,                                                      -- 权限描述
    CONSTRAINT uq_auth_permission UNIQUE (perm_name),                      -- 唯一约束
    FOREIGN KEY (sys_id) REFERENCES im_auth_system (id) ON DELETE CASCADE
);

-- 权限表索引
CREATE INDEX idx_im_auth_permission_status ON im_auth_permission (sys_id);

-- 权限表序列
CREATE SEQUENCE seq_im_auth_permission START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 权限表插入触发器
CREATE OR
    REPLACE TRIGGER trg_im_auth_permission
    BEFORE
        INSERT OR UPDATE
    ON im_auth_permission
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_permission.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := CURRENT_TIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := CURRENT_TIMESTAMP;
    END IF;
END;
/

-- 创建视图，默认过滤已删除记录
CREATE OR REPLACE VIEW v_im_auth_permission AS
SELECT *
FROM im_auth_permission
WHERE del_flag = '0';

-- **************************************
-- 角色-权限关联表：多对多映射
-- **************************************
CREATE TABLE
    im_auth_role_permission
(
    role_id NUMBER NOT NULL, -- 角色 ID
    perm_id NUMBER NOT NULL, -- 权限 ID
    PRIMARY KEY (role_id, perm_id),
    FOREIGN KEY (role_id) REFERENCES im_auth_role (id) ON DELETE CASCADE,
    FOREIGN KEY (perm_id) REFERENCES im_auth_permission (id) ON DELETE CASCADE
);

-- **************************************
-- 用户-权限关联表：支持直接授予用户权限
-- **************************************
CREATE TABLE
    im_auth_user_permission
(
    user_id NUMBER NOT NULL, -- 用户 ID
    perm_id NUMBER NOT NULL, -- 权限 ID
    PRIMARY KEY (user_id, perm_id),
    FOREIGN KEY (user_id) REFERENCES im_auth_user (id) ON DELETE CASCADE,
    FOREIGN KEY (perm_id) REFERENCES im_auth_permission (id) ON DELETE CASCADE
);

-- **************************************
-- OAuth2 客户端表：存储第三方应用接入信息
-- **************************************
CREATE TABLE
    im_auth_oauth_client
(
    id            NUMBER PRIMARY KEY,
    client_id     VARCHAR2(100) NOT NULL,                                      -- 客户端 ID（如：auth-center）
    client_name   VARCHAR2(100) NOT NULL,                                      -- 客户端名称（如：认证中心）
    sys_name      VARCHAR2(100) NOT NULL,                                      -- 归属系统标识（可用于在 token 中标识来源）
    client_secret VARCHAR2(255) NOT NULL,                                      -- 客户端密钥
    grant_types   VARCHAR2(255) NOT NULL,                                      -- 授权类型
    redirect_uris VARCHAR2(1000) DEFAULT '',                                   -- 回调 URI 列表
    scopes        VARCHAR2(500)  DEFAULT '',                                   -- 权限范围
    status        CHAR(1)        DEFAULT '0' CHECK ( status IN ('0', '1') ),   -- 状态：0 正常 1 停用
    del_flag      CHAR(1)        DEFAULT '0' CHECK ( del_flag IN ('0', '1') ), -- 删除标志（0代表存在 1代表删除）
    created_at    TIMESTAMP      DEFAULT SYSTIMESTAMP,                         -- 创建时间
    updated_at    TIMESTAMP      DEFAULT SYSTIMESTAMP,                         -- 更新时间
    created_by    VARCHAR2(50)   DEFAULT '',                                   -- 创建人
    updated_by    VARCHAR2(50)   DEFAULT '',                                   -- 更新人
    description   CLOB
);
-- OAuth2 客户端索引
CREATE INDEX idx_im_auth_oauth_client_client_id ON im_auth_oauth_client (client_id);

-- OAuth2 客户端序列
CREATE SEQUENCE seq_im_auth_oauth_client START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- OAuth2 客户端插入触发器
CREATE OR
    REPLACE TRIGGER trg_im_auth_oauth_client
    BEFORE INSERT OR UPDATE
    ON im_auth_oauth_client
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_oauth_client.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := CURRENT_TIMESTAMP;
    END IF;
    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := CURRENT_TIMESTAMP;
    END IF;
END;
/

-- **************************************
-- API Key 表：用于非交互式认证
-- **************************************
CREATE
    TABLE
    im_auth_api_key
(
    id          NUMBER PRIMARY KEY,                -- 主键 ID
    user_id     NUMBER               NOT NULL,     -- 用户 ID
    api_key     VARCHAR2(255) UNIQUE NOT NULL,     -- API Key 值
    expires_at  TIMESTAMP            NOT NULL,     -- 过期时间
    created_at  TIMESTAMP    DEFAULT SYSTIMESTAMP, -- 创建时间
    updated_at  TIMESTAMP    DEFAULT SYSTIMESTAMP, -- 更新时间
    created_by  VARCHAR2(50) DEFAULT '',           -- 创建人
    updated_by  VARCHAR2(50) DEFAULT '',           -- 更新人
    description CLOB,
    FOREIGN KEY (user_id) REFERENCES im_auth_user (id) ON DELETE CASCADE
);
-- API Key 索引
CREATE INDEX idx_im_auth_api_key_user_id ON im_auth_api_key (user_id);

-- API Key 序列
CREATE SEQUENCE seq_im_auth_api_key START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- API Key 插入触发器
CREATE OR
    REPLACE TRIGGER trg_im_auth_api_key
    BEFORE INSERT OR UPDATE
    ON im_auth_api_key
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_api_key.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := CURRENT_TIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := CURRENT_TIMESTAMP;
    END IF;
END;
/

-- **************************************
-- 会话表：保存登录会话信息
-- **************************************
CREATE TABLE
    im_auth_session
(
    id            NUMBER PRIMARY KEY,                -- 主键 ID
    user_id       NUMBER               NOT NULL,     -- 用户 ID
    session_token VARCHAR2(255) UNIQUE NOT NULL,     -- 会话 Token
    expires_at    TIMESTAMP            NOT NULL,     -- 过期时间
    created_at    TIMESTAMP    DEFAULT SYSTIMESTAMP, -- 创建时间
    updated_at    TIMESTAMP    DEFAULT SYSTIMESTAMP, -- 更新时间
    created_by    VARCHAR2(50) DEFAULT '',           -- 创建人
    updated_by    VARCHAR2(50) DEFAULT '',           -- 更新人
    description   CLOB,
    FOREIGN KEY (user_id) REFERENCES im_auth_user (id) ON DELETE CASCADE
);
-- 会话索引
CREATE INDEX idx_im_auth_session_user_id ON im_auth_session (user_id);

-- 会话序列
CREATE SEQUENCE seq_im_auth_session START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 会话插入触发器
CREATE OR
    REPLACE TRIGGER trg_im_auth_session
    BEFORE INSERT OR UPDATE
    ON im_auth_session
    FOR EACH ROW
BEGIN
    -- 插入时生成主键
    IF INSERTING AND :NEW.id IS NULL THEN
        :NEW.id := seq_im_auth_session.NEXTVAL;
    END IF;

    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := CURRENT_TIMESTAMP;
    END IF;

    -- 插入、更新时设置 updated_at
    IF INSERTING OR UPDATING THEN
        :NEW.updated_at := CURRENT_TIMESTAMP;
    END IF;
END;
/

-- **************************************
-- 审计日志表：记录用户行为
-- **************************************
CREATE TABLE
    im_auth_audit_log
(
    id          NUMBER PRIMARY KEY,             -- 主键 ID
    user_id     NUMBER        NOT NULL,         -- 用户 ID，可为空
    action      VARCHAR2(255) NOT NULL,         -- 操作行为
    ip_address  VARCHAR2(50),                   -- IP 地址
    created_at  TIMESTAMP DEFAULT SYSTIMESTAMP, -- 创建时间
    description CLOB,
    FOREIGN KEY (user_id) REFERENCES im_auth_user (id) ON DELETE SET NULL
);
-- 审计日志索引
CREATE INDEX idx_im_auth_audit_log_user_id ON im_auth_audit_log (user_id);

-- 审计日志序列
CREATE SEQUENCE seq_im_auth_audit_log START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
-- 审计日志插入触发器
CREATE OR
    REPLACE TRIGGER trg_im_auth_audit_log
    BEFORE INSERT
    ON im_auth_audit_log
    FOR EACH ROW
BEGIN
    -- 插入时设置 created_at
    IF INSERTING THEN
        :NEW.created_at := CURRENT_TIMESTAMP;
    END IF;
END;
/
