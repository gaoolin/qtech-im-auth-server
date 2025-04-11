package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.model.primary.SystemConfig;
import com.qtech.im.auth.service.api.IBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 16:41:01
 * desc   :
 */
@RestController
@RequestMapping("/auth/blacklist")
public class BlackListController {
    private final IBlackListService blackListService;

    @Autowired
    public BlackListController(IBlackListService blackListService) {
        this.blackListService = blackListService;
    }

    @PostMapping("/add")
    public Result<?> addBlackList(SystemConfig blackListItem) {
        boolean result = blackListService.addBlackList(blackListItem);
        return result ? Result.success() : Result.failure(ResultCode.CUSTOM_ERROR);
    }

    @GetMapping("/list")
    public Result<List<SystemConfig>> getBlackList() {
        return Result.success(blackListService.getBlackList());
    }
}
