package com.qtech.im.auth.service.api.rabc.impl;

import com.qtech.im.auth.model.primary.SystemConfig;
import com.qtech.im.auth.repository.primary.api.rabc.SysConfigRepository;
import com.qtech.im.auth.repository.primary.api.rabc.SysConfigSpecification;
import com.qtech.im.auth.service.api.rabc.ISysConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 10:45:45
 * desc   :
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    private final SysConfigRepository sysConfigRepository;

    public SysConfigServiceImpl(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
    }

    public Page<SystemConfig> list(SystemConfig systemConfig, Pageable pageable) {
        return sysConfigRepository.findAll(new SysConfigSpecification(systemConfig), pageable);
    }
}