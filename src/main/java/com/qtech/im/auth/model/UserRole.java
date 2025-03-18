package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:01:59
 * desc   :
 */

@Entity
@Table(name = "IM_AUTH_USER_ROLE")
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;
}
