package com.qtech.im.auth.service.others;

import com.qtech.im.auth.config.datasource.DataSourceContextHolder;
import com.qtech.im.auth.model.entity.primary.Department;
import com.qtech.im.auth.repository.primary.management.DeptRepository;
import com.qtech.im.auth.repository.second.EmployeeRepository;
import com.qtech.im.auth.utils.DelFlag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 17:03:32
 * desc   :  部门同步服务
 */
@Slf4j
@Service
public class DeptSyncService {

    private static final Long ROOT_ID = 100L;
    private static final String ROOT_NAME = "丘钛集团";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeptRepository deptRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 从 Oracle 获取序列下一个值（用于生成部门ID）
     */
    private Long getNextDeptId() {
        try {
            DataSourceContextHolder.set("secondary");

            return ((Number) entityManager
                    .createNativeQuery("SELECT IMWEB.SEQ_IM_AUTH_DEPT.NEXTVAL FROM dual")
                    .getSingleResult())
                    .longValue();

        } catch (Exception e) {
            log.error("获取部门序列ID失败", e);
            throw new RuntimeException("获取部门序列ID失败", e);
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    /**
     * 同步部门数据（从员工表 org_full_path 中提取）
     */
    @Transactional
    public void syncDepartments() {
        DataSourceContextHolder.set("primary");

        ensureRootDeptExists(); // 先确保根节点存在

        List<String> deptPaths = employeeRepository.findDistinctOrgFullPaths();
        Map<String, Long> deptNameToIdMap = new HashMap<>();

        for (String path : deptPaths) {
            insertPath(path, deptNameToIdMap);
        }

        DataSourceContextHolder.clear();
    }

    /**
     * 插入一个完整路径的所有部门（递归构建）
     */
    private void insertPath(String path, Map<String, Long> deptNameToIdMap) {
        String[] depts = path.split("-");
        Long parentId = ROOT_ID;
        StringBuilder ancestors = new StringBuilder("0");

        int startIndex = 0;
        if (depts.length > 0 && ROOT_NAME.equals(depts[0])) {
            startIndex = 1;
        }

        for (int i = startIndex; i < depts.length; i++) {
            String deptName = depts[i];
            String key = parentId + "-" + deptName;

            if (!deptNameToIdMap.containsKey(key)) {
                Optional<Department> existing = deptRepository.findByDeptNameAndParentId(deptName, parentId);
                Long deptId;

                if (existing.isPresent()) {
                    deptId = existing.get().getId();
                } else {
                    Department dept = new Department();
                    dept.setParentId(parentId);
                    dept.setDeptName(deptName);
                    dept.setAncestors(ancestors.toString());
                    dept.setOrderNum(0);
                    dept.setDelFlag(DelFlag.EXISTS);
                    dept.setCreateTime(LocalDateTime.now());

                    deptRepository.save(dept);
                    deptId = dept.getId();
                }

                deptNameToIdMap.put(key, deptId);
            }

            parentId = deptNameToIdMap.get(key);
            ancestors.append(",").append(parentId);
        }
    }

    /**
     * 确保顶层组织存在（丘钛集团）
     */
    private void ensureRootDeptExists() {
        Optional<Department> root = deptRepository.findById(ROOT_ID);

        if (root.isEmpty()) {
            Department dept = new Department();
            dept.setId(ROOT_ID);
            dept.setDeptName(ROOT_NAME);
            dept.setParentId(0L);
            dept.setAncestors("0");
            dept.setOrderNum(0);
            dept.setDelFlag(DelFlag.EXISTS);
            dept.setCreateTime(LocalDateTime.now());

            deptRepository.save(dept);
            log.info("已自动创建根部门: {} (ID: {})", ROOT_NAME, ROOT_ID);
        }
    }
}