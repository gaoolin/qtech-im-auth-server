package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:29:26
 * desc   :  角色实体类
 * <p>
 * 设计优化点
 * ✅ 继承 GrantedAuthority
 * <p>
 * Role 直接实现 GrantedAuthority，Spring Security 需要 getAuthority() 方法来识别权限。
 * 这样 Role 可以直接用于 CustomUserDetails，提高通用性。
 * ✅ 角色名称唯一（@Column(unique = true)）
 * <p>
 * 角色名称必须唯一，例如 ROLE_ADMIN、ROLE_USER。
 * 避免重复的角色定义，保持数据库一致性。
 * ✅ 采用 @Data 进行简化
 * <p>
 * 通过 Lombok 自动生成 getter/setter，减少样板代码。
 */

@Data
@Entity
@Table(name = "IM_AUTH_ROLE")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String roleName; // 角色名称，如 ROLE_ADMIN, ROLE_USER

    @Column(length = 255)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "IM_AUTH_ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    private Set<Permission> permissions = new HashSet<>();

    @Override
    public String getAuthority() {
        return roleName; // Spring Security 需要角色名称作为权限标识
    }
}

