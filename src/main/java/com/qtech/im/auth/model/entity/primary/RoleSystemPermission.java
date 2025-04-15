package com.qtech.im.auth.model.entity.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:04:23
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_ROLE_SYSTEM_PERMISSION")
public class RoleSystemPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "SYSTEM_ID", referencedColumnName = "ID")
    private System system;

    @ManyToOne
    @JoinColumn(name = "PERM_ID", referencedColumnName = "ID")
    private Permission permission;
}

