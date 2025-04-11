package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.primary.Department;
import com.qtech.im.auth.model.primary.DeptTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:43:29
 * desc   :
 */
public interface IDepartmentService {
    List<Department> getDeptInfo();

    Page<Department> findAll(Pageable pageable);

    Page<Department> getDeptInfoWithConditions(String keyword, Pageable pageable);

    Department createDept(Department dept);

    void deleteDept(Long id);

    Department updateDept(Department dept);

    List<DeptTree> getDeptTree();
}