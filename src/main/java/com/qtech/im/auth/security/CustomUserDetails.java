package com.qtech.im.auth.security;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.utils.UserStatus;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 13:36:18
 * desc   :
 * CustomUserDetails 主要作用：
 * 封装用户信息：存储用户名、密码、权限、账户状态（是否锁定、是否启用等）。
 * 配合 UserDetailsService 使用：Spring Security 通过 UserDetailsService 加载用户信息，返回 UserDetails 供认证和授权使用。
 * 自定义额外属性：可以在 CustomUserDetails 中添加额外的字段，例如用户ID、邮箱、角色列表等。
 * <p>
 * 设计优化点
 * ✅ 角色权限自动继承
 * <p>
 * 通过 flatMap(role -> role.getPermissions().stream()) 获取 所有角色的权限，使 Spring Security 可以识别权限。
 * ✅ 支持 RBAC 和 PBAC
 * <p>
 * RBAC（基于角色访问控制）：用户通过角色获取权限。
 * PBAC（基于权限访问控制）：未来可以直接为用户分配权限（未在当前实现）。
 */

@Data
public class CustomUserDetails implements UserDetails {
    private final Long id; // 保留 ID，方便以后扩展
    private final String employeeId;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String employeeId, String username, String password, boolean enabled, Set<Role> roles) {
        this.id = id;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = roles.stream()
                .flatMap(role -> role.getPermissions().stream()) // 角色权限合并
                .collect(Collectors.toSet());
    }

    // 直接接受 User 作为参数，便于通用化
    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getEmployeeId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getStatus() == UserStatus.ACTIVE,
                user.getRoles()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}