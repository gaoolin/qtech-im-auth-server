package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 14:55:05
 * desc   :
 */

@Data
@Entity
@Table(name = "IM_AUTH_USER_PERMISSION")
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PERM_ID")
    private Permission permission;
}
