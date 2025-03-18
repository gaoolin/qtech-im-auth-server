package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:25:59
 * desc   :  权限实体
 */

@Data
@Entity
@Table(name = "IM_AUTH_PERMISSION")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String systemName;

    @Column(length = 50, unique = true)
    private String applicationName;

    @Column(length = 50)
    private String resourceName;

    @Column(length = 50)
    private String permissionName; // 权限名称，如 READ_USER, WRITE_USER

    @Column(length = 20)
    private String actionType;

    @Column(length = 255)
    private String description;

    @Override
    public String getAuthority() {
        return permissionName; // Spring Security 需要权限标识
    }
}
