以下是 QTECH-IM-AUTH-SERVER 数据库 ER 图设计（dbdiagram.io 格式）：
```text
    Table im_auth_user {
      id integer [pk]
      employee_id varchar(20) [unique, note: '员工工号']
      username varchar(50) [unique, note: '用户名']
      password_hash varchar(255)
      email varchar(100)
      department varchar(100) [note: '部门']
      section varchar(100) [note: '小组/车间']
      gender varchar(10)
      status varchar(20)
      created_at timestamp
      updated_at timestamp
    }
    
    Table im_auth_role {
      id integer [pk]
      role_name varchar(50) [unique]
      description varchar(255)
    }
    
    Table im_auth_user_role {
      user_id integer [ref: > im_auth_user.id]
      role_id integer [ref: > im_auth_role.id]
      indexes {
        (user_id, role_id) [unique]
      }
    }
    
    Table im_auth_permission {
      id integer [pk]
      permission_name varchar(255)
      description varchar(255)
      resource_name varchar(255)
      action_type varchar(50)
      system_name varchar(100)
      application_name varchar(100)
      created_at timestamp
      updated_at timestamp
    }
    
    Table im_auth_role_permission {
      role_id integer [ref: > im_auth_role.id]
      permission_id integer [ref: > im_auth_permission.id]
      indexes {
        (role_id, permission_id) [unique]
      }
    }
    
    Table im_auth_user_permission {
      user_id integer [ref: > im_auth_user.id]
      permission_id integer [ref: > im_auth_permission.id]
      indexes {
        (user_id, permission_id) [unique]
      }
    }
    
    Table im_auth_oauth_client {
      client_id varchar(100) [pk]
      client_secret varchar(255)
      grant_types varchar(255)
      redirect_uris varchar(1000)
      scopes varchar(500)
      created_at timestamp
    }
    
    Table im_auth_api_key {
      id integer [pk]
      user_id integer [ref: > im_auth_user.id]
      api_key varchar(255) [unique]
      created_at timestamp
    }
    
    Table im_auth_session {
      id integer [pk]
      user_id integer [ref: > im_auth_user.id]
      session_token varchar(255) [unique]
      expires_at timestamp
      created_at timestamp
    }
    
    Table im_auth_audit_log {
      id integer [pk]
      user_id integer [ref: > im_auth_user.id]
      action varchar(255)
      ip_address varchar(50)
      created_at timestamp
    }
    
    // Relations
    table im_auth_user_role {
      user_id -> im_auth_user.id
      role_id -> im_auth_role.id
    }
    
    table im_auth_role_permission {
      role_id -> im_auth_role.id
      permission_id -> im_auth_permission.id
    }
    
    table im_auth_user_permission {
      user_id -> im_auth_user.id
      permission_id -> im_auth_permission.id
    }
    
    table im_auth_api_key {
      user_id -> im_auth_user.id
    }
    
    table im_auth_session {
      user_id -> im_auth_user.id
    }
    
    table im_auth_audit_log {
      user_id -> im_auth_user.id
    }
```