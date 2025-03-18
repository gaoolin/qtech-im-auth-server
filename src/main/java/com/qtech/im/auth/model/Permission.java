package com.qtech.im.auth.entity;

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
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // 权限名称，如 READ_USER, WRITE_USER

    private String description;

    @Override
    public String getAuthority() {
        return name; // Spring Security 需要权限标识
    }
}
