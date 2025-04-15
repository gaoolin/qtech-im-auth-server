package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.entity.primary.SystemConfig;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 16:42:18
 * desc   :
 */


public interface IBlackListService {
    boolean addBlackList(SystemConfig blackListItem);

    List<SystemConfig> getBlackList();
}
