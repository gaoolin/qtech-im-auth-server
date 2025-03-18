package com.qtech.im.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:27:45
 * desc   :
 * 改进点： ✅ 密码存储应使用加密方式，不能存储明文密码。
 * ✅ 默认值设置为 true，表示账号默认可用。
 */

@Data
@Entity
@Table(name = "IM_AUTH_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 存储哈希加密后的密码

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "IM_AUTH_USER_ROLE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();
}