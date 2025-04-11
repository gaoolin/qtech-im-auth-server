package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.repository.primary.management.RoleRepository;
import com.qtech.im.auth.repository.primary.management.SystemRepository;
import com.qtech.im.auth.service.management.ISystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 13:55:17
 * desc   :
 */

@Service
@Transactional
public class SystemServiceImpl implements ISystemService {
    private final SystemRepository systemRepository;
    private final RoleRepository roleRepository;

    public SystemServiceImpl(SystemRepository systemRepository, RoleRepository roleRepository) {
        this.systemRepository = systemRepository;
        this.roleRepository = roleRepository;
    }
}
