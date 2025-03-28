package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:04:23
 * desc   :
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "IM_AUTH_ROLE_PERMISSION")
@Data
public class RolePermission extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "PERM_ID")
    private Permission permission;
}

