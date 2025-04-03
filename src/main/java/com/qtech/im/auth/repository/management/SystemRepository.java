package com.qtech.im.auth.repository.management;

import com.qtech.im.auth.model.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 09:19:57
 * desc   :
 */

@Repository
public interface SystemRepository extends JpaRepository<System, Long>, JpaSpecificationExecutor<System> {
    Optional<System> findBySysName(String sysName);
}