package com.qtech.im.auth.model.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 14:55:05
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_USER_PERMISSION")
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PERM_ID", referencedColumnName = "ID")
    private Permission permission;
}
