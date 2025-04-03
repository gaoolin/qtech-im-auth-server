package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:25:59
 * desc   :  权限实体
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_PERMISSION")
public class Permission extends BaseModel implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PERM_NAME")
    private String permName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYS_ID", referencedColumnName = "ID")
    private System system;

    @Column(name = "APP_NAME")
    private String appName;

    @Column(name = "RSRC_NAME")
    private String rsrcName;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Override
    public String getAuthority() {
        return permName; // Spring Security 需要权限标识
    }
}
