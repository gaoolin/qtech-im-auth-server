package com.qtech.im.auth.model.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:01:59
 * desc   :
 */
@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_USER_SYSTEM_ROLE")
public class UserSystemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "SYSTEM_ID", referencedColumnName = "ID")
    private System systemId;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    private Role role;
}
