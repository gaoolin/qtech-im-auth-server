package com.qtech.im.auth.repository.primary.api.rabc;

import com.qtech.im.auth.model.primary.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 10:50:11
 * desc   :
 */
@Repository
public interface SysConfigRepository extends JpaRepository<SystemConfig, Long>, JpaSpecificationExecutor<SystemConfig> {
}