package com.qtech.im.auth.repository.primary.api;

import com.qtech.im.auth.model.entity.primary.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 16:45:17
 * desc   :
 */

@Repository
public interface BlackListRepository extends JpaRepository<SystemConfig, Long> {
    @Query("SELECT b FROM SystemConfig b WHERE b.configKey = 'sys.login.blackIPList'")
    List<SystemConfig> getBlackList();
}
