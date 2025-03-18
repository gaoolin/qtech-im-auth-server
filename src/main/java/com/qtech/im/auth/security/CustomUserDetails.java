package com.qtech.im.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/17 13:36:18
 * desc   :
 * CustomUserDetails 主要作用：
 * 封装用户信息：存储用户名、密码、权限、账户状态（是否锁定、是否启用等）。
 * 配合 UserDetailsService 使用：Spring Security 通过 UserDetailsService 加载用户信息，返回 UserDetails 供认证和授权使用。
 * 自定义额外属性：可以在 CustomUserDetails 中添加额外的字段，例如用户ID、邮箱、角色列表等。
 */

public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
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
