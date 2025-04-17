package com.qtech.im.auth.service.management;

import com.qtech.im.auth.model.dto.DeptDTO;
import com.qtech.im.auth.model.dto.DeptTreeNodeDTO;
import com.qtech.im.auth.model.dto.DeptViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:43:29
 * desc   :
 */
public interface IDepartmentService {
    Page<DeptDTO> getPage(PageRequest pageable);
    DeptDTO createDept(DeptDTO dept);
    DeptDTO updateDept(DeptDTO dept);
    void deleteDept(Long id);
    List<DeptTreeNodeDTO> getDeptTree();
    Page<DeptDTO> findDeptsWithConditions(String keyword, PageRequest pageable);
    Page<DeptViewDTO> findDeptViews(PageRequest pageable);
    Page<DeptViewDTO> findDeptViewsWithConditions(String keyword, PageRequest pageable);
}