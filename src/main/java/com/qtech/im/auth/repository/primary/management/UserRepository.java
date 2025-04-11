package com.qtech.im.auth.repository.primary.management;

import com.qtech.im.auth.model.primary.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:31:30
 * desc   :  用户数据访问层
 * 改进点： ✅ 支持邮箱查询，未来如果想支持 邮箱+密码登录，可以直接扩展 UserDetailsServiceImpl。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // 根据工号查询用户，工号唯一
    Optional<User> findByEmpId(String empId);

    // 根据用户名查询用户
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmpId(String empId);

    @Query("SELECT u FROM User u")
    List<User> findAllUsers();

    void deleteUserByEmpId(String empId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :username, u.pwHash = :pwHash, u.email = :email WHERE u.empId = :empId")
    int updateUserByEmpId(String empId, User user);

    List<User> findByEmpIdOrUsername(String employeeId, String username);
}