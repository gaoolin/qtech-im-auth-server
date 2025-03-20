package com.qtech.im.auth.model;

import com.qtech.im.auth.utils.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:27:45
 * desc   :
 * 改进点：
 * ✅ 密码存储应使用加密方式，不能存储明文密码。
 * ✅ 默认值设置为 true，表示账号默认可用。
 */

@Data
@Entity
@Table(name = "IM_AUTH_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_ID", unique = true, length = 50)
    private String employeeId;

    @Column(length = 50)
    private String username;

    @Column(name = "PASSWORD_HASH", length = 255)
    private String passwordHash;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String department;

    @Column(length = 50)
    private String section;

    @Column(length = 10)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "IM_AUTH_USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();
}