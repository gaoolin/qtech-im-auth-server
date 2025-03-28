package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:25:59
 * desc   :  权限实体
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "IM_AUTH_PERMISSION")
public class Permission extends BaseModel implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PERM_NAME", length = 50)
    private String permName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYS_ID", referencedColumnName = "ID")
    private System system;

    @Column(name = "STATUS", length = 1)
    private String status;

    @Column(name = "DEL_FLAG", length = 1)
    private String delFlag;

    @Override
    public String getAuthority() {
        return permName; // Spring Security 需要权限标识
    }
}
