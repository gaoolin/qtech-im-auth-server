package com.qtech.im.auth.controller.api.rabc;

import com.qtech.im.auth.model.primary.SystemConfig;
import com.qtech.im.auth.service.api.rabc.ISysConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 10:41:50
 * desc   :
 */

@RestController
@RequestMapping("/im/auth/sys")
public class ImAuthSysConfigController {
    private final ISysConfigService sysConfigService;

    public ImAuthSysConfigController(ISysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @PreAuthorize("hasPermission('sys:config:list')")
    @RequestMapping("/list")
    public ResponseEntity<?> list(SystemConfig sysConfig,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<SystemConfig> list = sysConfigService.list(sysConfig, pageable);
        return ResponseEntity.ok(list);
    }
}
