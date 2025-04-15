package com.qtech.im.auth.service.api.rabc;

import com.qtech.im.auth.model.entity.primary.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 10:44:31
 * desc   :
 */


public interface ISysConfigService {
    Page<SystemConfig> list(SystemConfig sysConfig, Pageable pageable);
}