package com.qtech.im.auth.repository;

import com.qtech.im.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:31:30
 * desc   :  用户数据访问层
 * 改进点： ✅ 支持邮箱查询，未来如果想支持 邮箱+密码登录，可以直接扩展 UserDetailsServiceImpl。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // 未来可以通过 Email 登录
}