package com.qtech.im.auth.model;

import com.qtech.im.auth.utils.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_USER")
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "EMP_ID", unique = true, length = 50)
    private String empId;
    @Column(name = "USERNAME", length = 50)
    private String username;
    @Column(name = "NICKNAME")
    private String nickName;
    @Column(name = "PW_HASH")
    private String pwHash;
    @Convert(converter = GenderConverter.class)
    @Column(name = "GENDER")
    private Gender gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPT_ID", referencedColumnName = "ID")
    private Department department;
    @Column(name = "AVATAR")
    private String avatar;
    @Column(name = "EMAIL", length = 100)
    private String email;
    @Column(name = "PHONE", length = 50)
    private String phone;
    @Convert(converter = UserTypeConverter.class)
    @Column(name = "USER_TYPE")
    private UserType userType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "IM_AUTH_USER_SYSTEM_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

}