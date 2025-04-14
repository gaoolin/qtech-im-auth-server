package com.qtech.im.auth.scheduleder;

import com.qtech.im.auth.service.others.DeptSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 08:55:48
 * desc   :  定时同步部门数据任务
 */
@Component
public class DeptSyncTask {
    private static final Logger logger = LoggerFactory.getLogger(DeptSyncTask.class);

    private final DeptSyncService deptSyncService;
    private boolean isRunning = false;

    public DeptSyncTask(DeptSyncService deptSyncService) {
        this.deptSyncService = deptSyncService;
    }

    // @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次（正式部署建议为每天凌晨）
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    public void sync() {
        if (isRunning) {
            logger.warn(">>>>> 上一次部门同步任务未完成，跳过本次执行。");
            return;
        }

        isRunning = true;
        try {
            logger.info(">>>>> 部门同步任务开始执行...");
            deptSyncService.syncDepartments();
            logger.info(">>>>> 部门同步任务执行完成。");
        } catch (Exception e) {
            logger.error(">>>>> 部门同步过程中发生异常：", e);
        } finally {
            isRunning = false;
        }
    }
}