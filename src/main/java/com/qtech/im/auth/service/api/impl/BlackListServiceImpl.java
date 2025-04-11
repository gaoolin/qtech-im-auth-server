package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.model.primary.SystemConfig;
import com.qtech.im.auth.repository.primary.api.BlackListRepository;
import com.qtech.im.auth.service.api.IBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 16:47:43
 * desc   :
 */
@Slf4j
@Service
public class BlackListServiceImpl implements IBlackListService {
    @Autowired
    private BlackListRepository blackListRepository;

    @Override
    public boolean addBlackList(SystemConfig blackListItem) {
        try {
            if (blackListItem == null) {
                throw new IllegalArgumentException("黑名单项不能为空");
            }
            blackListRepository.save(blackListItem); // 使用 JpaRepository 的 save 方法保存数据
            return true;
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一性约束冲突等异常
            throw new DataIntegrityViolationException("黑名单项已存在", e);
        } catch (Exception e) {
            // 捕获其他异常并记录日志
            log.error("添加黑名单项时发生异常", e);
            return false;
        }
    }

    @Override
    public List<SystemConfig> getBlackList() {
        return blackListRepository.getBlackList();
    }
}
